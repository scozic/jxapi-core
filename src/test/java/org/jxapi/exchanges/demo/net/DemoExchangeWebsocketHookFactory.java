package org.jxapi.exchanges.demo.net;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketHookFactory;

/**
 * {@link WebsocketHookFactory} for {@link DemoExchangeExchange} exchange. Will create {@link DemoExchangeWebsocketHook} instances.
 */
public class DemoExchangeWebsocketHookFactory implements WebsocketHookFactory {

  @Override
  public WebsocketHook createWebsocketHook(Exchange exchange) {
    return new DemoExchangeWebsocketHook();
  }

}
