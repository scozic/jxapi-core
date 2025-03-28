package org.jxapi.exchange.descriptor;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketMessageTopicMatcherFieldDescriptor}
 */
public class WebsocketMessageTopicMatcherFieldDescriptorTest {

    @Test
    public void testCreateWebsocketMessageTopicMatcherFieldDescriptor() {
        WebsocketMessageTopicMatcherFieldDescriptor descriptor = new WebsocketMessageTopicMatcherFieldDescriptor();
        Assert.assertNotNull(descriptor);
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketMessageTopicMatcherFieldDescriptor descriptor = new WebsocketMessageTopicMatcherFieldDescriptor();
        descriptor.setName("name");
        descriptor.setValue("value");
        Assert.assertEquals("name", descriptor.getName());
        Assert.assertEquals("value", descriptor.getValue());
    }

    @Test
    public void testToString() {
        WebsocketMessageTopicMatcherFieldDescriptor descriptor = new WebsocketMessageTopicMatcherFieldDescriptor();
        descriptor.setName("name");
        descriptor.setValue("value");
        Assert.assertEquals("WebsocketMessageTopicMatcherFieldDescriptor{\"name\":\"name\",\"value\":\"value\"}", 
                  descriptor.toString());
    }
}
