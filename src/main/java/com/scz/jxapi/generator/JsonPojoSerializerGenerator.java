package com.scz.jxapi.generator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.scz.jxapi.generator.exchange.EndpointParameter;
import com.scz.jxapi.util.EncodingUtil;

public class JsonPojoSerializerGenerator extends JavaTypeGenerator {

	private final String serializedTypeClassName;
	private final List<EndpointParameter> fields;
	
	public JsonPojoSerializerGenerator(String packageName, String serializedTypeClassName, List<EndpointParameter> fields) {
		super(packageName + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(serializedTypeClassName) + "Serializer");
		this.serializedTypeClassName = serializedTypeClassName;
		this.fields = fields;
		setTypeDeclaration("public class");
		String simpleDeserializedClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(serializedTypeClassName);
		setParentClassName("com.fasterxml.jackson.databind.ser.std.StdSerializer<" + simpleDeserializedClassName + ">");
		setDescription("Jackson JSON Serializer for " 
						+ serializedTypeClassName 
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING 
						+ "\n@see " + simpleDeserializedClassName);
	}
	
	public String generate() {
		addImport(IOException.class.getName());
		addImport(com.fasterxml.jackson.core.JsonGenerator.class);
		addImport(com.fasterxml.jackson.databind.SerializerProvider.class);
		addImport(com.fasterxml.jackson.databind.ser.std.StdSerializer.class);
		addImport(serializedTypeClassName);
		generateConstructor();
		generateDeserializeMethod();
		return super.generate();
	}

	private void generateConstructor() {
		appendMethod("public " + getSimpleName() + "()", "super(" + JavaCodeGenerationUtil.getClassNameWithoutPackage(serializedTypeClassName) + ".class);");
		appendToBody("\n");	
	}

	private void generateDeserializeMethod() {
		StringBuilder body = new StringBuilder();
		String simpleDeserializedClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(serializedTypeClassName);
		body.append("gen.writeStartObject();\n");
		fields.forEach(field -> {
			String getFieldValue = "value." + JavaCodeGenerationUtil.getGetAccessorMethodName(
					field.getName(),
					field.getType().name().toLowerCase(),
					fields.stream().map(f -> f.getName()).collect(Collectors.toList())) + "()";
			body.append("if (").append(getFieldValue).append(" != null)");
			body.append(JavaCodeGenerationUtil.generateCodeBlock(genWriteFieldInstruction(field, getFieldValue)));
		});
		body.append("gen.writeEndObject();");
		appendMethod("@Override\npublic void serialize(" 
						+ simpleDeserializedClassName 
						+ " value, JsonGenerator gen, SerializerProvider provider) throws IOException", 
					 body.toString());
	}
	
	private String genWriteFieldInstruction(EndpointParameter field, String getFieldValue) {
		switch (field.getType()) {
		case STRING:
			return "gen.writeStringField(\"" + msgFieldName(field) + "\", String.valueOf(" + getFieldValue + "));\n";
		case BIGDECIMAL:
			addImport(EncodingUtil.class);
			return "gen.writeStringField(\"" + msgFieldName(field) + "\", EncodingUtil.bigDecimalToString(" + getFieldValue + "));\n";
		case BOOLEAN:
			return "gen.writeBooleanField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
		case INT:
		case LONG:
		case TIMESTAMP:
			return "gen.writeNumberField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
		case STRING_LIST:
		case OBJECT:
		case OBJECT_LIST:
		case INT_LIST:
			return "gen.writeObjectField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
		default:
			throw new IllegalArgumentException("Unexpected field type for:" + field);
		}
	}
	
	private static String msgFieldName(EndpointParameter field) {
		return field.getMsgField() != null? field.getMsgField() : field.getName();
	}

}
