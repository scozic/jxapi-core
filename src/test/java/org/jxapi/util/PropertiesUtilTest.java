package org.jxapi.util;

import java.math.BigDecimal;
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
  public void testGetString() {
    Properties props = new Properties();
    props.put("key1", "value1");
    props.put("key2", "value2");
    props.put("key3", "value3");
    Assert.assertEquals("value1", PropertiesUtil.getString(props, "key1", "default"));
    Assert.assertEquals("default", PropertiesUtil.getString(props, "key4", "default"));
    
    DefaultConfigProperty prop = new DefaultConfigProperty();
    prop.setName("key1");
    Assert.assertEquals("value1", PropertiesUtil.getString(props, prop));
    Assert.assertNull(PropertiesUtil.getString(new Properties(), prop));
    prop.setDefaultValue("default");
    Assert.assertEquals("default", PropertiesUtil.getString(new Properties(), prop));
  }

  @Test
  public void testGetInt() {
    Properties props = new Properties();
    props.put("key1", "1");
    props.put("key2", "2");
    props.put("key3", 3);
    Assert.assertEquals(Integer.valueOf(1), PropertiesUtil.getInt(props, "key1", 0));
    Assert.assertEquals(Integer.valueOf(3), PropertiesUtil.getInt(props, "key3", 0));
    Assert.assertEquals(Integer.valueOf(0), PropertiesUtil.getInt(props, "key4", 0));
    
    DefaultConfigProperty prop = new DefaultConfigProperty();
    prop.setName("key1");
    Assert.assertEquals(Integer.valueOf(1), PropertiesUtil.getInt(props, prop));
    Assert.assertNull(PropertiesUtil.getInt(new Properties(), prop));
    prop.setDefaultValue(0);
    Assert.assertEquals(Integer.valueOf(0), PropertiesUtil.getInt(new Properties(), prop));
  }

  @Test
  public void testGetLong() {
    Properties props = new Properties();
    props.put("key1", "1");
    props.put("key2", "2");
    props.put("key3", 3);
    props.put("key4", "now()");
    Assert.assertEquals(Long.valueOf(1), PropertiesUtil.getLong(props, "key1", 0L));
    Assert.assertEquals(Long.valueOf(3), PropertiesUtil.getLong(props, "key3", 0L));
    Assert.assertTrue(System.currentTimeMillis() - PropertiesUtil.getLong(props, "key4", 0L) < 1000);
    Assert.assertEquals(Long.valueOf(0), PropertiesUtil.getLong(props, "key5", 0L));
    
    DefaultConfigProperty prop = new DefaultConfigProperty();
    prop.setName("key1");
    Assert.assertEquals(Long.valueOf(1), PropertiesUtil.getLong(props, prop));
    Assert.assertNull(PropertiesUtil.getLong(new Properties(), prop));
    prop.setDefaultValue(0L);
    Assert.assertEquals(Long.valueOf(0), PropertiesUtil.getLong(new Properties(), prop));
  }

  @Test
  public void testGetBigDecimal() {
    Properties props = new Properties();
    props.put("key1", "1");
    props.put("key2", "2");
    props.put("key3", 3.5);
    Assert.assertEquals(new BigDecimal("1"), PropertiesUtil.getBigDecimal(props, "key1", new java.math.BigDecimal("0")));
    Assert.assertEquals(new BigDecimal("3.5"), PropertiesUtil.getBigDecimal(props, "key3", null));
    Assert.assertEquals(new BigDecimal("12.5"), PropertiesUtil.getBigDecimal(props, "key4", new java.math.BigDecimal("12.5")));
    
    DefaultConfigProperty prop = new DefaultConfigProperty();
    prop.setName("key1");
    Assert.assertEquals(new BigDecimal("1"), PropertiesUtil.getBigDecimal(props, prop));
    Assert.assertNull(PropertiesUtil.getBigDecimal(new Properties(), prop));
    prop.setDefaultValue(new java.math.BigDecimal("0"));
    Assert.assertEquals(new BigDecimal("0"), PropertiesUtil.getBigDecimal(new Properties(), prop));
  }

  @Test
  public void testGetBoolean() {
    Properties props = new Properties();
    props.put("key1", "true");
    props.put("key2", "false");
    props.put("key3", true);
    Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBoolean(props, "key1", false));
    Assert.assertEquals(Boolean.valueOf(false), PropertiesUtil.getBoolean(props, "key2", null));
    Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBoolean(props, "key3", false));
    Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBoolean(props, "key4", true));
    
    DefaultConfigProperty prop = new DefaultConfigProperty();
    prop.setName("key1");
    Assert.assertEquals(Boolean.valueOf(true), PropertiesUtil.getBoolean(props, prop));
    Assert.assertNull(PropertiesUtil.getBoolean(new Properties(), prop));
    prop.setDefaultValue(false);
    Assert.assertEquals(Boolean.valueOf(false), PropertiesUtil.getBoolean(new Properties(), prop));
  }
}
