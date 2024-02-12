package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

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
 * Helper methods for generation of Java classes of a given exchange wrapper
 */
public class ExchangeJavaWrapperGeneratorUtil {
	
	public static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	public static final String DEFAULT_REQUEST_ARG_NAME = "request";
	
	/**
	 * Generates the expected full class name of JSON serializer class for a given
	 * POJO full class name.
	 * 
	 * @param pojoClassName The full class name of POJO. It is intended to be a
	 *                      generated POJO with '.pojo' in package name.
	 * @return Full class name of JSON serializer class, that is a class in sub
	 *         package 'serializers' of parent package of 'pojo' package, named
	 *         <code>&lt;POJO simple class name&gt; + 'Serializer'</code>.
	 */
	public static String getSerializerClassName(String pojoClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(pojoClassName), ".pojo");
		return pkg + ".serializers." + JavaCodeGenerationUtil.getClassNameWithoutPackage(pojoClassName) + "Serializer";
	}
	
	public static String generateRestEnpointRequestClassName(ExchangeDescriptor exchangeDescriptor, 
															 ExchangeApiDescriptor exchangeApiDescriptor, 
															 RestEndpointDescriptor restEndpointDescriptor) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												restEndpointDescriptor.getRequestDataType(), 
												restEndpointDescriptor.getRequestObjectName(), 
												"Request");
	}
	
	public static String generateRestEnpointResponseClassName(ExchangeDescriptor exchangeDescriptor, 
															  ExchangeApiDescriptor exchangeApiDescriptor, 
															  RestEndpointDescriptor restEndpointDescriptor) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												restEndpointDescriptor.getResponseDataType(), 
												restEndpointDescriptor.getResponseObjectName(), 
												"Response");
	}
	
	private static String generateRestEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, 
														   ExchangeApiDescriptor exchangeApiDescriptor, 
														   String endpointName, 
														   String dataType,
														   String objectName,
														   String suffix) {
		EndpointParameterType type = EndpointParameterType.fromTypeName(dataType);
		if (type.isObject() && objectName != null) {
			return exchangeDescriptor.getBasePackage() + "." 
					+ exchangeApiDescriptor.getName().toLowerCase() + ".pojo." 
					+ JavaCodeGenerationUtil.firstLetterToUpperCase(objectName);
		} else if (objectName != null) {
			throw new IllegalArgumentException(
					"Unexpected objectName provided:[" + objectName + "] for a non-object data type:" + dataType
							+ " in endpoint descriptor:" + endpointName + " " + suffix);
		}
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(endpointName)
				+ suffix;
	}
	
	public static void generateExchangeApiInterface(ExchangeDescriptor exchangeDescriptor, 
													ExchangeApiDescriptor exchangeApiDescriptor, 
													Path outputFolder) throws IOException {
		new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
		new ExchangeApiInterfaceImplementationGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
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
	
	public static String generateWebsocketEndpointMessageClassName(ExchangeDescriptor exchangeDescriptor,
																   ExchangeApiDescriptor exchangeApiDescriptor, 
																   WebsocketEndpointDescriptor websocketApi) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												websocketApi.getName(), 
												websocketApi.getMessageDataType(), 
												websocketApi.getMessageObjectName(), 
												"Message");
	}

	public static String generateWebsocketEndpointRequestClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												websocketApi.getName(), 
												websocketApi.getRequestDataType(), 
												websocketApi.getRequestObjectName(), 
												"Request");
	}
	
	public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, 
												  ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
										+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Api";
		return pkgPrefix + simpleInterfaceName;
	}
	
	public static boolean exchangeApiHasRateLimits(ExchangeApiDescriptor exchangeApiDescriptor, 
												   ExchangeDescriptor exchangeDescriptor) {
		if (exchangeDescriptor.getRateLimits() != null && !exchangeDescriptor.getRateLimits().isEmpty()) {
			return true;
		}
		if (exchangeApiDescriptor.getRateLimits() != null && !exchangeDescriptor.getRateLimits().isEmpty()) {
			return true;
		}
		for (RestEndpointDescriptor api : exchangeApiDescriptor.getRestEndpoints()) {
			if (api.getRateLimits() != null && !api.getRateLimits().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static String generateRateLimitVariableName(String rateLimitName) {
		return "RATE_LIMIT_" + JavaCodeGenerationUtil.getStaticVariableName(rateLimitName);
	}

	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
	}

	public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedTypeClassName), ".pojo") + ".deserializers";
		return pkg + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName) + "Deserializer";
	}

	/**
	 * Generates expected class name for an {@link EndpointParameter} instance
	 * according to its type (see
	 * {@link EndpointParameter#getEndpointParameterType()} .
	 * <ul>
	 * <li>For a primitive (see {@link CanonicalEndpointParameterTypes#isPrimitive}
	 * parameter type, the corresponding primitive type class is returned:
	 * <code>java.lang.String</code>, <code>java.lang.Integer</code> ...)</li>
	 * <li>For an object (see {@link CanonicalEndpointParameterTypes#OBJECT}
	 * parameter type), if the <code>objectName</code> (see
	 * {@link EndpointParameter#getObjectName()} is defined, that object name is
	 * returned.</li>
	 * <li>For list or map (see {@link CanonicalEndpointParameterTypes#LIST},
	 * {@link CanonicalEndpointParameterTypes#MAP}), the corresponding generic class
	 * is returned e.g. <code>java.util.List</code> or <code>java.util.Map</code>,
	 * with generic parameter set with the class for subtype (see
	 * {@link EndpointParameterType#getSubType()} of <code>endpointParameter</code>
	 * as returned by this method.</li>
	 * </ul>
	 * The returned value is the class simple name (without package prefix). The
	 * class full name is added to <code>imports</code> set passed in parameter. If
	 * type is a list or map, the generic parameter {@link List} or {@link Map} is
	 * also added to imports.
	 * <p>
	 * A few examples of returned values and import added for some examples of a
	 * parameter named <code>Bar</code>:
	 * <table border="1">
	 * <thead>
	 * <tr>
	 * <th>Type</th>
	 * <th>Enclosing class name</th>
	 * <th>objectName</th>
	 * <th>Returned value</th>
	 * <th>Imports added</th>
	 * <th>Notes</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>STRING</code></td>
	 * <td><i>any</i></td>
	 * <td><i>any</i></td>
	 * <td><code>String</code></td>
	 * <td><i>none</i></td>
	 * <td>Primitive parameter type, and {@link String} is in <code>java.lang</code>
	 * package, no import added.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>BIGDECIMAL</code></td>
	 * <td><i>any</i></td>
	 * <td><i>any</i></td>
	 * <td><code>BigDecimal</code></td>
	 * <td><code><ul><li><code>java.lang.BigDecimal</code></li>
	 * </ul>
	 * </td>
	 * <td>Primitive parameter type, and {@link BigDecimal} is in
	 * <code>java.math</code> package, <code>java.lang.BigDecimal</code> import
	 * added.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>OBJECT</code></td>
	 * <td><code>com.x.y.gen.pojo.Foo</code></td>
	 * <td><code>null</code></td>
	 * <td><code>FooBar</code></td>
	 * <td>
	 * <ul>
	 * <li></code>com.x.y.gen.pojo.FooBar</code></li>
	 * </ul>
	 * </td>
	 * <td><code>OBJECT</code> parameter and no object name specified, the returned
	 * type is generated name by concatenating enclosing class name with parameter
	 * name</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>OBJECT</code></td>
	 * <td><code>com.x.y.gen.pojo.Foo</code></td>
	 * <td><code>MySpecialObjectName</code></td>
	 * <td><code>MySpecialObjectName</code></td>
	 * <td><ul><li></code>com.x.y.gen.pojo.MySpecialObjectName</code></li>
	 * </ul>
	 * </td>
	 * <td><code>OBJECT</code> parameter with <code>objectName</code> specified, the
	 * returned type is <code>objectName</code> as class name, assumed to be in same
	 * package as enclosing class name package.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>OBJECT_MAP_LIST</td>
	 * <td><code>com.x.y.gen.pojo.Foo</code></td>
	 * <td><code>null</code></td>
	 * <td><code>List&lt;Map&lt;String, FooBar&gt;&gt;</code></td>
	 * <td>
	 * <ul>
	 * <li><code>java.util.List</code></li>
	 * <li><code>java.util.Map</code></li>
	 * <li><code>com.x.y.gen.pojo.FooBar</code></li>
	 * </ul>
	 * </td>
	 * <td><code>OBJECT</code> parameter nested in list of map with no
	 * <code>objectName</code> specified, the returned type is
	 * <code>List&lt;Map&lt;String, FooBar&gt;&gt;</code> and {@link Map},
	 * {@link List} and class for object parameter are added to imports.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * </p>
	 * 
	 * @param endpointParameter  The endpoint parameter
	 * @param imports            The imports of generator context that will be
	 *                           populated with classes used by returned type. That
	 *                           set must be not <code>null</code> and mutable.
	 * @param enclosingClassName The POJO class containing the endpoint to generate
	 *                           class name for type of.
	 * @return The simple class name for type of <code>enpointParameter</code>
	 *         parameter.
	 */
	public static String getClassNameForEndpointParameter(EndpointParameter endpointParameter, 
														  Set<String> imports, 
														  String enclosingClassName) {
		String objectClassName = null;
		if (endpointParameter.getEndpointParameterType().isObject()) {
			 objectClassName = getParameterObjectClassName(endpointParameter, imports, enclosingClassName);
			 
		}
		return getClassNameForParameterType(
					endpointParameter.getEndpointParameterType(), 
					imports, 
					objectClassName);
	}

	/**
	 * Generates the expected full class name for the object described by an
	 * {@link EndpointParameter}, assuming it is of 'object' type (see
	 * {@link EndpointParameterType#isObject()}).<br/>
	 * The returned class package will be one of
	 * <code>enclosingClassName</code>.<br/>
	 * 
	 * @param endpointParameter  The endpoint parameter
	 * @param imports            The imports of generator context that will be
	 *                           populated with classes used by returned type. That
	 *                           set must be not <code>null</code> and mutable.
	 * @param enclosingClassName The POJO class containing the endpoint to generate
	 *                           class name for type of.
	 * @return parameter object name with first letter to uppercase if
	 *         {@link EndpointParameter#getObjectName()} is not <code>null</code>,
	 *         else concatenation of <code>&lt;enclosingClassName&gt;+&lt;enpointParameter name&gt;</code>
	 */
	public static String getParameterObjectClassName(EndpointParameter endpointParameter, 
													 Set<String> imports, 
													 String enclosingClassName) {
		if (endpointParameter.getObjectName() != null) {
			 return JavaCodeGenerationUtil.getClassPackage(enclosingClassName) + "." 
					 + JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getObjectName());
		 } else {
			 return enclosingClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getName());
		 }
	}

	/**
	 * Same as
	 * {@link getClassNameForEndpointParameter} but
	 * with objectClassName known.
	 * 
	 * @param type            the type of endpointParameter to get type class name
	 *                        of.
	 * @param imports         The imports of generator context that will be
	 *                        populated with classes used by returned type. That set
	 *                        must be not <code>null</code> and mutable.
	 * @param objectClassName the object class name if parameter is of object type,
	 *                        as provided by
	 *                        {@link getParameterObjectClassName}
	 * @return simple class name for type
	 * 
	 * @see #getClassNameForParameterType(EndpointParameterType, Set, String)
	 */
	public static String getClassNameForParameterType(EndpointParameterType type, 
													  Set<String> imports, 
													  String objectClassName) {
		if (type == null) {
			return null;
		}
		String subTypeClassName = null;
		switch(type.getCanonicalType()) {
		case BIGDECIMAL:
			if (imports != null) {
				imports.add(BigDecimal.class.getName());
			}
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
			subTypeClassName = getClassNameForParameterType(type.getSubType(), imports, objectClassName);
			if (imports != null) {
				imports.add(List.class.getName());
				imports.add(subTypeClassName);
			}
			return List.class.getSimpleName() 
					+ "<" 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case MAP:
			subTypeClassName = getClassNameForParameterType(type.getSubType(), imports, objectClassName);
			if (imports != null) {
				imports.add(Map.class.getName());
				imports.add(subTypeClassName);
			}
			return Map.class.getSimpleName() 
					+ "<String, " 
					+ JavaCodeGenerationUtil.getClassNameWithoutPackage(subTypeClassName) 
					+ ">";
		case OBJECT:
			if (imports != null) {
				imports.add(objectClassName);
			}
			return JavaCodeGenerationUtil.getClassNameWithoutPackage(objectClassName);
			//return objectClassName;
		default:
			throw new IllegalArgumentException("Unexpected type for:" + type);
		}
	}

	/**
	 * Generates full class name of an {@link EndpointParameter} instance
	 * <code>leaf</code> type, see
	 * {@link EndpointParameterType#getLeafSubType(EndpointParameterType)}.
	 * 
	 * @param endpointParameterName See {@link EndpointParameter#getName()} name
	 * @param type                  See {@link EndpointParameter#getEndpointParameterType()}
	 * @param objectName            See {@link EndpointParameter#getObjectName()}
	 * @param imports               The imports of generator context that will be
	 *                              populated with classes used by returned type.
	 *                              That set must be not <code>null</code> and
	 *                              mutable.
	 * @param enclosingClassName    The POJO class containing the endpoint to
	 *                              generate class name for type of.
	 * @return the full class name of leaf subType of endpoint parameter.
	 */
	public static String getLeafObjectParameterClassName(String endpointParameterName, 
														 EndpointParameterType type, 
														 String objectName, 
														 Set<String> imports, 
														 String enclosingClassName) {
		String pkg = "";
		if (type.isObject()) {
			pkg = JavaCodeGenerationUtil.getClassPackage(enclosingClassName) + ".";
			if (objectName != null) {
				return pkg  + objectName;
			}
		}
		
		return pkg + getClassNameForParameterType(
				  EndpointParameterType.getLeafSubType(type)
				  ,imports
				  , enclosingClassName) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameterName);
	}

	public static String getNewJsonParameterDeserializerInstruction(EndpointParameterType type, String objectClassName, Set<String> imports) {
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

	public static List<EndpointParameter> findParametersForObjectName(String requestObjectName, ExchangeApiDescriptor exchangeApiDescriptor) {
		for (RestEndpointDescriptor restEndpointDescriptor: exchangeApiDescriptor.getRestEndpoints()) {
			if (requestObjectName.equals(restEndpointDescriptor.getRequestObjectName())) {
				if (restEndpointDescriptor.getParameters() != null) {
					return restEndpointDescriptor.getParameters();
				}
			}
			if (requestObjectName.equals(restEndpointDescriptor.getResponseObjectName())) {
				if (restEndpointDescriptor.getResponse() != null) {
					return restEndpointDescriptor.getResponse();
				}
			}
		}
		
		for (WebsocketEndpointDescriptor restEndpointDescriptor: exchangeApiDescriptor.getWebsocketEndpoints()) {
			if (requestObjectName.equals(restEndpointDescriptor.getRequestObjectName())) {
				if (restEndpointDescriptor.getParameters() != null) {
					return restEndpointDescriptor.getParameters();
				}
			}
			if (requestObjectName.equals(restEndpointDescriptor.getMessageObjectName())) {
				if (restEndpointDescriptor.getResponse() != null) {
					return restEndpointDescriptor.getResponse();
				}
			}
		}
		throw new IllegalArgumentException("Found no REST request or response or Websocket request or message with fields defined for objectName:"  + requestObjectName);
	}

	public static List<EndpointParameter> getEndpointParameters(List<EndpointParameter> endpointDescriptiorParameters, String requestObjectName, ExchangeApiDescriptor exchangeApiDescriptor) {
		if (endpointDescriptiorParameters != null) {
			return endpointDescriptiorParameters;
		} else if (requestObjectName != null) {
			return findParametersForObjectName(requestObjectName, exchangeApiDescriptor);
		} else {
			return List.of();
		}
	}

	public static boolean websocketEndpointHasArguments(WebsocketEndpointDescriptor websocketApi, ExchangeApiDescriptor exchangeApiDescriptor) {
		return ExchangeJavaWrapperGeneratorUtil.endpointHasArguments(websocketApi.getRequestDataType(), 
									websocketApi.getParameters(), 
									websocketApi.getRequestObjectName(), 
									exchangeApiDescriptor);
	}

	public static boolean restEndpointHasArguments(RestEndpointDescriptor restApi, ExchangeApiDescriptor exchangeApiDescriptor) {
		return ExchangeJavaWrapperGeneratorUtil.endpointHasArguments(restApi.getRequestDataType(), 
									restApi.getParameters(), 
									restApi.getRequestObjectName(), 
									exchangeApiDescriptor);
	}

	private static boolean endpointHasArguments(String requestDataType, List<EndpointParameter> parameters, String requestObjectName, ExchangeApiDescriptor exchangeApiDescriptor) {
		EndpointParameterType dataType = EndpointParameterType.fromTypeName(requestDataType);
		return (dataType != null && dataType.getCanonicalType() != CanonicalEndpointParameterTypes.OBJECT) 
				|| getEndpointParameters(parameters, requestObjectName, exchangeApiDescriptor).size() > 0;
	}
	
	public static String getRequestArgName(String requestArgNameFromApiDescriptor) {
		return Optional.ofNullable(requestArgNameFromApiDescriptor).orElse(DEFAULT_REQUEST_ARG_NAME);
	}

}
