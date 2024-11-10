package com.scz.jxapi.util;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link TestJXApiProperties}
 */
public class TestJXApiPropertiesTest {

	private static final String TEST_PROP = "myApp.myProp";
	
	@Before
	public void setUp() {
		clearProperties();
	}
	
	@After
	public void tearDown() {
		clearProperties();
	}
	
	private void clearProperties() {
		TestJXApiProperties.get().clear();
		System.getProperties().remove(TEST_PROP);
	}
	
	@Test
	public void testGetDefaultPropertiesValues() {
		Assert.assertEquals(TestJXApiProperties.DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION, TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION);
		Assert.assertEquals(TestJXApiProperties.DEMO_WS_DEFAULT_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION, TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);
	}
	
	@Test
	public void testGetPropertyFromSysProp() {
		System.getProperties().put(TEST_PROP, "testValue");
		Assert.assertEquals("testValue", TestJXApiProperties.getProperty(TEST_PROP));
	}
	
	@Test
	public void testGetPropertyFromTestJxapiProperties() {
		TestJXApiProperties.get().put(TEST_PROP, "testValue");
		Assert.assertEquals("testValue", TestJXApiProperties.getProperty(TEST_PROP));
	}
	
	@Test
	public void testFilterProperties() {
		Properties props = TestJXApiProperties.get();
		props.put("aProp", "aVal");
		props.put(TEST_PROP, "testValue");
		Properties filteredProp = TestJXApiProperties.filterProperties("myApp", true);
		Assert.assertEquals(1, filteredProp.size());
		Assert.assertEquals("testValue", filteredProp.get("myProp"));
	}
	
}
