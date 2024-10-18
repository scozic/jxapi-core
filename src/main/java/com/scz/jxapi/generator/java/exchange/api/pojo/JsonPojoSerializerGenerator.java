package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Generates a JSON message serializer class for a given POJO class using
 * Jackson.
 * The generated serializer class extends
 * {@link StdSerializer} and implements the
 * {@link JsonSerializer#serialize(Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)} method.
 * 
 * @see JsonGenerator
 * @see StdSerializer
 * @see JsonSerializer
 */
public class JsonPojoSerializerGenerator extends JavaTypeGenerator {

	private final String serializedTypeClassName;
	private final List<Field> fields;
	
	public JsonPojoSerializerGenerator(String serializedTypeClassName, List<Field> fields) {
		super(EndpointPojoGeneratorUtil.getSerializerClassName(serializedTypeClassName));
		this.serializedTypeClassName = serializedTypeClassName;
		this.fields = fields;
		setTypeDeclaration("public class");
		String simpleDeserializedClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(serializedTypeClassName);
		setParentClassName(StdSerializer.class.getName() + "<" + simpleDeserializedClassName + ">");
		setDescription("Jackson JSON Serializer for " 
						+ serializedTypeClassName 
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING 
						+ "\n@see " + simpleDeserializedClassName);
	}
	
	@Override
	public String generate() {
		addImport(IOException.class.getName());
		addImport(JsonGenerator.class);
		addImport(SerializerProvider.class);
		addImport(StdSerializer.class);
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
					field.getType().getCanonicalType().name().toLowerCase(),
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
	
	private String genWriteFieldInstruction(Field field, String getFieldValue) {
		if (!field.getType().getCanonicalType().isPrimitive) {
			return "gen.writeObjectField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
		}
		
		switch (field.getType().getCanonicalType()) {
		case STRING:
			return "gen.writeStringField(\"" + msgFieldName(field) + "\", String.valueOf(" + getFieldValue + "));\n";
		case BIGDECIMAL:
			addImport(EncodingUtil.class);
			return "gen.writeStringField(\"" + msgFieldName(field) + "\", EncodingUtil.bigDecimalToString(" + getFieldValue + "));\n";
		case BOOLEAN:
			return "gen.writeBooleanField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
		default: // INT LONG TIMESTAMP
			return "gen.writeNumberField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
		}
	}
	
	private static String msgFieldName(Field field) {
		return field.getMsgField() != null? field.getMsgField() : field.getName();
	}

}
