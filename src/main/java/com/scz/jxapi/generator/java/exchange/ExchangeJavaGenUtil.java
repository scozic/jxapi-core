package com.scz.jxapi.generator.java.exchange;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
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
public class ExchangeJavaGenUtil {
	
	private static final String GET_INSTANCE = ".getInstance()";

	private ExchangeJavaGenUtil() {}
	
	/**
	 * Default separator for string lists
	 */
	public static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	
	/**
	 * Special value that can be used in sample value of
	 * {@link CanonicalType#LONG},{@link CanonicalType#TIMESTAMP} types, which means
	 * current time {@link System#currentTimeMillis()} should be used.
	 */
	public static final String SPECIAL_SAMPLE_VALUE_NOW = "now()";
	
	/**
	 * Name of static variable storing the base HTTP (REST) URL of an exchange (see
	 * {@link ExchangeDescriptor#getHttpUrl()} or an API group (see
	 * {@link ExchangeApiDescriptor#getHttpUrl()}.
	 */
	public static final String HTTP_URL_STATIC_VARIABLE = "HTTP_URL";

	/**
	 * Name of static variable storing the base Websocket URL of an exchange (see
	 * {@link ExchangeDescriptor#getWebsocketUrl()} or an API group (see
	 * {@link ExchangeApiDescriptor#getWebsocketUrl()}.
	 */
	public static final String WEBSOCKET_URL_STATIC_VARIABLE = "WEBSOCKET_URL";
	
	/**
	 * @param exchangeDescriptor The exchange the API group belongs to
	 * @param exchangeApiDescriptor The exchange API group to get the full class name for
	 * @return The full name of the ExchangeApi interface class for the given
	 *         exchange and API. The returned class name package is base package is
	 *         <code>exchangeBasePackage.exchangeApiDescriptorName</code>. Its named
	 *         is generated as follows:
	 *         <code>ExchangeDescriptorNameExchangeApiDescriptorNameApi</code>.
	 */
	public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, 
												  ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
										+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Api";
		return pkgPrefix + simpleInterfaceName;
	}
	
	/**
	 * @param exchangeDescriptor The exchange the API group belongs to
	 * @param exchangeApiDescriptor The exchange API group to get the full class name for
	 * @return The full name of the ExchangeApi interface implmentation class for
	 *         the given exchange and API, which is interface class name (see
	 *         {@link #getApiInterfaceClassName(ExchangeDescriptor, ExchangeApiDescriptor)})
	 *         suffixed with <code>Impl</code>.
	 */
	public static String getApiInterfaceImplementationClassName(ExchangeDescriptor exchangeDescriptor, 
												  				ExchangeApiDescriptor exchangeApiDescriptor) {
		return getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) + "Impl";
	}
	
	/**
	 * @param rateLimitName The name of the rate limit to generate the static variable name for
	 * @return The name of static variable for the given rate limit name.
	 */
	public static String generateRateLimitVariableName(String rateLimitName) {
		return "RATE_LIMIT_" + JavaCodeGenerationUtil.getStaticVariableName(rateLimitName);
	}

	/**
	 * @param exchangeDescriptor The exchange to generate the full class name for
	 * @return The name of the Exchange interface class for the given exchange.
	 */
	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
	}

	/**
	 * @param exchangeDescriptor The exchange where constants are defined
	 * @return The full class name of constants interface defined at exchange level
	 * @see ExchangeDescriptor#getConstants()
	 */
	public static String getExchangeConstantsInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Constants";
	}
	
	/**
	 * @param exchangeDescriptor The exchange where the API group belongs to
	 * @param exchangeApiDescriptor The exchange API group for which constants interface stands for.
	 * @return the full class name of constants interface defined at exchange API level.
	 */
	public static String getExchangeApiConstantsInterfaceName(ExchangeDescriptor exchangeDescriptor, 
															  ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
										+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Constants";
		return pkgPrefix + simpleInterfaceName;
	}
	
	/**
	 * @param exchangeDescriptor The exchange where constants are defined
	 * @return The full class name of constants interface defined at exchange level
	 * @see ExchangeDescriptor#getConstants()
	 */
	public static String getExchangePropertiesInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() 
				+ "." 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ "Properties";
	}

	/**
	 * @param deserializedTypeClassName The full class name of the type to generate JSON message deserializer class name for.
	 * @return The name of JSON message deserializer class for the given deserialized type.
	 */
	public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
		return new StringBuilder()
					.append(StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedTypeClassName), ".pojo"))
					.append(".deserializers.")
					.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName))
					.append("Deserializer")
					.toString();
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
	 * 		         <li>BIGDECIMAL: {@link BigDecimal}</li>
	 * 		         <li>BOOLEAN: {@link Boolean}</li>
	 * 		         <li>INT: {@link Integer}</li>
	 * 		         <li>LONG: {@link Long}</li>
	 * 		         <li>TIMESTAMP: {@link Long}</li>
	 * 		         <li>STRING: {@link String}</li>
	 * 		       </ul>
	 * 		     <li>If this type is a 'List' type, returns generic <code>List&lt;SubTypeClassName&gt;</code>, 
	 * 				where <code>subTypeClassName</code> is the simple class name of the subType of the list, see {@link Type#getSubType()}.
	 * 		     <li>If this type is a 'Map' type, returns generic <code>Map&lt;String, SubTypeClassName&gt;</code>, 
	 * 				where <code>subTypeClassName</code> is the simple class name of the subType of the map, see {@link Type#getSubType()}.
	 * 		     <li>If this type is an 'Object' type, returns the simple class name of the object class name, 
	 * 				 see {@link JavaCodeGenerationUtil#getClassNameWithoutPackage(String)}.
	 * 		     </li>
	 * 		   </ul>
	 * @see #getClassNameForType(Type, Imports, String)
	 * @throws IllegalArgumentException if the type is not recognized.
	 */
	public static String getClassNameForType(Type type, 
										     Imports imports, 
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
			return canonicalTypeClass.getSimpleName();
		case BOOLEAN:
		case INT:
		case LONG:
		case TIMESTAMP:
		case STRING:
			return canonicalTypeClass.getSimpleName();
		case LIST:
			subTypeClassName = getClassNameForType(type.getSubType(), imports, objectClassName);
			if (imports != null) {
				imports.add(canonicalTypeClass.getName());
			}
			return canonicalTypeClass.getSimpleName() 
					+ "<" 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case MAP:
			subTypeClassName = getClassNameForType(type.getSubType(), imports, objectClassName);
			if (imports != null) {
				imports.add(canonicalTypeClass.getName());
			}
			return canonicalTypeClass.getSimpleName() 
					+ "<String, " 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case OBJECT:
			if (imports != null) {
				imports.add(objectClassName);
			}
			return JavaCodeGenerationUtil.getClassNameWithoutPackage(objectClassName);
		default:
			throw new IllegalArgumentException("Unexpected type for:" + type);
		}
	}

	/**
	 * Returns the instruction to create a new instance of a {@link AbstractJsonMessageDeserializer} for the given type.
	 * @param type The type of the field to deserialize
	 * @param objectClassName The object class name of the field to generate JsonFieldDeserializer for. 
	 * 						  This parameter is used only if the type is an 'object' type (see {@link Type#isObject()} ).
	 * @param imports The imports of the generator context that will be populated with classes 
	 * 				  used by returned type. That set must be not <code>null</code> and mutable.
	 * @return The instruction to get or create a new instance of a {@link AbstractJsonMessageDeserializer} for the given type:
	 * 		    <ul>
	 * 		      <li>if this type is a 'primitive' type, returns the instruction to get the 
	 * 				  singleton instance of the corresponding JsonFieldDeserializer:
	 * 		        <ul>
	 * 		          <li>BIGDECIMAL: {@link BigDecimalJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>BOOLEAN: {@link BooleanJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>INT: {@link IntegerJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>LONG: {@link LongJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>TIMESTAMP: {@link TimestampJsonFieldDeserializer#getInstance()}</li>
	 * 		          <li>STRING: {@link StringJsonFieldDeserializer#getInstance()}</li>
	 * 		        </ul>
	 * 		      </li>
	 * 		      <li>if this type is a 'List' type, returns the instruction to create a new instance of 
	 * 				  a {@link ListJsonFieldDeserializer} with the subType of the list, see {@link Type#getSubType()}.
	 * 		      <li>if this type is a 'Map' type, returns the instruction to create a new instance of 
	 * 				  a {@link MapJsonFieldDeserializer} with the subType of the map, see {@link Type#getSubType()}.
	 * 		      <li>if this type is an 'Object' type, returns the 'new' instruction to create a new instance of 
	 * 				  a {@link AbstractJsonMessageDeserializer} for the object 
	 * 				  class name, see {@link #getJsonMessageDeserializerClassName(String)}.
	 * 		    </ul>
	 * @see Type
	 * @throws IllegalArgumentException if the type is not recognized.
	 */
	public static String getNewJsonFieldDeserializerInstruction(Type type, String objectClassName, Imports imports) {
		if (type == null) {
			type  = Type.fromTypeName(CanonicalType.STRING.name());
		}
		switch (type.getCanonicalType()) {
		case BIGDECIMAL:
			imports.add(BigDecimalJsonFieldDeserializer.class.getName());
			return BigDecimalJsonFieldDeserializer.class.getSimpleName() +GET_INSTANCE;
		case BOOLEAN:
			imports.add(BooleanJsonFieldDeserializer.class.getName());
			return  BooleanJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
		case INT:
			imports.add(IntegerJsonFieldDeserializer.class.getName());
			return  IntegerJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
		case LONG:
			imports.add(LongJsonFieldDeserializer.class.getName());
			return  LongJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
		case STRING:
			imports.add(StringJsonFieldDeserializer.class.getName());
			return  StringJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
		case TIMESTAMP:
			imports.add(TimestampJsonFieldDeserializer.class.getName());
			return  TimestampJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
		case LIST:
			imports.add(ListJsonFieldDeserializer.class.getName());
			return "new " + ListJsonFieldDeserializer.class.getSimpleName() + "<>(" 
					+ getNewJsonFieldDeserializerInstruction(type.getSubType(), objectClassName, imports) + ")";
		case MAP:
			imports.add(MapJsonFieldDeserializer.class.getName());
			return "new " + MapJsonFieldDeserializer.class.getSimpleName() 
					+ "<>(" + getNewJsonFieldDeserializerInstruction(type.getSubType(), objectClassName, imports) +")";
		case OBJECT:
			String objectDeserializerClass = getJsonMessageDeserializerClassName(objectClassName);
			imports.add(objectDeserializerClass);
			return "new " +  JavaCodeGenerationUtil.getClassNameWithoutPackage(objectDeserializerClass) + "()";
		default:
			throw new IllegalArgumentException("Unexpected field type:" + type);
		}
	}

	/**
	 * @param type        must be a primitive type: {@link Type#STRING},
	 *                    {@link Type#INT}, {@link Type#LONG},
	 *                    {@link Type#TIMESTAMP}, {@link Type#BIGDECIMAL}.
	 *                    Otherwise,
	 * @param sampleValue primitive type sample value which can be a string '\"12\"'
	 *                    or object value '12'. Can be <code>null</code> in which
	 *                    case returned value is <code>null</code>
	 * @param imports     Imports set to add eventual additional classes required by
	 *                    the generation instruction to
	 * @return An instruction to create value represented by sample value
	 */
	public static String getPrimitiveTypeFieldSampleValueDeclaration(Type type, 
																	 Object sampleValue,	
																	 Imports imports) {
		if (sampleValue == null) {
			return null;
		}
		String sampleValueStr = sampleValue.toString();
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
			return "Long.valueOf(" + JavaCodeGenerationUtil.getQuotedString(sampleValueStr) + ")";
		default: // STRING
			return JavaCodeGenerationUtil.getQuotedString(sampleValueStr);
		}
	}
	
	/**
	 * @param exchangeDescriptor Exchange descriptor to retrieve base HTTP url from
	 *                           see {@link ExchangeDescriptor#getHttpUrl()}
	 * @return Generated public static {@link #HTTP_URL_STATIC_VARIABLE} variable
	 *         with value from {@link ExchangeDescriptor#getHttpUrl()}, or
	 *         <code>null</code> if that value is <code>null</code>.
	 */
	public static String getHttpUrlVariableDeclaration(ExchangeDescriptor exchangeDescriptor) {
		return getStaticUrlVariableDeclaration(ExchangeJavaGenUtil.HTTP_URL_STATIC_VARIABLE, 
											   exchangeDescriptor.getHttpUrl(), "Base REST API URL");
	}
	
	/**
	 * @param exchangeDescriptor Exchange descriptor to retrieve base Websocket url from
	 *                           see {@link ExchangeDescriptor#getWebsocketUrl()}
	 * @return Generated public static {@link #WEBSOCKET_URL_STATIC_VARIABLE} variable
	 *         with value from {@link ExchangeDescriptor#getWebsocketUrl()}, or
	 *         <code>null</code> if that value is <code>null</code>.
	 */
	public static String getWebsocketUrlVariableDeclaration(ExchangeDescriptor exchangeDescriptor) {
		return getStaticUrlVariableDeclaration(ExchangeJavaGenUtil.WEBSOCKET_URL_STATIC_VARIABLE, 
											   exchangeDescriptor.getWebsocketUrl(), "Base websocket endpoint URL");
	}
	
	private static String getStaticUrlVariableDeclaration(String variableName, String value, String description) {
		if (value == null) {
			return null;
		}
		return new StringBuilder()
				.append(JavaCodeGenerationUtil.generateJavaDoc(description))
				.append("\npublic static final String ")
				.append(variableName)
				.append(" = ")
				.append(JavaCodeGenerationUtil.getQuotedString(value))
				.append(";").toString();
	}

	/**
	 * Generates the name of the interface implementation class for the given exchange descriptor
	 * @param exchangeDescriptor exchange descriptor
	 * @return full name of the interface implementation class
	 */
	public static String getExchangeInterfaceImplementationName(ExchangeDescriptor exchangeDescriptor) {
		return getExchangeInterfaceImplementationName(ExchangeInterfaceImplementationGenerator.getExchangeInterfaceName(exchangeDescriptor));
	}
	
	/**
	 * Generates the name of the interface implementation class for the given exchange class name
	 * @param exchangeClassName exchange class bale
	 * @return full name of the interface implementation class
	 */
	public static String getExchangeInterfaceImplementationName(String exchangeClassName) {
		return exchangeClassName + "Impl";
	}
	
	/**
	 * Find the type of a field in context of REST/Websocket API code generation: If
	 * field type is <code>null</code> the type is assumed to be {@link Type#OBJECT}
	 * 
	 * @param field The field to retrieve type of in context of REST/Websocket API
	 *              code generation
	 * @return <code>null</code> if field is <code>null</code>, {@link Type#OBJECT}
	 *         if field type is <code>null</code>, the field type see
	 *         {@link Field#getType()} otherwise.
	 */
	public static Type getFieldType(Field field) {
		return field == null? null: Optional.ofNullable(field.getType()).orElse(Type.OBJECT);
	}

}
