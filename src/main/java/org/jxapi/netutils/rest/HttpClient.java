package org.jxapi.netutils.rest;

import org.jxapi.netutils.rest.ratelimits.RequestThrottler;

/**
 * HttpRequestExecutor implementation that applies an interceptor before executing the request
 * using a delegate executor.
 * 
 * @see HttpRequestInterceptor
 * @see HttpRequestExecutor
 */
public class HttpClient extends AbstractHttpRequestExecutor {

  private final HttpRequestInterceptor interceptor;
  private final HttpRequestExecutor executor;
  private final RequestThrottler throttler;
  
  /**
   * Creates a new HttpClient.
   * 
   * @param interceptor the interceptor to apply before executing the request
   * @param executor    the executor to use for executing the request
   */
  public HttpClient(HttpRequestInterceptor interceptor, HttpRequestExecutor executor, RequestThrottler throttler) {
    this.interceptor = interceptor;
    this.executor = executor;
    this.throttler = throttler;
  }
  
  @Override
  public FutureHttpResponse execute(HttpRequest request) {
    if (interceptor != null) {
      interceptor.intercept(request);
    }
    if (throttler != null) {
      return throttler.submit(request, executor::execute);
    }
    return executor.execute(request);
  }

  /**
   * @return the interceptor that is applied before executing the request. May be <code>null</code>.
   */
  public HttpRequestInterceptor getInterceptor() {
    return interceptor;
  }

  /**
   * @return the executor that is used for executing the request.
   */
  public HttpRequestExecutor getExecutor() {
    return executor;
  }
  
  /**
   * @return the request throttler that is used for throttling requests. May be
   *         <code>null</code>.
   */
  public RequestThrottler getThrottler() {
    return throttler;
  }

}
