package org.jxapi.util;

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

	@Test
	public void testGetStringProperty() {
		Properties props = new Properties();
		props.put("key1", "value1");
		props.put("key2", "value2");
		props.put("key3", "value3");
		Assert.assertEquals("value1", PropertiesUtil.getStringProperty(props, "key1", "default"));
		Assert.assertEquals("default", PropertiesUtil.getStringProperty(props, "key4", "default"));
	}

	@Test
	public void testGetIntProperty() {
		Properties props = new Properties();
		props.put("key1", "1");
		props.put("key2", "2");
		props.put("key3", 3);
		Assert.assertEquals(Integer.valueOf(1), PropertiesUtil.getIntProperty(props, "key1", 0));
		Assert.assertEquals(Integer.valueOf(3), PropertiesUtil.getIntProperty(props, "key3", 0));
		Assert.assertEquals(Integer.valueOf(0), PropertiesUtil.getIntProperty(props, "key4", 0));
	}

	@Test
	public void testGetLongProperty() {
		Properties props = new Properties();
		props.put("key1", "1");
		props.put("key2", "2");
		props.put("key3", 3);
		props.put("key4", "now()");
		Assert.assertEquals(Long.valueOf(1), PropertiesUtil.getLongProperty(props, "key1", 0L));
		Assert.assertEquals(Long.valueOf(3), PropertiesUtil.getLongProperty(props, "key3", 0L));
		Assert.assertTrue(System.currentTimeMillis() - PropertiesUtil.getLongProperty(props, "key4", 0L) < 1000);
		Assert.assertEquals(Long.valueOf(0), PropertiesUtil.getLongProperty(props, "key5", 0L));
	}

	@Test
	public void testGetBigDecimalProperty() {
		Properties props = new Properties();
		props.put("key1", "1");
		props.put("key2", "2");
		props.put("key3", 3.5);
		Assert.assertEquals(new java.math.BigDecimal("1"), PropertiesUtil.getBigDecimalProperty(props, "key1", new java.math.BigDecimal("0")));
		Assert.assertEquals(new java.math.BigDecimal("3.5"), PropertiesUtil.getBigDecimalProperty(props, "key3", null));
		Assert.assertEquals(new java.math.BigDecimal("12.5"), PropertiesUtil.getBigDecimalProperty(props, "key4", new java.math.BigDecimal("12.5")));
	}

	@Test
	public void testGetBooleanProperty() {
		Properties props = new Properties();
		props.put("key1", "true");
		props.put("key2", "false");
		props.put("key3", true);
		Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBooleanProperty(props, "key1", false));
		Assert.assertEquals(Boolean.valueOf(false), PropertiesUtil.getBooleanProperty(props, "key2", null));
		Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBooleanProperty(props, "key3", false));
		Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBooleanProperty(props, "key4", true));
	}
}
