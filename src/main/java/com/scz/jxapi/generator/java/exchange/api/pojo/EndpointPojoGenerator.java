package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.PojoField;
import com.scz.jxapi.generator.java.PojoGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.util.CollectionUtil;
import com.scz.jxapi.util.DeepCloneable;

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
@Deprecated
public class EndpointPojoGenerator extends PojoGenerator {
	
	private final List<Field> fields;

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
								 List<String> implementedInterfaces) throws IOException {
		super(className);
		this.fields = fields;
		String serializerClassName = EndpointPojoGeneratorUtil.getSerializerClassName(className);
		addImport(serializerClassName);
		addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
		setTypeDeclaration("@JsonSerialize(using = " 
										+ JavaCodeGenerationUtil.getClassNameWithoutPackage(serializerClassName) 
										+ ".class)\n" 
										+ getTypeDeclaration());
		setDescription(description);
		addImport(DeepCloneable.class.getName());
		String deepClonable = DeepCloneable.class.getName() + "<" + getSimpleName() + ">";
		setImplementedInterfaces(CollectionUtil.mergeLists(List.of(deepClonable), implementedInterfaces));
		if (!CollectionUtil.isEmpty(fields)) {
			for (Field field: fields) {
				if (ExchangeJavaGenUtil.isObjectField(field)) {
					generateObjectTypePojoField(field);
				} else {
					generateSimpleTypePojoField(field);
				}
			}
		}
	
	}
	
	@Override
	protected void generateAdditionalBody() {
		body.append("\n");
		generateDeepCloneMethod();
	}
	
	private void generateObjectTypePojoField(Field field) {
		String className = getName();
		Type fieldType = ExchangeJavaGenUtil.getFieldType(field);
		String objectParamClassName = ExchangeApiGeneratorUtil.getFieldLeafSubTypeClassName(
																				field.getName(), 
																				fieldType, 
																				field.getObjectName(), 
																				className);
		addImport(objectParamClassName);
		String fieldClass = ExchangeApiGeneratorUtil.getClassNameForField(field, getImports(), className);
		addField(PojoField.create(fieldClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	private void generateSimpleTypePojoField(Field field) {
		String fieldClass = ExchangeJavaGenUtil.getClassNameForType(ExchangeJavaGenUtil.getFieldType(field), getImports(), null);
		fieldClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldClass);
		addField(PojoField.create(fieldClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	private void generateDeepCloneMethod() {
		String signature = "@Override\npublic " + getSimpleName() + " deepClone()";
		String newDeclaration = "new " + getSimpleName() + "();\n";
		StringBuilder body = new StringBuilder();
		if (CollectionUtil.isEmpty(fields)) {
			body.append("return ").append(newDeclaration);
			
		} else {
			body.append(getSimpleName())
			.append(" clone = ")
			.append(newDeclaration);
		
			fields.forEach(f -> 
				body.append("clone.")
			    	.append(f.getName())
			    	.append(" = ")
			    	.append(EndpointPojoGeneratorUtil.generateDeepCloneFieldInstruction(f, getImports()))
			    	.append(";\n")
			);
			body.append("return clone;\n");
		}
		appendMethod(signature, body.toString());
	}


}
