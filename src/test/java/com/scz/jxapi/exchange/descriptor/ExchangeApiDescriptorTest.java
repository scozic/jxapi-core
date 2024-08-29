package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory;
import com.scz.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketFactory;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketHookFactory;

/**
 * Unit test for {@link ExchangeApiDescriptor}
 */
public class ExchangeApiDescriptorTest {

    @Test
    public void testCreateApiDescriptor() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        Assert.assertNotNull(apiDescriptor);
    }

    @Test
    public void testSettersAndGetters() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("name");
        apiDescriptor.setDescription("description");
        apiDescriptor.setHttpRequestExecutorFactory(MockHttpRequestExecutorFactory.class.getName());
        apiDescriptor.setHttpRequestInterceptorFactory(MockHttpRequestInterceptorFactory.class.getName());
        apiDescriptor.setRestEndpoints(List.of(new RestEndpointDescriptor()));
        apiDescriptor.setWebsocketEndpoints(List.of(new WebsocketEndpointDescriptor()));
        apiDescriptor.setWebsocketFactory(MockWebsocketFactory.class.getName());
        apiDescriptor.setWebsocketHookFactory(MockWebsocketHookFactory.class.getName());
        apiDescriptor.setRateLimits(List.of(new RateLimitRule()));
        apiDescriptor.setWebsocketUrl("ws://localhost:8080");
        Assert.assertEquals("name", apiDescriptor.getName());
        Assert.assertEquals("description", apiDescriptor.getDescription());
        Assert.assertEquals(MockHttpRequestExecutorFactory.class.getName() , apiDescriptor.getHttpRequestExecutorFactory());
        Assert.assertEquals(MockHttpRequestInterceptorFactory.class.getName() , apiDescriptor.getHttpRequestInterceptorFactory());
        Assert.assertEquals(1, apiDescriptor.getRestEndpoints().size());
        Assert.assertEquals(1, apiDescriptor.getWebsocketEndpoints().size());
        Assert.assertEquals(1, apiDescriptor.getRateLimits().size());
        Assert.assertEquals(MockWebsocketFactory.class.getName(), apiDescriptor.getWebsocketFactory());
        Assert.assertEquals(MockWebsocketHookFactory.class.getName(), apiDescriptor.getWebsocketHookFactory());
        Assert.assertEquals("ws://localhost:8080", apiDescriptor.getWebsocketUrl());
    }

    @Test
    public void testToString() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("name");
        apiDescriptor.setDescription("description");
        apiDescriptor.setHttpRequestExecutorFactory(MockHttpRequestExecutorFactory.class.getName());
        apiDescriptor.setHttpRequestInterceptorFactory(MockHttpRequestInterceptorFactory.class.getName());
        apiDescriptor.setRestEndpoints(List.of(new RestEndpointDescriptor()));
        apiDescriptor.setWebsocketEndpoints(List.of(new WebsocketEndpointDescriptor()));
        apiDescriptor.setRateLimits(List.of(new RateLimitRule()));
        Assert.assertEquals(
        		"ExchangeApiDescriptor{\"name\":\"name\",\"description\":\"description\",\"restEndpoints\":[{\"queryParams\":false}],\"httpRequestInterceptorFactory\":\"com.scz.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory\",\"httpRequestExecutorFactory\":\"com.scz.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory\",\"websocketEndpoints\":[{}],\"rateLimits\":[{\"timeFrame\":0,\"maxRequestCount\":-1,\"maxTotalWeight\":-1}]}", 
        		apiDescriptor.toString());
    }
}
