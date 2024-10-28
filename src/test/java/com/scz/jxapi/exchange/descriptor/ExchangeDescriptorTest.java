package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;

/**
 * Unit test for {@link ExchangeDescriptor}
 */
public class ExchangeDescriptorTest {

    @Test
    public void testCreateExchangeDescriptor() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        assert exchangeDescriptor != null;
    }

    @Test
    public void testSettersAndGetters() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("name");
        exchangeDescriptor.setDescription("description");
        exchangeDescriptor.setBasePackage("basePackage");
        exchangeDescriptor.setApis(List.of(new ExchangeApiDescriptor()));
        exchangeDescriptor.setRateLimits(List.of(new RateLimitRule()));
        exchangeDescriptor.setDocUrl("https://doc.myexchange.com");
        exchangeDescriptor.setConstants(List.of(new Constant()));
        exchangeDescriptor.setProperties(List.of(new Property()));
        Assert.assertEquals("name", exchangeDescriptor.getName());
        Assert.assertEquals("description", exchangeDescriptor.getDescription());
        Assert.assertEquals("basePackage", exchangeDescriptor.getBasePackage());
        Assert.assertEquals("https://doc.myexchange.com", exchangeDescriptor.getDocUrl());
        Assert.assertEquals(1, exchangeDescriptor.getApis().size());
        Assert.assertEquals(1, exchangeDescriptor.getRateLimits().size());
        Assert.assertEquals(1, exchangeDescriptor.getConstants().size());
        Assert.assertEquals(1, exchangeDescriptor.getProperties().size());
    }

    @Test
    public void testToString() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("name");
        exchangeDescriptor.setDescription("description");
        exchangeDescriptor.setBasePackage("basePackage");
        exchangeDescriptor.setApis(List.of(new ExchangeApiDescriptor()));
        exchangeDescriptor.setRateLimits(List.of(new RateLimitRule()));
        Assert.assertEquals("ExchangeDescriptor{\"apis\":[{}],\"basePackage\":\"basePackage\",\"description\":\"description\",\"name\":\"name\",\"rateLimits\":[{\"maxRequestCount\":-1,\"maxTotalWeight\":-1,\"timeFrame\":0}]}", exchangeDescriptor.toString());
    }
}
