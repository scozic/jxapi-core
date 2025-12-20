package org.jxapi.netutils.websocket;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.netutils.websocket.spring.SpringWebsocket;

/**
 * Default implementation of a {@link WebsocketFactory}.
 * <p>
 * This class creates a new instance of a {@link Websocket} implementation,
 * using a {@link ExchangeApi} instance that can be used to retrieve
 * configuration properties, or be cast as specific implementation of
 * ExchangeApi to access its API endpoints.
 * 
 * @see WebsocketFactory
 */
public class DefaultWebsocketFactory implements WebsocketFactory {

  @Override
  public Websocket createWebsocket(Exchange exchange) {
    return new SpringWebsocket();
  }

}
