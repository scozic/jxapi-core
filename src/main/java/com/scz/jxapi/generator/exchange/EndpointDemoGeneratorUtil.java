package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

public class EndpointDemoGeneratorUtil {
	
	private EndpointDemoGeneratorUtil() {}
	
	public static String generateEndpointParameterCreationMethod(EndpointParameter endpointParameter, 
												   String sampleValueVariableName, 
												   String defaultObjectClassName,
												   Set<String> imports) {
		EndpointParameterType type = ExchangeJavaWrapperGeneratorUtil.getEndpointParameterType(endpointParameter);
		String parameterObjectClassName = Optional.ofNullable(endpointParameter.getObjectName()).orElse(defaultObjectClassName);
		String parameterClassName =	ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
												type, 
												imports, 
												parameterObjectClassName);
		return new StringBuilder()
					 .append("protected ")
					 .append(parameterClassName)
					 .append(" create")
					 .append(JavaCodeGenerationUtil.firstLetterToUpperCase(sampleValueVariableName))
					 .append("() ")
					 .append(JavaCodeGenerationUtil.generateCodeBlock(
							 	generateEndpointParameterSampleValueDeclaration(
									 endpointParameter, 
									 sampleValueVariableName, 
									 parameterObjectClassName, 
									 imports)))
					 .toString();
	}

	public static String generateEndpointParameterSampleValueDeclaration(EndpointParameter endpointParameter, 
																		 String sampleValueVariableName, 
																		 String objectClassName, 
																		 Set<String> imports) {
		EndpointParameterType type = ExchangeJavaWrapperGeneratorUtil.getEndpointParameterType(endpointParameter);
		Object sampleValue = endpointParameter.getSampleValue();
		if (sampleValue == null && !type.isObject()) {
			return "return null;";
		}
		CanonicalEndpointParameterTypes canonicalType = type.getCanonicalType();
		if (canonicalType.isPrimitive) {
			imports.add(canonicalType.typeClass.getName());
			return "return " + getPrimitiveTypeParameterSampleValueDeclaration(endpointParameter, imports)	+";";
		}
//		String parameterDeclaration = new StringBuilder()
//										.append(ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
//													type, 
//													imports, 
//													parameterObjectClassName))
//										.append(" ")
//										.append(sampleValueVariableName)
//										.append(" = ").toString();
		String parameterValue = null;
		StringBuilder res = new StringBuilder();
		String itemVariableName = sampleValueVariableName;
		if (type.isObject()) {
			if (canonicalType != CanonicalEndpointParameterTypes.OBJECT) {
				itemVariableName = itemVariableName + "Item";
			}
			String itemClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
										EndpointParameterType.getLeafSubType(type), 
										imports, 
										objectClassName);
			res.append(itemClassName)
			   .append(" ")
			   .append(itemVariableName)
			   .append(" = new ")
			   .append(itemClassName)
			   .append("();\n");
			
			for (EndpointParameter childParam: endpointParameter.getParameters()) {
				EndpointParameterType childParamType = childParam.getEndpointParameterType();
				String setArg = JavaCodeGenerationUtil.getQuotedString(childParam.getSampleValue());
				String setAccessorName = JavaCodeGenerationUtil.getSetAccessorMethodName(
						childParam.getName(),  
						endpointParameter.getParameters().stream()
										 .map(f -> f.getName()).collect(Collectors.toList()));
				if (childParamType.isObject()) {
					setArg = itemVariableName + "_" + childParam.getName();
					res.append(generateEndpointParameterSampleValueDeclaration(childParam, 
									setArg, 
									ExchangeJavaWrapperGeneratorUtil.getParameterObjectClassName(childParam, imports, itemClassName), 
									imports));
				}
				if(childParamType.getCanonicalType().isPrimitive) {
					setArg = getPrimitiveTypeParameterSampleValueDeclaration(childParam, imports);
				} else if (childParamType.getCanonicalType() != CanonicalEndpointParameterTypes.OBJECT){
					// List or map
					if (setArg != null) {
						setArg = ExchangeJavaWrapperGeneratorUtil.getNewMessageDeserializerInstruction(childParamType, itemClassName, imports) 
									+ ".deserialize(" + setArg + ");";
					}
				}
				res.append(itemVariableName)
				   .append(".")
				   .append(setAccessorName)
				   .append("(")
				   .append(setArg)
				   .append(");\n");
			}
			parameterValue = itemVariableName;
		} else {
			parameterValue = JavaCodeGenerationUtil.getQuotedString(sampleValue);
		}
		
		if (canonicalType != CanonicalEndpointParameterTypes.OBJECT) {
			// Not primitive nor object type -> map or list type.
			if (type.isObject()) {
				parameterValue = getMapOrListSampleValueDeclaration(
									type, 
									parameterValue, 
									endpointParameter.getSampleMapKeyValue() == null? null: 
										endpointParameter.getSampleMapKeyValue().iterator(), 
									imports);
			} else {
				parameterValue = ExchangeJavaWrapperGeneratorUtil.getNewMessageDeserializerInstruction(type, objectClassName, imports) 
									+ ".deserialize(" + parameterValue + ")";
			}
		}
		
//		if (itemVariableName.equals(sampleValueVariableName)) {
//			// Type is not object or is of OBJECT canonical type: 'new' instruction has not be appended yet
//			res.append("return ").append(parameterValue).append(";");
//		}
		
		res.append("return ").append(parameterValue).append(";");
		
		return res.toString();
	}
	
	private static String getMapOrListSampleValueDeclaration(EndpointParameterType type, String itemValue, Iterator<String> sampleMapKeyValues, Set<String> imports) {
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
	
	private static String getPrimitiveTypeParameterSampleValueDeclaration(EndpointParameter endpointParameter, Set<String> imports) {
		EndpointParameterType type = endpointParameter.getEndpointParameterType();
		Object sampleValue = endpointParameter.getSampleValue();
		if (sampleValue == null && !type.isObject()) {
			return null;
		}
		String sampleValueStr = sampleValue.toString();
		CanonicalEndpointParameterTypes canonicalType = type.getCanonicalType();
		imports.add(canonicalType.typeClass.getName());
		switch (type.getCanonicalType()) {
		case BIGDECIMAL:
			imports.add(BigDecimal.class.getName());
			return "new BigDecimal(" + sampleValueStr + ")";
		case BOOLEAN:
			return "Boolean.valueOf(" + sampleValueStr + ")";
		case INT:
			return "Integer.valueOf(" + sampleValueStr + ")";
		case LONG:
		case TIMESTAMP:
			return "Long.valueOf(" + sampleValueStr + ")";
		case STRING:
			return JavaCodeGenerationUtil.getQuotedString(sampleValueStr);
		default:
			throw new IllegalArgumentException("Unexpected primitive canonical type for parameter:" + endpointParameter);
		}
	}

}
