package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ConstantValuePlaceholderResolverFactory;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Generates all java classes for a specific POJO for a REST or Websocket
 * endpoint. This means the java class for POJO itself, and also, if POJO
 * contains 'objects' fields (see {@link Type#isObject()}), the
 * POJOs for those objects. Remark: If an 'object' type field does not specify a
 * list of properties, corresponding class will not be generated. This can
 * happen for fields defining an objectName (see
 * {@link Field#getObjectName()} that has fields defined in another
 * endpoint.
 * 
 * @see PojoGenerator
 *
 */
public class PojoClassesGenerator implements ClassesGenerator {
  
  private final PojoGenerator rootPojoGenerator;
  private final List<Field> properties;
  private final PlaceHolderResolver docPlaceHolderResolver;
  private final ConstantValuePlaceholderResolverFactory constantsValuePlaceHolderResolverFactory;
  
  /**
   * Constructor.
   * 
   * @param className the name of the class
   * @param description the description to display in javadoc of the class
   * @param properties the fields of the class
   * @param implementedInterfaces the interfaces implemented by the class
   * @param docPlaceHolderResolver the resolver to use to resolve placeholders in descriptions.
   * @param constantValuePlaceHolderResolverFactory the factory to use for creating resolvers for constant value placeholders.
   * @throws IOException if an I/O error occurs
   */
  public PojoClassesGenerator(String className, 
       String description, 
       List<Field> properties, 
       List<String> implementedInterfaces,
       PlaceHolderResolver docPlaceHolderResolver,
       ConstantValuePlaceholderResolverFactory constantValuePlaceHolderResolverFactory) throws IOException {
    this.properties = properties;
    this.docPlaceHolderResolver = docPlaceHolderResolver;
    this.rootPojoGenerator = new PojoGenerator(
        className, 
        description, 
        properties, 
        implementedInterfaces, 
        docPlaceHolderResolver, 
        constantValuePlaceHolderResolverFactory);
    this.constantsValuePlaceHolderResolverFactory = constantValuePlaceHolderResolverFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    rootPojoGenerator.writeJavaFile(outputFolder);
    for (Field field: properties) {
      if (PojoGenUtil.isObjectField(field)) {
        generateObjectFieldTypePojos(outputFolder, rootPojoGenerator.getName(), field);
      }
    }
  }

  private void generateObjectFieldTypePojos(
      Path outputFolder, 
      String className, 
      Field field) throws IOException {
    String objectParamClassName = PojoGenUtil.getFieldLeafSubTypeClassName(
      field.getName(), 
      PojoGenUtil.getFieldType(field), 
      field.getObjectName(), 
      className);
    
    String description = PojoGenUtil.getObjectDescription(field);
    if (field.getProperties() != null) {
      PojoClassesGenerator subGen = new PojoClassesGenerator(
          objectParamClassName, 
          description, 
          field.getProperties(),
          field.getImplementedInterfaces(),
          docPlaceHolderResolver,
          constantsValuePlaceHolderResolverFactory);
      subGen.generateClasses(outputFolder);
    }
  }
}