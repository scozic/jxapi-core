package com.scz.jxapi.generator.java.exchange.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.ExchangeApi;
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
import com.scz.jxapi.netutils.deserialization.RawBigDecimalMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawBooleanMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawIntegerMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawLongMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import com.scz.jxapi.util.EncodingUtil;

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
	 * @param restEndpointDescriptor REST endpoint
	 * @return Expected name for generated exchange API group interface method for
	 *         calling given REST endpoint
	 */
	public static String getRestApiMethodName(RestEndpointDescriptor restEndpointDescriptor) {
		return JavaCodeGenerationUtil.firstLetterToLowerCase(restEndpointDescriptor.getName());
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
	 * field type, the corresponding primitive type class is returned:
	 * <code>java.lang.String</code>, <code>java.lang.Integer</code> ...)</li>
	 * <li>For an object (see {@link Type#OBJECT}
	 * field type), if the <code>objectName</code> (see
	 * {@link Field#getObjectName()} is defined, that object name is
	 * returned.</li>
	 * <li>For list or map (see {@link CanonicalType#LIST},
	 * {@link CanonicalType#MAP}), the corresponding generic class
	 * is returned e.g. <code>java.util.List</code> or <code>java.util.Map</code>,
	 * with generic parameter set with the class for subtype (see
	 * {@link Type#getSubType()} of <code>field</code>
	 * as returned by this method.</li>
	 * </ul>
	 * The returned value is the class simple name (without package prefix). The
	 * class full name is added to <code>imports</code> set passed in parameter. If
	 * type is a list or map, the generic parameter {@link List} or {@link Map} is
	 * also added to imports.
	 * <p>
	 * A few examples of returned values and imports added for some examples of a
	 * field named <code>Bar</code>:
	 * <table border="1">
	 * <caption>Examples of returned values and imports added</caption>
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
	 * <td>Primitive field type, and {@link String} is in <code>java.lang</code>
	 * package, no import added.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>BIGDECIMAL</code></td>
	 * <td><i>any</i></td>
	 * <td><i>any</i></td>
	 * <td><code>BigDecimal</code></td>
	 * <td><ul><li><code>java.lang.BigDecimal</code></li>
	 * </ul>
	 * </td>
	 * <td>Primitive field type, and {@link BigDecimal} is in
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
	 * <li><code>com.x.y.gen.pojo.FooBar</code></li>
	 * </ul>
	 * </td>
	 * <td><code>OBJECT</code> field type and no object name specified, the returned
	 * type is generated name by concatenating enclosing class name with field
	 * name</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>OBJECT</code></td>
	 * <td><code>com.x.y.gen.pojo.Foo</code></td>
	 * <td><code>MySpecialObjectName</code></td>
	 * <td><code>MySpecialObjectName</code></td>
	 * <td><ul><li><code>com.x.y.gen.pojo.MySpecialObjectName</code>
	 * </ul>
	 * </td>
	 * <td><code>OBJECT</code> field with <code>objectName</code> specified, the
	 * returned type is <code>objectName</code> as class name, assumed to be in same
	 * package as enclosing class name package.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td><code>OBJECT_MAP_LIST</code></td>
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
	 * <td><code>OBJECT</code> field nested in list of map with no
	 * <code>objectName</code> specified, the returned type is
	 * <code>List&lt;Map&lt;String, FooBar&gt;&gt;</code> and {@link Map},
	 * {@link List} and class for object field are added to imports.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @param field  The field
	 * @param imports            The imports of generator context that will be
	 *                           populated with classes used by returned type. That
	 *                           set must be not <code>null</code> and mutable.
	 * @param enclosingClassName The POJO class containing the endpoint to generate
	 *                           class name for type of.
	 * @return The simple class name for type of <code>field</code>
	 *         parameter.
	 */
	public static String getClassNameForField(Field field, 
											  Imports imports, 
											  String enclosingClassName) {
		String objectClassName = null;
		if (field.getType().isObject()) {
			 objectClassName = getFieldObjectClassName(field, enclosingClassName);
			 
		}
		return ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
					field.getType(), 
					imports, 
					objectClassName);
	}

	/**
	 * Generates the expected full class name for the object described by an
	 * {@link Field}, assuming it is of 'object' type (see
	 * {@link Type#isObject()}).<br>
	 * The returned class package will be one of
	 * <code>enclosingClassName</code>.<br>
	 * 
	 * @param field  The {@link Type#OBJECT} type field to get object class name for 
	 * @param enclosingClassName The POJO class containing the endpoint to generate
	 *                           class name for type of.
	 * @return field object name with first letter to uppercase if
	 *         {@link Field#getObjectName()} is not <code>null</code>,
	 *         else concatenation of <code>&lt;enclosingClassName&gt;+&lt;Field name&gt;</code>
	 */
	public static String getFieldObjectClassName(Field field, 
												 String enclosingClassName) {
		if (field.getObjectName() != null) {
			 return JavaCodeGenerationUtil.getClassPackage(enclosingClassName) 
					 		+ "." 
					 		+ JavaCodeGenerationUtil.firstLetterToUpperCase(field.getObjectName());
		 } else {
			 return enclosingClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
		 }
	}

	/**
	 * Generates the expected full class name for the leaf subType of an endpoint.
	 * <ul>
	 * <li>If the leaf subType canonical type is a primitive type (see
	 * {@link CanonicalType#isPrimitive}), the corresponding primitive
	 * type class, see {@link CanonicalType#typeClass } is returned.</li>
	 * <li>If the supplied <code>objectName</code> is not <code>null</code>, the
	 * class name is the object name with first letter to
	 * uppercase, and its package is the package of the enclosing class name.</li>
	 * <li>Otherwise, the class name is the concatenation of the package of the
	 * enclosing class name and the class name generated
	 * by
	 * {@link ExchangeJavaWrapperGeneratorUtil#getClassNameForType(Type, Imports, String)}
	 * for the
	 * leaf subType of the field type.</li>
	 * </ul>
	 * {@link Type#getLeafSubType(Type)}.
	 * 
	 * @param fieldName 			See {@link Field#getName()}
	 * @param type                  See {@link Field#getType()}
	 * @param objectName            See {@link Field#getObjectName()}
	 * @param enclosingClassName    The POJO class containing the endpoint to
	 *                              generate class name for type of.
	 * @return the full class name of leaf subType of field type.
	 */
	public static String getFieldLeafSubTypeClassName(String fieldName, 
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
		
		return pkg + ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
				  leafSubType,
				  new Imports(),
				  enclosingClassName) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(fieldName);
	}

	/**
	 * Generates the part of instruction to get a new instance of a message
	 * deserializer for a given message type.
	 * <ul>
	 * <li>For primitive types, the corresponding deserializer singleton instance is
	 * returned,
	 * for instance if type is {@link Type#STRING},
	 * {@link RawStringMessageDeserializer#getInstance()} singleton is returned.
	 * The deserializer class is added to imports</li>
	 * <li>For other 'structured' types (object, list or map) types, the
	 * corresponding new
	 * Json field deserializer instruction is returned, see
	 * {@link ExchangeJavaWrapperGeneratorUtil#getNewJsonFieldDeserializerInstruction(Type, String, Imports)}.</li>
	 * <li>
	 * </ul>
	 * 
	 * @param messageType The message type
	 * @param messageFullClassName The full class name of the message
	 * @param imports The imports of generator context that will be populated with
	 * @return Java code instruction to get a new instance of a message deserializer
	 */
	public static String getNewMessageDeserializerInstruction(Type messageType, 
															  String messageFullClassName, 
															  Imports imports) {
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
			return ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(messageType, messageFullClassName, imports);
		}
	}

	/**
	 * Checks if given REST endpoint has a request defined, and that request has
	 * arguments.
	 * 
	 * @param restApi               The REST endpoint descriptor
	 * @param exchangeApiDescriptor The exchange API descriptor, used to get the
	 *                              properties of the response field if it is an
	 *                              object type with actual properties defined in
	 *                              another API.
	 * @return <code>true</code> if the REST endpoint has a response defined, and
	 *         that
	 *         response is either a primitive type or an object type has at least
	 *         one property defined.
	 */
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

	/**
	 * 
	 * @param field The field to get the type of, see {@link Field#getType()}
	 * @return <code>null</code> if field is <code>null</code>, otherwise the
	 *         type of the field or default {@link Type#OBJECT} if this type is
	 *         null.
	 */
	public static Type getFieldType(Field field) {
		return field == null? null: Optional.ofNullable(field.getType()).orElse(Type.OBJECT);
	}

	/**
	 * Generates the name of expected static {@link String} variable holding a REST endpoint name.
	 * <br>
	 * Example: for <code>myRestEndpoint</code>, the returned value is <code>MY_REST_ENDPOINT_REST_API</code>
	 * @param restEndpointName The REST endpoint name
	 * @return the static variable name for the REST endpoint name.
	 */
	public static String getRestEndpointNameStaticVariable(String restEndpointName) {
		return JavaCodeGenerationUtil.getStaticVariableName(restEndpointName) + "_REST_API";
	}

	/**
	 * @param restApi The REST endpoint descriptor
	 * @param exchangeApiDescriptor The exchange API descriptor
	 * @return <code>true</code> if the REST endpoint has a request defined 
	 * 			{@link RestEndpointDescriptor#getRequest()}, and that request field has arguments, 
	 * 			see {@link #endpointHasArguments(Field, ExchangeApiDescriptor)}
	 */
	public static boolean restEndpointHasArguments(RestEndpointDescriptor restApi, 
												   ExchangeApiDescriptor exchangeApiDescriptor) {
		Field request = restApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request, exchangeApiDescriptor);
	}

	/**
	 * @param websocketApi The websocket endpoint descriptor
	 * @param exchangeApiDescriptor The exchange API descriptor
	 * @return <code>true</code> if the websocket endpoint has a request field defined 
	 * 			see {@link WebsocketEndpointDescriptor#getRequest()}, and that message field has arguments,
	 * 			see {@link #endpointHasArguments(Field, ExchangeApiDescriptor)}
	 */
	public static boolean websocketEndpointHasArguments(WebsocketEndpointDescriptor websocketApi, 
														ExchangeApiDescriptor exchangeApiDescriptor) {
		Field request = websocketApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request, exchangeApiDescriptor);
	}

	/**
	 * @param endpointRequest The endpoint request field
	 * @param exchangeApiDescriptor  The exchange API descriptor of the endpoint
	 * @return <code>true</code> if the endpoint request is not <code>null</code> and its type has arguments, 
	 * 			see {@link #endpointHasArguments(Field, ExchangeApiDescriptor)}
	 */
	public static boolean endpointHasArguments(Field endpointRequest, 
											   ExchangeApiDescriptor exchangeApiDescriptor) {
		if (endpointRequest == null) {
			return false;
		}
		Type dataType = endpointRequest.getType();
		return (dataType != null && dataType.getCanonicalType() != CanonicalType.OBJECT) 
				|| getFieldPropertiesCount(endpointRequest, exchangeApiDescriptor) > 0;
	}

	/**
	 * The name property of Field of a REST request/Websocket subscribe request is
	 * used to generate the name of the argument in the generated method signature.
	 * <br>
	 * If the name property is not defined, the default name
	 * {@link #DEFAULT_REQUEST_ARG_NAME} is used.
	 * Notice this function argument is used in API interface method declaration,
	 * but not in the generated method implementation.
	 * This is because the generated method implementation uses a fixed name for the
	 * request argument, see {@link #DEFAULT_REQUEST_ARG_NAME}
	 * to make sure there is no collising between this name and the other variables
	 * used in the generated method implementation or interface implementation
	 * class.
	 * 
	 * @param requestArgNameFromApiDescriptor The name property of the request field of REST request or Websocket subscribe request.
	 * @return <code>requestArgNameFromApiDescriptor</code> if not
	 *         <code>null</code>, otherwise {@link #DEFAULT_REQUEST_ARG_NAME}
	 */
	public static String getRequestArgName(String requestArgNameFromApiDescriptor) {
		return Optional.ofNullable(requestArgNameFromApiDescriptor).orElse(DEFAULT_REQUEST_ARG_NAME);
	}

	/**
	 * Generates the name of expected static {@link String} variable holding a Websocket endpoint name.
	 * @param websocketEndpointName The Websocket endpoint name
	 * @return the static variable name for the Websocket endpoint name.
	 */
	public static String getWebsocketEndpointNameStaticVariable(String websocketEndpointName) {
		return JavaCodeGenerationUtil.getStaticVariableName(websocketEndpointName) + "_WS_API";
	}

	/**
	 * A given {@link Field} may have an object name defined, see
	 * {@link Field#getObjectName()} and properties of this objectName defined in
	 * another API of the enclosing ExchangeApi, or it could be the case for some
	 * sub-properties defined, see {@link Field#getProperties()}.
	 * <br>
	 * This method performs a recursive resolution of properties for the given field
	 * among all 'object' properties of every REST request or response or Websockey
	 * subscribe request and message.
	 * <br>
	 * If provided {@link Field} has a defined objectName, is of 'object' type (see
	 * {@link Type#isObject()}) and has <code>null</code> sub properties then its
	 * sub properties are set with ones of first field found in any object field of
	 * any
	 * REST/Websocket API that has same object name as this field.
	 * <br>
	 * Same is done for all sub-properties of this field recursively.
	 * 
	 * @param exchangeApiDescriptor The exchange API descriptor containing the REST
	 *                              and Websocket endpoints to search for properties
	 *                              of the object name.
	 * @param field                 the field to resolve properties for.
	 * @return The field with resolved properties. Will be a clone of the input
	 *         field if some properties were resolved, or same field if no
	 *         properties were resolved.
	 * @throws IllegalArgumentException if no properties were found for the object
	 *                                  name of the field in the enclosing API.
	 */
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
			&& field.getProperties() == null) {
			field = field.clone();
			field.setType(type);
			field.setProperties(findPropertiesForObjectNameInApi(field.getObjectName(), exchangeApiDescriptor));
		}
		return field;
	}
	
	/**
	 * Counts the number of properties of a field, eventually resolving these
	 * properties in enclosing API (see
	 * {@link #resolveFieldProperties(ExchangeApiDescriptor, Field)}).
	 * 
	 * @param field                 The field to count properties of
	 * @param exchangeApiDescriptor The enclosing API descriptor to resolve
	 *                              properties in
	 * @return 0 if field is <code>null</code>, or the number of properties of the
	 *         field, eventually resolved in enclosing API.
	 */
	public static int getFieldPropertiesCount(Field field, 
											  ExchangeApiDescriptor exchangeApiDescriptor) {
		return Optional.ofNullable(field == null? null: resolveFieldProperties(exchangeApiDescriptor, field).getProperties())
		               .orElse(List.of()).size();
	}

	/**
	 * Finds the properties of an object name in an API descriptor: return
	 * properties (see {@link Field#getProperties()})
	 * of first field found in any object field of any REST/Websocket API that has
	 * same object name as this field.
	 * 
	 * @param requestObjectName     The object name to find properties for.
	 * @param exchangeApiDescriptor The API descriptor to search for properties of
	 *                              the object name.
	 * @return The list of properties of the object name in the API descriptor.
	 * @throws IllegalArgumentException if no properties were found for the object
	 *                                  name in the enclosing API, or if either
	 *                                  <code>requestObjectName</code> or
	 *                                  <code>exchangeApiDescriptor</code> is
	 *                                  <code>null</code>.
	 */
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

	/**
	 * Finds the properties of an object name in a field: return properties (see
	 * {@link Field#getProperties()}) found in provided field if it has the same
	 * object name as the one provided, or properties of first field found in its
	 * sub-properties that carries expected objectName recursively.
	 * 
	 * @param objectName The object name to find properties for.
	 * @param param      The field to search for properties of the object name.
	 * @return The list of properties of the object name in the field.
	 */
	public static List<Field> findPropertiesForObjectNameInField(String objectName, Field param) {
		if (objectName == null) {
			return null;
		}
		if (param == null) {
			return null;
		}
		List<Field> res = param.getProperties();
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

	/**
	 * Resolves properties of all fields in a list of fields, see
	 * {@link #resolveFieldProperties(ExchangeApiDescriptor, Field)}.
	 * 
	 * @param exchangeApiDescriptor The enclosing API descriptor to resolve
	 *                              properties in
	 * @param fields                The list of fields to resolve properties for
	 * @return The list of fields with resolved properties
	 */
	public static List<Field> resolveAllFieldProperties(ExchangeApiDescriptor exchangeApiDescriptor, 
														List<Field> fields) {
		return fields.stream().map(f -> resolveFieldProperties(exchangeApiDescriptor, f))
							  .collect(Collectors.toList());
	}

	/**
	 * Generates expected public static variable name in a generated {@link ExchangeApi} for HTTP URL of a REST endpoint.
	 * @param restEndpointDescriptor The REST endpoint descriptor
	 * @return The expected static variable name for the REST endpoint URL, for instance <code>MY_REST_ENDPOINT_URL</code>
	 */
	public static String getRestEndpointUrlStaticVariableName(RestEndpointDescriptor restEndpointDescriptor) {
		return JavaCodeGenerationUtil.getStaticVariableName(restEndpointDescriptor.getName()) + "_URL";
	}
	
		/**
	 * Generates expected public static variable name in a generated {@link ExchangeApi} for websocket HTTP URL.
	 * @param exchangeApiDescriptor The ExchangeApi descriptor
	 * @return The expected static variable name for the websocket URL, for instance <code>MY_API_WS_URL</code>
	 */
	public static String getWebsocketUrlStaticVariableName(ExchangeApiDescriptor exchangeApiDescriptor) {
		return JavaCodeGenerationUtil.getStaticVariableName(exchangeApiDescriptor.getName()) + "_WS_URL";
	}
	
	/**
	 * Generates expected public static variable declaration in a generated {@link ExchangeApi} for REST base URL.
	 * It will be a concatenation of the exchange base URL and the API base URL if the API base URL is not an absolute URL.
	 * @param exchangeDescriptor The exchange descriptor
	 * @param exchangeApiDescriptor The exchange API descriptor
	 * @param imports The imports of the generated class
	 * @return public static declaration for a variable named {@link ExchangeJavaWrapperGeneratorUtil#HTTP_URL_STATIC_VARIABLE} holding the base URL of the REST API.
	 */
	public static String getHttpUrlVariableDeclaration(ExchangeDescriptor exchangeDescriptor, 
													   ExchangeApiDescriptor exchangeApiDescriptor,
													   Imports imports) {
		StringBuilder s = new StringBuilder()
				.append("public static final String ")
				.append(ExchangeJavaWrapperGeneratorUtil.HTTP_URL_STATIC_VARIABLE)
				.append(" = ");
		String url = "";
		String apiUrl = exchangeApiDescriptor.getHttpUrl();
		if (apiUrl != null) {
			url = JavaCodeGenerationUtil.getQuotedString(apiUrl);
			if (EncodingUtil.isAbsoluteUrl(apiUrl)) {
				return s.append(url).append(";").toString();
			}
		}
		
		String exchangeUrl = exchangeDescriptor.getHttpUrl();
		if (exchangeUrl != null) {
			String exchangeInterfaceImplementationName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceImplementationName(exchangeDescriptor);
			imports.add(exchangeInterfaceImplementationName);
			String exchangeUrlVar = JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeInterfaceImplementationName) 
								 + "."  
								 + ExchangeJavaWrapperGeneratorUtil.HTTP_URL_STATIC_VARIABLE;
			if (!url.isEmpty()) {
				exchangeUrlVar += " + " + url;
			}
			url = exchangeUrlVar;
		}
		if (url.isEmpty()) {
			return null;
		}
		
		return s.append(url).append(";").toString();
	}
	
	/**
	 * Generates expected public static variable declaration in a generated
	 * {@link ExchangeApi} for REST endpoint URL.
	 * It will be a concatenation of the base URL and the endpoint URL if the
	 * endpoint URL is not an absolute URL.
	 * 
	 * @param hasBaseUrl             <code>true</code> if the API has a base URL,
	 *                               <code>false</code> otherwise
	 * @param restEndpointDescriptor The REST endpoint descriptor
	 * @return public static declaration for a variable named
	 *         <code>MY_REST_ENDPOINT_URL</code> holding the URL of the REST
	 *         endpoint.
	 */
	public static String getRestEndpointUrlVariableDeclaration(boolean hasBaseUrl, 
															   RestEndpointDescriptor restEndpointDescriptor) {
		StringBuilder s = new StringBuilder()
							.append("public static final String ")
							.append(getRestEndpointUrlStaticVariableName(restEndpointDescriptor))
							.append(" = ");
		String restUrl = restEndpointDescriptor.getUrl();
		String url = "";
		if (restUrl != null) {
			url =  JavaCodeGenerationUtil.getQuotedString(restUrl);
			if (EncodingUtil.isAbsoluteUrl(restUrl)) {
				return s.append(url).append(";").toString();
			}
		}
		
		if (hasBaseUrl) {
			s.append(ExchangeJavaWrapperGeneratorUtil.HTTP_URL_STATIC_VARIABLE);
			if (!url.isEmpty()) {
				s.append(" + ").append(url);
			}
			return s.append(";").toString();
		}
		
		throw new IllegalArgumentException("Invalid URL '" 
											+ url 
											+ "' for enpoint:" 
											+ restEndpointDescriptor);
	}
	
	/**
	 * Generates expected public static variable declaration in a generated
	 * {@link ExchangeApi} for websocket URL.
	 * It will be a concatenation of the exchange websocket URL and the API
	 * websocket URL if the API websocket URL is not an absolute URL.
	 * 
	 * @param exchangeDescriptor    The exchange descriptor
	 * @param exchangeApiDescriptor The exchange API descriptor
	 * @param imports               The imports of the generated class
	 * @return public static declaration for a variable named
	 *         {@link ExchangeJavaWrapperGeneratorUtil#WEBSOCKET_URL_STATIC_VARIABLE}
	 *         holding the base URL of the websocket API.
	 */
	public static String getWebsocketUrlVariableDeclaration(ExchangeDescriptor exchangeDescriptor, 
															ExchangeApiDescriptor exchangeApiDescriptor,
															Imports imports) {
		StringBuilder s = new StringBuilder()
							.append("public static final String ")
							.append(ExchangeJavaWrapperGeneratorUtil.WEBSOCKET_URL_STATIC_VARIABLE)
							.append(" = ");
		String url = "";
		String apiUrl = exchangeApiDescriptor.getWebsocketUrl();
		if (apiUrl != null) {
			String apiUrlQuoted = JavaCodeGenerationUtil.getQuotedString(apiUrl);
			url = apiUrlQuoted;
			if (EncodingUtil.isAbsoluteUrl(apiUrl)) {
				return s.append(url).append(";").toString();
			}
		}
		
		String exchangeUrl = exchangeDescriptor.getWebsocketUrl();
		if (exchangeUrl != null) {
			String exchangeInterfaceName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceImplementationName(exchangeDescriptor);
			imports.add(exchangeInterfaceName);
			String exchangeUrlVar = JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeInterfaceName) 
									 + "."  
									 + ExchangeJavaWrapperGeneratorUtil.WEBSOCKET_URL_STATIC_VARIABLE;
			if (!url.isEmpty()) {
				exchangeUrlVar += " + " + url;
			}
			url = exchangeUrlVar;
		}
		if (url.isEmpty()) {
 			return null;
		}
		
		return s.append(url).append(";").toString();
	}
	
}
