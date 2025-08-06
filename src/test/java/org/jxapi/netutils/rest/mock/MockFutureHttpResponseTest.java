package org.jxapi.netutils.rest.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.util.EncodingUtil;



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
    public void testToString() throws ParseException {
      HttpRequest request = HttpRequest.create("myEndpoint", "http://example.com", HttpMethod.GET, null, null, 1);
        request.setTime(new SimpleDateFormat(EncodingUtil.DATE_FORMAT_ISO_8601).parse("2025-06-29T21:22:10.177+0200"));
        MockFutureHttpResponse response = new MockFutureHttpResponse(request);
        String expected = "MockFutureHttpResponse{\"request\":{\"endpoint\":\"myEndpoint\",\"httpMethod\":\"GET\",\"url\":\"http://example.com\",\"weight\":1,\"time\":\"2025-06-29T21:22:10.177+0200\"}}";
        assertEquals(expected, response.toString());
    }
    
    @Test
    public void testConstructWithNoRequest() {
        MockFutureHttpResponse response = new MockFutureHttpResponse();
        assertNull(response.getRequest());
    }

}
