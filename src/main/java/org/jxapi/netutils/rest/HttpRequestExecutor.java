package org.jxapi.netutils.rest;

import java.util.concurrent.Future;

import org.jxapi.util.Disposable;

/**
 * Interface for HTTP transport layer responsible of executing an HTTP request asynchronously.
 */
public interface HttpRequestExecutor extends Disposable {
  
  /**
   * Default delay in milliseconds after which connection should be aborted if
   * response to a request has not been received.
   */
  long DEFAULT_REQUEST_TIMEOUT = 30000L;

  /**
   * Submits a request for asynchronous execution.
   * @param request the request to execute
   * @return {@link Future} task that will complete when response is received.
   */
  FutureHttpResponse execute(HttpRequest request);
  
  /**
   * Sets the timeout for the requests submitted to this executor.
   * <p>
   * The timeout is the delay in milliseconds after which connection should be aborted if response to a request has not been received.
   * </p>
   * The default value is {@value #DEFAULT_REQUEST_TIMEOUT}.
   * <p>
   * A negative or 0 value means no timeout (wait forever for response).
   * @param requestTimeout the timeout in milliseconds
   */
  void setRequestTimeout(long requestTimeout);

  /**
   * Returns the timeout for the requests submitted to this executor.
   * @return the timeout in milliseconds
   * @see #setRequestTimeout(long)
   */
  long getRequestTimeout();
}
