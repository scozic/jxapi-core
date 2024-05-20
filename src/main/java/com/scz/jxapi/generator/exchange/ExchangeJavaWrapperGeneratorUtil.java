package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.deserialization.RawBigDecimalMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawBooleanMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawIntegerMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawLongMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
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
		EndpointParameter request = restEndpointDescriptor.getRequest();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												request.getEndpointParameterType(), 
												request.getObjectName(), 
												"Request");
	}
	
	public static String generateRestEnpointResponseClassName(ExchangeDescriptor exchangeDescriptor, 
															  ExchangeApiDescriptor exchangeApiDescriptor, 
															  RestEndpointDescriptor restEndpointDescriptor) {
		EndpointParameter response = restEndpointDescriptor.getResponse();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												response.getEndpointParameterType(), 
												response.getObjectName(), 
												"Response");
	}
	
	private static String generateRestEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, 
														   ExchangeApiDescriptor exchangeApiDescriptor, 
														   String endpointName, 
														   EndpointParameterType type,
														   String objectName,
														   String suffix) {
		if (type == null) {
			type = EndpointParameterType.fromTypeName(CanonicalEndpointParameterTypes.OBJECT.name());
		}
		if (type.isObject() && objectName != null) {
			return exchangeDescriptor.getBasePackage() + "." 
					+ exchangeApiDescriptor.getName().toLowerCase() + ".pojo." 
					+ JavaCodeGenerationUtil.firstLetterToUpperCase(objectName);
		} else if (objectName != null) {
			throw new IllegalArgumentException(
					"Unexpected objectName provided:[" + objectName + "] for a non-object data type:" + type
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
	
	public static String generateWebsocketEndpointMessageClassName(ExchangeDescriptor exchangeDescriptor,
																   ExchangeApiDescriptor exchangeApiDescriptor, 
																   WebsocketEndpointDescriptor websocketApi) {
		EndpointParameter message = websocketApi.getMessage();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												websocketApi.getName(), 
												message.getEndpointParameterType(), 
												message.getObjectName(), 
												"Message");
	}

	public static String generateWebsocketEndpointRequestClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		EndpointParameter request = websocketApi.getRequest();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												websocketApi.getName(), 
												request.getEndpointParameterType(), 
												request.getObjectName(), 
												"Request");
	}
	
	public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, 
												  ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
										+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Api";
		return pkgPrefix + simpleInterfaceName;
	}
	
	public static String getWebsocketSubscribeMethodName(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		return "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketEndpointDescriptor.getName());
	}
	
	public static String getWebsocketUnsubscribeMethodName(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		return "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketEndpointDescriptor.getName());
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
		if (type == null) {
			type  = EndpointParameterType.fromTypeName(CanonicalEndpointParameterTypes.STRING.name());
		}
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
		List<EndpointParameter> res = null;
		for (RestEndpointDescriptor restEndpointDescriptor: 
				Optional.ofNullable(exchangeApiDescriptor.getRestEndpoints()).orElse(List.of())) {
			res = findParametersForObjectName(requestObjectName, restEndpointDescriptor.getRequest());
			if (res != null) {
				break;
			}
			res = findParametersForObjectName(requestObjectName, restEndpointDescriptor.getResponse());
			if (res != null) {
				break;
			}
		}
		
		if (res == null) {
			for (WebsocketEndpointDescriptor websocketEndpointDescriptor: 
					Optional.ofNullable(exchangeApiDescriptor.getWebsocketEndpoints()).orElse(List.of())) {
				res = findParametersForObjectName(requestObjectName, websocketEndpointDescriptor.getRequest());
				if (res != null) {
					break;
				}
				res = findParametersForObjectName(requestObjectName, websocketEndpointDescriptor.getMessage());
				if (res != null) {
					break;
				}
			}
		}
		
		if (res != null) {
			return resolveEndpointParameters(exchangeApiDescriptor, res);
		}
		throw new IllegalArgumentException("Found no REST request or response or Websocket request or message with fields defined for objectName:"  
										   + requestObjectName);
	}
	
	private static List<EndpointParameter> findParametersForObjectName(String requestObjetName, EndpointParameter param) {
		if (param == null) {
			return null;
		}
		List<EndpointParameter> res = param.getParameters();
		if (res == null) {
			return null;
		}
		if (Objects.equals(requestObjetName, param.getObjectName())) {
			return res;
		}
		for (EndpointParameter p: res) {
			res = findParametersForObjectName(requestObjetName, p);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	public static List<EndpointParameter> getEndpointParameters(List<EndpointParameter> endpointDescriptiorParameters, 
																String requestObjectName, 
																ExchangeApiDescriptor exchangeApiDescriptor) {
		if (endpointDescriptiorParameters != null) {
			return endpointDescriptiorParameters;
		} else if (requestObjectName != null) {
			return findParametersForObjectName(requestObjectName, exchangeApiDescriptor);
		} else {
			return List.of();
		}
	}
	
	public static EndpointParameter resolveEndpointParameters(ExchangeApiDescriptor exchangeApiDescriptor, 
															  EndpointParameter parameter) {
		if (parameter == null) {
			return null;
		}
		if (parameter.getEndpointParameterType() == null) {
			return parameter; 
		}
		if (parameter.getEndpointParameterType() != null 
			&& parameter.getEndpointParameterType().isObject() 
			&& parameter.getObjectName() != null 
			&& parameter.getParameters() == null) {
			parameter = parameter.clone();
			parameter.setParameters(findParametersForObjectName(parameter.getObjectName(), exchangeApiDescriptor));
		}
		return parameter;
	}
	
	public static List<EndpointParameter> resolveEndpointParameters(ExchangeApiDescriptor exchangeApiDescriptor, 
			List<EndpointParameter> parameters) {
		return parameters.stream().map(e -> resolveEndpointParameters(exchangeApiDescriptor, e)).collect(Collectors.toList());
	}

	public static boolean websocketEndpointHasArguments(WebsocketEndpointDescriptor websocketApi, 
														ExchangeApiDescriptor exchangeApiDescriptor) {
		EndpointParameter request = websocketApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request.getEndpointParameterType(), 
									request.getParameters(), 
									request.getObjectName(), 
									exchangeApiDescriptor);
	}

	public static boolean restEndpointHasArguments(RestEndpointDescriptor restApi, 
												   ExchangeApiDescriptor exchangeApiDescriptor) {
		EndpointParameter request = restApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request.getEndpointParameterType(),
									request.getParameters(), 
									request.getObjectName(), 
									exchangeApiDescriptor);
	}

	private static boolean endpointHasArguments(EndpointParameterType dataType, 
												List<EndpointParameter> parameters, 
												String requestObjectName, 
												ExchangeApiDescriptor exchangeApiDescriptor) {
		return (dataType != null && dataType.getCanonicalType() != CanonicalEndpointParameterTypes.OBJECT) 
				|| getEndpointParameters(parameters, requestObjectName, exchangeApiDescriptor).size() > 0;
	}
	
	public static String getRequestArgName(String requestArgNameFromApiDescriptor) {
		return Optional.ofNullable(requestArgNameFromApiDescriptor).orElse(DEFAULT_REQUEST_ARG_NAME);
	}

	public static boolean restEndpointHasResponse(RestEndpointDescriptor restApi, ExchangeApiDescriptor exchangeApiDescriptor) {
		EndpointParameter response = restApi.getResponse();
		if (response == null) {
			return false;
		}
		EndpointParameterType dataType = response.getEndpointParameterType();
		return (dataType != null && dataType.getCanonicalType() != CanonicalEndpointParameterTypes.OBJECT) 
				|| getEndpointParameters(response.getParameters(), response.getObjectName(), exchangeApiDescriptor).size() > 0;
	}
	
	public static String getNewMessageDeserializerInstruction(EndpointParameterType messageType, 
															  String messageFullClassName, 
															  Set<String> imports) {
		if (messageType == null) {
			messageType  = EndpointParameterType.fromTypeName(CanonicalEndpointParameterTypes.STRING.name());
		}
		switch (messageType.getCanonicalType()) {
		case BIGDECIMAL:
			imports.add(RawBigDecimalMessageDeserializer.class.getName());
			return RawBigDecimalMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case BOOLEAN:
			imports.add(RawBooleanMessageDeserializer.class.getName());
			return RawBooleanMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case INT:
			imports.add(RawIntegerMessageDeserializer.class.getName());
			return RawIntegerMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case TIMESTAMP:
		case LONG:
			imports.add(RawLongMessageDeserializer.class.getName());
			return RawLongMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case STRING:
			imports.add(RawStringMessageDeserializer.class.getName());
			return RawStringMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case OBJECT:
		case LIST:
		case MAP:
		default:
			return getNewJsonParameterDeserializerInstruction(messageType, messageFullClassName, imports);
		}
	}

	public static EndpointParameterType getEndpointParameterType(EndpointParameter parameter) {
		return parameter == null? null: Optional.ofNullable(parameter.getEndpointParameterType()).orElse(EndpointParameterType.OBJECT);
	}

}
