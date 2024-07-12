package com.scz.jxapi.exchange.descriptor;

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
        field.setParameters(List.of());
        field.setImplementedInterfaces(List.of("com.x.y.MyInterface"));
        field.setSampleMapKeyValue(List.of("key1", "value1"));
        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT_MAP"), field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals("MyObject", field.getObjectName());
        Assert.assertEquals(List.of(), field.getParameters());
        Assert.assertEquals(List.of("com.x.y.MyInterface"), field.getImplementedInterfaces());
        Assert.assertEquals(List.of("key1", "value1"), field.getSampleMapKeyValue());
    }

    @Test 
    public void testCreate() {
        Field field = Field.create("STRING", "myName", "m", "myDescription", "mySampleValue");
        Assert.assertEquals("myName", field.getName());
        Assert.assertEquals(Type.STRING, field.getType());
        Assert.assertEquals("myDescription", field.getDescription());
        Assert.assertEquals("mySampleValue", field.getSampleValue());
        Assert.assertEquals("m", field.getMsgField());
    }

    @Test
    public void testCreateObject() {
        Field field = Field.createObject("OBJECT", "myName", "m", "myDescription", List.of());
        Assert.assertEquals("myName", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT"), field.getType());
        Assert.assertEquals("myDescription", field.getDescription());
        Assert.assertEquals("m", field.getMsgField());
        Assert.assertEquals(List.of(), field.getParameters());
    }

    @Test
    public void testCloneObjectField() {
        Field field = new Field();
        field.setName("name");
        field.setType(Type.fromTypeName("OBJECT_MAP"));
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        field.setObjectName("MyObject");
        field.setParameters(List.of());
        field.setImplementedInterfaces(List.of("com.x.y.MyInterface"));
        field.setSampleMapKeyValue(List.of("key1", "value1"));

        field = field.clone();

        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT_MAP") , field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals("MyObject", field.getObjectName());
        Assert.assertEquals(List.of(), field.getParameters());
        Assert.assertEquals(List.of("com.x.y.MyInterface"), field.getImplementedInterfaces());
        Assert.assertEquals(List.of("key1", "value1"), field.getSampleMapKeyValue());
    }

    @Test
    public void cloneSimpleField() {
        Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");

        field = field.clone();

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
        Assert.assertEquals("Field{\"name\":\"name\",\"description\":\"description\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"sampleValue\":\"sampleValue\",\"msgField\":\"f\"}", field.toString());
    }
}
