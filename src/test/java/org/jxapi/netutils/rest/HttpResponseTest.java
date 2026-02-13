package org.jxapi.netutils.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HttpResponse}
 */
public class HttpResponseTest {

    @Test
    public void testIsStatusCodeOk() {
        Assert.assertTrue(HttpResponse.isStatusCodeOk(200));
        Assert.assertTrue(HttpResponse.isStatusCodeOk(299));
        Assert.assertFalse(HttpResponse.isStatusCodeOk(300));
    }

    @Test
    public void testSetResponseCode() {
        HttpResponse response = new HttpResponse();
        response.setResponseCode(200);
        Assert.assertEquals(200, response.getResponseCode());
    }

    @Test
    public void testSetBody() {
        HttpResponse response = new HttpResponse();
        response.setBody("body");
        Assert.assertEquals("body", response.getBody());
    }

    @Test
    public void testSetException() {
        HttpResponse response = new HttpResponse();
        Exception exception = new Exception();
        response.setException(exception);
        Assert.assertEquals(exception, response.getException());
    }

    @Test
    public void testSetHeaders() {
        HttpResponse response = new HttpResponse();
        Map<String, List<String>> headers = new HashMap<>();
        response.setHeaders(headers);
        Assert.assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testSetTime() {
        HttpResponse response = new HttpResponse();
        Date time = new Date();
        response.setTime(time);
        Assert.assertEquals(time, response.getTime());
    }

    @Test
    public void testSetHeader() {
        HttpResponse response = new HttpResponse();
        response.setHeader("headerName", "headerValue");
        response.setHeader("headerName2", "headerValue2");
        Assert.assertEquals("headerValue", response.getHeaders().get("headerName").get(0));
        Assert.assertEquals("headerValue2", response.getHeaders().get("headerName2").get(0));
    }

    @Test
    public void testSetHeaderList() {
        HttpResponse response = new HttpResponse();
        response.setHeader("headerName", List.of("headerValue1", "headerValue2"));
        Assert.assertEquals(List.of("headerValue1", "headerValue2"), response.getHeaders().get("headerName"));
    }

    @Test
    public void testSetRequest() {
        HttpResponse response = new HttpResponse();
        HttpRequest request = new HttpRequest();
        response.setRequest(request);
        Assert.assertEquals(request, response.getRequest());
    }

    @Test
    public void testGetRoundTrip_NullResponseTime() {
        HttpResponse response = new HttpResponse();
        Assert.assertEquals(0L, response.getRoundTrip());
    }

    @Test
    public void testGetRoundTrip_NullRequest() {
        HttpResponse response = new HttpResponse();
        response.setTime(new Date());
        Assert.assertEquals(0L, response.getRoundTrip());
    }

    @Test
    public void testGetRoundTrip_NullRequestTime() {
        HttpResponse response = new HttpResponse();
        response.setTime(new Date());
        response.setRequest(new HttpRequest());
        Assert.assertEquals(0L, response.getRoundTrip());
    }

    @Test
    public void testGetRoundTrip() {
        HttpResponse response = new HttpResponse();
        response.setTime(new Date());
        HttpRequest request = new HttpRequest();
        request.setTime(new Date(response.getTime().getTime() - 1000L));
        response.setRequest(request);
        Assert.assertEquals(1000L, response.getRoundTrip());
    }

    @Test
    public void testToString() {
        HttpResponse response = new HttpResponse();
        response.setResponseCode(200);
        response.setBody("hello");
        response.setException(new RuntimeException("error!"));
        response.setHeader("headerName", "headerValue");
        response.setTime(new Date(1725663894119L));
        HttpRequest request = new HttpRequest();
        request.setTime(new Date(response.getTime().getTime() - 1000L));
        response.setRequest(request);
        Assert.assertEquals("{\"responseCode\":200,\"exception\":\"java.lang.RuntimeException: error!\",\"request\":{\"time\":\"2024-09-07T01:04:53.119+0200\"},\"body\":\"length=5\",\"headers\":{\"headerName\":[\"headerValue\"]},\"time\":\"2024-09-07T01:04:54.119+0200\",\"roundTrip\":1000}", 
                            response.toString());
        response.setBody(null);
        response.setException(null);
        response.setHeaders(null);
        response.setTime(null);
        response.setRequest(null);
        Assert.assertEquals("{\"responseCode\":200,\"roundTrip\":0}", 
            response.toString());
    }
}
