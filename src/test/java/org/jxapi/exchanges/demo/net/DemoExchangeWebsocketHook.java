package org.jxapi.exchanges.demo.net;

import org.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import org.jxapi.netutils.websocket.AbstractWebsocketHook;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketClient;

/**
 * {@link WebsocketHook} implementation for {@link DemoExchangeExchange}. Will perform following custom actions:
 * <ul>
 * <li>Replace target websocket URL of bound {@link WebsocketClient} with one with configured {@link DemoExchangeProperties#HOST} and {@link DemoExchangeProperties#WEBSOCKET_PORT}.
 * <li>Override {@link #afterConnect()} to send a 'greetings' message after connection (see {@link DemoExchangeConstants#WEBSOCKET_LOGIN_MESSAGE}
 * <li>Override {@link #beforeDisconnect()} to send a 'greetings' message after connection (see {@link DemoExchangeConstants#WEBSOCKET_LOGOUT_MESSAGE}
 * </ul>
 */
public class DemoExchangeWebsocketHook extends AbstractWebsocketHook {
  
  @Override
  public void afterConnect() throws WebsocketException {
    websocketClient.send(DemoExchangeConstants.WEBSOCKET_LOGIN_MESSAGE);
  }
  
  @Override
  public void beforeDisconnect() throws WebsocketException {
    websocketClient.send(DemoExchangeConstants.WEBSOCKET_LOGOUT_MESSAGE);
  }

}
