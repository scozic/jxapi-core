package org.jxapi.netutils.rest;

import org.jxapi.exchange.ExchangeApi;

/**
 * Hook called from {@link ExchangeApi} rest endpoint call to modify a {@link HttpRequest} before it is executed.
 * This is where API specific request requirements (specific headers, signature argument...) are enforced.
 */
public interface HttpRequestInterceptor {
  
  /**
   * Called just before execution of a HttptRequest. Implementations are free to
   * modifiy request provided in argument to fill API specific requirements such
   * as adding specific headers or adding signature argument. Remark: Calls to
   * {@link ExchangeApi} REST endpoints are thread safe (can be called from any
   * thread) so implementations thread safe too.
   * 
   * @param request HttpRequest to be executed.
   */
  void intercept(HttpRequest request);

}
