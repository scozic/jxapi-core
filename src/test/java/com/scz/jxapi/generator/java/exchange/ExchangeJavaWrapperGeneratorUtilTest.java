package com.scz.jxapi.generator.java.exchange;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.TimestampJsonFieldDeserializer;

/**
 * Unit test for {@link ExchangeJavaWrapperGeneratorUtil}
 */
public class ExchangeJavaWrapperGeneratorUtilTest {
	
	@Test
	public void testGetApiInterfacClassName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
		apiDescriptor.setName("Spot");
		Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApi", ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, apiDescriptor));
	}		
		
	@Test
	public void testGetExchangeInterfaceName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		Assert.assertEquals("com.x.y.z.TestExchangeExchange", ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor));
	}

	@Test
	public void testGenerateRateLimitVariableName() {
		Assert.assertEquals("RATE_LIMIT_TEST_RATE_LIMIT", ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName("testRateLimit"));
	}

	@Test
	public void testGetJsonMessageDeserializerClassName() {
		Assert.assertEquals("com.x.y.deserializers.MyObjectDeserializer", ExchangeJavaWrapperGeneratorUtil.getJsonMessageDeserializerClassName("com.x.y.pojo.MyObject"));
	}

	@Test
	public void testGetClassNameForParameterType_INT() {
		Assert.assertEquals("Integer", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.INT, new HashSet<>(), null));
	}

	@Test
	public void testGetClassNameForParameterType_STRING() {
		Assert.assertEquals("String", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.STRING, new HashSet<>(), null));
	}

	@Test
	public void testGetClassNameForParameterType_BOOLEAN() {
		Assert.assertEquals("Boolean", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.BOOLEAN, new HashSet<>(), null));
	}

	@Test
	public void testGetClassNameForParameterType_BIGDECIMAL() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("BigDecimal", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.BIGDECIMAL, imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BigDecimal.class.getName()));
	}

	@Test
	public void testGetClassNameForParameterType_BIGDECIMAL_NullImports() {
		Assert.assertEquals("BigDecimal", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.BIGDECIMAL, null, null));
	}

	@Test
	public void testGetClassNameForParameterType_LONG() {
		Assert.assertEquals("Long", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.LONG, new HashSet<>(), null));	
	}

	@Test
	public void testGetClassNameForParameterType_TIMESTAMP() {
		Assert.assertEquals("Long", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.TIMESTAMP, new HashSet<>(), null));
	}

	@Test
	public void testGetClassNameForParameterType_STRING_LIST() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("List<String>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("STRING_LIST"), imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(List.class.getName()));
	}

	@Test
	public void testGetClassNameForParameterType_STRING_LIST_NullImports() {
		Assert.assertEquals("List<String>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("STRING_LIST"), null, null));
	}

	@Test
	public void testGetClassNameForParameterType_INT_MAP() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("Map<String, Integer>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("INT_MAP"), imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(Map.class.getName()));
	}

	@Test
	public void testGetClassNameForParameterType_INT_MAP_NullImports() {
		Assert.assertEquals("Map<String, Integer>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("INT_MAP"), null, null));
	}

	@Test
	public void testGetClassNameForParameterType_OBJECT() {
		Set<String> imports = new HashSet<>();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("MyObject", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.OBJECT, imports, objectClassName));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(objectClassName));
	}

	@Test
	public void testGetClassNameForParameterType_OBJECT_LIST_MAP() {
		Set<String> imports = new HashSet<>();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("Map<String, List<MyObject>>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
		Assert.assertEquals(3, imports.size());
		Assert.assertTrue(imports.contains(objectClassName));
		Assert.assertTrue(imports.contains(Map.class.getName()));
		Assert.assertTrue(imports.contains(List.class.getName()));
	}

	@Test
	public void testGetClassNameForParameterType_NullType() {
		Assert.assertNull(ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(null, null, null));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_INT() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("IntegerJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.INT, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_BOOLEAN() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("BooleanJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.BOOLEAN, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BooleanJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_BIGDECIMAL() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("BigDecimalJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.BIGDECIMAL, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BigDecimalJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_LONG() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("LongJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.LONG, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(LongJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_TIMESTAMP() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("TimestampJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.TIMESTAMP, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(TimestampJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_STRING() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.STRING, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class.getName()));
	}
	
	@Test
	public void testGetNewJsonFieldDeserializerInstruction_STRING_LIST() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance())", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("STRING_LIST"), null, imports));
		Assert.assertEquals(2, imports.size());
		Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class.getName()));
		Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_INT_MAP() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("INT_MAP"), null, imports));
		Assert.assertEquals(2, imports.size());
		Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class.getName()));
		Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_OBJECT() {
		Set<String> imports = new HashSet<>();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("new MyObjectDeserializer()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("OBJECT"), objectClassName, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyObjectDeserializer"));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_OBJECT_LIST_MAP() {
		Set<String> imports = new HashSet<>();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("Map<String, List<MyObject>>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
		Assert.assertEquals(3, imports.size());
		Assert.assertTrue(imports.contains(objectClassName));
		Assert.assertTrue(imports.contains(Map.class.getName()));
		Assert.assertTrue(imports.contains(List.class.getName()));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_NullType() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(null, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class.getName()));
	}
}
