package org.jxapi.netutils.rest.mock;

import org.junit.Before;
import org.junit.Test;

import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;

import static org.junit.Assert.*;

/**
 * Unit test for {@link MockHttpRequestExecutor}
 */
public class MockHttpRequestExecutorTest {

    private MockHttpRequestExecutor executor;

    @Before
    public void setUp() {
        executor = new MockHttpRequestExecutor();
    }

    @Test
    public void testExecute() {
        HttpRequest request = new HttpRequest();
        FutureHttpResponse response = executor.execute(request);

        assertNotNull(response);
        assertEquals(1, executor.size());
    }

    @Test
    public void testGetSubmittedRequests() {
        HttpRequest request1 = new HttpRequest();
        HttpRequest request2 = new HttpRequest();

        executor.execute(request1);
        executor.execute(request2);

        assertEquals(2, executor.getSubmittedRequests().size());
    }

    @Test
    public void testSize() {
        HttpRequest request1 = new HttpRequest();
        HttpRequest request2 = new HttpRequest();

        executor.execute(request1);
        executor.execute(request2);

        assertEquals(2, executor.size());
    }

    @Test
    public void testPopRequest() {
        HttpRequest request1 = new HttpRequest();
        HttpRequest request2 = new HttpRequest();

        executor.execute(request1);
        executor.execute(request2);

        MockFutureHttpResponse poppedRequest = executor.popRequest();

        assertNotNull(poppedRequest);
        assertEquals(request1, poppedRequest.getRequest());
        assertEquals(1, executor.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopRequestEmpty() {
        executor.popRequest();
    }
}
