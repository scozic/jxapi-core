package org.jxapi.netutils.websocket;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketException}
 */
public class WebsocketExceptionTest {
    
    @Test
    public void testCreateWebsocketExceptionWithMessage() {
        WebsocketException websocketException = new WebsocketException("message");
        Assert.assertEquals("message", websocketException.getMessage());
        Assert.assertNotNull(websocketException);
    }

    @Test
    public void testCreateWebsocketExceptionWithCause() {
        Exception cause = new Exception("cause");
        WebsocketException websocketException = new WebsocketException(cause);
        Assert.assertNotNull(websocketException);
        Assert.assertEquals(cause, websocketException.getCause());
    }

    @Test
    public void testCreateWebsocketExceptionWithMessageAndCause() {
        Exception cause = new Exception("cause");
        WebsocketException websocketException = new WebsocketException("message", cause);
        Assert.assertNotNull(websocketException);
        Assert.assertEquals("message", websocketException.getMessage());
        Assert.assertEquals(cause, websocketException.getCause());
    }


}
