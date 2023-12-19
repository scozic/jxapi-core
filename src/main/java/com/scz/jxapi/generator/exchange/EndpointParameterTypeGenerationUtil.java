package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JsonMessageDeserializerGenerator;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.TimestampJsonFieldDeserializer;

/**
 * 
 * Helper static methods around generation of {@link EndpointParameterType} related Java code.
 */
public class EndpointParameterTypeGenerationUtil {

	private EndpointParameterTypeGenerationUtil() {}
	
	public static String getClassNameForEndpointParameter(EndpointParameter endpointParameter, Set<String> imports, String enclosingClassName) {
		String objectClassName = null;
		if (endpointParameter.getEndpointParameterType().isObject()) {
			 if (endpointParameter.getObjectName() != null) {
				 objectClassName = endpointParameter.getObjectName();
			 } else {

				 objectClassName = enclosingClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getName());
			 }
		}
		return getClassNameForParameterType(endpointParameter.getEndpointParameterType(), imports, objectClassName);
	}

//	public static String getClassNameForEndpointParameter(EndpointParameter endpointParameter, Set<String> imports, String exchangeName, String exchangeApiName, String apiName) {
//		String enclosingClassName = null;
//		if (endpointParameter.getEndpointParameterType().isObject()) {
//			 if (endpointParameter.getObjectName() != null) {
//				 enclosingClassName = endpointParameter.getObjectName();
//			 } else {
//
//			 }
//		}
//		 enclosingClassName = "";
//		 if (exchangeName != null) {
//			 enclosingClassName += JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeName);
//		 }
//		 if (exchangeApiName != null) {
//			 enclosingClassName += JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiName);
//		 }
//		 if (apiName != null) {
//			 enclosingClassName += JavaCodeGenerationUtil.firstLetterToUpperCase(apiName);
//		 }
//		enclosingClassName += JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getName());
//		return getClassNameForParameterType(endpointParameter.getEndpointParameterType(), imports, enclosingClassName);
//	}
	
	public static String getClassNameForParameterType(EndpointParameterType type, Set<String> imports, String objectClassName) {
		switch(type.getType()) {
		case BIGDECIMAL:
			imports.add(BigDecimal.class.getName());
			return BigDecimal.class.getSimpleName();
		case BOOLEAN:
			return Boolean.class.getSimpleName();
		case INT:
			return Integer.class.getSimpleName();
		case LONG:
		case TIMESTAMP:
			return Long.class.getSimpleName();
		case STRING:
			return String.class.getSimpleName();
		case LIST:
			imports.add(List.class.getName());
			return List.class.getSimpleName() 
					+ "<" 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(getClassNameForParameterType(type.getSubType(), imports, objectClassName)) 
					+ ">";
		case MAP:
			imports.add(Map.class.getName());
			return Map.class.getSimpleName() 
					+ "<String, " 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(getClassNameForParameterType(type.getSubType(), imports, objectClassName)) 
					+ ">";
		case OBJECT:
			imports.add(objectClassName);
			return objectClassName;
		default:
			throw new IllegalArgumentException("Unexpected type for:" + type);
		}
	}

	public static String getNewNonPrimitiveParameterDeserializerInstruction(EndpointParameterType type, String objectClassName, Set<String> imports) {
		switch (type.getType()) {
		case BIGDECIMAL:
			imports.add(BigDecimalJsonFieldDeserializer.class.getName());
			return "BigDecimalJsonFieldDeserializer.getInstance()";
		case BOOLEAN:
			imports.add(BooleanJsonFieldDeserializer.class.getName());
			return  BooleanJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case INT:
			imports.add(IntegerJsonFieldDeserializer.class.getName());
			return  IntegerJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case LONG:
			imports.add(LongJsonFieldDeserializer.class.getName());
			return  LongJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case STRING:
			imports.add(StringJsonFieldDeserializer.class.getName());
			return  StringJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case TIMESTAMP:
			imports.add(TimestampJsonFieldDeserializer.class.getName());
			return  TimestampJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case LIST:
		case OBJECT:
		case MAP:
			return "new " + JsonMessageDeserializerGenerator.generateNonPrimitiveParameterDeserializerClassName(type, objectClassName, imports) 
						  + "(" + getNewNonPrimitiveParameterDeserializerInstruction(type.getSubType(), objectClassName, imports) + ")";
		default:
			throw new IllegalArgumentException("Unexpected field type:" + type);
		}
	}
}
