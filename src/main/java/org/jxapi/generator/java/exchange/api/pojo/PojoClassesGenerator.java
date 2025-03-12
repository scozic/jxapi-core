package org.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;

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
	
	/**
	 * Constructor.
	 * 
	 * @param className the name of the class
	 * @param description the description to display in javadoc of the class
	 * @param properties the fields of the class
	 * @param implementedInterfaces the interfaces implemented by the class
	 * @throws IOException if an I/O error occurs
	 */
	public PojoClassesGenerator(String className, 
			 String description, 
			 List<Field> properties, 
			 List<String> implementedInterfaces) throws IOException {
		this.properties = properties;
		this.rootPojoGenerator = new PojoGenerator(className, description, properties, implementedInterfaces);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		rootPojoGenerator.writeJavaFile(outputFolder);
		for (Field field: properties) {
			if (ExchangeJavaGenUtil.isObjectField(field)) {
				generateObjectFieldTypePojos(outputFolder, rootPojoGenerator.getName(), field);
			}
		}
	}

	private void generateObjectFieldTypePojos(Path outputFolder, String className, Field field) throws IOException {
		String objectParamClassName = ExchangeApiGenUtil.getFieldLeafSubTypeClassName(
												field.getName(), 
												ExchangeJavaGenUtil.getFieldType(field), 
												field.getObjectName(), 
												className);
		
		if (field.getProperties() != null) {
			new PojoClassesGenerator(objectParamClassName, 
									  field.getDescription(), 
									  field.getProperties(),
									  field.getImplementedInterfaces()).generateClasses(outputFolder);
		}
	}
}