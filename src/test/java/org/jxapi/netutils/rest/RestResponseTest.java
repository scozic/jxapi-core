package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RestResponse}
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
        response.setHttpStatus(200);
        response.setResponse("response");
        response.setException(new Exception("fail!"));
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHeader("headerName", "headerValue");
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("RestResponse{\"exception\":\"java.lang.Exception: fail!\",\"httpStatus\":200,\"response\":\"\\\"response\\\"\"}", response.toString());
    }     
    
    @Test
    public void testToString_NullException() {
        RestResponse<String> response = new RestResponse<>();
        response.setHttpStatus(200);
        response.setResponse("response");
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHeader("headerName", "headerValue");
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("RestResponse{\"httpStatus\":200,\"response\":\"\\\"response\\\"\"}", response.toString());
    }
    
    @Test
    public void testToString_NullResponseButHttpResponse() {
        RestResponse<String> response = new RestResponse<>();
        response.setHttpStatus(200);
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHeader("headerName", "headerValue");
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("RestResponse{\"httpStatus\":200,\"roundtrip\":0}", response.toString());
    }
    
    @Test
    public void testToString_NullResponseAndNullHttpResponse() {
        RestResponse<String> response = new RestResponse<>();
        response.setHttpStatus(200);
        Assert.assertEquals("RestResponse{\"httpStatus\":200}", response.toString());
    }
}
