package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.PojoField;
import com.scz.jxapi.generator.PojoGenerator;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Helper methods for generation of Java classes of a given exchange wrapper
 */
public class ExchangeJavaWrapperGeneratorUtil {
	
	private static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	
	public static String getSerializerClassName(String pojoClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(pojoClassName), ".pojo");
		return pkg + ".serializers." + JavaCodeGenerationUtil.getClassNameWithoutPackage(pojoClassName) + "Serializer";
	}
	
	public static String generateRestEnpointRequestClassName(ExchangeDescriptor exchangeDescriptor, 
															 ExchangeApiDescriptor exchangeApiDescriptor, 
															 RestEndpointDescriptor restEndpointDescriptor) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor, "Request");
	}
	
	public static String generateRestEnpointResponseClassName(ExchangeDescriptor exchangeDescriptor, 
															  ExchangeApiDescriptor exchangeApiDescriptor, 
															  RestEndpointDescriptor restEndpointDescriptor) {
		if (ResponseDataType.STRING.equals(restEndpointDescriptor.getResponseDataType())) {
			return String.class.getName();
		}
		String responseObjectName = restEndpointDescriptor.getResponseObjectName();
		if (responseObjectName != null) {
			return exchangeDescriptor.getBasePackage() + "." 
					+ exchangeApiDescriptor.getName().toLowerCase() + ".pojo." 
					+ JavaCodeGenerationUtil.firstLetterToUpperCase(responseObjectName);
		}
		return generateRestEnpointPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor, "Response");
	}
	
	private static String generateRestEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, 
														   ExchangeApiDescriptor exchangeApiDescriptor, 
														   RestEndpointDescriptor restEndpointDescriptor, 
														   String suffix) {
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
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
		if (ResponseDataType.STRING.equals(websocketApi.getResponseDataType())) {
			return String.class.getName();
		}
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
				+ "Message";
	}

	public static String generateWebsocketEndpointRequestClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
				+ "Request";
	}
	
	public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, 
												  ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
										+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Api";
		return pkgPrefix + simpleInterfaceName;
	}
	
	public static String generateGetUrlParametersBodyFromTemplate(String urlParametersTemplate, 
																  List<EndpointParameter> endpointParameters, 
																  String stringListSeparator) {
		if (endpointParameters.isEmpty()) {
			return "return \"" + urlParametersTemplate + "\";\n";
		}
		if (stringListSeparator == null) {
			stringListSeparator = DEFAULT_STRING_LIST_SEPARATOR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("return ")
		  .append(EncodingUtil.class.getSimpleName())
		  .append(".substituteArguments(\"")
		  .append(urlParametersTemplate)
		  .append("\", ");
		int n = endpointParameters.size();
		for (int i = 0; i < n; i++) {
			String name = endpointParameters.get(i).getName();
			String value = name;
			if (endpointParameters.get(i).getEndpointParameterType().getCanonicalType() == CanonicalEndpointParameterTypes.LIST) {
				value = EncodingUtil.class.getSimpleName() + ".listToString(" + name + ", \"" + stringListSeparator + "\")"; 
			}
			sb.append("\"").append(name).append("\", ").append(value);
			if (i < n - 1) {
				sb.append(", ");
			}
		}
		sb.append(");\n");
		return sb.toString();
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
			imports.add(objectClassName);
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

}
