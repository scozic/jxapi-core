package com.scz.jxapi.generator.java.exchange.api.demo;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;

public class EndpointDemoGeneratorUtil {
	
	public static final String SPECIAL_SAMPLE_VALUE_NOW = "now()";
	
	private EndpointDemoGeneratorUtil() {}
	
	public static String generateEndpointParameterCreationMethod(
												   Field endpointParameter, 
												   String defaultObjectClassName,
												   Set<String> imports) {
		String parameterObjectClassName = Optional.ofNullable(endpointParameter.getObjectName())
												  .orElse(defaultObjectClassName);
		return new StringBuilder()
					 .append(generateEndpointParameterCreationMethodDeclaration(
							 endpointParameter, 
							 defaultObjectClassName, 
							 imports))
					 .append(" ")
					 .append(JavaCodeGenerationUtil.generateCodeBlock(
							 	generateEndpointParameterSampleValueDeclaration(
									 endpointParameter,  
									 "request",
									 parameterObjectClassName, 
									 imports,
									 "return ") 
							 	+ ";"))
					 .toString();
	}
	
	public static String generateEndpointParameterCreationMethodDeclaration(Field endpointParameter, 
																			String defaultObjectClassName,
																			Set<String> imports) {
		Type type = ExchangeApiGeneratorUtil.getFieldType(endpointParameter);
		String parameterObjectClassName = Optional.ofNullable(endpointParameter.getObjectName()).orElse(defaultObjectClassName);
		String parameterClassName =	ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
												type, 
												imports, 
												parameterObjectClassName);
		return new StringBuilder().append("public static ")
								  .append(parameterClassName)
								  .append(" ")
								  .append(generateEndpointParameterCreationMethodName(endpointParameter))
								  .append("()")
								  .toString();
	}
	
	public static String generateEndpointParameterCreationMethodName(Field endpointParameter) {
		return "create" + JavaCodeGenerationUtil.firstLetterToUpperCase(Optional.ofNullable(endpointParameter.getName()).orElse("request"));
	}

	private static String generateEndpointParameterSampleValueDeclaration(Field endpointParameter, 
																		 String sampleValueVariableName, 
																		 String objectClassName, 
																		 Set<String> imports,
																		 String returnOrResultAffectation) {
		Type type = ExchangeApiGeneratorUtil.getFieldType(endpointParameter);
		Object sampleValue = endpointParameter.getSampleValue();
		if (sampleValue == null && !type.isObject()) {
			return returnOrResultAffectation + "null";
		}
		CanonicalType canonicalType = type.getCanonicalType();
		if (canonicalType.isPrimitive) {
			imports.add(canonicalType.typeClass.getName());
			return returnOrResultAffectation + getPrimitiveTypeParameterSampleValueDeclaration(endpointParameter, imports);
		}

		String parameterValue = null;
		StringBuilder res = new StringBuilder();
		String itemVariableName = sampleValueVariableName;
		if (type.isObject()) {
			if (canonicalType != CanonicalType.OBJECT) {
				itemVariableName = itemVariableName + "Item";
			}
			String itemClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
										Type.getLeafSubType(type), 
										imports, 
										objectClassName);
			res.append(itemClassName)
			   .append(" ")
			   .append(itemVariableName)
			   .append(" = new ")
			   .append(itemClassName)
			   .append("();\n");
			
			for (Field childParam: endpointParameter.getParameters()) {
				Type childParamType = childParam.getType();
				String setArg = JavaCodeGenerationUtil.getQuotedString(childParam.getSampleValue());
				String setAccessorName = JavaCodeGenerationUtil.getSetAccessorMethodName(
						childParam.getName(),  
						endpointParameter.getParameters().stream()
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
					res.append(generateEndpointParameterSampleValueDeclaration(childParam, 
									setArg, 
									ExchangeApiGeneratorUtil.getFieldObjectClassName(childParam, itemClassName), 
									imports,
									setChildParamInstruction));
				} else if(childParamType.getCanonicalType().isPrimitive) {
					setArg = getPrimitiveTypeParameterSampleValueDeclaration(childParam, imports);
					if (setArg != null) {
						res.append(setChildParamInstruction).append(setArg);
					}
				} else {
					// List or map
					if (setArg != null) {
						setArg = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(childParamType, itemClassName, imports) 
									+ ".deserialize(" + setArg + ")";
						res.append(setChildParamInstruction).append(setArg);
					}
				}
				if (setArg != null) {
					res.append(");\n");
				}
				
			}
			parameterValue = itemVariableName;
		} else {
			parameterValue = JavaCodeGenerationUtil.getQuotedString(sampleValue);
		}
		
		if (canonicalType != CanonicalType.OBJECT) {
			// Not primitive nor object type -> map or list type.
			if (type.isObject()) {
				parameterValue = getMapOrListSampleValueDeclaration(
									type, 
									parameterValue, 
									endpointParameter.getSampleMapKeyValue() == null? null: 
										endpointParameter.getSampleMapKeyValue().iterator(), 
									imports);
			} 
			else {
				parameterValue = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(type, objectClassName, imports) 
									+ ".deserialize(" + parameterValue + ")";
			}
		}
		
		if (parameterValue == null) {
			return returnOrResultAffectation + "null";
		} else {
			res.append(returnOrResultAffectation).append(parameterValue);//.append(";");
			return res.toString();
		}
	}
	
	private static String getMapOrListSampleValueDeclaration(Type type, 
															 String itemValue, 
															 Iterator<String> sampleMapKeyValues, 
															 Set<String> imports) {
		if (type.getSubType() == null) {
			return itemValue;
		}
		switch (type.getCanonicalType()) {
		case LIST:
			imports.add(List.class.getName());
			return "List.of(" + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) + ")";
		case MAP:
			if (sampleMapKeyValues == null || !sampleMapKeyValues.hasNext()) {
				return null;
			}
			String sampleKeyValue = JavaCodeGenerationUtil.getQuotedString(sampleMapKeyValues.next());
			return "Map.of(" + sampleKeyValue + ", " + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) + ")";
		default:
			throw new IllegalArgumentException("Unexpected type, should be LIST or MAP :" + type);
		}
	}
	
	private static String getPrimitiveTypeParameterSampleValueDeclaration(Field endpointParameter, 
																		  Set<String> imports) {
		Type type = endpointParameter.getType();
		Object sampleValue = endpointParameter.getSampleValue();
		if (sampleValue == null) {
			return null;
		}
		String sampleValueStr = sampleValue.toString();
		CanonicalType canonicalType = type.getCanonicalType();
		imports.add(canonicalType.typeClass.getName());
		switch (type.getCanonicalType()) {
		case BIGDECIMAL:
			imports.add(BigDecimal.class.getName());
			return "new BigDecimal(" + JavaCodeGenerationUtil.getQuotedString(sampleValueStr) + ")";
		case BOOLEAN:
			return "Boolean.valueOf(" + sampleValueStr + ")";
		case INT:
			return "Integer.valueOf(" + sampleValueStr + ")";
		case LONG:
		case TIMESTAMP:
			if (SPECIAL_SAMPLE_VALUE_NOW.equals(sampleValueStr)) {
				return "Long.valueOf(System.currentTimeMillis())";
			}
			return "Long.valueOf(" + sampleValueStr + ")";
		case STRING:
			return JavaCodeGenerationUtil.getQuotedString(sampleValueStr);
		default:
			throw new IllegalArgumentException("Unexpected primitive canonical type for parameter:" + endpointParameter);
		}
	}

	public static String getRestApiDemoClassName(ExchangeDescriptor exchangeDescriptor, 
												 ExchangeApiDescriptor exchangeApiDescriptor, 
												 RestEndpointDescriptor restApi) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
		return pkgPrefix + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(restApi.getName())
									 + "Demo";
	}

	public static String getWebsocketApiDemoClassName(ExchangeDescriptor exchangeDescriptor, 
													  ExchangeApiDescriptor exchangeApiDescriptor, 
													  WebsocketEndpointDescriptor websocketApi) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
		return pkgPrefix + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
									 + "Demo";
	}
	
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
