package org.jxapi.netutils.rest;

import org.jxapi.exchange.Exchange;
import org.jxapi.util.FactoryUtil;

/**
 * Factory interface for creating {@link HttpRequestExecutor} instances.
 * 
 * @see HttpRequestExecutor
 */
public interface HttpRequestExecutorFactory {
  
  /**
   * Creates an {@link HttpRequestExecutor} instance for the given exchange.
   * 
   * @param exchange Exchange for which the executor is created.
   * @return {@link HttpRequestExecutor} instance.
   */
  HttpRequestExecutor createExecutor(Exchange exchange);
  
  /**
   * Factory method to instantiate {@link HttpRequestExecutorFactory} from its
   * class name.
   * 
   * @param cls Name of {@link HttpRequestExecutorFactory} implementation
   *            class. Should have a default constructor.
   * @return factory of <code>cls</code> class.
   * @throws IllegalArgumentException If provided class cannot be instantiated by
   *                                  reflection or does not provide a default
   *                                  constructor.
   */
  public static HttpRequestExecutorFactory fromClassName(String cls) {
    return FactoryUtil.fromClassName(cls);
  }
}
