package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RestResponse} and
 * {@link RestResponseToStringJsonSerializer} through
 * {@link RestResponse#toString()}.
 */
public class RestResponseTest {

    @Test
    public void testSetHttpStatus() {
        RestResponse<String> response = new RestResponse<>();
        response.setHttpStatus(200);
        Assert.assertEquals(200, response.getHttpStatus());
    }

    @Test
    public void testSetException() {
        RestResponse<String> response = new RestResponse<>();
        Exception exception = new Exception();
        response.setException(exception);
        Assert.assertEquals(exception, response.getException());
    }

    @Test
    public void testSetResponse() {
        RestResponse<String> response = new RestResponse<>();
        response.setResponse("response");
        Assert.assertEquals("response", response.getResponse());
    }

    @Test
    public void testIsOk_OkResponse() {
        RestResponse<String> response = new RestResponse<>();
        response.setHttpStatus(200);
        Assert.assertTrue(response.isOk());
    }

    @Test
    public void testIsOk_KoResponseBadStatusCode() {
        RestResponse<String> response = new RestResponse<>();
        response.setHttpStatus(500);
        Assert.assertFalse(response.isOk());
    }

    @Test
    public void testIsOk_KoResponseHasException() {
        RestResponse<String> response = new RestResponse<>();
        response.setException(new Exception());
        Assert.assertFalse(response.isOk());
    }

    @Test
    public void testGetHttpResponse() {
        HttpResponse httpResponse = new HttpResponse();
        RestResponse<String> response = new RestResponse<>(httpResponse);
        Assert.assertEquals(httpResponse, response.getHttpResponse());
    }

    @Test
    public void testSetHttpResponse() {
        RestResponse<String> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        response.setHttpResponse(httpResponse);
        Assert.assertEquals(httpResponse, response.getHttpResponse());
    }


    @Test
    public void testGetEndpoint() {
        RestResponse<String> response = new RestResponse<>();
        HttpRequest request = new HttpRequest();
        request.setEndpoint("http://example.com");
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setRequest(request);
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("http://example.com", response.getEndpoint());
    }

    @Test
    public void testGetEndpoint_NullHttpResponse() {
        RestResponse<String> response = new RestResponse<>();
        Assert.assertNull(response.getEndpoint());
    }

    @Test
    public void testGetEndpoint_NullHttpRequest() {
        RestResponse<String> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        response.setHttpResponse(httpResponse);
        Assert.assertNull(response.getEndpoint());
    }

    @Test
    public void testToString() {
        RestResponse<String> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(200);
        httpResponse.setBody("response");
        httpResponse.setHeader("headerName", "headerValue");
        httpResponse.setException(new Exception("fail!"));
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("{\"httpResponse\":{\"responseCode\":200,\"exception\":\"java.lang.Exception: fail!\",\"body\":\"length=8\",\"headers\":{\"headerName\":[\"headerValue\"]},\"roundTrip\":0},\"paginated\":false}", response.toString());
        response.setPaginated(true);
        Assert.assertEquals("{\"httpResponse\":{\"responseCode\":200,\"exception\":\"java.lang.Exception: fail!\",\"body\":\"length=8\",\"headers\":{\"headerName\":[\"headerValue\"]},\"roundTrip\":0},\"paginated\":true}", response.toString());
    }     
    
}
