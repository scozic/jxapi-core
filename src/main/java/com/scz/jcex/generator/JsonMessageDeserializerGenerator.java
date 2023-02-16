package com.scz.jcex.generator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.scz.jcex.generator.exchange.EndpointParameter;
import com.scz.jcex.generator.exchange.EndpointParameterType;
import com.scz.jcex.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;

public class JsonMessageDeserializerGenerator extends JavaTypeGenerator {

	private final String deserializedTypeClassName;
	private final List<EndpointParameter> fields;
	private final Set<String> deserializerDeclarations = new TreeSet<>();
	
	public JsonMessageDeserializerGenerator(String packageName, String deserializedTypeClassName, List<EndpointParameter> fields) {
		super(packageName + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName) + "Deserializer");
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
				.append(field.getName())
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
			addImport(BigDecimal.class.getName());
			return "new BigDecimal(parser.nextTextValue())";
		case BOOLEAN:
			return "parser.nextBooleanValue()";
		case INT:
			return "parser.nextIntValue(0)";
		case LONG:
			return "parser.nextLongValue(0)";
		case STRING:
			return "parser.nextTextValue()";
		case STRING_LIST:
			addImport(StringListFieldDeserializer.class.getName());
			return StringListFieldDeserializer.class.getSimpleName() + ".getInstance().deserialize(parser)";
		case STRUCT:
		case STRUCT_LIST:
			return generateStructDeserializer(field) +".deserialize(parser)";
		case TIMESTAMP:
			return "parser.nextLongValue(0L)";
		default:
			throw new IllegalArgumentException("Unexpected field type for field:" + field);
		}
	}
	
	private String generateStructDeserializer(EndpointParameter field) {
		String fieldTypeName = deserializedTypeClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
		addImport(fieldTypeName);
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
