package com.scz.jxapi.util;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpResponse;
import com.scz.jxapi.netutils.rest.RestResponse;

/**
 * Unit test for {@link DemoUtil}
 */
public class DemoUtilTest {

    @Test(expected = NullPointerException.class)
    public void testCheckResponseNullResponse() throws InterruptedException, ExecutionException {
        DemoUtil.checkResponse(null);
    }

    @Test(expected = ExecutionException.class)
    public void testCheckResponseNotOk() throws InterruptedException, ExecutionException {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(404);
        futureResponse.complete(new RestResponse<>(httpResponse));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test(expected = ExecutionException.class)
    public void testCheckResponseException() throws InterruptedException, ExecutionException {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        futureResponse.completeExceptionally(new Exception("Test execution error"));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test
    public void testCheckResponseOk() throws InterruptedException, ExecutionException {
        FutureRestResponse<String> futureResponse = new FutureRestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(200);
        RestResponse<String> restResponse = new RestResponse<>(httpResponse);
        restResponse.setResponse("hello");
        futureResponse.complete(restResponse);
        Assert.assertEquals(restResponse,DemoUtil.checkResponse(futureResponse));
    }
    
    @Test
    public void testLogWsMessage() {
    	Assert.assertNotNull(DemoUtil.logWsMessage("Hello World!"));
    }
}
