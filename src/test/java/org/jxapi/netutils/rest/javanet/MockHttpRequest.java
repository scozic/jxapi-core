package org.jxapi.netutils.rest.javanet;

import java.util.concurrent.CompletableFuture;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;

/**
 * Mock implementation of {@link HttpRequest} that is used to wrap incoming requests in {@link MockHttpServer}.
 * Every request is wrapped in this object and stored in a queue for client to retrieve and answer.
 * Every request must be answered with a {@link HttpResponse} object using {@link CompletableFuture#complete(Object)} method.
 */
public class MockHttpRequest extends CompletableFuture<HttpResponse> {

  private HttpRequest httpRequest;
  
  /**
   * Creates a new instance of {@link MockHttpRequest} with no underlying {@link HttpRequest}.
   */
  public MockHttpRequest() {
    this(null);
  }
  
  /**
   * Creates a new instance of {@link MockHttpRequest} with the given underlying {@link HttpRequest}.
   * 
   * @param httpRequest Underlying {@link HttpRequest} to wrap.
   */
  public MockHttpRequest(HttpRequest httpRequest) {
    this.setHttpRequest(httpRequest);
  }

  /**
   * @return Underlying {@link HttpRequest} wrapped in this object.
   */
  public HttpRequest getHttpRequest() {
    return httpRequest;
  }

  /**
   * Sets the underlying {@link HttpRequest} wrapped in this object.
   * 
   * @param httpRequest {@link HttpRequest} to wrap.
   */
  public void setHttpRequest(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }
}
