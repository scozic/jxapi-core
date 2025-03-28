package org.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Field}
 */
public class FieldTest {
    @Test
    public void testCreateField() {
        Field field = new Field();
        Assert.assertNotNull(field);
    }

    @Test
    public void testGettersAndSetters() {
        Field field = new Field();
        field.setName("name");
        field.setType("OBJECT_MAP");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        field.setObjectName("MyObject");
        field.setProperties(List.of());
        field.setImplementedInterfaces(List.of("com.x.y.MyInterface"));
        field.setSampleMapKeyValue(List.of("key1", "value1"));
        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT_MAP"), field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals("MyObject", field.getObjectName());
        Assert.assertEquals(List.of(), field.getProperties());
        Assert.assertEquals(List.of("com.x.y.MyInterface"), field.getImplementedInterfaces());
        Assert.assertEquals(List.of("key1", "value1"), field.getSampleMapKeyValue());
    }

    @Test
    public void testDeepCloneObjectField() {
        Field field = new Field();
        field.setName("name");
        field.setType(Type.fromTypeName("OBJECT_MAP"));
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        field.setObjectName("MyObject");
        field.setProperties(List.of());
        field.setImplementedInterfaces(List.of("com.x.y.MyInterface"));
        field.setSampleMapKeyValue(List.of("key1", "value1"));

        field = field.deepClone();

        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT_MAP") , field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals("MyObject", field.getObjectName());
        Assert.assertEquals(List.of(), field.getProperties());
        Assert.assertEquals(List.of("com.x.y.MyInterface"), field.getImplementedInterfaces());
        Assert.assertEquals(List.of("key1", "value1"), field.getSampleMapKeyValue());
    }

    @Test
    public void testDeepCloneSimpleField() {
        Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");

        field = field.deepClone();

        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.STRING , field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
    }

    @Test
    public void testToString() {
        Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        Assert.assertEquals("Field{\"description\":\"description\",\"msgField\":\"f\",\"name\":\"name\",\"sampleValue\":\"sampleValue\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false}}", field.toString());
    }
    
    @Test
    public void testHashCodeIsToStringHashCode() {
      Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        Assert.assertEquals(field.toString().hashCode(), field.hashCode());
    }
    
    @Test
    public void testEquals_NotEqualsOtherNull() {
      Field field = new Field();
    // Remark: Sonar issue about usage of equals is not relevant, assertNotEquals
    // would skip call to Field.equals() using null as expected value
      Assert.assertFalse(field.equals(null));
    }
    
    @Test
    public void testEquals_NotEqualsOtherNotField() {
      Field field = new Field();
      Assert.assertNotEquals(new Object(), field);
    }
    
    @Test
    public void testEquals_NotEqualsOtherDifferentField() {
        Field f1 = new Field();
        f1.setName("name");
        f1.setType("STRING");
        f1.setDescription("description");
        f1.setSampleValue("sampleValue");
        f1.setMsgField("f");
        
        Field f2 = new Field();
        f2.setName("bar");
        f2.setType("STRING");
        f2.setDescription("description");
        f2.setSampleValue("sampleValue");
        f2.setMsgField("f");
      Assert.assertNotEquals(f1, f2);
    }
    
    @Test
    public void testEquals_SameField() {
        Field f1 = new Field();
        f1.setName("name");
        f1.setType("STRING");
        f1.setDescription("description");
        f1.setSampleValue("sampleValue");
        f1.setMsgField("f");
        
        Field f2 = new Field();
        f2.setName("name");
        f2.setType("STRING");
        f2.setDescription("description");
        f2.setSampleValue("sampleValue");
        f2.setMsgField("f");
      Assert.assertEquals(f1, f2);
    }
    
    @Test
    public void testBuilder() {
      FieldBuilder fb = Field.builder();
      Assert.assertNotNull(fb);
    }
}
