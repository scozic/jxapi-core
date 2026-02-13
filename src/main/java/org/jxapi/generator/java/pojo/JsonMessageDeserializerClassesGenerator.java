package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

/**
 * Generator for all classes for deserializing from JSON a particular POJO, that is:
 * <ul>
 * <li>A class extending {@link AbstractJsonMessageDeserializer} for serialization of POJO.
 * <li>For each of 'Object' type {@link Field} belonging to that pojo, the deserializer classes for corresponding nested POJO.
 * </ul>
 */
public class JsonMessageDeserializerClassesGenerator implements ClassesGenerator {
  
  private String deserializedClassName;
  private List<Field> fields;
  
  /**
   * Constructor.
   * 
   * @param deserializedClassName the fully qualified name of the POJO class to deserialize
   * @param fields the properties of the POJO class
   * @throws IllegalArgumentException if fields is <code>null</code>
   */
  public JsonMessageDeserializerClassesGenerator(String deserializedClassName, List<Field> fields) {
    this.deserializedClassName = deserializedClassName;
    if (fields == null) {
      throw new IllegalArgumentException("null fields provided for " + deserializedClassName);
    }
    this.fields = fields;
  }

  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    Imports imports = new Imports();
    for (Field field: fields) {
      if ((PojoGenUtil.isObjectField(field))
        && field.getProperties() != null) {
        Field objectParam = Field.builder()
                     .name(field.getName())
                     .type(Type.OBJECT)
                     .description(field.getDescription())
                     .msgField(field.getMsgField())
                     .properties(field.getProperties())
                     .objectName(field.getObjectName())
                     .build();
        new JsonMessageDeserializerClassesGenerator(
            JavaCodeGenUtil.getClassPackage(deserializedClassName) 
              + "."
              + PojoGenUtil.getClassNameForField(
                  objectParam, 
                  imports, 
                  deserializedClassName), 
            field.getProperties())
          .generateClasses(outputFolder);
      }
    }
    JsonPojoDeserializerGenerator deserializerGenerator = new JsonPojoDeserializerGenerator(deserializedClassName, fields);
    deserializerGenerator.writeJavaFile(outputFolder);
  }

}
