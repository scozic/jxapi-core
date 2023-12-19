package com.scz.jxapi.generator;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.exchange.EndpointParameter;
import com.scz.jxapi.generator.exchange.EndpointParameterType;
import com.scz.jxapi.generator.exchange.EndpointParameterTypeGenerationUtil;
import com.scz.jxapi.generator.exchange.EndpointParameterTypes;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import com.scz.jxapi.util.JsonUtil;

public class JsonMessageDeserializerGenerator extends JavaTypeGenerator {
	
	public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedTypeClassName), ".pojo") + ".deserializers";
		return pkg + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName) + "Deserializer";
	}

	private final String deserializedTypeClassName;
	private final List<EndpointParameter> fields;
	private final Set<String> nonPrimitiveTypeFieldsDeserializerDeclarations = new TreeSet<>();
//	private final Set<String> listDeserializerDeclarations = new TreeSet<>();
	
	public JsonMessageDeserializerGenerator(String deserializedTypeClassName, List<EndpointParameter> fields) {
		super(getJsonMessageDeserializerClassName(deserializedTypeClassName));
		this.deserializedTypeClassName = deserializedTypeClassName;
		this.fields = fields;
		setTypeDeclaration("public class");
		String simpleDeserializedClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName);
		setParentClassName(AbstractJsonMessageDeserializer.class.getName() + "<" + simpleDeserializedClassName + ">");
		setDescription("Parses incoming JSON messages into " 
						+ deserializedTypeClassName 
						+ " instances\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING 
						+ "\n@see " + deserializedTypeClassName);
	}
	
	public String generate() {
		addImport(IOException.class.getName());
		addImport(com.fasterxml.jackson.core.JsonParser.class.getName());
		addImport(com.fasterxml.jackson.core.JsonToken.class.getName());
		addImport(JsonParserUtil.class.getName());
		addImport(deserializedTypeClassName);
		generateDeserializeMethod();
		return super.generate();
	}

	private void generateDeserializerDeclarations() {
		nonPrimitiveTypeFieldsDeserializerDeclarations.forEach(this::appendToBody);
		appendToBody("\n");
	}

	private void generateDeserializeMethod() {
		String indent = JavaCodeGenerationUtil.INDENTATION;
		StringBuilder body = new StringBuilder();
		String simpleDeserializedClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName);
		body.append(simpleDeserializedClassName)
		    .append(" msg = new ")
		    .append(simpleDeserializedClassName)
		    .append("();\nwhile(parser.nextToken() != JsonToken.END_OBJECT) {\n");
		body.append(indent)
		    .append("switch(parser.getCurrentName()) {\n");
		String dblIndent = indent + indent;
		fields.forEach(field -> {
			body.append(indent)
				.append("case \"")
				.append(field.getMsgField() != null? field.getMsgField() : field.getName())
				.append("\":\n");
			if (!field.getEndpointParameterType().getType().isPrimitive) {
				body.append(dblIndent)
					.append("parser.nextToken();\n");
			}
			body.append(dblIndent)
				.append("msg.")
				.append(
					JavaCodeGenerationUtil.	getSetAccessorMethodName(
							field.getName(),  
							fields.stream().map(f -> f.getName()).collect(Collectors.toList())))
				.append("(")
				.append(getParseFieldInstruction(field))
				.append(");\n")	
				.append(indent).append("break;\n");
		});
		body.append(indent).append("default:\n")
			.append(dblIndent)
				.append(JsonParserUtil.class.getSimpleName())
				.append(".")
				.append("skipNextValue(parser);\n")
			.append(indent).append("}\n")
			.append("}\n")
			.append("\n return msg;");
		
		generateDeserializerDeclarations();
		appendMethod("@Override\npublic " 
						+ simpleDeserializedClassName 
						+ " deserialize(JsonParser parser) throws IOException", 
					 body.toString());
	}

	private String getParseFieldInstruction(EndpointParameter field) {
		EndpointParameterTypes canonicalType = field.getEndpointParameterType().getType();
		if (!canonicalType.isPrimitive) {
			return generateNonPrimitiveTypeParameterDeserializerDeclaration(field) +".deserialize(parser)";
		}
		
		switch (canonicalType) {
		case BIGDECIMAL:
			addImport("static " + JsonUtil.class.getName() + ".readNextBigDecimal");
			return "readNextBigDecimal(parser)";
		case BOOLEAN:
			addImport("static " + JsonUtil.class.getName() + ".readNextBoolean");
			return "readNextBoolean(parser)";
		case INT:
			addImport("static " + JsonUtil.class.getName() + ".readNextInteger");
			return "readNextInteger(parser)";
		case LONG:
		case TIMESTAMP:
			addImport("static " + JsonUtil.class.getName() + ".readNextLong");
			return "readNextLong(parser)";
		case STRING:
			return "parser.nextTextValue()";
		default:
			throw new IllegalArgumentException("Unexpected field type for field:" + field);
		}
	}
	
	private String generateNonPrimitiveTypeParameterDeserializerDeclaration(EndpointParameter field) {
		EndpointParameterType type = field.getEndpointParameterType();
		String objectParameterClassName = EndpointParameterTypeGenerationUtil.getClassNameForEndpointParameter(field, getImports(), deserializedTypeClassName);
		String simpleDeserializerTypeName = generateNonPrimitiveParameterDeserializerClassName(field.getEndpointParameterType(), objectParameterClassName, getImports());
		String deserializerVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(field.getName() + "Deserializer");
		String variableDeclaration = "private final " + simpleDeserializerTypeName + " " + deserializerVariableName + " = " 
										+ EndpointParameterTypeGenerationUtil.getNewNonPrimitiveParameterDeserializerInstruction(type, simpleDeserializerTypeName, getImports()) + ";\n";
		nonPrimitiveTypeFieldsDeserializerDeclarations.add(variableDeclaration);
		return deserializerVariableName;
	}
	
	public static String generateNonPrimitiveParameterDeserializerClassName(EndpointParameterType type, String objectClassName, Set<String> imports) {	
		switch (type.getType()) {
		case OBJECT:
			String deserializerTypeName = objectClassName + "Deserializer";
			imports.add(deserializerTypeName);
			return JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializerTypeName);
		case LIST:
			imports.add(ListJsonFieldDeserializer.class.getName());
			return ListJsonFieldDeserializer.class.getSimpleName() + "<" + EndpointParameterTypeGenerationUtil.getClassNameForParameterType(type.getSubType(), imports, objectClassName) + ">";
		case MAP:
			imports.add(MapJsonFieldDeserializer.class.getName());
			return MapJsonFieldDeserializer.class.getSimpleName() + "<" + EndpointParameterTypeGenerationUtil.getClassNameForParameterType(type.getSubType(), imports, objectClassName) + ">";
		default:
			throw new IllegalArgumentException("Unexpected field type:" + type);
		}
	}
	
}
