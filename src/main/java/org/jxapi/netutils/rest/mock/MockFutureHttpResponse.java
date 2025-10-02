package org.jxapi.netutils.rest.mock;

import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;

/**
 * This class represents a mock implementation of the FutureHttpResponse class.
 * It is used for testing purposes to simulate a future HTTP response.
 * 
 * @see MockHttpRequestExecutor
 */
public class MockFutureHttpResponse extends FutureHttpResponse {

  private final HttpRequest request;

  /**
   * Constructs a new MockFutureHttpResponse object with no associated HttpRequest.
   */
  public MockFutureHttpResponse() {
    this(null);
  }

  /**
   * Constructs a new MockFutureHttpResponse object with the specified HttpRequest.
   *
   * @param request the HttpRequest associated with this response
   */
  public MockFutureHttpResponse(HttpRequest request) {
    this.request = request;
  }

  /**
   * Returns the HttpRequest associated with this response.
   *
   * @return the HttpRequest associated with this response
   */
  public HttpRequest getRequest() {
    return request;
  }

  /**
   * Returns a string representation of this object.
   *
   * @return a string representation of this object
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + "\"request\":" + request + '}';
  }
}
