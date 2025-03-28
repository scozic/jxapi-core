package org.jxapi.netutils.websocket;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;

/**
 * Unit test for {@link WebsocketSubscribeRequest}
 */
public class WebsocketSubscribeRequestTest {

    @Test
    public void testCreate() {
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create("request", "topic", WebsocketMessageTopicMatcherFactory.create());
        Assert.assertNotNull(request);
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
        request.setMessageTopicMatcherFactory(WebsocketMessageTopicMatcherFactory.create());
        Assert.assertEquals("endpoint", request.getEnpoint());
        Assert.assertEquals("request", request.getRequest());
        Assert.assertEquals("topic", request.getTopic());
        Assert.assertNotNull(request.getMessageTopicMatcherFactory());
    }

    @Test
    public void testToString() {
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create("request", "topic", WebsocketMessageTopicMatcherFactory.create());
        Assert.assertEquals("WebsocketSubscribeRequest{\"messageTopicMatcherFactory\":{},\"request\":\"request\",\"topic\":\"topic\"}", 
                  request.toString());
    }
}
