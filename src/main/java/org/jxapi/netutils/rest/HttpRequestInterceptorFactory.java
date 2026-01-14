package org.jxapi.netutils.rest;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.util.FactoryUtil;

/**
 * Factory class for {@link HttpRequestInterceptorFactory}. Class name of such
 * factory implementation can be provided in exchange descriptor to create
 * specific interceptor for a given HTTP client.
 * 
 * @see HttpRequestInterceptor
 */
public interface HttpRequestInterceptorFactory {
  
  /**
   * @param exchange    {@link ExchangeApi} instance to configure the interceptor
   *                    for. It can be used to access configuration properties.
   * @return new instance of {@link HttpRequestInterceptor} with properties set.
   */
  HttpRequestInterceptor createInterceptor(Exchange exchange);
  
  /**
   * Factory method to instantiate {@link HttpRequestInterceptorFactory} from its
   * class name.
   * 
   * @param cls Name of {@link HttpRequestInterceptorFactory} implementation
   *            class. Should have a default constructor.
   * @return factory of <code>cls</code> class.
   * @throws IllegalArgumentException If provided class cannot be instantiated by
   *                                  reflection or does not provide a default
   *                                  constructor.
   */
  public static HttpRequestInterceptorFactory fromClassName(String cls) {
    return FactoryUtil.fromClassName(cls);
  }
}
