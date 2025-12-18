package org.jxapi.exchanges.demo.net;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketHookFactory;

/**
 * {@link WebsocketHookFactory} for {@link DemoExchangeExchange} exchange. Will create {@link DemoExchangeWebsocketHook} instances.
 */
public class DemoExchangeWebsocketHookFactory implements WebsocketHookFactory {

  @Override
  public WebsocketHook createWebsocketHook(ExchangeApi exchangeApi) {
    return new DemoExchangeWebsocketHook();
  }

  @Override
  public WebsocketHook createWebsocketHook(Exchange exchangeApi) {
    return new DemoExchangeWebsocketHook();
  }

}
