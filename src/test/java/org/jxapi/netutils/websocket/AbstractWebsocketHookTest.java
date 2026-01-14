package org.jxapi.netutils.websocket;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.netutils.websocket.mock.MockWebsocket;

/**
 * Unit test for {@link AbstractWebsocketHook}
 */
public class AbstractWebsocketHookTest {

  
  @Test
  public void testInit() {
    TestWebsocketHook hook = new TestWebsocketHook();
    WebsocketClient mgr = new DefaultWebsocketClient(new MockWebsocket(), hook);
    Assert.assertEquals(mgr, hook.getWebsocketManager());
  }
  
  private static class TestWebsocketHook extends AbstractWebsocketHook {
    public WebsocketClient getWebsocketManager() {
      return this.websocketClient;
    }
  }

}
