package org.jxapi.netutils.rest.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jxapi.netutils.rest.AbstractHttpRequestExecutor;
import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;

/**
 * A mock implementation of the HttpRequestExecutor interface.
 * This class is used for testing purposes to simulate the execution of HTTP requests.
 * Whenever a request is submitted for execution, a {@link MockFutureHttpResponse} for this request is added to a queue.
 * Incoming queued requests awaiting response should be acknowledged by retrieving them from queue using {@link #popRequest()} and completing them using {@link CompletableFuture#complete(Object)}  
 */
public class MockHttpRequestExecutor extends AbstractHttpRequestExecutor {

  private List<MockFutureHttpResponse> submittedRequests = new ArrayList<>();

  /**
   * Executes the given HttpRequest and returns a FutureHttpResponse.
   * This method adds the MockFutureHttpResponse to the list of submitted requests.
   *
   * @param request The HttpRequest to execute.
   * @return A FutureHttpResponse representing the result of the execution.
   */
  @Override
  public FutureHttpResponse execute(HttpRequest request) {
    MockFutureHttpResponse res = new MockFutureHttpResponse(request);
    submittedRequests.add(res);
    return res;
  }

  /**
   * Returns the list of submitted requests.
   *
   * @return The list of submitted requests.
   */
  public List<MockFutureHttpResponse> getSubmittedRequests() {
    return submittedRequests;
  }

  /**
   * Returns the number of submitted requests.
   *
   * @return The number of submitted requests.
   */
  public int size() {
    return submittedRequests.size();
  }

  /**
   * Removes and returns the first submitted request from the list.
   * Throws an IllegalStateException if no request has been submitted.
   *
   * @return The first submitted request.
   * @throws IllegalStateException if no request has been submitted.
   */
  public MockFutureHttpResponse popRequest() {
    if (submittedRequests.isEmpty()) {
      throw new IllegalStateException("No request submitted");
    }
    return submittedRequests.remove(0);
  }

}
