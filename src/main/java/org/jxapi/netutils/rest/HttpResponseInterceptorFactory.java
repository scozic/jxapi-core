package org.jxapi.netutils.rest;

import org.jxapi.exchange.Exchange;
import org.jxapi.util.FactoryUtil;

/**
 * Factory class for {@link HttpResponseInterceptorFactory}. Class name of such
 * factory implementation can be provided in exchange descriptor to create
 * specific response interceptor for a given HTTP client.
 * 
 * @see HttpResponseInterceptor
 */
public interface HttpResponseInterceptorFactory {

  /**
   * @param exchange    {@link Exchange} instance to configure the interceptor
   *                    for. It can be used to access configuration properties.
   * @return new instance of {@link HttpResponseInterceptor} configured with properties from provided exchange.
   */
  HttpResponseInterceptor createInterceptor(Exchange exchange);
  
  /**
   * Factory method to instantiate {@link HttpResponseInterceptorFactory} from its
   * class name.
   * 
   * @param cls Name of {@link HttpResponseInterceptorFactory} implementation
   *            class. Should have a default constructor.
   * @return factory of <code>cls</code> class.
   * @throws IllegalArgumentException If provided class cannot be instantiated by
   *                                  reflection or does not provide a default
   *                                  constructor.
   */
  public static HttpResponseInterceptorFactory fromClassName(String cls) {
    return FactoryUtil.fromClassName(cls);
  }
}
