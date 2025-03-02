package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.util.JsonUtil;

/**
 * Generates a JSON message deserializer class for a given POJO class using
 * Jackson.
 * The generated deserializer class extends
 * {@link AbstractJsonMessageDeserializer} and implements the
 * {@link AbstractJsonMessageDeserializer#deserialize(JsonParser)} method.
 * 
 * @see JsonParser
 * @see AbstractJsonMessageDeserializer
 * @see JsonDeserializer
 * @see MessageDeserializer
 */
public class JsonMessageDeserializerGenerator extends JavaTypeGenerator {
	
	private final String deserializedTypeClassName;
	private final List<Field> fields;
	private final Set<String> nonPrimitiveTypeFieldsDeserializerDeclarations = new TreeSet<>();
	
	/**
	 * Constructor.
	 * 
	 * @param deserializedTypeClassName the fully qualified name of the POJO class to deserialize
	 * @param fields the properties of the POJO class
	 */
	public JsonMessageDeserializerGenerator(String deserializedTypeClassName, List<Field> fields) {
		super(ExchangeJavaGenUtil.getJsonMessageDeserializerClassName(deserializedTypeClassName));
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
	
	@Override
	public String generate() {
		addImport(IOException.class.getName());
		addImport(JsonParser.class.getName());
		addImport(JsonToken.class.getName());
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
			Type type = ExchangeJavaGenUtil.getFieldType(field);
			body.append(indent)
				.append("case \"")
				.append(field.getMsgField() != null? field.getMsgField() : field.getName())
				.append("\":\n");
			if (!type.getCanonicalType().isPrimitive) {
				body.append(dblIndent)
					.append("parser.nextToken();\n");
			}
			body.append(dblIndent)
				.append("msg.")
				.append(
					JavaCodeGenerationUtil.getSetAccessorMethodName(
							field.getName(),  
							fields.stream().map(Field::getName).collect(Collectors.toList())))
				.append("(")
				.append(getParseFieldInstruction(field))
				.append(");\n")	
				.append(indent).append("break;\n");
		});
		addImport("static " + JsonUtil.class.getName() + ".skipNextValue");
		body.append(indent).append("default:\n")
			.append(dblIndent)
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

	private String getParseFieldInstruction(Field field) {
		CanonicalType canonicalType = ExchangeJavaGenUtil.getFieldType(field).getCanonicalType();
		if (!canonicalType.isPrimitive) {
			return generateNonPrimitiveTypeFieldDeserializerDeclaration(field) +".deserialize(parser)";
		}
		
		switch (canonicalType) {
		case BIGDECIMAL:
			return getPrimitiveNonStringParseFieldInstruction("readNextBigDecimal");
		case BOOLEAN:
			return getPrimitiveNonStringParseFieldInstruction("readNextBoolean");
		case INT:
			return getPrimitiveNonStringParseFieldInstruction("readNextInteger");
		case LONG:
		case TIMESTAMP:
			return getPrimitiveNonStringParseFieldInstruction("readNextLong");
		default: // STRING
			return "parser.nextTextValue()";
		}
	}
	
	private String getPrimitiveNonStringParseFieldInstruction(String methodName) {
		addImport("static " + JsonUtil.class.getName() + "." + methodName);
		return methodName + "(parser)";
	}
	
	private String generateNonPrimitiveTypeFieldDeserializerDeclaration(Field field) {
		Type type = ExchangeJavaGenUtil.getFieldType(field);
		String objectFieldClassName = ExchangeApiGenUtil.getFieldLeafSubTypeClassName(
													field.getName(), 
													type, 
													field.getObjectName(),
													deserializedTypeClassName);
		String simpleDeserializerTypeName = generateNonPrimitiveFieldDeserializerClassName(
												type, 
												objectFieldClassName, 
												getImports());
		
		String deserializerVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(field.getName() + "Deserializer");
		String variableDeclaration = "private final " + simpleDeserializerTypeName + " " + deserializerVariableName + " = " 
										+ ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(
											type, 
											objectFieldClassName, 
											getImports()) 
										+ ";\n";
		nonPrimitiveTypeFieldsDeserializerDeclarations.add(variableDeclaration);
		return deserializerVariableName;
	}
	
	/**
	 * Generates the class name of the deserializer for a non-primitive field (see {@link CanonicalType#isPrimitive}).
	 * @param type the type of the field 
	 * @param objectClassName When the field is an object, the simple class name of the object class.
	 * @param imports The set of imports in context of the generated class. Necessary imports will be added to this set.
	 * @return The class name of the deserializer for the non primitive field.
	 */
	public static String generateNonPrimitiveFieldDeserializerClassName(Type type, String objectClassName, Imports imports) {	
		String fieldClass = null;
		switch (type.getCanonicalType()) {
		case OBJECT:
			String deserializerTypeName = ExchangeJavaGenUtil.getJsonMessageDeserializerClassName(objectClassName);
			imports.add(deserializerTypeName);
			return JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializerTypeName);
		case LIST:
			imports.add(ListJsonFieldDeserializer.class.getName());
			fieldClass = ExchangeJavaGenUtil.getClassNameForType(type.getSubType(), imports, objectClassName);
			return ListJsonFieldDeserializer.class.getSimpleName() 
						+ "<" 
						+ fieldClass
						+ ">";
		default: // MAP
			imports.add(MapJsonFieldDeserializer.class.getName());
			fieldClass = ExchangeJavaGenUtil.getClassNameForType(type.getSubType(), imports, objectClassName);
			return MapJsonFieldDeserializer.class.getSimpleName() 
						+ "<" 
						+ JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldClass)
						+ ">";
		}
	}
	
}
