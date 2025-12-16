package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

/**
 * Generator for all classes for deserializing from JSON a particular POJO, that is:
 * <ul>
 * <li>A class extending {@link AbstractJsonMessageDeserializer} for serialization of POJO.
 * <li>For each of 'Object' type {@link Field} belonging to that POJO, the deserializer class for corresponding nested POJO.
 * </ul>
 */
public class JsonPojoSerializerClassesGenerator implements ClassesGenerator {
  
  private String deserializedClassName;
  private List<Field> fields;
  
  /**
   * Constructor.
   * 
   * @param deserializedClassName the fully qualified name of the POJO class to deserialize
   * @param fields the properties of the POJO class
   * @throws IllegalArgumentException if fields is <code>null</code>
   */
  public JsonPojoSerializerClassesGenerator(String deserializedClassName, List<Field> fields) {
    this.deserializedClassName = deserializedClassName;
    if (fields == null) {
      throw new IllegalArgumentException("null fields for " + deserializedClassName);
    }
    this.fields = fields;
  }

  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(deserializedClassName, fields);
    for (Field field: fields) {
      Type type = PojoGenUtil.getFieldType(field);
      if ((type.isObject())
        && field.getProperties() != null) {
        new JsonPojoSerializerClassesGenerator( 
                   PojoGenUtil.getFieldLeafSubTypeClassName(
                         field.getName(), 
                         type, 
                         field.getObjectName(), 
                         deserializedClassName),
                   field.getProperties()).generateClasses(outputFolder);
      }
    }
    
    generator.writeJavaFile(outputFolder);
    
  }

}
