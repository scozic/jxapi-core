package org.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.rest.ratelimits.RateLimitRule;

/**
 * Unit test for {@link ExchangeDescriptor}
 */
public class ExchangeDescriptorTest {

    @Test
    public void testCreateExchangeDescriptor() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        Assert.assertNotNull(exchangeDescriptor);
    }

    @Test
    public void testSettersAndGetters() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("name");
        exchangeDescriptor.setDescription("description");
        exchangeDescriptor.setBasePackage("com.x.y");
        exchangeDescriptor.setApis(List.of(new ExchangeApiDescriptor()));
        exchangeDescriptor.setRateLimits(List.of(new RateLimitRule()));
        exchangeDescriptor.setDocUrl("https://doc.myexchange.com");
        exchangeDescriptor.setConstants(List.of(new Constant()));
        exchangeDescriptor.setProperties(List.of(new DefaultConfigProperty()));
        exchangeDescriptor.setHttpRequestInterceptorFactory("com.x.y.net.MyHttpRequestInterceptorFactory");
        exchangeDescriptor.setHttpRequestExecutorFactory("com.x.y.net.MyHttpRequestExecutorFactory");
        exchangeDescriptor.setHttpRequestTimeout(2000L);
        exchangeDescriptor.setWebsocketFactory("com.x.y.net.MyWebsocketFactory");
        exchangeDescriptor.setWebsocketHookFactory("com.x.y.net.MyWebsocketHookFactory");
        exchangeDescriptor.setVersion("1.0.0");
        exchangeDescriptor.setJxapi("0.15.0");
        exchangeDescriptor.setAfterInitHookFactory("com.x.y.MyExchangeHookFactory");
        Assert.assertEquals("name", exchangeDescriptor.getId());
        Assert.assertEquals("description", exchangeDescriptor.getDescription());
        Assert.assertEquals("com.x.y", exchangeDescriptor.getBasePackage());
        Assert.assertEquals("https://doc.myexchange.com", exchangeDescriptor.getDocUrl());
        Assert.assertEquals(1, exchangeDescriptor.getApis().size());
        Assert.assertEquals(1, exchangeDescriptor.getRateLimits().size());
        Assert.assertEquals(1, exchangeDescriptor.getConstants().size());
        Assert.assertEquals(1, exchangeDescriptor.getProperties().size());
        Assert.assertEquals("com.x.y.net.MyHttpRequestInterceptorFactory", exchangeDescriptor.getHttpRequestInterceptorFactory());
        Assert.assertEquals("com.x.y.net.MyHttpRequestExecutorFactory", exchangeDescriptor.getHttpRequestExecutorFactory());
        Assert.assertEquals(2000L, exchangeDescriptor.getHttpRequestTimeout());
        Assert.assertEquals("com.x.y.net.MyWebsocketFactory", exchangeDescriptor.getWebsocketFactory());
        Assert.assertEquals("com.x.y.net.MyWebsocketHookFactory", exchangeDescriptor.getWebsocketHookFactory());
        Assert.assertEquals("1.0.0", exchangeDescriptor.getVersion());
        Assert.assertEquals("0.15.0", exchangeDescriptor.getJxapi());
        Assert.assertEquals("com.x.y.MyExchangeHookFactory", exchangeDescriptor.getAfterInitHookFactory());
    }

    @Test
    public void testToString() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("name");
        exchangeDescriptor.setDescription("description");
        exchangeDescriptor.setBasePackage("basePackage");
        exchangeDescriptor.setApis(List.of(new ExchangeApiDescriptor()));
        exchangeDescriptor.setRateLimits(List.of(new RateLimitRule()));
        exchangeDescriptor.setVersion("1.0.0");
        exchangeDescriptor.setJxapi("0.15.0");
        Assert.assertEquals("ExchangeDescriptor{\"apis\":[{\"httpRequestTimeout\":-1}],\"basePackage\":\"basePackage\",\"description\":\"description\",\"httpRequestTimeout\":-1,\"id\":\"name\",\"jxapi\":\"0.15.0\",\"rateLimits\":[{\"granularity\":10,\"maxRequestCount\":-1,\"maxTotalWeight\":-1,\"timeFrame\":0}],\"version\":\"1.0.0\"}", exchangeDescriptor.toString());
    }
}
