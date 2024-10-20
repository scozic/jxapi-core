package com.scz.jxapi.netutils.rest.mock;
import org.junit.Test;
import org.slf4j.LoggerFactory;

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
        String expected = "MockFutureHttpResponse{\"cancelled\":false,\"completedExceptionally\":false,\"done\":false,\"numberOfDependents\":0}";
    	// FIXME
        LoggerFactory.getLogger(MockFutureHttpResponseTest.class).info(">>>>>>>>>>>>>>>>> expected:\n" + expected + "\nactual\n" + response.toString());
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
