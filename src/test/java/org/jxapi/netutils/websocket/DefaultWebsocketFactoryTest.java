package org.jxapi.netutils.websocket;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.Exchange;
import org.jxapi.netutils.websocket.spring.SpringWebsocket;

/**
 * Unit test for {@link DefaultWebsocketFactory}
 */
public class DefaultWebsocketFactoryTest {
  
  @Test
  public void testCreateDefaultWebsocket() {
    Websocket sock = new DefaultWebsocketFactory().createWebsocket((Exchange) null);
    Assert.assertNotNull(sock);
    Assert.assertTrue(sock instanceof SpringWebsocket);
  }

}
