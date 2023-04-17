package com.scz.jxapi.generator;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.exchange.EndpointParameter;
import com.scz.jxapi.generator.exchange.EndpointParameterType;
import com.scz.jxapi.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import com.scz.jxapi.util.EncodingUtil;

public class JsonMessageDeserializerGenerator extends JavaTypeGenerator {
	
	public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedTypeClassName), ".pojo") + ".deserializers";
		return pkg + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName) + "Deserializer";
	}

	private final String deserializedTypeClassName;
	private final List<EndpointParameter> fields;
	private final Set<String> deserializerDeclarations = new TreeSet<>();
	
	public JsonMessageDeserializerGenerator(String deserializedTypeClassName, List<EndpointParameter> fields) {
		super(getJsonMessageDeserializerClassName(deserializedTypeClassName));
		this.deserializedTypeClassName = deserializedTypeClassName;
		this.fields = fields;
		setTypeDeclaration("public class");
		String simpleDeserializedClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName);
		setParentClassName("com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer<" + simpleDeserializedClassName + ">");
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
		deserializerDeclarations.forEach(this::appendToBody);
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
			if (field.getType() == EndpointParameterType.STRUCT 
				|| field.getType() == EndpointParameterType.STRUCT_LIST
				|| field.getType() == EndpointParameterType.STRING_LIST) {
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
		switch (field.getType()) {
		case BIGDECIMAL:
			addImport("static " + EncodingUtil.class.getName() + ".readNextBigDecimal");
			return "readNextBigDecimal(parser)";
		case BOOLEAN:
			return "Boolean.valueOf(parser.nextBooleanValue())";
		case INT:
			addImport("static " + EncodingUtil.class.getName() + ".readNextInteger");
			return "readNextInteger(parser)";
		case LONG:
		case TIMESTAMP:
			addImport("static " + EncodingUtil.class.getName() + ".readNextLong");
			return "readNextLong(parser)";
		case STRING:
			return "parser.nextTextValue()";
		case STRING_LIST:
			addImport(StringListFieldDeserializer.class.getName());
			return StringListFieldDeserializer.class.getSimpleName() + ".getInstance().deserialize(parser)";
		case STRUCT:
		case STRUCT_LIST:
			return generateStructDeserializer(field) +".deserialize(parser)";
		default:
			throw new IllegalArgumentException("Unexpected field type for field:" + field);
		}
	}
	
	private String generateStructDeserializer(EndpointParameter field) {
		String fieldTypeName = deserializedTypeClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
		String deserializerTypeName = this.getPackage() + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldTypeName + "Deserializer");
		addImport(deserializerTypeName);
		String simpleDeserializerTypeName = JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializerTypeName);
		String deserializerVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(simpleDeserializerTypeName);
		deserializerDeclarations.add("private final " + simpleDeserializerTypeName + " " + deserializerVariableName 
										+ " = new " + simpleDeserializerTypeName + "();\n");
		switch (field.getType()) {
		case STRUCT:
			return deserializerVariableName;
		case STRUCT_LIST:
			addImport(StructListFieldDeserializer.class.getName());
			addImport(fieldTypeName);
			String listDeserializerTypeName = StructListFieldDeserializer.class.getSimpleName() + "<" + JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldTypeName) + ">";
			String simpleListDeserializerTypeName = JavaCodeGenerationUtil.getClassNameWithoutPackage(listDeserializerTypeName);
			String listDeserializerVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldTypeName + "ListDeserializer"));
			deserializerDeclarations.add("private final " + simpleListDeserializerTypeName + " " + listDeserializerVariableName 
											+ " = new " + simpleListDeserializerTypeName + "(" + deserializerVariableName + ");\n");
			return listDeserializerVariableName;
		default:
			throw new IllegalArgumentException("Unexpected field type for field:" + field);
		}
	}
	
}
