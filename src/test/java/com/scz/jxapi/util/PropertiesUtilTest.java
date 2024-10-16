package com.scz.jxapi.util;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link PropertiesUtil}
 */
public class PropertiesUtilTest {

	@Test
	public void testFilterProps_NullProps() {
		Assert.assertNull(PropertiesUtil.filterProperties(null, "namespace", true));
	}

	@Test
	public void testFilterProps_EmptyProps() {
		Assert.assertTrue(PropertiesUtil.filterProperties(new Properties(), "namespace", true).isEmpty());
	}

	@Test
	public void testFilterProps_WithNamespace() {
		Properties props = new Properties();
		props.put("namespace.key1", "value1");
		props.put("namespace.key2", "value2");
		props.put("namespace.key3", "value3");
		props.put(1, "value4");
		Properties expected = new Properties();
		expected.put("key1", "value1");
		expected.put("key2", "value2");
		expected.put("key3", "value3");
		Assert.assertEquals(expected, PropertiesUtil.filterProperties(props, "namespace", true));
	}

	@Test
	public void testFilterProps_WithNamespaceNoRemove() {
		Properties props = new Properties();
		props.put("namespace.key1", "value1");
		props.put("namespace.key2", "value2");
		props.put("namespace.key3", "value3");
		props.put(1, "value4");
		Properties expected = new Properties();
		expected.put("namespace.key1", "value1");
		expected.put("namespace.key2", "value2");
		expected.put("namespace.key3", "value3");
		Assert.assertEquals(expected, PropertiesUtil.filterProperties(props, "namespace", false));
	}
}
