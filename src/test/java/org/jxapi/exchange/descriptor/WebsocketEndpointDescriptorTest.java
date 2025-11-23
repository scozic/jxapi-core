package org.jxapi.exchange.descriptor;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketEndpointDescriptor}
 */
public class WebsocketEndpointDescriptorTest {

    @Test
    public void testCreateWebsocketEndpointDescriptor() {
        WebsocketEndpointDescriptor descriptor = new WebsocketEndpointDescriptor();
        Assert.assertNotNull(descriptor);
    }
    
    private void setTestValues(WebsocketEndpointDescriptor descriptor) {
      descriptor.setName("name");
        descriptor.setDescription("description");
        descriptor.setDocUrl("https://doc.myexchange.com/websocket/name");
        descriptor.setTopic("atopic");
        descriptor.setRequest(Field.builder()
               .type(Type.STRING)
               .name("myRequestField")
               .msgField("mf")
               .description("Test request")
               .sampleValue("foo")
               .build());
        descriptor.setMessage(Field.builder()
             .type(Type.STRING)
             .name("myMessageField")
             .msgField("mm")
             .description("Test message")
             .sampleValue("bar")
             .build());
        WebsocketTopicMatcherDescriptor websocketMessageTopicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
        websocketMessageTopicMatcherDescriptor.setFieldName("field1");
        websocketMessageTopicMatcherDescriptor.setFieldValue("value1");
        descriptor.setTopicMatcher(websocketMessageTopicMatcherDescriptor);
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketEndpointDescriptor descriptor = new WebsocketEndpointDescriptor();
        setTestValues(descriptor);
        Assert.assertEquals("name", descriptor.getName());
        Assert.assertEquals("description", descriptor.getDescription());
        Assert.assertEquals("https://doc.myexchange.com/websocket/name", descriptor.getDocUrl());
        Assert.assertEquals("atopic", descriptor.getTopic());
        WebsocketTopicMatcherDescriptor matcherDescriptor = descriptor.getTopicMatcher();
        Assert.assertNotNull(matcherDescriptor);
        Assert.assertEquals("field1", matcherDescriptor.getFieldName());
        Assert.assertEquals("value1", matcherDescriptor.getFieldValue());
        Assert.assertEquals("myRequestField", descriptor.getRequest().getName());
        Assert.assertEquals("STRING", descriptor.getRequest().getType().toString());
        Assert.assertEquals("mf", descriptor.getRequest().getMsgField());
        Assert.assertEquals("Test request", descriptor.getRequest().getDescription());
        Assert.assertEquals("foo", descriptor.getRequest().getSampleValue());
        Assert.assertEquals("myMessageField", descriptor.getMessage().getName());
        Assert.assertEquals("STRING", descriptor.getMessage().getType().toString());
        Assert.assertEquals("mm", descriptor.getMessage().getMsgField());
        Assert.assertEquals("Test message", descriptor.getMessage().getDescription());
        Assert.assertEquals("bar", descriptor.getMessage().getSampleValue());
    }

    @Test
    public void testToString() {
        WebsocketEndpointDescriptor descriptor = new WebsocketEndpointDescriptor();
        setTestValues(descriptor);
        Assert.assertEquals(
            "WebsocketEndpointDescriptor{\"name\":\"name\",\"topic\":\"atopic\",\"description\":\"description\",\"docUrl\":\"https://doc.myexchange.com/websocket/name\",\"request\":{\"name\":\"myRequestField\",\"description\":\"Test request\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"sampleValue\":\"foo\",\"msgField\":\"mf\"},\"message\":{\"name\":\"myMessageField\",\"description\":\"Test message\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"sampleValue\":\"bar\",\"msgField\":\"mm\"},\"topicMatcher\":{\"fieldName\":\"field1\",\"fieldValue\":\"value1\"}}", 
                  descriptor.toString());
    }
}
