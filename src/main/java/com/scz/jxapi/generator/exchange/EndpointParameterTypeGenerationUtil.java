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
			imports.add(objectClassName);
			return JavaCodeGenerationUtil.getClassNameWithoutPackage(objectClassName);
			//return objectClassName;
		default:
			throw new IllegalArgumentException("Unexpected type for:" + type);
		}
	}
	
	public static String getLeafObjectParameterClassName(String endpointParameterName, EndpointParameterType type, String endpointParameterObjectName, Set<String> imports, String enclosingPojoClassName) {
		String pkg = "";
		if (type.isObject()) {
			pkg = JavaCodeGenerationUtil.getClassPackage(enclosingPojoClassName) + ".";
			if (endpointParameterObjectName != null) {
				return pkg  + endpointParameterObjectName;
			}
		}
		
		return pkg + getClassNameForParameterType(
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
