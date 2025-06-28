package org.jxapi.exchange.descriptor;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Constant}
 */
public class ConstantTest {

    @Test
    public void testCreate() {
        Constant c = Constant.create("name", Type.LONG, "description", "12345678901");
        Assert.assertEquals("name", c.getName());
        Assert.assertEquals(Type.LONG, c.getType());
        Assert.assertEquals("description", c.getDescription());
        Assert.assertEquals("12345678901", c.getValue());
    }

    @Test
    public void testSettersAndGetters() {
        Constant c = new Constant();
        Assert.assertEquals(Type.STRING, c.getType());
        c.setName("name");
        c.setType(Type.INT);
        c.setDescription("description");
        c.setValue(456);
        Assert.assertEquals("name", c.getName());
        Assert.assertEquals(Type.INT, c.getType());
        Assert.assertEquals("description", c.getDescription());
        Assert.assertEquals(456, c.getValue());
    }

    @Test
    public void testToString() {
        Constant c = Constant.create("name", Type.STRING, "description", "value");
        Assert.assertEquals("Constant{\"name\":\"name\",\"description\":\"description\",\"value\":\"value\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false}}",
                            c.toString());
    }
}
