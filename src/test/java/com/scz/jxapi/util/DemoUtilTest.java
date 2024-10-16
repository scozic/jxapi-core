package com.scz.jxapi.util;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpResponse;
import com.scz.jxapi.netutils.rest.RestResponse;

/**
 * Unit test for {@link DemoUtil}
 */
public class DemoUtilTest {

    @Test(expected = NullPointerException.class)
    public void testCheckResponseNullResponse() {
        DemoUtil.checkResponse(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckResponseNotOk() {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(404);
        futureResponse.complete(new RestResponse<>(httpResponse));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckResponseException() {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        futureResponse.completeExceptionally(new Exception("Test execution error"));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test
    public void testCheckResponseOk() {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(200);
        futureResponse.complete(new RestResponse<>(httpResponse));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test
    public void testPrettyPrintResponse_NullHttpResponse() {
        RestResponse<?> response = new RestResponse<>();
        Assert.assertEquals("{\n"
        		+ "  \"httpStatus\" : 0,\n"
        		+ "  \"ok\" : false\n"
        		+ "}", DemoUtil.prettyPrintResponse(response));
    }
    
    @Test
    public void testPrettyPrintResponse_NullHttpRequestInHttpResponse() {
        RestResponse<?> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(200);
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("{\n"
        		+ "  \"httpStatus\" : 0,\n"
        		+ "  \"httpResponse\" : {\n"
        		+ "    \"responseCode\" : 200,\n"
        		+ "    \"roundTrip\" : 0\n"
        		+ "  },\n"
        		+ "  \"ok\" : false\n"
        		+ "}", DemoUtil.prettyPrintResponse(response));
    }
    
    @Test
    public void testPrettyPrintResponse_HttpRequestInHttpResponse() {
        RestResponse<?> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        HttpRequest httpRequest = new HttpRequest();
        httpResponse.setRequest(httpRequest);
        httpResponse.setResponseCode(200);
        response.setHttpResponse(httpResponse);
        Assert.assertEquals("{\n"
        		+ "  \"httpStatus\" : 0,\n"
        		+ "  \"httpResponse\" : {\n"
        		+ "    \"responseCode\" : 200,\n"
        		+ "    \"request\" : {\n"
        		+ "      \"weight\" : 0,\n"
        		+ "      \"throttledTime\" : 0\n"
        		+ "    },\n"
        		+ "    \"roundTrip\" : 0\n"
        		+ "  },\n"
        		+ "  \"ok\" : false\n"
        		+ "}", DemoUtil.prettyPrintResponse(response));
    }
    
    @Test
    public void testLogWsMessage() {
    	DemoUtil.logWsMessage("Hello World!");
    }
}
