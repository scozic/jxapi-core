package org.jxapi.netutils.rest;

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
  
  /**
   * Creates a new HttpClient.
   * 
   * @param interceptor the interceptor to apply before executing the request
   * @param executor    the executor to use for executing the request
   */
  public HttpClient(HttpRequestInterceptor interceptor, HttpRequestExecutor executor) {
    this.interceptor = interceptor;
    this.executor = executor;
  }
  
  @Override
  public FutureHttpResponse execute(HttpRequest request) {
    if (getInterceptor() != null) {
      getInterceptor().intercept(request);
    }
    return getExecutor().execute(request);
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

}
