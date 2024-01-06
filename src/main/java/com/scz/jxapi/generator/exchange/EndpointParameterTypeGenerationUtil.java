package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.TimestampJsonFieldDeserializer;

/**
 * Helper static methods around generation of {@link EndpointParameterType} related Java code.
 */
public class EndpointParameterTypeGenerationUtil {

	private EndpointParameterTypeGenerationUtil() {}
	
	/**
	 * Generates expected class 
	 * @param endpointParameter
	 * @param imports
	 * @param enclosingClassName
	 * @return
	 */
	public static String getClassNameForEndpointParameter(EndpointParameter endpointParameter, Set<String> imports, String enclosingClassName) {
		String objectClassName = null;
		if (endpointParameter.getEndpointParameterType().isObject()) {
			 objectClassName = getParameterObjectClassName(endpointParameter, imports, enclosingClassName);
		}
		return getClassNameForParameterType(endpointParameter.getEndpointParameterType(), imports, objectClassName);
	}
	
	public static String getParameterObjectClassName(EndpointParameter endpointParameter, Set<String> imports, String enclosingClassName) {
		if (endpointParameter.getObjectName() != null) {
			 return JavaCodeGenerationUtil.getClassPackage(enclosingClassName) + "." + endpointParameter.getObjectName();
		 } else {
			 return enclosingClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getName());
		 }
	}
	
	public static String getClassNameForParameterType(EndpointParameterType type, Set<String> imports, String objectClassName) {
		String subTypeClassName = null;
		switch(type.getCanonicalType()) {
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
			subTypeClassName = getClassNameForParameterType(type.getSubType(), imports, objectClassName);
			imports.add(subTypeClassName);
			return List.class.getSimpleName() 
					+ "<" 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case MAP:
			imports.add(Map.class.getName());
			subTypeClassName = getClassNameForParameterType(type.getSubType(), imports, objectClassName);
			imports.add(subTypeClassName);
			return Map.class.getSimpleName() 
					+ "<String, " 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case OBJECT:
			return objectClassName;
		default:
			throw new IllegalArgumentException("Unexpected type for:" + type);
		}
	}
	
	public static String getLeafObjectParameterClassName(String endpointParameterName, EndpointParameterType type, String endpointParameterObjectName, Set<String> imports, String enclosingPojoClassName) {
		if (type.isObject() && endpointParameterObjectName != null) {
			return JavaCodeGenerationUtil.getClassPackage(enclosingPojoClassName) + "."  + endpointParameterObjectName;
		}
		return getClassNameForParameterType(
				  EndpointParameterType.getLeafSubType(type)
				  ,imports
				  , enclosingPojoClassName) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameterName);
	}

	public static String getNewNonPrimitiveParameterDeserializerInstruction(EndpointParameterType type, String objectClassName, Set<String> imports) {
		switch (type.getCanonicalType()) {
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
			imports.add(ListJsonFieldDeserializer.class.getName());
			return "new " + ListJsonFieldDeserializer.class.getSimpleName() + "<>(" + getNewNonPrimitiveParameterDeserializerInstruction(type.getSubType(), objectClassName, imports) + ")";
		case MAP:
			imports.add(MapJsonFieldDeserializer.class.getName());
			return "new " + MapJsonFieldDeserializer.class.getSimpleName() + "<>(" + getNewNonPrimitiveParameterDeserializerInstruction(type.getSubType(), objectClassName, imports) +")";
		case OBJECT:
			String objectDeserializerClass = ExchangeJavaWrapperGeneratorUtil.getJsonMessageDeserializerClassName(objectClassName);
			imports.add(objectDeserializerClass);
			return "new " +  JavaCodeGenerationUtil.getClassNameWithoutPackage(objectDeserializerClass) + "()";
		default:
			throw new IllegalArgumentException("Unexpected field type:" + type);
		}
	}
}
