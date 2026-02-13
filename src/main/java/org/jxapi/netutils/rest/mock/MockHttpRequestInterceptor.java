package org.jxapi.netutils.rest.mock;

import java.util.ArrayList;
import java.util.List;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestInterceptor;

/**
 * A mock implementation of the HttpRequestInterceptor interface.
 * This class is used for testing purposes to simulate the interception of HTTP requests.
 * Provides a list of prepared interceptors that can be applied to the request.
 */
public class MockHttpRequestInterceptor implements HttpRequestInterceptor {

  private final List<HttpRequestInterceptor> preparedInterceptors = new ArrayList<>();

  /**
   * Intercepts the given HttpRequest.
   * This method applies the first prepared interceptor to the request.
   * If no prepared interceptor is available, the request is not modified.
   * Multiple prepared interceptors can be added to the list. The first one added will be applied first. 
   * If multiple interceptors are added, they will be applied in the order they were added.
   *
   * @param request The HttpRequest to intercept.
   */
  @Override
  public void intercept(HttpRequest request) {
    preparedInterceptors.forEach(i -> i.intercept(request));
  }
  
  /**
   * Adds a prepared interceptor to the list of interceptors.
   *
   * @param interceptor The interceptor to add.
   */
  public void addPreparedInterceptor(HttpRequestInterceptor interceptor) {
    preparedInterceptors.add(interceptor);
  }
  
  /**
   * Adds a prepared interceptor that throws the specified exception.
   *
   * @param exception The exception to throw.
   */
  public void addPreparedThrow(RuntimeException exception) {
    addPreparedInterceptor(r -> {throw exception;});
  }
  
  /**
   * Clears the list of prepared interceptors.
   */
  public void clearPreparedInterceptors() {
    preparedInterceptors.clear();
  }
  
  /**
   * @return The list of prepared interceptors.
   */
  public List<HttpRequestInterceptor> getPreparedInterceptors() {
    return this.preparedInterceptors;
  }
}
