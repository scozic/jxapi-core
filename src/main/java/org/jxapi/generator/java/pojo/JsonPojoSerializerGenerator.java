package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import org.jxapi.netutils.serialization.json.MapJsonValueSerializer;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Generates a JSON message serializer class for a given POJO class using
 * Jackson.
 * The generated serializer class extends
 * {@link StdSerializer} and implements the
 * {@link StdSerializer#serialize(Object, JsonGenerator, SerializerProvider)} method.
 * 
 * @see JsonGenerator
 * @see StdSerializer
 * @see JsonSerializer
 */
public class JsonPojoSerializerGenerator extends JavaTypeGenerator {
  
  private static final String GEN = "(gen, ";

  private static final String STATIC = "static ";

  private final String serializedTypeClassName;
  private final List<Field> fields;
  private final Set<String> nonPrimitiveTypeFieldsSerializerDeclarations = new TreeSet<>();
  
  /**
   * Constructor.
   * 
   * @param serializedTypeClassName the fully qualified name of the POJO class to
   *                                serialize
   * @param fields                  the properties of the POJO class
   */
  public JsonPojoSerializerGenerator(String serializedTypeClassName, List<Field> fields) {
    super(PojoGenUtil.getSerializerClassName(serializedTypeClassName));
    this.serializedTypeClassName = serializedTypeClassName;
    this.fields = fields;
    setTypeDeclaration("public class");
    String simpleDeserializedClassName = JavaCodeGenUtil.getClassNameWithoutPackage(serializedTypeClassName);
    setParentClassName(AbstractJsonValueSerializer.class.getName() + "<" + simpleDeserializedClassName + ">");
    setDescription("Jackson JSON Serializer for " 
            + serializedTypeClassName 
            + "\n@see " + simpleDeserializedClassName);
  }
  
  @Override
  public String generate() {
    addImport(IOException.class.getName());
    addImport(JsonGenerator.class);
    addImport(SerializerProvider.class);
    addImport(AbstractJsonValueSerializer.class);
    addImport(serializedTypeClassName);
    nonPrimitiveTypeFieldsSerializerDeclarations.forEach(this::appendToBody);
    appendToBody("\n");
    appendToBody(generateSerialVersionUidDeclaration());
    generateConstructor();
    generateSerializeMethod();
    return super.generate();
  }
  
  private String generateSerialVersionUidDeclaration() {
	    return "private static final long serialVersionUID = " 
	        + PojoGenUtil.generateSerialVersionUid(getName(), fields, List.of()) 
	        + "L;\n\n";
  }

  private void generateConstructor() {
    appendMethod("public " + getSimpleName() + "()", 
                "super(" + JavaCodeGenUtil.getClassNameWithoutPackage(serializedTypeClassName) + ".class);",
                "Constructor");
    appendToBody("\n");  
  }

  private void generateSerializeMethod() {
    StringBuilder body = new StringBuilder();
    String simpleSerializedClassName = JavaCodeGenUtil.getClassNameWithoutPackage(serializedTypeClassName);
    body.append("gen.writeStartObject();\n");
    fields.forEach(field -> {
      StringBuilder beforeParseFieldInstruction = new StringBuilder();
      String parseFieldInstruction = getParseFieldInstruction(field, beforeParseFieldInstruction);
      body.append(beforeParseFieldInstruction.toString())
          .append(parseFieldInstruction);
    });
    body.append("gen.writeEndObject();");
    nonPrimitiveTypeFieldsSerializerDeclarations.forEach(this::appendToBody);
    appendToBody("\n");
    appendMethod("@Override\npublic void serialize(" 
            + simpleSerializedClassName 
            + " value, JsonGenerator gen, SerializerProvider provider) throws IOException", 
           body.toString());
  }
  
  private String getParseFieldInstruction(Field field, StringBuilder beforeParseFieldInstruction) {
    Type type = PojoGenUtil.getFieldType(field);
    String getFieldValue = "value." + JavaCodeGenUtil.getGetAccessorMethodName(
        field.getName(),
        type,
        fields.stream().map(Field::getName).toList()) + "()";
    CanonicalType canonicalType = PojoGenUtil.getFieldType(field).getCanonicalType();
    if (!canonicalType.isPrimitive) {
      return generateNonPrimitiveTypeFieldSerializerDeclaration(field, getFieldValue, beforeParseFieldInstruction);
    }
    
    String msgFieldName = getMsgField(field);
    switch (canonicalType) {
    case BIGDECIMAL:
      return getPrimitiveParseFieldInstruction("writeBigDecimalField", msgFieldName, getFieldValue);
    case BOOLEAN:
      return getPrimitiveParseFieldInstruction("writeBooleanField", msgFieldName, getFieldValue);
    case INT:
      return getPrimitiveParseFieldInstruction("writeIntField", msgFieldName, getFieldValue);
    case LONG:
      return getPrimitiveParseFieldInstruction("writeLongField", msgFieldName, getFieldValue);
    default: // STRING
      return getPrimitiveParseFieldInstruction("writeStringField", msgFieldName, getFieldValue);
    }
  }
  
  private static String getMsgField(Field field) {
    return Optional.ofNullable(field.getMsgField()).orElse(field.getName());
  }
  
  private String getPrimitiveParseFieldInstruction(String methodName, String msgFieldName, String getFieldValue) {
    addImport(STATIC + JsonUtil.class.getName() + "." + methodName);
    return new StringBuilder()
        .append(methodName)
        .append(GEN)
        .append(JavaCodeGenUtil.getQuotedString(msgFieldName))
        .append(", ")
        .append(getFieldValue)
        .append(");\n")
        .toString();
  }
  
  private String generateNonPrimitiveTypeFieldSerializerDeclaration(Field field, String getFieldValue, StringBuilder beforeParseFieldInstruction) {
    String msgFieldName = getMsgField(field);
    String objectName = field.getObjectName();
    if (JavaCodeGenUtil.isFullClassName(objectName)) {
      return getFullClassObjectNameParseFieldInstruction(msgFieldName, getFieldValue);
    }
    Type type = PojoGenUtil.getFieldType(field);
    String objectFieldClassName = PojoGenUtil.getFieldLeafSubTypeClassName(
      field.getName(), 
      type, 
      objectName,
      serializedTypeClassName);
    String simpleSerializerTypeName = generateNonPrimitiveFieldSeserializerClassName(
      type, 
      objectFieldClassName, 
      getImports());
    
    String serializerVariableName = JavaCodeGenUtil.firstLetterToLowerCase(field.getName() + "Serializer");
    String newSerializerInstruction = PojoGenUtil.getNewJsonValueSerializerInstruction(
            type,
            objectFieldClassName, 
            false,
            getImports());
    StringBuilder variableDeclaration = new StringBuilder()
        .append("private ");
    if (!type.isObject()) {
      variableDeclaration.append("final ");
    }
    variableDeclaration.append(simpleSerializerTypeName)
      .append(" ")
      .append(serializerVariableName);
    if (type.isObject()) {
      variableDeclaration.append(";\n");
    } else {
      variableDeclaration
      .append(" = ")
      .append(newSerializerInstruction)
      .append(";\n");
    }
    
    nonPrimitiveTypeFieldsSerializerDeclarations.add(variableDeclaration.toString());
    
    StringBuilder createObjectSerializerInstruction = new StringBuilder();
    if (type.isObject()) {
      createObjectSerializerInstruction
        .append("if(")
        .append(serializerVariableName)
        .append(" == null) {\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(serializerVariableName)
        .append(" = ")
        .append(newSerializerInstruction)
        .append(";\n}");
      beforeParseFieldInstruction.append(
        createObjectSerializerInstruction.toString())
        .append("\n");
    }
    String methodName = "writeCustomSerializerField";
    addImport(STATIC + JsonUtil.class.getName() + "." + methodName);
    return new StringBuilder()
        .append(methodName)
        .append(GEN)
        .append(JavaCodeGenUtil.getQuotedString(msgFieldName))
        .append(", ")
        .append(getFieldValue)
        .append(", ")
        .append(serializerVariableName)
        .append(", provider);\n")
        .toString();
  }
  
  private String getFullClassObjectNameParseFieldInstruction(String msgFieldName, String getFieldValue) {
    String methodName = "writeObjectField";
    addImport(STATIC + JsonUtil.class.getName() + "." + methodName);
    return new StringBuilder()
        .append(methodName)
        .append(GEN)
        .append(JavaCodeGenUtil.getQuotedString(msgFieldName))
        .append(", ")
        .append(getFieldValue)
        .append(");\n")
        .toString();
  }
  
  /**
   * Generates the class name of the serializer for a non-primitive field (see {@link CanonicalType#isPrimitive}).
   * @param type the type of the field 
   * @param objectClassName When the field is an object, the simple class name of the object class.
   * @param imports The set of imports in context of the generated class. Necessary imports will be added to this set.
   * @return The class name of the serializer for the non primitive field.
   */
  public static String generateNonPrimitiveFieldSeserializerClassName(Type type, String objectClassName, Imports imports) {  
    String fieldClass = null;
    switch (type.getCanonicalType()) {
    case OBJECT:
      String serializerTypeName = PojoGenUtil.getSerializerClassName(objectClassName);
      imports.add(serializerTypeName);
      return JavaCodeGenUtil.getClassNameWithoutPackage(serializerTypeName);
    case LIST:
      imports.add(ListJsonValueSerializer.class.getName());
      fieldClass = PojoGenUtil.getClassNameForType(type.getSubType(), imports, objectClassName);
      return ListJsonValueSerializer.class.getSimpleName() 
            + "<" 
            + fieldClass
            + ">";
    default: // MAP
      imports.add(MapJsonValueSerializer.class.getName());
      fieldClass = PojoGenUtil.getClassNameForType(type.getSubType(), imports, objectClassName);
      return MapJsonValueSerializer.class.getSimpleName() 
            + "<" 
            + JavaCodeGenUtil.getClassNameWithoutPackage(fieldClass)
            + ">";
    }
  }
}
