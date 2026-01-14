package org.jxapi.exchanges.demo.net;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.jxapi.netutils.rest.HttpRequestInterceptorFactory;

/**
 * {@link HttpRequestInterceptorFactory} implementation for
 * {@link DemoExchangeExchange}. Will create DemoExchangeHttpRequestInterceptor
 * instances.
 */
public class DemoExchangeHttpRequestInterceptorFactory implements HttpRequestInterceptorFactory {

  @Override
  public HttpRequestInterceptor createInterceptor(Exchange exchange) {
    return new DemoExchangeHttpRequestInterceptor();
  }

}
