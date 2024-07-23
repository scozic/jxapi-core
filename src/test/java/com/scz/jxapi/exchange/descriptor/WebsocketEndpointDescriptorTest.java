package com.scz.jxapi.exchange.descriptor;

import java.util.Arrays;

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
        descriptor.setUrl("url");
        descriptor.setTopicParametersListSeparator("|");
        descriptor.setTopic("atopic");
        descriptor.setRequest(Field.create("STRING", "myRequestField", "mf", "Test request", "foo"));
        descriptor.setMessage(Field.create("STRING", "myMessageField", "mm", "Test message", "bar"));
        WebsocketMessageTopicMatcherFieldDescriptor websocketMessageTopicMatcherDescriptor = new WebsocketMessageTopicMatcherFieldDescriptor();
        websocketMessageTopicMatcherDescriptor.setName("field1");
        websocketMessageTopicMatcherDescriptor.setValue("value1");
        descriptor.setMessageTopicMatcherFields(Arrays.asList(websocketMessageTopicMatcherDescriptor));
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketEndpointDescriptor descriptor = new WebsocketEndpointDescriptor();
        setTestValues(descriptor);
        Assert.assertEquals("name", descriptor.getName());
        Assert.assertEquals("description", descriptor.getDescription());
        Assert.assertEquals("url", descriptor.getUrl());
        Assert.assertEquals("atopic", descriptor.getTopic());
        Assert.assertEquals("|", descriptor.getTopicParametersListSeparator());
        Assert.assertEquals(1, descriptor.getMessageTopicMatcherFields().size());
        Assert.assertEquals("field1", descriptor.getMessageTopicMatcherFields().get(0).getName());
        Assert.assertEquals("value1", descriptor.getMessageTopicMatcherFields().get(0).getValue());
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
        Assert.assertEquals("WebsocketEndpointDescriptor{\"name\":\"name\",\"topic\":\"atopic\",\"description\":\"description\",\"url\":\"url\","
        		+ "\"request\":{\"name\":\"myRequestField\",\"description\":\"Test request\",\"type\":{\"canonicalType\":\"STRING\""
        		+ ",\"object\":false},\"sampleValue\":\"foo\",\"msgField\":\"mf\"},"
        		+ "\"message\":{\"name\":\"myMessageField\",\"description\":\"Test message\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"sampleValue\":\"bar\",\"msgField\":\"mm\"},"
        		+ "\"topicParametersListSeparator\":\"|\","
        		+ "\"messageTopicMatcherFields\":[{\"value\":\"value1\",\"name\":\"field1\"}]}", 
        					descriptor.toString());
    }
}
