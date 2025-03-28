package org.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketMessageTopicMatcherDescriptor}
 */
public class WebsocketMessageTopicMatcherDescriptorTest {

    @Test
    public void testCreateWebsocketMessageTopicMatcherDescriptor() {
        WebsocketMessageTopicMatcherDescriptor descriptor = new WebsocketMessageTopicMatcherDescriptor();
        Assert.assertNotNull(descriptor);
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketMessageTopicMatcherDescriptor descriptor = new WebsocketMessageTopicMatcherDescriptor();
        WebsocketMessageTopicMatcherFieldDescriptor field = new WebsocketMessageTopicMatcherFieldDescriptor();
        field.setName("name");
        field.setValue("value");
        descriptor.setFields(List.of(field));
        Assert.assertEquals(1, descriptor.getFields().size());
        Assert.assertEquals("name", descriptor.getFields().get(0).getName());
        Assert.assertEquals("value", descriptor.getFields().get(0).getValue());
    }

    @Test
    public void testToString() {
        WebsocketMessageTopicMatcherDescriptor descriptor = new WebsocketMessageTopicMatcherDescriptor();
        WebsocketMessageTopicMatcherFieldDescriptor field = new WebsocketMessageTopicMatcherFieldDescriptor();
        field.setName("name");
        field.setValue("value");
        descriptor.setFields(List.of(field));
        Assert.assertEquals("WebsocketMessageTopicMatcherDescriptor{\"fields\":[{\"name\":\"name\",\"value\":\"value\"}]}", 
                  descriptor.toString());
    }
}
