package com.scz.jxapi.generator.java.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.TimestampJsonFieldDeserializer;

/**
 * Helper static methods for generation of Java classes of a given exchange wrapper
 */
public class ExchangeJavaWrapperGeneratorUtil {
	
	/**
	 * Default separator for string lists
	 */
	public static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	
	/**
	 * @return The full name of the ExchangeApi interface class for the given exchange and API.
	 */
	public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, 
												  ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
										+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Api";
		return pkgPrefix + simpleInterfaceName;
	}
	
	/**
	 * @return The name of static variable for the given rate limit name.
	 */
	public static String generateRateLimitVariableName(String rateLimitName) {
		return "RATE_LIMIT_" + JavaCodeGenerationUtil.getStaticVariableName(rateLimitName);
	}

	/**
	 * @return The name of the Exchange interface class for the given exchange.
	 */
	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
	}

	/**
	 * @return The name of JSON message deserializer class for the given deserialized type.
	 */
	public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedTypeClassName), ".pojo") + ".deserializers";
		return pkg + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName) + "Deserializer";
	}

	/**
	 * @param type            the type of field (see {@link Field}) to get type class name
	 *                        of.
	 * @param imports         The imports of generator context that will be
	 *                        populated with classes used by returned type. That set
	 *                        must be not <code>null</code> and mutable.
	 * @param objectClassName the object (full) class name of leaf object type if <code>type</code> is of object type
	 * @return The simple class name associated to given <code>type</code>:
	 * 		   <ul> 
	 * 		     <li>if this type is a 'Primitive' type, returns the simple class name of the corresponding Java class:
	 * 		       <ul>
	 * 		         <li>BIGDECIMAL -> {@link BigDecimal}</li>
	 * 		         <li>BOOLEAN -> {@link Boolean}</li>
	 * 		         <li>INT -> {@link Integer}</li>
	 * 		         <li>LONG -> {@link Long}</li>
	 * 		         <li>TIMESTAMP -> {@link Long}</li>
	 * 		         <li>STRING -> {@link String}</li>
	 * 		       </ul>
	 * 		     <li>If this type is a 'List' type, returns generic <code>List<SubTypeClassName></code>, where <code>subTypeClassName</code> is the simple class name of the subType of the list, see {@link Type#getSubType()}.
	 * 		     <li>If this type is a 'Map' type, returns generic <code>Map<String, SubTypeClassName></code>, where <code>subTypeClassName</code> is the simple class name of the subType of the map, see {@link Type#getSubType()}.
	 * 		     <li>If this type is an 'Object' type, returns the simple class name of the object class name, see {@link #getClassNameWithoutPackage(String)}.
	 * 		     </li>
	 * 		   </ul>
	 * @see #getClassNameForParameterType(Type, Set, String)
	 * @throws IllegalArgumentException if the type is not recognized.
	 */
	public static String getClassNameForParameterType(Type type, 
													  Set<String> imports, 
													  String objectClassName) {
		if (type == null) {
			return null;
		}
		String subTypeClassName = null;
		CanonicalType canonicalType = type.getCanonicalType();
		Class<?> canonicalTypeClass = canonicalType.typeClass;
		switch(canonicalType) {
		case BIGDECIMAL:
			if (imports != null) {
				imports.add(BigDecimal.class.getName());
			}
		case BOOLEAN:
		case INT:
		case LONG:
		case TIMESTAMP:
		case STRING:
			return canonicalTypeClass.getSimpleName();
		case LIST:
			subTypeClassName = getClassNameForParameterType(type.getSubType(), imports, objectClassName);
			if (imports != null) {
				imports.add(canonicalTypeClass.getName());
			}
			return canonicalTypeClass.getSimpleName() 
					+ "<" 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case MAP:
			subTypeClassName = getClassNameForParameterType(type.getSubType(), imports, objectClassName);
			if (imports != null) {
				imports.add(canonicalTypeClass.getName());
			}
			return canonicalTypeClass.getSimpleName() 
					+ "<String, " 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case OBJECT:
			if (objectClassName != null) {
				
			}
			imports.add(objectClassName);
			return JavaCodeGenerationUtil.getClassNameWithoutPackage(objectClassName);
		default:
			throw new IllegalArgumentException("Unexpected type for:" + type);
		}
	}

	/**
	 * Returns the instruction to create a new instance of a {@link JsonFieldDeserializer} for the given type.
	 * @param type The type of the field to deserialize
	 * @param objectClassName The object class name of the field to generate JsonFieldDeserializer for. 
	 * 						  This parameter is used only if the type is an 'object' type (see {@link Type#isObject()} ).
	 * @param imports The imports of the generator context that will be populated with classes 
	 * 				  used by returned type. That set must be not <code>null</code> and mutable.
	 * @return The instruction to get or create a new instance of a {@link JsonFieldDeserializer} for the given type:
	 * 		    <ul>
	 * 		      <li>if this type is a 'primitive' type, returns the instruction to get the 
	 * 				  singleton instance of the corresponding JsonFieldDeserializer:
	 * 		        <ul>
	 * 		          <li>BIGDECIMAL -> {@link BigDecimalJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>BOOLEAN -> {@link BooleanJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>INT -> {@link IntegerJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>LONG -> {@link LongJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>TIMESTAMP -> {@link TimestampJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>STRING -> {@link StringJsonFieldDeserializer#getInstance()}</li>
	 * 		        </ul>
	 * 		      </li>
	 * 		      <li>if this type is a 'List' type, returns the instruction to create a new instance of 
	 * 				  a {@link ListJsonFieldDeserializer} with the subType of the list, see {@link Type#getSubType()}.
	 * 		      <li>if this type is a 'Map' type, returns the instruction to create a new instance of 
	 * 				  a {@link MapJsonFieldDeserializer} with the subType of the map, see {@link Type#getSubType()}.
	 * 		      <li>if this type is an 'Object' type, returns the 'new' instruction to create a new instance of 
	 * 				  a {@link com.scz.jxapi.netutils.deserialization.json.field.JsonFieldDeserializer} for the object 
	 * 				  class name, see {@link #getJsonMessageDeserializerClassName(String)}.
	 * 		    </ul>
	 * @see Type
	 * @throws IllegalArgumentException if the type is not recognized.
	 */
	public static String getNewJsonParameterDeserializerInstruction(Type type, String objectClassName, Set<String> imports) {
		if (type == null) {
			type  = Type.fromTypeName(CanonicalType.STRING.name());
		}
		switch (type.getCanonicalType()) {
		case BIGDECIMAL:
			imports.add(BigDecimalJsonFieldDeserializer.class.getName());
			return BigDecimalJsonFieldDeserializer.class.getSimpleName() +".getInstance()";
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
			return "new " + ListJsonFieldDeserializer.class.getSimpleName() + "<>(" + getNewJsonParameterDeserializerInstruction(type.getSubType(), objectClassName, imports) + ")";
		case MAP:
			imports.add(MapJsonFieldDeserializer.class.getName());
			return "new " + MapJsonFieldDeserializer.class.getSimpleName() + "<>(" + getNewJsonParameterDeserializerInstruction(type.getSubType(), objectClassName, imports) +")";
		case OBJECT:
			String objectDeserializerClass = getJsonMessageDeserializerClassName(objectClassName);
			imports.add(objectDeserializerClass);
			return "new " +  JavaCodeGenerationUtil.getClassNameWithoutPackage(objectDeserializerClass) + "()";
		default:
			throw new IllegalArgumentException("Unexpected field type:" + type);
		}
	}

}
