package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.PojoField;
import com.scz.jxapi.generator.PojoGenerator;

/**
 * Specific {@link PojoGenerator} for REST/Websocket endpoints request/response POJOs.
 * @author Sylvestre COZIC
 *
 */
public class EndpointPojoGenerator extends PojoGenerator {
	
	private static final Logger log = LoggerFactory.getLogger(EndpointPojoGenerator.class);

	public EndpointPojoGenerator(String className, 
								 String description, 
								 List<EndpointParameter> fields, 
								 List<String> implementedInterfaces, 
								 String additionnalClassBody) throws IOException {
		super(className);
		if (log.isDebugEnabled())
			log.debug("Generating POJO:" + className + " with fields:" 
						+ StringUtils.truncate(String.valueOf(fields), 192) 
						+ ", implemented interfaces:" + implementedInterfaces);
		String serializerClassName = ExchangeJavaWrapperGeneratorUtil.getSerializerClassName(className);
		addImport(serializerClassName);
		addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
		setTypeDeclaration("@JsonSerialize(using = " 
										+ JavaCodeGenerationUtil.getClassNameWithoutPackage(serializerClassName) 
										+ ".class)\n" 
										+ getTypeDeclaration());
		setDescription(description);
		setImplementedInterfaces(implementedInterfaces);
		for (EndpointParameter field: fields) {
			if (field.getEndpointParameterType().isObject()) {
				generateObjectParameterTypePojoField(field);
			} else {
				generateSimpleParameterTypePojoField(field);
			}
		}
		if (additionnalClassBody != null) {
			appendToBody(additionnalClassBody);
		}
	}
	
	private void generateObjectParameterTypePojoField(EndpointParameter field) throws IOException {
		String className = getName();
		String objectParamClassName = EndpointParameterTypeGenerationUtil.getLeafObjectParameterClassName(
																				field.getName(), 
																				field.getEndpointParameterType(), 
																				field.getObjectName(), 
																				getImports(), 
																				className);
		addImport(objectParamClassName);
		String objectClass = EndpointParameterTypeGenerationUtil.getClassNameForEndpointParameter(field, getImports(), className);
		addField(PojoField.create(objectClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	private void generateSimpleParameterTypePojoField(EndpointParameter field) {
		String parameterClass = EndpointParameterTypeGenerationUtil.getClassNameForParameterType(field.getEndpointParameterType(), getImports(), null);
		if (!parameterClass.startsWith("java.lang") && parameterClass.contains(".")) {
			addImport(parameterClass);
			parameterClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(parameterClass);
		}
		addField(PojoField.create(parameterClass, field.getName(), field.getMsgField(), field.getDescription()));
	}

}
