package org.jxapi.netutils.websocket;

/**
 * Abstract implementation of a {@link WebsocketHook}. It provides a reference
 * to the {@link WebsocketClient} that is passed to the hook in the
 * {@link #init(WebsocketClient)} method.
 */
public abstract class AbstractWebsocketHook implements WebsocketHook {

  /**
   * The {@link WebsocketClient} instance that is passed to the hook in the
   * {@link #init(WebsocketClient)} method.
   */
  protected WebsocketClient websocketClient;
  
  @Override
  public void init(WebsocketClient websocketClient) {
    this.websocketClient = websocketClient;
  }

}
