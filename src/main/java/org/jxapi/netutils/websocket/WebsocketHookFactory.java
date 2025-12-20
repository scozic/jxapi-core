package org.jxapi.netutils.websocket;

import org.jxapi.exchange.Exchange;
import org.jxapi.util.FactoryUtil;

/**
 * Factory for creating {@link WebsocketHook} instances.
 * <p>
 * Actual implementations of this factory should provide a public
 * default constructor with no argument so they can be instantiated by
 * reflection.
 */
public interface WebsocketHookFactory {
  
  /**
   * Create a new instance of a {@link WebsocketHook} implementation.
   * 
   * @param exchange the {@link Exchange} instance that can be used to
   *                 retrieve configuration properties or be cast as
   *                 specific implementation of Exchange to access its API endpoints.
   * @return a new instance of a {@link WebsocketHook} implementation.
   */
  WebsocketHook createWebsocketHook(Exchange exchange);
  
  /**
   * Factory method to instantiate {@link WebsocketHookFactory} from its
   * class name.
   * 
   * @param cls Name of {@link WebsocketHookFactory} implementation
   *            class. Should have a default constructor.
   * @return factory of <code>cls</code> class.
   * @throws IllegalArgumentException If provided class cannot be instantiated by
   *                                  reflection or does not provide a default
   *                                  constructor.
   */
  public static WebsocketHookFactory fromClassName(String cls) {
    return FactoryUtil.fromClassName(cls);
  }
}
