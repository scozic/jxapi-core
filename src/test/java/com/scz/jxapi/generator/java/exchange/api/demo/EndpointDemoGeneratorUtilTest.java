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

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
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
		Field param = Field.create("STRING", "myStringParam", null, null, "Hello World");
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static String createMyStringParam() {\n"
				+ "  return \"Hello World\";\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameterNullSampleValue() {
		Field param = Field.create("STRING", "myStringParam", null, null, null);
		Set<String> imports = new TreeSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static String createMyStringParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerParameter() {
		Field param = Field.create("INT", "myIntParam", null, null, List.of());
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
		Field param = Field.create("LONG", "myLongParam", null, null, List.of());
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
		Field param = Field.create("TIMESTAMP", "myLongParam", null, null, List.of());
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
		Field param = Field.create("BIGDECIMAL", "myBigDecimalParam", null, null, List.of());
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
		Field param = Field.create("BOOLEAN", "myBooleanParam", null, null, List.of());
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
		Field param = Field.createObject(null, "myObjParam", null, null, List.of(
					Field.create("INT", "foo", null, null, 123),
					Field.create("STRING", "hello", null, null, "Hello World"),
					Field.create("LONG", "aLong", null, null, 9876543210L),
					Field.create("BIGDECIMAL", "bDecimal", null, null, "123.45"),
					Field.create("BOOLEAN", "cBool", null, null, true),
					Field.create("STRING", "theVoidStr", null, null, null),
					Field.create("STRING_LIST", "theVoidList", null, null, null),
					Field.create("STRING_MAP", "theVoidMap", null, null, null)
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
		Field param = Field.createObject(null, "myObjParam", null, null, List.of(
					Field.create("INT", "foo", null, null, 123),
					Field.create("STRING", "hello", null, null, "Hello World"),
					Field.createObject("OBJECT_LIST", "bar", null, null, List.of(
							Field.create("STRING", "id", null, null, "id#0"),
							Field.create("BOOLEAN", "enabled", null, null, true),
							Field.create("TIMESTAMP", "time", null, null, null),
							Field.create("BIGDECIMAL_LIST_MAP", "bestBids", null, null, "{\"BTC_USDT\": [\"69268.61\", \"69268.62\"], \"ETH_USDT\":[\"3427.98\", \"3427.90\"]}")
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
		Field param = Field.create("INT_LIST", "myIntListParam", null, null, List.of());
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
		Field param = Field.create("STRING_LIST", "myStringListParam", null, null, List.of());
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
		Field param = Field.create("LONG_LIST", "myLongListParam", null, null, List.of());
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
		Field param = Field.create("BIGDECIMAL_LIST", "myBigDecimalListParam", null, null, List.of());
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
		Field param = Field.create("BOOLEAN_LIST", "myBooleanListParam", null, null, List.of());
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
		Field param = Field.createObject("OBJECT_LIST", "myObjListParam", null, null, List.of(
				Field.create("INT", "foo", null, null, 123),
				Field.create("STRING", "hello", null, null, "Hello World")
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
		Field param = Field.createObject("OBJECT_MAP", "myObjMapParam", null, null, List.of(
				Field.create("INT", "foo", null, null, 123),
				Field.create("STRING", "hello", null, null, "Hello World")
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
		Field param = Field.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				Field.create("INT", "foo", null, null, 123),
				Field.create("STRING", "hello", null, null, "Hello World")
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
		Field param = Field.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				Field.create("INT", "foo", null, null, 123),
				Field.create("STRING", "hello", null, null, "Hello World")
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
		Field param = Field.createObject("OBJECT_MAP_LIST_MAP", "myObjListParam", null, null, List.of(
				Field.create("INT", "foo", null, null, 123),
				Field.create("STRING", "hello", null, null, "Hello World")
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
		Field param = Field.create("STRING_MAP", "adresses", null, null, "{\"Jamie\": \"London\", \"Amina\": \"Djibouti\"}");
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
	@Test
	public void testGetRestApiDemoClassName() {
			ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
			exchangeDescriptor.setName("MyExchange");
			exchangeDescriptor.setBasePackage("com.x.y.z.gen");
			ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
			exchangeApiDescriptor.setName("MyApi");
			RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
			restEndpointDescriptor.setName("MyRestEndpoint");
			String className = EndpointDemoGeneratorUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor);
			Assert.assertEquals("com.x.y.z.gen.myapi.demo.MyExchangeMyApiMyRestEndpointDemo", className);
	}
	
	@Test
	public void testGetWebsocketApiDemoClassName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("MyExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z.gen");
		ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
		exchangeApiDescriptor.setName("MyApi");
		WebsocketEndpointDescriptor websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
		websocketEndpointDescriptor.setName("MyWebsocketEndpoint");
		String className = EndpointDemoGeneratorUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor);
		Assert.assertEquals("com.x.y.z.gen.myapi.demo.MyExchangeMyApiMyWebsocketEndpointDemo", className);
	}
	
	@Test
	public void testGetNewTestApiInstruction() {
		String exchangeId = "MyExchange";
		String exchangeImplClassName = "com.x.y.z.gen.MyExchangeImpl";
		String simpleApiClassName  = "MyApi";
		Assert.assertEquals("MyApi api = new MyExchangeImpl(\"test-MyExchange\", TestJXApiProperties.filterProperties(\"MyExchange\", true)).getMyApi();\n", 
				EndpointDemoGeneratorUtil.getNewTestApiInstruction(exchangeId, exchangeImplClassName, simpleApiClassName));
	}
}
