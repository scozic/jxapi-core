package org.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ConfigPropertyDescriptor}
 */
public class ConfigPropertyDescriptorTest {

  @Test
  public void testCreate() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProperty", Type.STRING, "My property", "myValue");
      Assert.assertEquals("myProperty", property.getName());
      Assert.assertEquals("My property", property.getDescription());
      Assert.assertEquals(Type.STRING, property.getType());
      Assert.assertEquals("myValue", property.getDefaultValue());
  }
  
  @Test
  public void testCreateGroup() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.create("subProperty1", Type.STRING, "Sub property 1", "value1");
    ConfigPropertyDescriptor p2 = ConfigPropertyDescriptor.create("subProperty2", Type.INT, "Sub property 2", 42);
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.createGroup("myGroup", "My group", List.of(p1, p2));
    Assert.assertEquals("myGroup", property.getName());
    Assert.assertEquals("My group", property.getDescription());
    
    Assert.assertEquals(Type.OBJECT, property.getType());
    Assert.assertNull(property.getDefaultValue());
    Assert.assertNotNull(property.getProperties());
    Assert.assertEquals(2, property.getProperties().size());
    Assert.assertEquals(p1, property.getProperties().get(0));
    Assert.assertEquals(p2, property.getProperties().get(1));
    
  }
  
  @Test
  public void testCreateEmptyGroup() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.createGroup("myGroup", "My group", null);
    Assert.assertEquals("myGroup", property.getName());
    Assert.assertEquals("My group", property.getDescription());
    Assert.assertEquals(Type.OBJECT, property.getType());
    Assert.assertNull(property.getDefaultValue());
    Assert.assertNotNull(property.getProperties());
    Assert.assertEquals(0, property.getProperties().size());
  }
  
  @Test
  public void testIsGroup() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProperty", Type.STRING, "My property",
        "myValue");
    Assert.assertFalse(property.isGroup());

    ConfigPropertyDescriptor group = ConfigPropertyDescriptor.createGroup("myGroup", "My group", List.of(property));
    Assert.assertTrue(group.isGroup());
  }

  @Test
  public void testSettersAndGetters() {
    ConfigPropertyDescriptor property = new ConfigPropertyDescriptor();
      property.setName("myProperty");
      property.setDescription("My property");
      property.setType(Type.STRING.toString());
      property.setDefaultValue("myValue");

      Assert.assertEquals("myProperty", property.getName());
      Assert.assertEquals("My property", property.getDescription());
      Assert.assertEquals(Type.STRING, property.getType());
      Assert.assertEquals("myValue", property.getDefaultValue());
  }

  @Test
  public void testToString() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProperty", Type.STRING, "My property", "myValue");
      Assert.assertEquals("ConfigPropertyDescriptor{\"name\":\"myProperty\",\"description\":\"My property\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"defaultValue\":\"myValue\",\"group\":false}", property.toString());
  }

}
