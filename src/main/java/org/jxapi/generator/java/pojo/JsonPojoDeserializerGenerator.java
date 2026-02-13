package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.JsonUtil;

/**
 * Generates a JSON deserializer class for a given POJO class using
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
public class JsonPojoDeserializerGenerator extends JavaTypeGenerator {
  
  private static final String STATIC = "static ";
  private final String deserializedTypeClassName;
  private final List<Field> fields;
  private final Set<String> nonPrimitiveTypeFieldsDeserializerDeclarations = new TreeSet<>();
  
  /**
   * Constructor.
   * 
   * @param deserializedTypeClassName the fully qualified name of the POJO class to deserialize
   * @param fields the properties of the POJO class
   */
  public JsonPojoDeserializerGenerator(String deserializedTypeClassName, List<Field> fields) {
    super(PojoGenUtil.getJsonMessageDeserializerClassName(deserializedTypeClassName));
    this.deserializedTypeClassName = deserializedTypeClassName;
    this.fields = fields;
    setTypeDeclaration("public class");
    String simpleDeserializedClassName = JavaCodeGenUtil.getClassNameWithoutPackage(deserializedTypeClassName);
    setParentClassName(AbstractJsonMessageDeserializer.class.getName() + "<" + simpleDeserializedClassName + ">");
    setDescription("Parses incoming JSON messages into " 
            + deserializedTypeClassName 
            + " instances\n"
            + "@see " + deserializedTypeClassName);
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
    String indent = JavaCodeGenUtil.INDENTATION;
    StringBuilder body = new StringBuilder();
    String simpleDeserializedClassName = JavaCodeGenUtil.getClassNameWithoutPackage(deserializedTypeClassName);
    body.append(simpleDeserializedClassName)
        .append(" msg = new ")
        .append(simpleDeserializedClassName)
        .append("();\nwhile(parser.nextToken() != JsonToken.END_OBJECT) {\n");
    body.append(indent)
        .append("switch(parser.currentName()) {\n");
    String dblIndent = indent + indent;
    fields.forEach(field -> {
      Type type = PojoGenUtil.getFieldType(field);
      body.append(indent)
        .append("case \"")
        .append(Optional.ofNullable(field.getMsgField()).orElse(field.getName()))
        .append("\":\n");
      if (!type.getCanonicalType().isPrimitive 
          && !JavaCodeGenUtil.isFullClassName(field.getObjectName())) {
        body.append(dblIndent)
            .append("parser.nextToken();\n");
      }
      StringBuilder beforeParseFieldInstruction = new StringBuilder();
      String parseFieldInstruction = getParseFieldInstruction(field, beforeParseFieldInstruction);
      body.append(beforeParseFieldInstruction.toString())
        .append(dblIndent)
        .append("msg.")
        .append(
          JavaCodeGenUtil.getSetAccessorMethodName(
              field.getName(),  
              fields.stream().map(Field::getName).collect(Collectors.toList())))
        .append("(")
        .append(parseFieldInstruction)
        .append(");\n")  
        .append(indent).append("break;\n");
    });
    addImport(STATIC + JsonUtil.class.getName() + ".skipNextValue");
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

  private String getParseFieldInstruction(Field field, StringBuilder beforeParseFieldInstruction) {
    CanonicalType canonicalType = PojoGenUtil.getFieldType(field).getCanonicalType();
    if (!canonicalType.isPrimitive) {
      return generateNonPrimitiveTypeFieldDeserializerDeclaration(field, beforeParseFieldInstruction);
    }
    
    switch (canonicalType) {
    case BIGDECIMAL:
      return getPrimitiveParseFieldInstruction("readNextBigDecimal");
    case BOOLEAN:
      return getPrimitiveParseFieldInstruction("readNextBoolean");
    case INT:
      return getPrimitiveParseFieldInstruction("readNextInteger");
    case LONG:
      return getPrimitiveParseFieldInstruction("readNextLong");
    default: // STRING
      return getPrimitiveParseFieldInstruction("readNextString");
    }
  }
  
  private String getPrimitiveParseFieldInstruction(String methodName) {
    addImport(STATIC + JsonUtil.class.getName() + "." + methodName);
    return methodName + "(parser)";
  }
  
  private String generateNonPrimitiveTypeFieldDeserializerDeclaration(Field field, StringBuilder beforeParseFieldInstruction) {
    String objectName = field.getObjectName();
    if (JavaCodeGenUtil.isFullClassName(objectName)) {
      return getFullClassObjectNameParseFieldInstruction(field);
    }
    Type type = PojoGenUtil.getFieldType(field);
    String objectFieldClassName = PojoGenUtil.getFieldLeafSubTypeClassName(
      field.getName(), 
      type, 
      objectName,
      deserializedTypeClassName);
    String simpleDeserializerTypeName = generateNonPrimitiveFieldDeserializerClassName(
      type, 
      objectFieldClassName, 
      getImports());
    
    String deserializerVariableName = JavaCodeGenUtil.firstLetterToLowerCase(field.getName() + "Deserializer");
    String newDeserializerInstruction = PojoGenUtil.getNewJsonFieldDeserializerInstruction(
            type,
            objectFieldClassName, 
            false,
            getImports());
    StringBuilder variableDeclaration = new StringBuilder()
        .append("private ");
    if (!type.isObject()) {
      variableDeclaration.append("final ");
    }
    variableDeclaration.append(simpleDeserializerTypeName)
      .append(" ")
      .append(deserializerVariableName);
    if (type.isObject()) {
      variableDeclaration.append(";\n");
    } else {
      variableDeclaration
      .append(" = ")
      .append(newDeserializerInstruction)
      .append(";\n");
    }
    
    nonPrimitiveTypeFieldsDeserializerDeclarations.add(variableDeclaration.toString());
    
    StringBuilder createObjectDeserializerInstruction = new StringBuilder();
    if (type.isObject()) {
      createObjectDeserializerInstruction
        .append("if(")
        .append(deserializerVariableName)
        .append(" == null) {\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(deserializerVariableName)
        .append(" = ")
        .append(newDeserializerInstruction)
        .append(";\n}");
      beforeParseFieldInstruction.append(
        JavaCodeGenUtil.indent(
          JavaCodeGenUtil.indent(createObjectDeserializerInstruction.toString())))
        .append("\n");
    }
    return deserializerVariableName + ".deserialize(parser)";
  }
  
  private String getFullClassObjectNameParseFieldInstruction(Field field) {
    String objectName = field.getObjectName();
    String res = "readNextObject(parser";
    addImport(STATIC + JsonUtil.class.getName() + ".readNextObject");
    if (Object.class.getName().equals(objectName)) {
      return res + ")";
    } else {
      return res + ", " + objectName + ".class)";
    }
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
      String deserializerTypeName = PojoGenUtil.getJsonMessageDeserializerClassName(objectClassName);
      imports.add(deserializerTypeName);
      return JavaCodeGenUtil.getClassNameWithoutPackage(deserializerTypeName);
    case LIST:
      imports.add(ListJsonFieldDeserializer.class.getName());
      fieldClass = PojoGenUtil.getClassNameForType(type.getSubType(), imports, objectClassName);
      return ListJsonFieldDeserializer.class.getSimpleName() 
            + "<" 
            + fieldClass
            + ">";
    default: // MAP
      imports.add(MapJsonFieldDeserializer.class.getName());
      fieldClass = PojoGenUtil.getClassNameForType(type.getSubType(), imports, objectClassName);
      return MapJsonFieldDeserializer.class.getSimpleName() 
            + "<" 
            + JavaCodeGenUtil.getClassNameWithoutPackage(fieldClass)
            + ">";
    }
  }
  
}
