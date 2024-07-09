package com.scz.jxapi.netutils.rest.mock;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.HttpRequest;

import static org.junit.Assert.*;



/**
 * Unit test for {@link MockFutureHttpResponse}
 */
public class MockFutureHttpResponseTest {

    @Test
    public void testGetRequest() {
        HttpRequest request = new HttpRequest();
        MockFutureHttpResponse response = new MockFutureHttpResponse(request);
        assertEquals(request, response.getRequest());
    }

    @Test
    public void testToString() {
        MockFutureHttpResponse response = new MockFutureHttpResponse();
        String expected = "MockFutureHttpResponse{\"done\":false,\"cancelled\":false,\"completedExceptionally\":false,\"numberOfDependents\":0}";
        assertEquals(expected, response.toString());
    }
    
    @Test
    public void testConstructWithNoRequest() {
        MockFutureHttpResponse response = new MockFutureHttpResponse();
        assertNull(response.getRequest());
    }
    
    @Test
    public void testConstructWithRequest() {
        HttpRequest request = new HttpRequest();
        MockFutureHttpResponse response = new MockFutureHttpResponse(request);
        assertEquals(request, response.getRequest());
    }

}
