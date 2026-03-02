package org.jxapi.netutils.rest.mock;

import java.util.ArrayList;
import java.util.List;

import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.HttpResponseInterceptor;

public class MockHttpResponseInterceptor implements HttpResponseInterceptor {

  private final List<HttpResponseInterceptor> preparedInterceptors = new ArrayList<>();

  /**
   * Intercepts the given HttpResponse by executing all prepared interceptors on it.
   *
   * @param response The HttpResponse to intercept.
   */
  @Override
  public void intercept(HttpResponse response) {
    preparedInterceptors.forEach(i -> i.intercept(response));
  }
  
  /**
   * Adds a prepared interceptor to the list of interceptors.
   *
   * @param interceptor The interceptor to add.
   */
  public void addPreparedInterceptor(HttpResponseInterceptor interceptor) {
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
  public List<HttpResponseInterceptor> getPreparedInterceptors() {
    return this.preparedInterceptors;
  }

}
