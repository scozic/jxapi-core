package com.scz.jxapi.generator.java.exchange.api;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.netutils.deserialization.RawBigDecimalMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawBooleanMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawIntegerMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawLongMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;

/**
 * Helper static methods around generation of {@link ExchangeApi} java classes implementation.
 */
public class ExchangeApiGeneratorUtil {
	
	private ExchangeApiGeneratorUtil() {}
	
	/**
	 * Default name for request argument
	 */
	public static final String DEFAULT_REQUEST_ARG_NAME = "request";

	/**
	 * Generates expected POJO class name for a REST endpoint request POJO.
	 * <p>
	 * Relevant when request is defined for the endpoint and is of object type (see {@link Type#isObject()}).
	 * <p>
	 * The package of the generated class is the sub-package <code>pojo</code> of the base package of the exchange descriptor.
	 * <ul>
	 * <li>If <code>objectName</code> is defined, the generated class simple name is the object name</li>
	 * <li>Otherwise, the generated class simple name is the concatenation of the exchange, exchange API, endpoint name and <code>Request</code> suffix</li>
	 * </ul>
	 * @param exchangeDescriptor      The exchange descriptor
	 * @param exchangeApiDescriptor   The exchange API descriptor
	 * @param restEndpointDescriptor  The REST endpoint descriptor
	 * @return The expected class name for a endpoint request, for instance <code>com.x.y.api.pojo.MyExchangeMyApiMyEndpointRequest</code>
	 * @throws IllegalArgumentException if request is not defined for the endpoint or is not of object type (see {@link Type#isObject()})
	 */
	public static String generateRestEnpointRequestPojoClassName(ExchangeDescriptor exchangeDescriptor, 
															 	 ExchangeApiDescriptor exchangeApiDescriptor, 
															 	 RestEndpointDescriptor restEndpointDescriptor) {
		Field request = restEndpointDescriptor.getRequest();
		if (request == null) {
			throw new IllegalArgumentException("No request for endpoint:" + restEndpointDescriptor);
		}
		return generateEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												request.getType(), 
												request.getObjectName(), 
												"Request");
	}

	/**
	 * Generates expected class name for a REST endpoint response POJO.
	 * <p>
	 * Works similarly to {@link #generateRestEnpointRequestPojoClassName(ExchangeDescriptor, ExchangeApiDescriptor, RestEndpointDescriptor)}, 
	 * but using <code>Response</code> as method name suffix.
	 * @param exchangeDescriptor      The exchange descriptor
	 * @param exchangeApiDescriptor   The exchange API descriptor
	 * @param restEndpointDescriptor  The REST endpoint descriptor
	 * @return The expected class name for a endpoint response, for instance <code>com.x.y.api.pojo.MyExchangeMyApiMyEndpointResponse</code>
	 * @throws IllegalArgumentException If response is not defined for the endpoint or is not of object type (see {@link Type#isObject()})
	 */
	public static String generateRestEnpointResponsePojoClassName(ExchangeDescriptor exchangeDescriptor, 
															  	  ExchangeApiDescriptor exchangeApiDescriptor, 
															      RestEndpointDescriptor restEndpointDescriptor) {
		Field response = restEndpointDescriptor.getResponse();
		if (response == null) {
			throw new IllegalArgumentException("No response for endpoint:" + restEndpointDescriptor);
		}
		return generateEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												response.getType(), 
												response.getObjectName(), 
												"Response");
	}
	
	/**
	 * Generates expected class name for a Websocket endpoint message POJO.
	 * <p>
	 * Works similarly to {@link #generateRestEnpointRequestPojoClassName(ExchangeDescriptor, ExchangeApiDescriptor, RestEndpointDescriptor)}, 
	 * but using <code>Message</code> as method name suffix.
	 * @param exchangeDescriptor      The exchange descriptor
	 * @param exchangeApiDescriptor   The exchange API descriptor
	 * @param websocketApi			  The websocket endpoint descriptor
	 * @return The expected class name for a endpoint message, for instance <code>com.x.y.api.pojo.MyExchangeMyApiMyEndpointMessage</code>
	 * @throws IllegalArgumentException If message is not defined for the endpoint or is not of object type (see {@link Type#isObject()})
	 */
	public static String generateWebsocketEndpointMessagePojoClassName(
							ExchangeDescriptor exchangeDescriptor,
							ExchangeApiDescriptor exchangeApiDescriptor, 
							WebsocketEndpointDescriptor websocketApi) {
		Field message = websocketApi.getMessage();
		if (message == null) {
			throw new IllegalArgumentException("No message for endpoint:" + websocketApi);
		}
		return generateEnpointPojoClassName(exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi.getName(), 
						message.getType(), 
						message.getObjectName(), 
						"Message");
	}
	
	/**
	 * Generates expected class name for a websocket endpoint request POJO.
	 * <p>
	 * Works similarly to {@link #generateRestEnpointRequestPojoClassName(ExchangeDescriptor, ExchangeApiDescriptor, RestEndpointDescriptor)}, 
	 * but using <code>Request</code> as method name suffix.
	 * @param exchangeDescriptor      The exchange descriptor
	 * @param exchangeApiDescriptor   The exchange API descriptor
	 * @param websocketApi			  The websocket endpoint descriptor
	 * @return The expected class name for a endpoint request, for instance <code>com.x.y.api.pojo.MyExchangeMyApiMyEndpointRequest</code>
	 * @throws IllegalArgumentException If request is not defined for the endpoint or is not of object type (see {@link Type#isObject()})
	 */
	public static String generateWebsocketEndpointRequestPojoClassName(
								ExchangeDescriptor exchangeDescriptor,
				   				ExchangeApiDescriptor exchangeApiDescriptor, 
				   				WebsocketEndpointDescriptor websocketApi) {
		Field request = websocketApi.getRequest();
		if (request == null) {
			throw new IllegalArgumentException("No request for endpoint:" + websocketApi);
		}
		return generateEnpointPojoClassName(
					exchangeDescriptor, 
					exchangeApiDescriptor, 
					websocketApi.getName(), 
					request.getType(), 
					request.getObjectName(), 
					"Request");
	}

	private static String generateEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, 
													   ExchangeApiDescriptor exchangeApiDescriptor, 
													   String endpointName, 
													   Type type,
													   String objectName,
													   String suffix) {
		if (type == null) {
			type = Type.fromTypeName(CanonicalType.OBJECT.name());
		}
		if (!type.isObject())
			throw new IllegalArgumentException("Not an object field type:" + type);
		if (objectName != null) {
			return exchangeDescriptor.getBasePackage() + "." 
					+ exchangeApiDescriptor.getName().toLowerCase() + ".pojo." 
					+ JavaCodeGenerationUtil.firstLetterToUpperCase(objectName);
		}
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(endpointName)
				+ suffix;
	}

	/**
	 * Generates expected name for a websocket endpoint subscription method
	 * @param websocketEndpointDescriptor The websocket endpoint descriptor
	 * @return expected name for a websocket endpoint subscription method, like <code>subscribeMyEndpoint</code>
	 */
	public static String getWebsocketSubscribeMethodName(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		return "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketEndpointDescriptor.getName());
	}

	/**
	 * Generates expected name for a websocket endpoint unsubscription method
	 * @param websocketEndpointDescriptor The websocket endpoint descriptor
	 * @return expected name for a websocket endpoint unsubscription method, like <code>unsubscribeMyEndpoint</code>
	 */
	public static String getWebsocketUnsubscribeMethodName(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		return "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketEndpointDescriptor.getName());
	}

	/**
	 * Generates expected class name for an {@link Field} instance
	 * according to its type (see
	 * {@link Field#getType()} .
	 * <ul>
	 * <li>For a primitive (see {@link CanonicalType#isPrimitive}
	 * parameter type, the corresponding primitive type class is returned:
	 * <code>java.lang.String</code>, <code>java.lang.Integer</code> ...)</li>
	 * <li>For an object (see {@link CanonicalType#OBJECT}
	 * parameter type), if the <code>objectName</code> (see
	 * {@link Field#getObjectName()} is defined, that object name is
	 * returned.</li>
	 * <li>For list or map (see {@link CanonicalType#LIST},
	 * {@link CanonicalType#MAP}), the corresponding generic class
	 * is returned e.g. <code>java.util.List</code> or <code>java.util.Map</code>,
	 * with generic parameter set with the class for subtype (see
	 * {@link Type#getSubType()} of <code>endpointParameter</code>
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
	public static String getClassNameForField(Field endpointParameter, 
											  Set<String> imports, 
											  String enclosingClassName) {
		String objectClassName = null;
		if (endpointParameter.getType().isObject()) {
			 objectClassName = getFieldObjectClassName(endpointParameter, enclosingClassName);
			 
		}
		return ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					endpointParameter.getType(), 
					imports, 
					objectClassName);
	}

	/**
	 * Generates the expected full class name for the object described by an
	 * {@link Field}, assuming it is of 'object' type (see
	 * {@link Type#isObject()}).<br/>
	 * The returned class package will be one of
	 * <code>enclosingClassName</code>.<br/>
	 * 
	 * @param endpointParameter  The endpoint parameter
	 * @param enclosingClassName The POJO class containing the endpoint to generate
	 *                           class name for type of.
	 * @return parameter object name with first letter to uppercase if
	 *         {@link Field#getObjectName()} is not <code>null</code>,
	 *         else concatenation of <code>&lt;enclosingClassName&gt;+&lt;enpointParameter name&gt;</code>
	 */
	public static String getFieldObjectClassName(Field endpointParameter, 
												 String enclosingClassName) {
		if (endpointParameter.getObjectName() != null) {
			 return JavaCodeGenerationUtil.getClassPackage(enclosingClassName) 
					 		+ "." 
					 		+ JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getObjectName());
		 } else {
			 return enclosingClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameter.getName());
		 }
	}

	/**
	 * Generates the expected full class name for the leaf subType of an endpoint.
	 * {@link Type#getLeafSubType(Type)}.
	 * 
	 * @param endpointParameterName See {@link Field#getName()} name
	 * @param type                  See {@link Field#getType()}
	 * @param objectName            See {@link Field#getObjectName()}
	 * @param enclosingClassName    The POJO class containing the endpoint to
	 *                              generate class name for type of.
	 * @return the full class name of leaf subType of endpoint parameter.
	 */
	public static String getFieldLeafSubTypeClassName(String endpointParameterName, 
													  Type type, 
													  String objectName,  
													  String enclosingClassName) {
		Type leafSubType = Type.getLeafSubType(type);
		if (leafSubType.getCanonicalType().isPrimitive) {
			return leafSubType.getCanonicalType().typeClass.getName();
		}
		String pkg = JavaCodeGenerationUtil.getClassPackage(enclosingClassName) + ".";
		if (objectName != null) {
			return pkg + objectName;
		}
		
		return pkg + ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				  leafSubType,
				  new HashSet<>(),
				  enclosingClassName) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(endpointParameterName);
	}

	public static String getNewMessageDeserializerInstruction(Type messageType, 
															  String messageFullClassName, 
															  Set<String> imports) {
		if (messageType == null) {
			messageType  = Type.STRING;
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
			return ExchangeJavaWrapperGeneratorUtil.getNewJsonParameterDeserializerInstruction(messageType, messageFullClassName, imports);
		}
	}

	public static boolean restEndpointHasResponse(RestEndpointDescriptor restApi, 
												  ExchangeApiDescriptor exchangeApiDescriptor) {
		Field response = restApi.getResponse();
		if (response == null) {
			return false;
		}
		Type dataType = response.getType();
		return (dataType != null && dataType.getCanonicalType() != CanonicalType.OBJECT) 
				|| getFieldPropertiesCount(response, exchangeApiDescriptor) > 0;
	}

	public static Type getFieldType(Field parameter) {
		return parameter == null? null: Optional.ofNullable(parameter.getType()).orElse(Type.OBJECT);
	}

	public static String getRestEndpointNameStaticVariable(String restEndpointName) {
		return JavaCodeGenerationUtil.getStaticVariableName(restEndpointName) + "_REST_API";
	}

	public static boolean restEndpointHasArguments(RestEndpointDescriptor restApi, 
												   ExchangeApiDescriptor exchangeApiDescriptor) {
		Field request = restApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request, exchangeApiDescriptor);
	}

	public static boolean websocketEndpointHasArguments(WebsocketEndpointDescriptor websocketApi, 
														ExchangeApiDescriptor exchangeApiDescriptor) {
		Field request = websocketApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request, exchangeApiDescriptor);
	}

	public static boolean endpointHasArguments(Field endpointRequest, 
											   ExchangeApiDescriptor exchangeApiDescriptor) {
		if (endpointRequest == null) {
			return false;
		}
		Type dataType = endpointRequest.getType();
		return (dataType != null && dataType.getCanonicalType() != CanonicalType.OBJECT) 
				|| getFieldPropertiesCount(endpointRequest, exchangeApiDescriptor) > 0;
	}

	public static String getRequestArgName(String requestArgNameFromApiDescriptor) {
		return Optional.ofNullable(requestArgNameFromApiDescriptor).orElse(DEFAULT_REQUEST_ARG_NAME);
	}

	public static String getWebsocketEndpointNameStaticVariable(String websocketEndpointName) {
		return JavaCodeGenerationUtil.getStaticVariableName(websocketEndpointName) + "_WS_API";
	}

	public static Field resolveFieldProperties(ExchangeApiDescriptor exchangeApiDescriptor, 
											   Field field) {
 		if (field == null) {
			return null;
		}
		Type type = field.getType();
		if (type == null) {
			if (field.getObjectName() != null) {
				type = Type.OBJECT;
			} else {
				return field;
			}
		}
		if (type.isObject() 
			&& field.getObjectName() != null 
			&& field.getParameters() == null) {
			field = field.clone();
			field.setType(type);
			field.setParameters(findPropertiesForObjectNameInApi(field.getObjectName(), exchangeApiDescriptor));
		}
		return field;
	}
	
	public static int getFieldPropertiesCount(Field field, 
											  ExchangeApiDescriptor exchangeApiDescriptor) {
		return Optional.ofNullable(field == null? null: resolveFieldProperties(exchangeApiDescriptor, field).getParameters())
		               .orElse(List.of()).size();
	}

	public static List<Field> findPropertiesForObjectNameInApi(String requestObjectName, 
															   ExchangeApiDescriptor exchangeApiDescriptor) {
		if (requestObjectName == null) {
			throw new IllegalArgumentException("null objectName");
		}
		if (exchangeApiDescriptor == null) {
			throw new IllegalArgumentException("null exchangeApiDescriptor");
		}
		List<Field> res = null;
		for (RestEndpointDescriptor restEndpointDescriptor: 
				Optional.ofNullable(exchangeApiDescriptor.getRestEndpoints()).orElse(List.of())) {
			res = findPropertiesForObjectNameInField(requestObjectName, restEndpointDescriptor.getRequest());
			if (res != null) {
				break;
			}
			res = findPropertiesForObjectNameInField(requestObjectName, restEndpointDescriptor.getResponse());
			if (res != null) {
				break;
			}
		}
		
		if (res == null) {
			for (WebsocketEndpointDescriptor websocketEndpointDescriptor: 
					Optional.ofNullable(exchangeApiDescriptor.getWebsocketEndpoints()).orElse(List.of())) {
				res = findPropertiesForObjectNameInField(requestObjectName, websocketEndpointDescriptor.getRequest());
				if (res != null) {
					break;
				}
				res = findPropertiesForObjectNameInField(requestObjectName, websocketEndpointDescriptor.getMessage());
				if (res != null) {
					break;
				}
			}
		}
		
		if (res != null) {
			return resolveAllFieldProperties(exchangeApiDescriptor, res);
		}
		throw new IllegalArgumentException("Found no REST request or response or Websocket request or message with fields defined for objectName:"  
										   + requestObjectName);
	}

	public static List<Field> findPropertiesForObjectNameInField(String objectName, Field param) {
		if (objectName == null) {
			return null;
		}
		if (param == null) {
			return null;
		}
		List<Field> res = param.getParameters();
		if (res == null) {
			return null;
		}
		if (Objects.equals(objectName, param.getObjectName())) {
			return res;
		}
		for (Field p: res) {
			res = findPropertiesForObjectNameInField(objectName, p);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	public static List<Field> resolveAllFieldProperties(ExchangeApiDescriptor exchangeApiDescriptor, 
														List<Field> fields) {
		return fields.stream().map(e -> resolveFieldProperties(exchangeApiDescriptor, e))
							  .collect(Collectors.toList());
	}

}
