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
	public void testGetClassNameForParameterType_INT_MAP() {
		Set<String> imports = new HashSet<>();
		Assert.assertEquals("Map<String, Integer>", ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(Type.fromTypeName("INT_MAP"), imports, null));
		Assert.assertEquals(1, imports.size());
		Assert.assertTrue(imports.contains(Map.class.getName()));
	}


}
