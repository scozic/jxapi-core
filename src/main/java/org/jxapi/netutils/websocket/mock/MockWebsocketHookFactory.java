package org.jxapi.netutils.websocket.mock;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketHookFactory;

/**
 * {@link WebsocketHookFactory} implementation for creating {@link MockWebsocketHook} instances.
 */
public class MockWebsocketHookFactory implements WebsocketHookFactory {

  @Override
  public WebsocketHook createWebsocketHook(ExchangeApi exchangeApi) {
    return new MockWebsocketHook();
  }

}
