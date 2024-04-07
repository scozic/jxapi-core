package com.scz.jxapi.generator.exchange;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;

/**
 * Unit test for {@link EndpointDemoGeneratorUtil}
 */
public class EndpointDemoGeneratorUtilTest {

	@Test
	public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameter() {
		EndpointParameter param = EndpointParameter.create("STRING", "myStringParam", null, null, List.of());
		param.setSampleValue("Hello World");
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), null, imports);
		Assert.assertEquals(
				"protected String createMyStringParam() {\n"
				+ "  return \"Hello World\";\n"
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
	public void testGenerateEndpointParameterCreationMethodSimpleObjectParameter() {
		EndpointParameter param = EndpointParameter.createObject(null, "myObjParam", null, null, List.of(
					EndpointParameter.create("INT", "foo", null, null, 123),
					EndpointParameter.create("STRING", "hello", null, null, "Hello World")
				));
		param.setSampleValue(123);
		Set<String> imports = new HashSet<>();
		String method = EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(param, param.getName(), "MyRequest", imports);
		Assert.assertEquals(
				"protected MyRequest createMyObjParam() {\n"
				+ "  MyRequest myObjParam = new MyRequest();\n"
				+ "  myObjParam.setFoo(Integer.valueOf(123));\n"
				+ "  myObjParam.setHello(\"Hello World\");\n"
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
							EndpointParameter.create("BOOLEAN", "enabled", null, null, true)
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
				+ "  return myObjParam;\n"
				+ "}\n", method);
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
		param.setSampleValue("[true, false]");
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
}
