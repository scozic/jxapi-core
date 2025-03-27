package org.jxapi.generator.java.exchange.api.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;

/**
 * Unit test for {@link EndpointDemoGenUtil}
 */
public class EndpointDemoGenUtilTest {

	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameter() {
		Field param = Field.builder().type(Type.STRING).name("myStringParam").sampleValue("Hello World").build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static String createMyStringParam() {\n"
				+ "  return \"Hello World\";\n"
				+ "}\n", method);
		checkImports(imports);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameterNullSampleValue() {
		Field param = Field.builder().type(Type.STRING).name("myStringParam").build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static String createMyStringParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
		checkImports(imports);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerParameter() {
		Field param = Field.builder().type(Type.INT).name("myIntParam").sampleValue(123).build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Integer createMyIntParam() {\n"
			  + "  return Integer.valueOf(123);\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveLongParameter() {
		Field param = Field.builder().type(Type.LONG).name("myLongParam").sampleValue(123L).build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Long createMyLongParam() {\n"
			  + "  return Long.valueOf(\"123\");\n"
			  + "}\n", method);
		checkImports(imports);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveTimestampParameterSpecialValueNow() {
		Field param = Field.builder().type(Type.LONG).name("myLongParam").sampleValue("now()").build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Long createMyLongParam() {\n"
			  + "  return Long.valueOf(System.currentTimeMillis());\n"
			  + "}\n", method);
		checkImports(imports);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalParameter() {
		Field param = Field.builder().type(Type.BIGDECIMAL).name("myBigDecimalParam").sampleValue("123.45").build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static BigDecimal createMyBigDecimalParam() {\n"
			  + "  return new BigDecimal(\"123.45\");\n"
			  + "}\n", method);
		checkImports(imports, BigDecimal.class);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveBooleanParameter() {
		Field param = Field.builder().type(Type.BOOLEAN).name("myBooleanParam").sampleValue(true).build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static Boolean createMyBooleanParam() {\n"
				+ "  return Boolean.valueOf(true);\n"
				+ "}\n", method);
		checkImports(imports);
	}

	@Test
	public void testGenerateEndpointParameterCreationMethodSimpleObjectParameter() {
		Field param = Field.builder()
						   .name("myObjParam")
						   .property(Field.builder().type(Type.INT).name("foo").sampleValue(123).build())
						   .property(Field.builder().type(Type.STRING).name("hello").sampleValue("Hello World").build())
						   .property(Field.builder().type(Type.LONG).name("aLong").sampleValue(9876543210L).build())
						   .property(Field.builder().type(Type.BIGDECIMAL).name("bDecimal").sampleValue("123.45").build())
						   .property(Field.builder().type(Type.BOOLEAN).name("cBool").sampleValue(true).build())
						   .property(Field.builder().type(Type.STRING).name("theVoidStr").build())
						   .property(Field.builder().type("STRING_LIST").name("theVoidList").build())
						   .property(Field.builder().type("STRING_MAP").name("theVoidMap").build())
						   .build();
						   
		param.setSampleValue(123);
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static MyRequest createMyObjParam() {\n"
				+ "  MyRequest request = new MyRequest();\n"
				+ "  request.setFoo(Integer.valueOf(123));\n"
				+ "  request.setHello(\"Hello World\");\n"
				+ "  request.setALong(Long.valueOf(\"9876543210\"));\n"
				+ "  request.setBDecimal(new BigDecimal(\"123.45\"));\n"
				+ "  request.setCBool(Boolean.valueOf(true));\n"
				+ "  return request;\n"
				+ "}\n", method);
		checkImports(imports, BigDecimal.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodComplexObjectParameter() {
		Field param = Field.builder()
				   		   .name("myObjParam")
				   		   .property(Field.builder().type(Type.INT).name("foo").sampleValue(123).build())
				   		   .property(Field.builder().type(Type.STRING).name("hello").sampleValue("Hello World").build())
				   		   .property(Field.builder().type("OBJECT_LIST").name("bar")
				   				   		  .property(Field.builder().type(Type.STRING).name("id").sampleValue("id#0").build())
				   				   		  .property(Field.builder().type(Type.BOOLEAN).name("enabled").sampleValue(true).build())
				   				   		  .property(Field.builder().type(Type.LONG).name("time").build())
				   				   		  .property(Field.builder().type("BIGDECIMAL_LIST_MAP").name("bestBids")
				   				   				  		 .sampleValue("{\"BTC_USDT\": [\"69268.61\", \"69268.62\"], \"ETH_USDT\":[\"3427.98\", \"3427.90\"]}")
				   				   				  		 .build())
				   				   	      .build())
				   		   .build();
		
		param.setSampleValue(123);
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
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
		
		checkImports(imports, 
				 List.class,
				 BigDecimalJsonFieldDeserializer.class,
				 ListJsonFieldDeserializer.class,
				 MapJsonFieldDeserializer.class );
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerList() {
		Field param = Field.builder()
						   .type("INT_LIST")
						   .name("myIntListParam")
						   .sampleValue(List.of(1,3,5,7))
						   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<Integer> createMyIntListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(\"[1, 3, 5, 7]\");\n"
				+ "}\n", method);
		checkImports(imports,
					 List.class,
					 IntegerJsonFieldDeserializer.class, 
					 ListJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringList() {
		Field param = Field.builder()
						   .type("STRING_LIST")
						   .name("myStringListParam")
						   .sampleValue("[\"BTC\",\"ETH\"]")
						   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
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
		Field param = Field.builder()
						   .type("LONG_LIST")
						   .name("myLongListParam")
						   .sampleValue("[1234567890,9876543210]")
						   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
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
		Field param = Field.builder()
						   .type("BIGDECIMAL_LIST")
						   .name("myBigDecimalListParam")
						   .sampleValue("[1234.56789,9876.54321]")
						   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
		Assert.assertEquals(
				"public static List<BigDecimal> createMyBigDecimalListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance()).deserialize(\"[1234.56789,9876.54321]\");\n"
				+ "}\n", method);
		checkImports(imports,
					 BigDecimal.class,
					 List.class,
					 BigDecimalJsonFieldDeserializer.class,
					 ListJsonFieldDeserializer.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveBooleanList() {
		Field param = Field.builder()
				   .type("BOOLEAN_LIST")
				   .name("myBooleanListParam")
				   .sampleValue("[true, false]")
				   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, imports);
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
		Field param = Field.builder()
						   .type("OBJECT_LIST")
						   .name("myObjListParam")
						   .property(Field.builder()
								   		  .type("INT")
								   		  .name("foo")
								   		  .sampleValue(123)
								   		  .build())
						   .property(Field.builder()
							   		      .type("STRING")
							   		      .name("hello")
							   		      .sampleValue("Hello World")
							   		      .build())
						   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
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
		Field param = Field.builder()
						   .type("OBJECT_MAP")
						   .name("myObjMapParam")
						   .property(Field.builder()
								   		  .type("INT")
								   		  .name("foo")
								   		  .sampleValue(123)
								   		  .build())
						   .property(Field.builder()
							   		      .type("STRING")
							   		      .name("hello")
							   		      .sampleValue("Hello World")
							   		      .build())
						   .sampleMapKeyValues(List.of("myKey0"))
						   .build();
		
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
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
		Field param = Field.builder()
						   .type("OBJECT_MAP")
						   .name("myObjMapParam")
						   .property(Field.builder()
								   		  .type("INT")
								   		  .name("foo")
								   		  .sampleValue(123)
								   		  .build())
						   .property(Field.builder()
							   		      .type("STRING")
							   		      .name("hello")
							   		      .sampleValue("Hello World")
							   		      .build())
						   .sampleMapKeyValues(null)
						   .build();
		param.setSampleMapKeyValue(null);
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, MyRequest> createMyObjMapParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
		checkImports(imports, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapEmptySampleMapKey() {
		Field param = Field.builder()
						   .type("OBJECT_MAP")
						   .name("myObjMapParam")
						   .property(Field.builder()
								   		  .type("INT")
								   		  .name("foo")
								   		  .sampleValue(123)
								   		  .build())
						   .property(Field.builder()
							   		      .type("STRING")
							   		      .name("hello")
							   		      .sampleValue("Hello World")
							   		      .build())
						   .sampleMapKeyValues(List.of())
						   .build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, MyRequest> createMyObjMapParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
		checkImports(imports, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapMap() {
		Field param = Field.builder()
						   .type("OBJECT_MAP_LIST_MAP")
						   .name("myObjMapListMapParam")
						   .property(Field.builder()
								   		  .type("INT")
								   		  .name("foo")
								   		  .sampleValue(123)
								   		  .build())
						   .property(Field.builder()
							   		      .type("STRING")
							   		      .name("hello")
							   		      .sampleValue("Hello World")
							   		      .build())
						   .sampleMapKeyValues(List.of("m1Key", "m2Key"))
						   .build();
		
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, List<Map<String, MyRequest>>> createMyObjMapListMapParam() {\n"
				+ "  MyRequest requestItem = new MyRequest();\n"
				+ "  requestItem.setFoo(Integer.valueOf(123));\n"
				+ "  requestItem.setHello(\"Hello World\");\n"
				+ "  return Map.of(\"m1Key\", List.of(Map.of(\"m2Key\", requestItem)));\n"
				+ "}\n", method);
		checkImports(imports, List.class, Map.class);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodStringValuesMap() {
		Field param = Field.builder().type("STRING_MAP").name("adresses").sampleValue("{\"Jamie\": \"London\", \"Amina\": \"Djibouti\"}").build();
		Imports imports = new Imports();
		String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", imports);
		Assert.assertEquals(
				"public static Map<String, String> createAdresses() {\n"
				+ "  return new MapJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"{\\\"Jamie\\\": \\\"London\\\", \\\"Amina\\\": \\\"Djibouti\\\"}\");\n"
				+ "}\n", method);
		checkImports(imports, 
					 Map.class, 
					 MapJsonFieldDeserializer.class, 
					 StringJsonFieldDeserializer.class);
	}
	
	private void checkImports(Imports actualImports, Class<?>...expectedImports) {
		String[] expected = Arrays.stream(expectedImports)
								  .map(i -> i.getName())
								  .collect(Collectors.toList())
								  .toArray(new String[expectedImports.length]);
		List<String> actual = actualImports.getAllImports().stream().filter(s -> !s.startsWith("java.lang") && s.contains(".")).collect(Collectors.toList());
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
			exchangeDescriptor.setId("MyExchange");
			exchangeDescriptor.setBasePackage("com.x.y.z.gen");
			ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
			exchangeApiDescriptor.setName("MyApi");
			RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
			restEndpointDescriptor.setName("MyRestEndpoint");
			String className = EndpointDemoGenUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor);
			Assert.assertEquals("com.x.y.z.gen.myapi.demo.MyExchangeMyApiMyRestEndpointDemo", className);
	}
	
	@Test
	public void testGetWebsocketApiDemoClassName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setId("MyExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z.gen");
		ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
		exchangeApiDescriptor.setName("MyApi");
		WebsocketEndpointDescriptor websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
		websocketEndpointDescriptor.setName("MyWebsocketEndpoint");
		String className = EndpointDemoGenUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor);
		Assert.assertEquals("com.x.y.z.gen.myapi.demo.MyExchangeMyApiMyWebsocketEndpointDemo", className);
	}
	
	@Test
	public void testGetNewTestApiInstruction() {
		String simpleApiClassName  = "MyApi";
		Assert.assertEquals("MyApi api = exchange.getMyApi();", 
				EndpointDemoGenUtil.getNewTestApiInstruction("exchange", "api", simpleApiClassName));
	}
	
	@Test
	public void testGetNewTestExchangeInstruction() {
		String exchangeClassName = "com.x.y.z.gen.MyExchange";
		Assert.assertEquals("MyExchange exchange = new MyExchangeImpl(\"test-\" + MyExchange.ID, props);",
				EndpointDemoGenUtil.getNewTestExchangeInstruction(exchangeClassName, "exchange", "props"));
	}
}
