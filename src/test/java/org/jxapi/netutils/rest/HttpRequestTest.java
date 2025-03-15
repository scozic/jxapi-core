package org.jxapi.netutils.rest;

import org.junit.Test;

import org.jxapi.netutils.rest.ratelimits.RateLimitRule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

/**
 * Unit test for {@link HttpRequest}
 */
public class HttpRequestTest {

    @Test
    public void testSetMethod() {
        HttpRequest request = new HttpRequest();
        request.setHttpMethod(HttpMethod.GET);
        Assert.assertEquals(HttpMethod.GET, request.getHttpMethod());
    }

    @Test
    public void testSetUrl() {
        HttpRequest request = new HttpRequest();
        request.setUrl("http://example.com");
        Assert.assertEquals("http://example.com", request.getUrl());
    }

    @Test
    public void testSetHeaders() {
        HttpRequest request = new HttpRequest();
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", List.of("application/json"));
        request.setHeaders(headers);
        Assert.assertEquals(headers, request.getHeaders());
    }
    
    @Test
    public void testSetHeader() {
    	HttpRequest request = new HttpRequest();
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", List.of("application/json"));
        headers.put("User-Agent", List.of("Mozilla/5.0"));
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "Mozilla/5.0");
        Assert.assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testSetBody() {
        HttpRequest request = new HttpRequest();
        request.setBody("body");
        Assert.assertEquals("body", request.getBody());
    }

    @Test
    public void testSetTime() {
        HttpRequest request = new HttpRequest();
        Date date = new Date(1000);
        request.setTime(date);
        Assert.assertEquals(date, request.getTime());
    }

    @Test
    public void testSetEndpoint() {
        HttpRequest request = new HttpRequest();
        request.setEndpoint("endpoint");
        Assert.assertEquals("endpoint", request.getEndpoint());
    }

    @Test
    public void testSetRequest() {
        HttpRequest request = new HttpRequest();
        request.setRequest("request");
        Assert.assertEquals("request", request.getRequest());
    }

    @Test
    public void testSetRateLimits() {
        HttpRequest request = new HttpRequest();
        List<RateLimitRule> rateLimits = List.of(RateLimitRule.createRule("rule1", 60000, 50));
        request.setRateLimits(rateLimits);
        Assert.assertEquals(rateLimits, request.getRateLimits());
    }

    @Test
    public void testSetWeight() {
        HttpRequest request = new HttpRequest();
        request.setWeight(10);
        Assert.assertEquals(10, request.getWeight());
    }

    @Test
    public void testSetThrotthledTime() {
        HttpRequest request = new HttpRequest();
        request.setThrottledTime(1000);
        Assert.assertEquals(1000, request.getThrottledTime());
    }

    @Test
    public void testToString() {
        HttpRequest request = new HttpRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setUrl("http://example.com");
        request.setBody("body");
        request.setTime(new Date(1000));
        request.setEndpoint("endpoint");
        request.setRequest("request");
        request.setThrottledTime(1000);
        List<RateLimitRule> rateLimits = List.of(RateLimitRule.createRule("rule1", 60000, 50));
        request.setRateLimits(rateLimits);
        Assert.assertEquals("HttpRequest{\"body\":\"body\",\"endpoint\":\"endpoint\",\"httpMethod\":\"GET\",\"rateLimits\":[{\"granularity\":10,\"id\":\"rule1\",\"maxRequestCount\":50,\"maxTotalWeight\":-1,\"timeFrame\":60000}],\"request\":\"request\",\"throttledTime\":1000,\"time\":1000,\"url\":\"http://example.com\",\"weight\":0}", 
        					request.toString());
    }

    @Test
    public void testCreate() {
    	List<RateLimitRule> rateLimits = List.of(RateLimitRule.createRule("rule1", 60000, 50));
        HttpRequest request = HttpRequest.create("endpoint", "http://example.com", HttpMethod.GET, "request", rateLimits, 10, "myBody");
        Assert.assertEquals("endpoint", request.getEndpoint());
        Assert.assertEquals("http://example.com", request.getUrl());
        Assert.assertEquals(HttpMethod.GET, request.getHttpMethod());
        Assert.assertEquals("request", request.getRequest());
        Assert.assertEquals(rateLimits, request.getRateLimits());
        Assert.assertEquals(10, request.getWeight());
        Assert.assertEquals("myBody", request.getBody());
    }
}
