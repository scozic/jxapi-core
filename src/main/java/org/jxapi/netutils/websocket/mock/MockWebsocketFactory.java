package org.jxapi.netutils.websocket.mock;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketFactory;

/**
 * {@link WebsocketFactory} implementation for creating {@link MockWebsocket} instances.
 */
public class MockWebsocketFactory implements WebsocketFactory {

  @Override
  public Websocket createWebsocket(ExchangeApi exchangeApi) {
    return new MockWebsocket();
  }

  @Override
  public Websocket createWebsocket(Exchange exchange) {
    return new MockWebsocket();
  }

}
