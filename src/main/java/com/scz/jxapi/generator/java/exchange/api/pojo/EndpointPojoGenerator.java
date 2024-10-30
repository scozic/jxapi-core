package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.PojoField;
import com.scz.jxapi.generator.java.PojoGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.util.CollectionUtil;

/**
 * Specific {@link PojoGenerator} for REST/Websocket endpoints
 * request/response/message 'object' properties and sub-properties.
 * <ul>
 * <li>Such POJOs are annotated with
 * {@link com.fasterxml.jackson.databind.annotation.JsonSerialize} to use a
 * custom serializer.</li>
 * <li>Properties of the POJOs are generated according to the properties of the
 * API method request/response/message {@link Field} when it is of 'object'
 * type.</li>
 * <li>Can be supplemented with additional class body and implemented
 * interfaces.</li>
 * </ul>
 */
public class EndpointPojoGenerator extends PojoGenerator {

	/**
	 * Constructor.
	 * 
	 * @param className the name of the class
	 * @param description the description to display in javadoc of the class
	 * @param fields the fields of the class
	 * @param implementedInterfaces the interfaces implemented by the class
	 * @param additionnalClassBody the additionnal body of the class
	 * @throws IOException if an I/O error occurs
	 */
	public EndpointPojoGenerator(String className, 
								 String description, 
								 List<Field> fields, 
								 List<String> implementedInterfaces, 
								 String additionnalClassBody) throws IOException {
		super(className);
		String serializerClassName = EndpointPojoGeneratorUtil.getSerializerClassName(className);
		addImport(serializerClassName);
		addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
		setTypeDeclaration("@JsonSerialize(using = " 
										+ JavaCodeGenerationUtil.getClassNameWithoutPackage(serializerClassName) 
										+ ".class)\n" 
										+ getTypeDeclaration());
		setDescription(description);
		setImplementedInterfaces(implementedInterfaces);
		if (!CollectionUtil.isEmpty(fields)) {
			for (Field field: fields) {
				if (field.getType().isObject()) {
					generateObjectTypePojoField(field);
				} else {
					generateSimpleTypePojoField(field);
				}
			}
		}
	
		if (additionnalClassBody != null) {
			appendToBody(additionnalClassBody);
		}
	}
	
	private void generateObjectTypePojoField(Field field) throws IOException {
		String className = getName();
		String objectParamClassName = ExchangeApiGeneratorUtil.getFieldLeafSubTypeClassName(
																				field.getName(), 
																				field.getType(), 
																				field.getObjectName(), 
																				className);
		addImport(objectParamClassName);
		String objectClass = ExchangeApiGeneratorUtil.getClassNameForField(field, getImports(), className);
		addField(PojoField.create(objectClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	private void generateSimpleTypePojoField(Field field) {
		String fieldClass = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(field.getType(), getImports(), null);
		fieldClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldClass);
		addField(PojoField.create(fieldClass, field.getName(), field.getMsgField(), field.getDescription()));
	}

}
