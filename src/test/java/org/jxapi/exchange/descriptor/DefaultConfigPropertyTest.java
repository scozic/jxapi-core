package org.jxapi.exchange.descriptor;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;

/**
 * Unit test for {@link DefaultConfigProperty}
 */
public class DefaultConfigPropertyTest {

    @Test
    public void testCreate() {
        ConfigProperty property = DefaultConfigProperty.create("myProperty", Type.STRING, "My property", "myValue");
        Assert.assertEquals("myProperty", property.getName());
        Assert.assertEquals("My property", property.getDescription());
        Assert.assertEquals(Type.STRING, property.getType());
        Assert.assertEquals("myValue", property.getDefaultValue());
    }

    @Test
    public void testSettersAndGetters() {
        DefaultConfigProperty property = new DefaultConfigProperty();
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
        ConfigProperty property = DefaultConfigProperty.create("myProperty", Type.STRING, "My property", "myValue");
        Assert.assertEquals("DefaultConfigProperty{\"name\":\"myProperty\",\"description\":\"My property\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"defaultValue\":\"myValue\"}", property.toString());
    }


}
