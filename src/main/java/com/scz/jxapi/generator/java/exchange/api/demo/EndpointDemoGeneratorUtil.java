package com.scz.jxapi.generator.java.exchange.api.demo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.util.TestJXApiProperties;

/**
 * Helper methods around REST or Websocket endpoint demo snippets code generation.
 */
public class EndpointDemoGeneratorUtil {
	
	private EndpointDemoGeneratorUtil() {}
	
	/**
	 * Generates a method (with full signature and method body) that creates a
	 * sample value for the given {@link Field} (which can be the data type
	 * definition of a REST/Websocket request, response/message, or of a nested
	 * {@link Type#OBJECT} property.
	 * 
	 * @param property        the property for which to generate the
	 *                        creation method.
	 * @param objectClassName Default class name for property value, relevant when
	 *                        property is an object type and has no object name.
	 * @param imports         the set of imports to add to the class containing the
	 *                        method. Will be populated with the necessary imports.
	 * @return the method code with signature (method declaration) and body
	 * 
	 * @see #generateFieldCreationMethodDeclaration(Field, String, Imports)
	 * @see #generateFieldSampleValueDeclaration(Field, String, String, Imports, String)
	 */
	public static String generateFieldCreationMethod(Field property, 
												     String objectClassName,
												     Imports imports) {
		return new StringBuilder()
					 .append(generateFieldCreationMethodDeclaration(
							 property, 
							 objectClassName, 
							 imports))
					 .append(" ")
					 .append(JavaCodeGenerationUtil.generateCodeBlock(
							 	generateFieldSampleValueDeclaration(
									 property,  
									 "request",
									 objectClassName, 
									 imports,
									 "return ") 
							 	+ ";"))
					 .toString();
	}
	
	/**
	 * Generates the method declaration for a method that creates a sample value for
	 * the given endpoint property.
	 * 
	 * @param field      the endpoint property for which to generate the
	 *                               creation method.
	 * @param defaultObjectClassName Default class name for property value, relevant
	 *                               when property is an object type and has no
	 *                               object name.
	 * @param imports                the set of imports to add to the class
	 *                               containing the method. Will be populated with
	 *                               the necessary imports.
	 * @return the method declaration
	 */
	public static String generateFieldCreationMethodDeclaration(Field field, 
																String defaultObjectClassName,
																Imports imports) {
		Type type = ExchangeApiGeneratorUtil.getFieldType(field);
		String fieldClassName =	ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
												type, 
												imports, 
												defaultObjectClassName);
		return new StringBuilder().append("public static ")
								  .append(fieldClassName)
								  .append(" ")
								  .append(generateFieldCreationMethodName(field))
								  .append("()")
								  .toString();
	}
	
	/**
	 * Generates the method name for a method that creates a sample value for the
	 * given endpoint property.
	 * 
	 * @param field the field for which to generate the
	 *                          creation method.
	 * @return the method name
	 */
	public static String generateFieldCreationMethodName(Field field) {
		return "create" + JavaCodeGenerationUtil.firstLetterToUpperCase(Optional.ofNullable(field.getName()).orElse("request"));
	}

	private static String generateFieldSampleValueDeclaration(Field field, 
															  String sampleValueVariableName, 
															  String objectClassName, 
															  Imports imports,
															  String returnOrResultAffectation) {
		Type type = ExchangeApiGeneratorUtil.getFieldType(field);
		Object sampleValue = field.getSampleValue();
		if (sampleValue == null && !type.isObject()) {
			return returnOrResultAffectation + "null";
		}
		CanonicalType canonicalType = type.getCanonicalType();
		if (canonicalType.isPrimitive) {
			imports.add(canonicalType.typeClass.getName());
			return returnOrResultAffectation + getPrimitiveTypeFieldSampleValueDeclaration(field, imports);
		}

		String fieldValue = null;
		StringBuilder res = new StringBuilder();
		String itemVariableName = sampleValueVariableName;
		if (type.isObject()) {
			if (canonicalType != CanonicalType.OBJECT) {
				itemVariableName = itemVariableName + "Item";
			}
			String itemClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
										Type.getLeafSubType(type), 
										imports, 
										objectClassName);
			res.append(itemClassName)
			   .append(" ")
			   .append(itemVariableName)
			   .append(" = new ")
			   .append(itemClassName)
			   .append("();\n");
			
			for (Field childParam: field.getProperties()) {
				Type childParamType = childParam.getType();
				String setArg = JavaCodeGenerationUtil.getQuotedString(childParam.getSampleValue());
				String setAccessorName = JavaCodeGenerationUtil.getSetAccessorMethodName(
						childParam.getName(),  
						field.getProperties().stream()
										 .map(f -> f.getName()).collect(Collectors.toList()));
				String setChildParamInstruction = 
							new StringBuilder()
									.append(itemVariableName)
									.append(".")
									.append(setAccessorName)
									.append("(")
									.toString();
				if (childParamType.isObject()) {
					setArg = itemVariableName + "_" + childParam.getName();
					res.append(generateFieldSampleValueDeclaration(childParam, 
									setArg, 
									ExchangeApiGeneratorUtil.getFieldObjectClassName(childParam, objectClassName), 
									imports,
									setChildParamInstruction));
				} else if(childParamType.getCanonicalType().isPrimitive) {
					setArg = getPrimitiveTypeFieldSampleValueDeclaration(childParam, imports);
					if (setArg != null) {
						res.append(setChildParamInstruction).append(setArg);
					}
				} else {
					// List or map
					if (setArg != null) {
						setArg = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(childParamType, objectClassName, imports) 
									+ ".deserialize(" + setArg + ")";
						res.append(setChildParamInstruction).append(setArg);
					}
				}
				if (setArg != null) {
					res.append(");\n");
				}
				
			}
			fieldValue = itemVariableName;
		} else {
			fieldValue = JavaCodeGenerationUtil.getQuotedString(sampleValue);
		}
		
		if (canonicalType != CanonicalType.OBJECT) {
			// Not primitive nor object type -> map or list type.
			if (type.isObject()) {
				fieldValue = getMapOrListSampleValueDeclaration(
									type, 
									fieldValue, 
									field.getSampleMapKeyValue() == null? null: 
										field.getSampleMapKeyValue().iterator(), 
									imports);
			} 
			else {
				fieldValue = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(type, objectClassName, imports) 
									+ ".deserialize(" + fieldValue + ")";
			}
		}
		
		if (fieldValue == null) {
			return returnOrResultAffectation + "null";
		} else {
			res.append(returnOrResultAffectation).append(fieldValue);//.append(";");
			return res.toString();
		}
	}
	
	private static String getMapOrListSampleValueDeclaration(Type type, 
															 String itemValue, 
															 Iterator<String> sampleMapKeyValues, 
															 Imports imports) {
		if (type.getSubType() == null) {
			return itemValue;
		}
		switch (type.getCanonicalType()) {
		case LIST:
			imports.add(List.class.getName());
			return "List.of(" + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) + ")";
		default: // MAP
			if (sampleMapKeyValues == null || !sampleMapKeyValues.hasNext()) {
				return null;
			}
			String sampleKeyValue = JavaCodeGenerationUtil.getQuotedString(sampleMapKeyValues.next());
			return "Map.of(" + sampleKeyValue + ", " + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) + ")";
		}
	}
	
	private static String getPrimitiveTypeFieldSampleValueDeclaration(Field field, Imports imports) {
		return ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(
					field.getType(), 
					field.getSampleValue(), 
					imports);
	}

	/**
	 * Generates the class name for the REST API demo class.
	 * <ul>
	 * <li>The returned class package name is the base package of the exchange
	 * descriptor + the API name + ".demo".</li>
	 * <li>The returned class name is the concatenation of the exchange name, the
	 * API name and the REST API name, and <code>Demo</code> with the first letter
	 * of each word capitalized.</li>
	 * </ul>
	 * 
	 * Example:
	 * <code>com.scz.jxapi.exchange.api.demo.MyExchangeMyApiMyRestEndpointDemo</code>
	 * 
	 * @param exchangeDescriptor    The exchange descriptor containing the API.
	 * @param exchangeApiDescriptor The API descriptor containing the REST API.
	 * @param restApi               The REST API descriptor.
	 * @return the class name for the REST API demo class.
	 */
	public static String getRestApiDemoClassName(ExchangeDescriptor exchangeDescriptor, 
												 ExchangeApiDescriptor exchangeApiDescriptor, 
												 RestEndpointDescriptor restApi) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
		return pkgPrefix + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(restApi.getName())
									 + "Demo";
	}

	/**
	 * Generates the class name for the Websocket API demo class.
	 * <ul>
	 * <li>The returned class package name is the base package of the exchange
	 * descriptor + the API name + ".demo".</li>
	 * <li>The returned class name is the concatenation of the exchange name, the
	 * API name and the Websocket API name, and <code>Demo</code> with the first
	 * letter of each word capitalized.</li>
	 * </ul>
	 * 
	 * Example:
	 * <code>com.scz.jxapi.exchange.api.demo.MyExchangeMyApiMyWebsocketEndpointDemo</code>
	 * 
	 * @param exchangeDescriptor    The exchange descriptor containing the API.
	 * @param exchangeApiDescriptor The API descriptor containing the Websocket API.
	 * @param websocketApi          The Websocket API descriptor.
	 * @return the class name for the Websocket API demo class.
	 */
	public static String getWebsocketApiDemoClassName(ExchangeDescriptor exchangeDescriptor, 
													  ExchangeApiDescriptor exchangeApiDescriptor, 
													  WebsocketEndpointDescriptor websocketApi) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
		return pkgPrefix + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
									 + "Demo";
	}
	
	/**
	 * Generates a new test API instantiation instruction for the given exchange ID,
	 * exchange implementation class name and simple API class name.
	 * <ul>
	 * <li>The returned instruction is declaration of a variable named
	 * <code>api</code> of the given simple API class name, initialized with a new
	 * instance of the given exchange implementation class name.</li>
	 * <li>The exchange Id is used as expected prefix to filter properties returned
	 * by {@link TestJXApiProperties#filterProperties(String, boolean)} .</li>
	 * </ul>
	 * 
	 * Example:
	 * <code>MyApi api = new MyExchangeImpl("test-my-exchange", TestJXApiProperties.filterProperties("my-exchange", true)).getMyApi();</code>
	 * 
	 * @param exchangeId            The exchange ID.
	 * @param exchangeImplClassName The exchange implementation class name.
	 * @param simpleApiClassName    The simple API class name.
	 * @return the new test API instantiation instruction.
	 * 
	 * @see TestJXApiProperties#filterProperties(String, boolean)
	 */
	public static String getNewTestApiInstruction(String exchangeId, String exchangeImplClassName, String simpleApiClassName) {
		return new StringBuilder()
				.append(simpleApiClassName)
				.append(" api = new ")
				.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeImplClassName))
				.append("(\"test-")
				.append(exchangeId)
				.append("\", TestJXApiProperties.filterProperties(\"")
				.append(exchangeId)
				.append("\", true)).get")
				.append(simpleApiClassName)
				.append("();\n")
				.toString();
	}

}
