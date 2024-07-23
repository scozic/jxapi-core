package com.scz.jxapi.generator.java;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link PojoField}
 */
public class PojoFieldTest {
    
    @Test
    public void testCreatePojoField() {
        PojoField pojoField = new PojoField();
        Assert.assertNotNull(pojoField);
    }

    private void setDefaultValues(PojoField pojoField) {
        pojoField.setType("type");
        pojoField.setName("name");
        pojoField.setDescription("description");
        pojoField.setMsgField("msgField");
    }

    @Test
    public void testGettersAndSetters() {
        PojoField pojoField = new PojoField();
        setDefaultValues(pojoField);
        Assert.assertEquals("type", pojoField.getType());
        Assert.assertEquals("name", pojoField.getName());
        Assert.assertEquals("description", pojoField.getDescription());
        Assert.assertEquals("msgField", pojoField.getMsgField());
    }

    @Test
    public void testToString() {
        PojoField pojoField = new PojoField();
        setDefaultValues(pojoField);
        Assert.assertEquals("PojoField{\"type\":\"type\",\"name\":\"name\",\"description\":\"description\",\"msgField\":\"msgField\"}", 
        					pojoField.toString());
    }

    @Test
    public void testCreate() {
        PojoField pojoField = PojoField.create("type", "name");
        Assert.assertEquals("type", pojoField.getType());
        Assert.assertEquals("name", pojoField.getName());
        Assert.assertNull(pojoField.getDescription());
        Assert.assertNull(pojoField.getMsgField());
    }

    @Test
    public void testCreateWithMsgFieldAndDescription() {
        PojoField pojoField = PojoField.create("type", "name", "msgField", "description");
        Assert.assertEquals("type", pojoField.getType());
        Assert.assertEquals("name", pojoField.getName());
        Assert.assertEquals("description", pojoField.getDescription());
        Assert.assertEquals("msgField", pojoField.getMsgField());
    }
}
