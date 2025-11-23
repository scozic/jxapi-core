package org.jxapi.netutils.websocket;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.netutils.websocket.multiplexing.WSMTMFUtil;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;

/**
 * Unit test for {@link WebsocketSubscribeRequest}
 */
public class WebsocketSubscribeRequestTest {

    @Test
    public void testCreate() {
        WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.and(List.of());
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create("request", "topic", topicMatcherFactory);
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
        WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.and(List.of());
        request.setMessageTopicMatcherFactory(topicMatcherFactory);
        Assert.assertEquals("endpoint", request.getEnpoint());
        Assert.assertEquals("request", request.getRequest());
        Assert.assertEquals("topic", request.getTopic());
        Assert.assertSame(topicMatcherFactory, request.getMessageTopicMatcherFactory());
    }

    @Test
    public void testToString() {
        WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.and(List.of());
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create("request", "topic", topicMatcherFactory);
        Assert.assertEquals("WebsocketSubscribeRequest{\"request\":\"request\",\"messageTopicMatcherFactory\":{},\"topic\":\"topic\"}", 
                  request.toString());
    }
}
