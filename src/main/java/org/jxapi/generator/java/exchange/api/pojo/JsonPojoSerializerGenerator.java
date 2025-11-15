package org.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.util.EncodingUtil;

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

  private final String serializedTypeClassName;
  private final List<Field> fields;
  
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
    setParentClassName(StdSerializer.class.getName() + "<" + simpleDeserializedClassName + ">");
    setDescription("Jackson JSON Serializer for " 
            + serializedTypeClassName 
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
    appendMethod("public " + getSimpleName() + "()", 
                "super(" + JavaCodeGenUtil.getClassNameWithoutPackage(serializedTypeClassName) + ".class);",
                "Constructor");
    appendToBody("\n");  
  }

  private void generateDeserializeMethod() {
    StringBuilder body = new StringBuilder();
    String simpleDeserializedClassName = JavaCodeGenUtil.getClassNameWithoutPackage(serializedTypeClassName);
    body.append("gen.writeStartObject();\n");
    fields.forEach(field -> {
      Type type = ExchangeGenUtil.getFieldType(field);
      String getFieldValue = "value." + JavaCodeGenUtil.getGetAccessorMethodName(
          field.getName(),
          type.getCanonicalType().name().toLowerCase(),
          fields.stream().map(Field::getName).collect(Collectors.toList())) + "()";
      body.append("if (").append(getFieldValue).append(" != null)");
      body.append(JavaCodeGenUtil.generateCodeBlock(genWriteFieldInstruction(field, getFieldValue)));
    });
    body.append("gen.writeEndObject();");
    appendMethod("@Override\npublic void serialize(" 
            + simpleDeserializedClassName 
            + " value, JsonGenerator gen, SerializerProvider provider) throws IOException", 
           body.toString());
  }
  
  private String genWriteFieldInstruction(Field field, String getFieldValue) {
    Type type = ExchangeGenUtil.getFieldType(field);
    if (!type.getCanonicalType().isPrimitive) {
      return "gen.writeObjectField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
    }
    
    switch (type.getCanonicalType()) {
    case STRING:
      return "gen.writeStringField(\"" + msgFieldName(field) + "\", String.valueOf(" + getFieldValue + "));\n";
    case BIGDECIMAL:
      addImport(EncodingUtil.class);
      return "gen.writeStringField(\"" + msgFieldName(field) + "\", EncodingUtil.bigDecimalToString(" + getFieldValue + "));\n";
    case BOOLEAN:
      return "gen.writeBooleanField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
    default: // INT LONG
      return "gen.writeNumberField(\"" + msgFieldName(field) + "\", " + getFieldValue + ");\n";
    }
  }
  
  private static String msgFieldName(Field field) {
    return field.getMsgField() != null? field.getMsgField() : field.getName();
  }

}
