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

	public static String generateRestEnpointRequestClassName(ExchangeDescriptor exchangeDescriptor, 
															 ExchangeApiDescriptor exchangeApiDescriptor, 
															 RestEndpointDescriptor restEndpointDescriptor) {
		Field request = restEndpointDescriptor.getRequest();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												request.getType(), 
												request.getObjectName(), 
												"Request");
	}

	public static String generateRestEnpointResponseClassName(ExchangeDescriptor exchangeDescriptor, 
															  ExchangeApiDescriptor exchangeApiDescriptor, 
															  RestEndpointDescriptor restEndpointDescriptor) {
		Field response = restEndpointDescriptor.getResponse();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												restEndpointDescriptor.getName(), 
												response.getType(), 
												response.getObjectName(), 
												"Response");
	}

	private static String generateRestEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, 
														   ExchangeApiDescriptor exchangeApiDescriptor, 
														   String endpointName, 
														   Type type,
														   String objectName,
														   String suffix) {
		if (type == null) {
			type = Type.fromTypeName(CanonicalType.OBJECT.name());
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

	public static String generateWebsocketEndpointMessageClassName(ExchangeDescriptor exchangeDescriptor,
																   ExchangeApiDescriptor exchangeApiDescriptor, 
																   WebsocketEndpointDescriptor websocketApi) {
		Field message = websocketApi.getMessage();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												websocketApi.getName(), 
												message.getType(), 
												message.getObjectName(), 
												"Message");
	}

	public static String generateWebsocketEndpointRequestClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		Field request = websocketApi.getRequest();
		return generateRestEnpointPojoClassName(exchangeDescriptor, 
												exchangeApiDescriptor, 
												websocketApi.getName(), 
												request.getType(), 
												request.getObjectName(), 
												"Request");
	}

	public static String getWebsocketSubscribeMethodName(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		return "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketEndpointDescriptor.getName());
	}

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
	 * Generates full class name of an {@link Field} instance
	 * <code>leaf</code> type, see
	 * {@link Type#getLeafSubType(Type)}.
	 * 
	 * @param endpointParameterName See {@link Field#getName()} name
	 * @param type                  See {@link Field#getType()}
	 * @param objectName            See {@link Field#getObjectName()}
	 * @param enclosingClassName    The POJO class containing the endpoint to
	 *                              generate class name for type of.
	 * @return the full class name of leaf subType of endpoint parameter.
	 */
	public static String getLeafObjectFieldClassName(String endpointParameterName, 
													 Type type, 
													 String objectName,  
													 String enclosingClassName) {
		String pkg = "";
		if (type.isObject()) {
			pkg = JavaCodeGenerationUtil.getClassPackage(enclosingClassName) + ".";
			if (objectName != null) {
				return pkg + objectName;
			}
		}
		
		return pkg + ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				  Type.getLeafSubType(type),
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

	public static boolean restEndpointHasResponse(RestEndpointDescriptor restApi, ExchangeApiDescriptor exchangeApiDescriptor) {
		Field response = restApi.getResponse();
		if (response == null) {
			return false;
		}
		Type dataType = response.getType();
		return (dataType != null && dataType.getCanonicalType() != CanonicalType.OBJECT) 
				|| getEndpointParameters(response.getParameters(), response.getObjectName(), exchangeApiDescriptor).size() > 0;
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
		return endpointHasArguments(request.getType(),
									request.getParameters(), 
									request.getObjectName(), 
									exchangeApiDescriptor);
	}

	public static boolean websocketEndpointHasArguments(WebsocketEndpointDescriptor websocketApi, 
														ExchangeApiDescriptor exchangeApiDescriptor) {
		Field request = websocketApi.getRequest();
		if (request == null) {
			return false;
		}
		return endpointHasArguments(request.getType(), 
									request.getParameters(), 
									request.getObjectName(), 
									exchangeApiDescriptor);
	}

	public static boolean endpointHasArguments(Type dataType, 
												List<Field> parameters, 
												String requestObjectName, 
												ExchangeApiDescriptor exchangeApiDescriptor) {
		return (dataType != null && dataType.getCanonicalType() != CanonicalType.OBJECT) 
				|| getEndpointParameters(parameters, requestObjectName, exchangeApiDescriptor).size() > 0;
	}

	public static String getRequestArgName(String requestArgNameFromApiDescriptor) {
		return Optional.ofNullable(requestArgNameFromApiDescriptor).orElse(ExchangeJavaWrapperGeneratorUtil.DEFAULT_REQUEST_ARG_NAME);
	}

	public static String getWebsocketEndpointNameStaticVariable(String websocketEndpointName) {
		return JavaCodeGenerationUtil.getStaticVariableName(websocketEndpointName) + "_WS_API";
	}

	public static Field resolveEndpointFields(ExchangeApiDescriptor exchangeApiDescriptor, 
															  Field field) {
		if (field == null) {
			return null;
		}
		if (field.getType() == null) {
			return field; 
		}
		if (field.getType() != null 
			&& field.getType().isObject() 
			&& field.getObjectName() != null 
			&& field.getParameters() == null) {
			field = field.clone();
			field.setParameters(findParametersForObjectName(field.getObjectName(), exchangeApiDescriptor));
		}
		return field;
	}

	public static List<Field> getEndpointParameters(List<Field> endpointDescriptiorParameters, 
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

	public static List<Field> findParametersForObjectName(String requestObjectName, ExchangeApiDescriptor exchangeApiDescriptor) {
		List<Field> res = null;
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

	public static List<Field> findParametersForObjectName(String requestObjetName, Field param) {
		if (param == null) {
			return null;
		}
		List<Field> res = param.getParameters();
		if (res == null) {
			return null;
		}
		if (Objects.equals(requestObjetName, param.getObjectName())) {
			return res;
		}
		for (Field p: res) {
			res = findParametersForObjectName(requestObjetName, p);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	public static List<Field> resolveEndpointParameters(ExchangeApiDescriptor exchangeApiDescriptor, 
														List<Field> fields) {
		return fields.stream().map(e -> resolveEndpointFields(exchangeApiDescriptor, e))
							  .collect(Collectors.toList());
	}

}
