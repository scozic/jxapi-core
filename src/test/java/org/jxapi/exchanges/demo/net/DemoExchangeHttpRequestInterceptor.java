package org.jxapi.exchanges.demo.net;

import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link HttpRequestInterceptor} implementation for {@link DemoExchangeExchange}.<br>
 * This interceptor is used to log the HTTP requests sent to the exchange.
 * @see DemoExchangeProperties#HOST
 * @see DemoExchangeProperties#HTTP_PORT
 */
public class DemoExchangeHttpRequestInterceptor implements HttpRequestInterceptor {
  
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeHttpRequestInterceptor.class);
  
  @Override
  public void intercept(HttpRequest request) {
    log.debug("Intercepted request:{}", request);
  }

}
