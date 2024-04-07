package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;

/**
 * Unit test for {@link EndpointDemoGeneratorUtil}
 */
public class EndpointDemoGeneratorUtilTest {

	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameter() {
		EndpointParameter param = EndpointParameter.create("STRING", "myStringParam", null, null, "Hello World");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected String createMyStringParam() {\n"
				+ "  return \"Hello World\";\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameterNullSampleValue() {
		EndpointParameter param = EndpointParameter.create("STRING", "myStringParam", null, null, null);
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected String createMyStringParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerParameter() {
		EndpointParameter param = EndpointParameter.create("INT", "myIntParam", null, null, List.of());
		param.setSampleValue(123);
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected Integer createMyIntParam() {\n"
			  + "  return Integer.valueOf(123);\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveLongParameter() {
		EndpointParameter param = EndpointParameter.create("LONG", "myLongParam", null, null, List.of());
		param.setSampleValue(123L);
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected Long createMyLongParam() {\n"
			  + "  return Long.valueOf(123);\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalParameter() {
		EndpointParameter param = EndpointParameter.create("BIGDECIMAL", "myBigDecimalParam", null, null, List.of());
		param.setSampleValue("123.45");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected BigDecimal createMyBigDecimalParam() {\n"
			  + "  return new BigDecimal(\"123.45\");\n"
			  + "}\n", method);
	}
	
	@Test
	public void  testGenerateEndpointParameterCreationMethodPrimitiveBooleanParameter() {
		EndpointParameter param = EndpointParameter.create("BOOLEAN", "myBooleanParam", null, null, List.of());
		param.setSampleValue(true);
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected Boolean createMyBooleanParam() {\n"
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
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected MyRequest createMyObjParam() {\n"
				+ "  MyRequest myObjParam = new MyRequest();\n"
				+ "  myObjParam.setFoo(Integer.valueOf(123));\n"
				+ "  myObjParam.setHello(\"Hello World\");\n"
				+ "  myObjParam.setALong(Long.valueOf(9876543210));\n"
				+ "  myObjParam.setBDecimal(new BigDecimal(\"123.45\"));\n"
				+ "  myObjParam.setCBool(Boolean.valueOf(true));\n"
				+ "  myObjParam.setTheVoidStr(null);\n"
				+ "  myObjParam.setTheVoidList(null);\n"
				+ "  myObjParam.setTheVoidMap(null);\n"
				+ "  return myObjParam;\n"
				+ "}\n", method);
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
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected MyRequest createMyObjParam() {\n"
				+ "  MyRequest myObjParam = new MyRequest();\n"
				+ "  myObjParam.setFoo(Integer.valueOf(123));\n"
				+ "  myObjParam.setHello(\"Hello World\");\n"
				+ "  MyRequestBar myObjParam_barItem = new MyRequestBar();\n"
				+ "  myObjParam_barItem.setId(\"id#0\");\n"
				+ "  myObjParam_barItem.setEnabled(Boolean.valueOf(true));\n"
				+ "  myObjParam_barItem.setTime(null);\n"
				+ "  myObjParam_barItem.setBestBids(new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())).deserialize(\"{\\\"BTC_USDT\\\": [\\\"69268.61\\\", \\\"69268.62\\\"], \\\"ETH_USDT\\\":[\\\"3427.98\\\", \\\"3427.90\\\"]}\"));\n"
				+ "  myObjParam.setBar(List.of(myObjParam_barItem));\n"
				+ "  return myObjParam;\n"
				+ "}\n", method);
		
		Map<String, List<BigDecimal>> map = new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())).deserialize("{\"BTC_USDT\": [\"69268.61\", \"69268.62\"], \"ETH_USDT\":[\"3427.98\", \"3427.90\"]}");
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerList() {
		EndpointParameter param = EndpointParameter.create("INT_LIST", "myIntListParam", null, null, List.of());
		param.setSampleValue(List.of(1,3,5,7));
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected List<Integer> createMyIntListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(\"[1, 3, 5, 7]\");\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringList() {
		EndpointParameter param = EndpointParameter.create("STRING_LIST", "myStringListParam", null, null, List.of());
		param.setSampleValue("[\"BTC\",\"ETH\"]");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected List<String> createMyStringListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"BTC\\\",\\\"ETH\\\"]\");\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveLongList() {
		EndpointParameter param = EndpointParameter.create("LONG_LIST", "myLongListParam", null, null, List.of());
		param.setSampleValue("[1234567890,9876543210]");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected List<Long> createMyLongListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(LongJsonFieldDeserializer.getInstance()).deserialize(\"[1234567890,9876543210]\");\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalList() {
		EndpointParameter param = EndpointParameter.create("BIGDECIMAL_LIST", "myBigDecimalListParam", null, null, List.of());
		param.setSampleValue("[1234.56789,9876.54321]");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected List<BigDecimal> createMyBigDecimalListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance()).deserialize(\"[1234.56789,9876.54321]\");\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveBooleanList() {
		EndpointParameter param = EndpointParameter.create("BOOLEAN_LIST", "myBooleanListParam", null, null, List.of());
		param.setSampleValue("[true, false]");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected List<Boolean> createMyBooleanListParam() {\n"
				+ "  return new ListJsonFieldDeserializer<>(BooleanJsonFieldDeserializer.getInstance()).deserialize(\"[true, false]\");\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectList() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_LIST", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected List<MyRequest> createMyObjListParam() {\n"
				+ "  MyRequest myObjListParamItem = new MyRequest();\n"
				+ "  myObjListParamItem.setFoo(Integer.valueOf(123));\n"
				+ "  myObjListParamItem.setHello(\"Hello World\");\n"
				+ "  return List.of(myObjListParamItem);\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMap() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(List.of("myKey0"));
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected Map<String, MyRequest> createMyObjListParam() {\n"
				+ "  MyRequest myObjListParamItem = new MyRequest();\n"
				+ "  myObjListParamItem.setFoo(Integer.valueOf(123));\n"
				+ "  myObjListParamItem.setHello(\"Hello World\");\n"
				+ "  return Map.of(\"myKey0\", myObjListParamItem);\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapNullSampleMapKey() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(null);
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected Map<String, MyRequest> createMyObjListParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapEmptySampleMapKey() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(List.of());
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected Map<String, MyRequest> createMyObjListParam() {\n"
				+ "  return null;\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodObjectMapMap() {
		EndpointParameter param = EndpointParameter.createObject("OBJECT_MAP_LIST_MAP", "myObjListParam", null, null, List.of(
				EndpointParameter.create("INT", "foo", null, null, 123),
				EndpointParameter.create("STRING", "hello", null, null, "Hello World")
			));
		param.setSampleMapKeyValue(List.of("m1Key", "m2Key"));
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected Map<String, List<Map<String, MyRequest>>> createMyObjListParam() {\n"
				+ "  MyRequest myObjListParamItem = new MyRequest();\n"
				+ "  myObjListParamItem.setFoo(Integer.valueOf(123));\n"
				+ "  myObjListParamItem.setHello(\"Hello World\");\n"
				+ "  return Map.of(\"m1Key\", List.of(Map.of(\"m2Key\", myObjListParamItem)));\n"
				+ "}\n", method);
	}
	
	@Test
	public void testGenerateEndpointParameterCreationMethodStringValuesMap() {
		EndpointParameter param = EndpointParameter.create("STRING_MAP", "adresses", null, null, "{\"Jamie\": \"London\", \"Amina\": \"Djibouti\"}");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected Map<String, String> createAdresses() {\n"
				+ "  return new MapJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"{\\\"Jamie\\\": \\\"London\\\", \\\"Amina\\\": \\\"Djibouti\\\"}\");\n"
				+ "}\n", method);
	}
}
