package com.scz.jxapi.generator.java.exchange.api.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.EndpointParameter;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;

/**
 * Unit test for {@link EndpointDemoGeneratorUtil}
 */
public class EndpointDemoGeneratorUtilTest {

	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameter() {
		EndpointParameter param = EndpointParameter.create("STRING", "myStringParam", null, null, "Hello World");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static String createMyStringParam() {\n"
				+ "  return \"Hello World\";\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameterNullSampleValue() {
		EndpointParameter param = EndpointParameter.create("STRING", "myStringParam", null, null, null);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static String createMyStringParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerParameter() {
		EndpointParameter param = EndpointParameter.create("INT", "myIntParam", null, null, List.of());
		param.setSampleValue(123);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Integer createMyIntParam() {\n"
			  + "  return Integer.valueOf(123);\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveLongParameter() {
		EndpointParameter param = EndpointParameter.create("LONG", "myLongParam", null, null, List.of());
		param.setSampleValue(123L);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Long createMyLongParam() {\n"
			  + "  return Long.valueOf(123);\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveTimestampParameterSpecialValueNow() {
		EndpointParameter param = EndpointParameter.create("TIMESTAMP", "myLongParam", null, null, List.of());
		param.setSampleValue("now()");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Long createMyLongParam() {\n"
			  + "  return Long.valueOf(System.currentTimeMillis());\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalParameter() {
		EndpointParameter param = EndpointParameter.create("BIGDECIMAL", "myBigDecimalParam", null, null, List.of());
		param.setSampleValue("123.45");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static BigDecimal createMyBigDecimalParam() {\n"
			  + "  return new BigDecimal(\"123.45\");\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveBooleanParameter() {
		EndpointParameter param = EndpointParameter.create("BOOLEAN", "myBooleanParam", null, null, List.of());
		param.setSampleValue(true);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Boolean createMyBooleanParam() {\n"
				+ "  return Boolean.valueOf(true);\n"
				+ "}\n", method);
	}

	@Test
	public void testGenerateEndpointParameterCreationMethodSimpleObjectParameter() {
		EndpointParameter param = EndpointParameter.createObject(null, "myObjParam", null, null, List.of(
					EndpointParameter.create("INT", "foo", null, null, 123),
					EndpointParameter.create("STRING", "hello", null, null, "Hello World"),
					EndpointParameter.create("LONG", "aLong", null, null, 9876543210L),
					EndpointParameter.create("BIGDECIMAL", "bDecimal", null, null, "123.45"),
					EndpointParameter.create("BOOLEAN", "cBool", null, null, true),
					EndpointParameter.create("STRING", "theVoidStr", null, null, null),
					EndpointParameter.create("STRING_LIST", "theVoidList", null, null, null),
					EndpointParameter.create("STRING_MAP", "theVoidMap", null, null, null)
				));
		param.setSampleValue(123);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static MyRequest createMyObjParam() {\n"
				+ "  MyRequest request = new MyRequest();\n"
				+ "  request.setFoo(Integer.valueOf(123));\n"
				+ "  request.setHello(\"Hello World\");\n"
				+ "  request.setALong(Long.valueOf(9876543210));\n"
				+ "  request.setBDecimal(new BigDecimal(\"123.45\"));\n"
				+ "  request.setCBool(Boolean.valueOf(true));\n"
				+ "  return request;\n"
				+ "}\n", method);
		checkImports(imports, BigDecimal.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodComplexObjectParameter() {
		EndpointParameter param = EndpointParameter.createObject(null, "myObjParam", null, null, List.of(
					EndpointParameter.create("INT", "foo", null, null, 123),
					EndpointParameter.create("STRING", "hello", null, null, "Hello World"),
					EndpointParameter.createObject("OBJECT_LIST", "bar", null, null, List.of(
							EndpointParameter.create("STRING", "id", null, null, "id#0"),
							EndpointParameter.create("BOOLEAN", "enabled", null, null, true),
							EndpointParameter.create("TIMESTAMP", "time", null, null, null),
							EndpointParameter.create("BIGDECIMAL_LIST_MAP", "bestBids", null, null, "{\"BTC_USDT\": [\"69268.61\", \"69268.62\"], \"ETH_USDT\":[\"3427.98\", \"3427.90\"]}")
					))
				));
		param.setSampleValue(123);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static MyRequest createMyObjParam() {\n"
				+ "  MyRequest request = new MyRequest();\n"
				+ "  request.setFoo(Integer.valueOf(123));\n"
				+ "  request.setHello(\"Hello World\");\n"
				+ "  MyRequestBar request_barItem = new MyRequestBar();\n"
				+ "  request_barItem.setId(\"id#0\");\n"
				+ "  request_barItem.setEnabled(Boolean.valueOf(true));\n"
				+ "  request_barItem.setBestBids(new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())).deserialize(\"{\\\"BTC_USDT\\\": [\\\"69268.61\\\", \\\"69268.62\\\"], \\\"ETH_USDT\\\":[\\\"3427.98\\\", \\\"3427.90\\\"]}\"));\n"
				+ "  request.setBar(List.of(request_barItem));\n"
				+ "  return request;\n"
				+ "}\n", method);
		
		//Map<String, List<BigDecimal>> map = new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())).deserialize("{\"BTC_USDT\": [\"69268.61\", \"69268.62\"], \"ETH_USDT\":[\"3427.98\", \"3427.90\"]}");
		checkImports(imports, 
				 List.class,
				 ListJsonFieldDeserializer.class, 
				 MapJsonFieldDeserializer.class,
				 BigDecimalJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerList() {
		EndpointParameter param = EndpointParameter.create("INT_LIST", "myIntListParam", null, null, List.of());
		param.setSampleValue(List.of(1,3,5,7));
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<Integer> createMyIntListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(\"[1, 3, 5, 7]\");\n"
				+ "}\n", method);
		checkImports(imports, 
					 List.class, 
					 ListJsonFieldDeserializer.class, 
					 IntegerJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringList() {
		EndpointParameter param = EndpointParameter.create("STRING_LIST", "myStringListParam", null, null, List.of());
		param.setSampleValue("[\"BTC\",\"ETH\"]");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<String> createMyStringListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"BTC\\\",\\\"ETH\\\"]\");\n"
				+ "}\n", method);
		checkImports(imports, 
					 List.class, 
					 ListJsonFieldDeserializer.class, 
					 StringJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveLongList() {
		EndpointParameter param = EndpointParameter.create("LONG_LIST", "myLongListParam", null, null, List.of());
		param.setSampleValue("[1234567890,9876543210]");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<Long> createMyLongListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(LongJsonFieldDeserializer.getInstance()).deserialize(\"[1234567890,9876543210]\");\n"
				+ "}\n", method);
		checkImports(imports, 
				 	 List.class, 
				 	 ListJsonFieldDeserializer.class, 
				 	 LongJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalList() {
		EndpointParameter param = EndpointParameter.create("BIGDECIMAL_LIST", "myBigDecimalListParam", null, null, List.of());
		param.setSampleValue("[1234.56789,9876.54321]");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<BigDecimal> createMyBigDecimalListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance()).deserialize(\"[1234.56789,9876.54321]\");\n"
				+ "}\n", method);
		checkImports(imports, 
					 List.class,
					 BigDecimal.class,
					 ListJsonFieldDeserializer.class, 
					 BigDecimalJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveBooleanList() {
		EndpointParameter param = EndpointParameter.create("BOOLEAN_LIST", "myBooleanListParam", null, null, List.of());
		param.setSampleValue("[true, false]");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<Boolean> createMyBooleanListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(BooleanJsonFieldDeserializer.getInstance()).deserialize(\"[true, false]\");\n"
				+ "}\n", method);
		checkImports(imports, 
					 List.class, 
					 BooleanJsonFieldDeserializer.class, 
					 ListJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectList() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_LIST", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static List<MyRequest> createMyObjListParam() {\n"
				+ "  MyRequest requestItem = new MyRequest();\n"
				+ "  requestItem.setFoo(Integer.valueOf(123));\n"
				+ "  requestItem.setHello(\"Hello World\");\n"
				+ "  return List.of(requestItem);\n"
				+ "}\n", method);
		checkImports(imports, List.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMap() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP", "myObjMapParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(List.of("myKey0"));
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, MyRequest> createMyObjMapParam() {\n"
				+ "  MyRequest requestItem = new MyRequest();\n"
				+ "  requestItem.setFoo(Integer.valueOf(123));\n"
				+ "  requestItem.setHello(\"Hello World\");\n"
				+ "  return Map.of(\"myKey0\", requestItem);\n"
				+ "}\n", method);
		checkImports(imports, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapNullSampleMapKey() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(null);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, MyRequest> createMyObjListParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
		checkImports(imports, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapEmptySampleMapKey() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(List.of());
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, MyRequest> createMyObjListParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
		checkImports(imports, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapMap() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP_LIST_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(List.of("m1Key", "m2Key"));
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, List<Map<String, MyRequest>>> createMyObjListParam() {\n"
				+ "  MyRequest requestItem = new MyRequest();\n"
				+ "  requestItem.setFoo(Integer.valueOf(123));\n"
				+ "  requestItem.setHello(\"Hello World\");\n"
				+ "  return Map.of(\"m1Key\", List.of(Map.of(\"m2Key\", requestItem)));\n"
				+ "}\n", method);
		checkImports(imports, List.class, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodStringValuesMap() {
		EndpointParameter param = EndpointParameter.create("STRING_MAP", "adresses", null, null, "{\"Jamie\": \"London\", \"Amina\": \"Djibouti\"}");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, String> createAdresses() {\n"
				+ "  return new MapJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"{\\\"Jamie\\\": \\\"London\\\", \\\"Amina\\\": \\\"Djibouti\\\"}\");\n"
				+ "}\n", method);
		checkImports(imports, 
					 Map.class, 
					 MapJsonFieldDeserializer.class, 
					 StringJsonFieldDeserializer.class);
	}
	
	private void checkImports(Set<String> actualImports, Class<?>...expectedImports) {
		String[] expected = Arrays.stream(expectedImports)
								  .map(i -> i.getName())
								  .collect(Collectors.toList())
								  .toArray(new String[expectedImports.length]);
		Arrays.sort(expected);
		List<String> actual = actualImports.stream().filter(s -> !s.startsWith("java.lang") && s.contains(".")).collect(Collectors.toList());
		BinaryOperator<String> concat = (s1, s2) -> s1 + "\n" + s2;
		String errMsg = "Unexpected imports, expected:\n" 
						+ Arrays.stream(expected)
								.reduce(concat).orElse(null) 
						+ "\n actual:\n" 
						+ actual.stream().reduce(concat).orElse(null);
		Assert.assertEquals(errMsg, expected.length, actual.size());
		for (int i = 0; i < expected.length; i++) {
			Assert.assertEquals(errMsg, expected[i], actual.get(i));
		}
	}
}
