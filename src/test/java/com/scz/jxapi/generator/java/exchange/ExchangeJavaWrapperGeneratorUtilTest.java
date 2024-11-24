package com.scz.jxapi.generator.java.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
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
	public void testGetApiInterfaceClassName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
		apiDescriptor.setName("Spot");
		Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApi", 
							ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, apiDescriptor));
	}
	
	@Test
	public void testGetApiInterfaceImplementationClassName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
		apiDescriptor.setName("Spot");
		Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApiImpl", 
							ExchangeJavaWrapperGeneratorUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, apiDescriptor));
	}	
		
	@Test
	public void testGetExchangeInterfaceName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		Assert.assertEquals("com.x.y.z.TestExchangeExchange", 
							ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor));
	}

	@Test
	public void testGenerateRateLimitVariableName() {
		Assert.assertEquals("RATE_LIMIT_TEST_RATE_LIMIT", 
							ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName("testRateLimit"));
	}

	@Test
	public void testGetJsonMessageDeserializerClassName() {
		Assert.assertEquals("com.x.y.deserializers.MyObjectDeserializer", 
							ExchangeJavaWrapperGeneratorUtil.getJsonMessageDeserializerClassName("com.x.y.pojo.MyObject"));
	}

	@Test
	public void testGetClassNameForParameterType_INT() {
		Assert.assertEquals("Integer", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.INT, new Imports(), null));
	}

	@Test
	public void testGetClassNameForParameterType_STRING() {
		Assert.assertEquals("String", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.STRING, new Imports(), null));
	}

	@Test
	public void testGetClassNameForParameterType_BOOLEAN() {
		Assert.assertEquals("Boolean", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.BOOLEAN, new Imports(), null));
	}

	@Test
	public void testGetClassNameForParameterType_BIGDECIMAL() {
		Imports imports = new Imports();
		Assert.assertEquals("BigDecimal", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.BIGDECIMAL, imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BigDecimal.class));
	}

	@Test
	public void testGetClassNameForParameterType_BIGDECIMAL_NullImports() {
		Assert.assertEquals("BigDecimal", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.BIGDECIMAL, null, null));
	}

	@Test
	public void testGetClassNameForParameterType_LONG() {
		Assert.assertEquals("Long", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.LONG, new Imports(), null));	
	}

	@Test
	public void testGetClassNameForParameterType_TIMESTAMP() {
		Assert.assertEquals("Long", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.TIMESTAMP, new Imports(), null));
	}

	@Test
	public void testGetClassNameForParameterType_STRING_LIST() {
		Imports imports = new Imports();
		Assert.assertEquals("List<String>", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(List.class));
	}

	@Test
	public void testGetClassNameForParameterType_STRING_LIST_NullImports() {
		Assert.assertEquals("List<String>", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), null, null));
	}

	@Test
	public void testGetClassNameForParameterType_INT_MAP() {
		Imports imports = new Imports();
		Assert.assertEquals("Map<String, Integer>", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(Map.class));
	}

	@Test
	public void testGetClassNameForParameterType_INT_MAP_NullImports() {
		Assert.assertEquals("Map<String, Integer>", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), null, null));
	}

	@Test
	public void testGetClassNameForParameterType_OBJECT() {
		Imports imports = new Imports();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("MyObject", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.OBJECT, imports, objectClassName));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(objectClassName));
	}

	@Test
	public void testGetClassNameForParameterType_OBJECT_LIST_MAP() {
		Imports imports = new Imports();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("Map<String, List<MyObject>>", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
		Assert.assertEquals(3, imports.size());
		Assert.assertTrue(imports.contains(objectClassName));
		Assert.assertTrue(imports.contains(Map.class));
		Assert.assertTrue(imports.contains(List.class));
	}

	@Test
	public void testGetClassNameForParameterType_NullType() {
		Assert.assertNull(ExchangeJavaWrapperGeneratorUtil.getClassNameForType(null, null, null));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_INT() {
		Imports imports = new Imports();
		Assert.assertEquals("IntegerJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.INT, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_BOOLEAN() {
		Imports imports = new Imports();
		Assert.assertEquals("BooleanJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.BOOLEAN, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BooleanJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_BIGDECIMAL() {
		Imports imports = new Imports();
		Assert.assertEquals("BigDecimalJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.BIGDECIMAL, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BigDecimalJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_LONG() {
		Imports imports = new Imports();
		Assert.assertEquals("LongJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.LONG, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(LongJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_TIMESTAMP() {
		Imports imports = new Imports();
		Assert.assertEquals("TimestampJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.TIMESTAMP, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(TimestampJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_STRING() {
		Imports imports = new Imports();
		Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.STRING, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
	}
	
	@Test
	public void testGetNewJsonFieldDeserializerInstruction_STRING_LIST() {
		Imports imports = new Imports();
		Assert.assertEquals("new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance())", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("STRING_LIST"), null, imports));
		Assert.assertEquals(2, imports.size());
		Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class));
		Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_INT_MAP() {
		Imports imports = new Imports();
		Assert.assertEquals("new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("INT_MAP"), null, imports));
		Assert.assertEquals(2, imports.size());
		Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class));
		Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_OBJECT() {
		Imports imports = new Imports();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("new MyObjectDeserializer()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("OBJECT"), objectClassName, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyObjectDeserializer"));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_OBJECT_LIST_MAP() {
		Imports imports = new Imports();
		String objectClassName = "com.x.y.z.MyObject";
		Assert.assertEquals("Map<String, List<MyObject>>", ExchangeJavaWrapperGeneratorUtil.getClassNameForType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
		Assert.assertEquals(3, imports.size());
		Assert.assertTrue(imports.contains(objectClassName));
		Assert.assertTrue(imports.contains(Map.class));
		Assert.assertTrue(imports.contains(List.class));
	}

	@Test
	public void testGetNewJsonFieldDeserializerInstruction_NullType() {
		Imports imports = new Imports();
		Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeJavaWrapperGeneratorUtil.getNewJsonFieldDeserializerInstruction(null, null, imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
	}
	
	@Test 
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_NullSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals(null, ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, null, imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test 
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_BigDecimalSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("new BigDecimal(\"1.23\")", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BIGDECIMAL, "1.23", imports));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(BigDecimal.class));
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_LongSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("Long.valueOf(\"123\")", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "123", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_TimestampSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("Long.valueOf(\"123\")", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.TIMESTAMP, "123", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_TimestampNowSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("Long.valueOf(System.currentTimeMillis())", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.TIMESTAMP, "now()", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_StringSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("\"test\"", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.STRING, "test", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_IntegersSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("Integer.valueOf(1)", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, "1", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_BooleanSampleValue() {
		Imports imports = new Imports();
		Assert.assertEquals("Boolean.valueOf(true)", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BOOLEAN, "true", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetPrimitiveTypeParameterSampleValueDeclaration_NonPrimitiveType() {
		Imports imports = new Imports();
		Assert.assertEquals("\"[1, 3, 5]\"", ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.fromTypeName("INT_LIST"), "[1, 3, 5]", imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testGetExchangeConstantsInterfaceName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		Assert.assertEquals("com.x.y.z.TestExchangeConstants", 
							ExchangeJavaWrapperGeneratorUtil.getExchangeConstantsInterfaceName(exchangeDescriptor));
	}

	@Test
	public void testGetExchangeApiConstantsInterfaceName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
		apiDescriptor.setName("Spot");
		Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotConstants", 
							ExchangeJavaWrapperGeneratorUtil.getExchangeApiConstantsInterfaceName(exchangeDescriptor, apiDescriptor));
	}

	@Test
	public void testGetExchangePropertiesInterfaceName() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("TestExchange");
		exchangeDescriptor.setBasePackage("com.x.y.z");
		Assert.assertEquals("com.x.y.z.TestExchangeProperties", 
							ExchangeJavaWrapperGeneratorUtil.getExchangePropertiesInterfaceName(exchangeDescriptor));
	}
	
	@Test
	public void testGetHttpUrlVariableDeclaration() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setHttpUrl("https//myexchange.com/api");
		Assert.assertEquals("/**\n"
				+ " * Base REST API URL\n"
				+ " */\n"
				+ "public static final String HTTP_URL = \"https//myexchange.com/api\";", 
				ExchangeJavaWrapperGeneratorUtil.getHttpUrlVariableDeclaration(exchangeDescriptor));
	}
	
	@Test
	public void testGetWebsocketUrlVariableDeclaration() {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setWebsocketUrl("https//myexchange.com/ws");
		Assert.assertEquals("/**\n"
				+ " * Base websocket endpoint URL\n"
				+ " */\n"
				+ "public static final String WEBSOCKET_URL = \"https//myexchange.com/ws\";", 
				ExchangeJavaWrapperGeneratorUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor));
	}
}
