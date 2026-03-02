package org.jxapi.netutils.rest;

import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpRequestExecutor implementation that applies an interceptor before executing the request
 * using a delegate executor.
 * 
 * @see HttpRequestInterceptor
 * @see HttpRequestExecutor
 */
public class HttpClient extends AbstractHttpRequestExecutor {
  
  private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

  private final HttpRequestInterceptor interceptor;
  private final HttpResponseInterceptor responseInterceptor;
  private final HttpRequestExecutor executor;
  private final RequestThrottler throttler;
  
  /**
   * Creates a new HttpClient.
   * 
   * @param interceptor the interceptor to apply before executing the request
   * @param executor    the executor to use for executing the request
   * @param throttler   the request throttler to use for throttling requests. May
   *                    be <code>null</code>.
   */
  public HttpClient(HttpRequestInterceptor interceptor, 
                    HttpRequestExecutor executor, 
                    RequestThrottler throttler, 
                    HttpResponseInterceptor responseInterceptor) {
    this.interceptor = interceptor;
    this.executor = executor;
    this.throttler = throttler;
    this.responseInterceptor = responseInterceptor;
  }
  
  @Override
  public FutureHttpResponse execute(HttpRequest request) {
    if (interceptor != null) {
      interceptor.intercept(request);
    }
    if (throttler != null) {
      return processResponse(throttler.submit(request, executor::execute));
    } else {
      return processResponse(executor.execute(request));
    }
    
  }
  
  private FutureHttpResponse processResponse(FutureHttpResponse response) {
    if (responseInterceptor != null) {
      FutureHttpResponse processedResponse = new FutureHttpResponse();
      response.thenAccept(resp -> {
        if (resp.getException() == null) {
          try {
            responseInterceptor.intercept(resp);
          } catch (Exception e) {
            log.error("Exception in response interceptor for response {}: {}", resp, e.getMessage(), e); 
            resp.setException(e);
          }        
        }
        processedResponse.complete(resp);
      });
      return processedResponse;
    }
    return response;
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

  /**
   * @return the interceptor that is applied after receiving the response. May be <code>null</code>.
   */
  public HttpResponseInterceptor getResponseInterceptor() {
    return responseInterceptor;
  }

}
