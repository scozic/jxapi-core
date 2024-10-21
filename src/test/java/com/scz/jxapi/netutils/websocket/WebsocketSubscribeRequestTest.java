package com.scz.jxapi.netutils.websocket;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;

/**
 * Unit test for {@link WebsocketSubscribeRequest}
 */
public class WebsocketSubscribeRequestTest {

    @Test
    public void testCreate() {
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create("endpoint", "request", "topic", WebsocketMessageTopicMatcherFactory.createFactory());
        Assert.assertNotNull(request);
        Assert.assertEquals("endpoint", request.getEnpoint());
        Assert.assertEquals("request", request.getRequest());
        Assert.assertEquals("topic", request.getTopic());
        Assert.assertNotNull(request.getMessageTopicMatcherFactory());
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketSubscribeRequest request = new WebsocketSubscribeRequest();
        request.setEnpoint("endpoint");
        request.setRequest("request");
        request.setTopic("topic");
        request.setMessageTopicMatcherFactory(WebsocketMessageTopicMatcherFactory.createFactory());
        Assert.assertEquals("endpoint", request.getEnpoint());
        Assert.assertEquals("request", request.getRequest());
        Assert.assertEquals("topic", request.getTopic());
        Assert.assertNotNull(request.getMessageTopicMatcherFactory());
    }

    @Test
    public void testToString() {
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create("endpoint", "request", "topic", WebsocketMessageTopicMatcherFactory.createFactory());
        Assert.assertEquals("WebsocketSubscribeRequest{\"enpoint\":\"endpoint\",\"messageTopicMatcherFactory\":{},\"request\":\"request\",\"topic\":\"topic\"}", 
        					request.toString());
    }
}
