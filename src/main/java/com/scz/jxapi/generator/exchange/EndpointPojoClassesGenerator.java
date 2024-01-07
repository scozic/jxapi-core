package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

/**
 * Generates all java classes for a specific POJO for a REST or Websocket
 * endpoint. This means the java class for POJO itself, and also, if POJO
 * contains 'objects' fields (see {@link EndpointParameterType#isObject()}), the
 * POJOs for those objects. Remark: If an 'object' type field does not specify a
 * list of parameters, corresponding class will not be generated. This can
 * happen for fields defining an objectName (see
 * {@link EndpointParameter#getObjectName()} that has fields defined in another
 * enpoint.
 * 
 * @author Sylvestre COZIC
 *
 */
public class EndpointPojoClassesGenerator implements ClassesGenerator {
	
	private final EndpointPojoGenerator rootPojoGenerator;
	private final List<EndpointParameter> fields;
	
	public EndpointPojoClassesGenerator(String className, 
			 String description, 
			 List<EndpointParameter> fields, 
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
		for (EndpointParameter field: fields) {
			if (field.getEndpointParameterType().isObject()) {
				generateObjectParameterTypePojoField(outputFolder, rootPojoGenerator.getName(), field);
			}
		}
	}


	private void generateObjectParameterTypePojoField(Path outputFolder, String className, EndpointParameter field) throws IOException {
		String objectParamClassName = EndpointParameterTypeGenerationUtil.getLeafObjectParameterClassName(
												field.getName(), 
												field.getEndpointParameterType(), 
												field.getObjectName(), 
												new HashSet<>(), 
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