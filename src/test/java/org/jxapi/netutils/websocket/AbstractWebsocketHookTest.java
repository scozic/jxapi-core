package org.jxapi.netutils.websocket;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.Exchange;
import org.jxapi.netutils.websocket.mock.MockWebsocket;

/**
 * Unit test for {@link AbstractWebsocketHook}
 */
public class AbstractWebsocketHookTest {

  
  @Test
  public void testInit() {
    TestWebsocketHook hook = new TestWebsocketHook();
    WebsocketManager mgr = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), hook);
    Assert.assertEquals(mgr, hook.getWebsocketManager());
  }
  
  private static class TestWebsocketHook extends AbstractWebsocketHook {
    public WebsocketManager getWebsocketManager() {
      return this.websocketManager;
    }
  }

}
