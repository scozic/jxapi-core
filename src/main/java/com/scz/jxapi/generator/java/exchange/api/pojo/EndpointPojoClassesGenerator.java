package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;

/**
 * Generates all java classes for a specific POJO for a REST or Websocket
 * endpoint. This means the java class for POJO itself, and also, if POJO
 * contains 'objects' fields (see {@link Type#isObject()}), the
 * POJOs for those objects. Remark: If an 'object' type field does not specify a
 * list of parameters, corresponding class will not be generated. This can
 * happen for fields defining an objectName (see
 * {@link Field#getObjectName()} that has fields defined in another
 * enpoint.
 * 
 * @author Sylvestre COZIC
 *
 */
public class EndpointPojoClassesGenerator implements ClassesGenerator {
	
	private final EndpointPojoGenerator rootPojoGenerator;
	private final List<Field> fields;
	
	public EndpointPojoClassesGenerator(String className, 
			 String description, 
			 List<Field> fields, 
			 List<String> implementedInterfaces, 
			 String additionnalClassBody) throws IOException {
		this.fields = fields;
		this.rootPojoGenerator = new EndpointPojoGenerator(className, description, fields, implementedInterfaces, additionnalClassBody);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		rootPojoGenerator.writeJavaFile(outputFolder);
		for (Field field: fields) {
			if (field.getType().isObject()) {
				generateObjectParameterTypePojoField(outputFolder, rootPojoGenerator.getName(), field);
			}
		}
	}


	private void generateObjectParameterTypePojoField(Path outputFolder, String className, Field field) throws IOException {
		String objectParamClassName = ExchangeApiGeneratorUtil.getLeafObjectFieldClassName(
												field.getName(), 
												field.getType(), 
												field.getObjectName(), 
												className);
		
		if (field.getParameters() != null) {
			new EndpointPojoClassesGenerator(objectParamClassName, 
									  field.getDescription(), 
									  field.getParameters(),
									  field.getImplementedInterfaces(), 
									  null).generateClasses(outputFolder);
		}
	}
}