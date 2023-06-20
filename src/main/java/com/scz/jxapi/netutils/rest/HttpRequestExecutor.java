package com.scz.jxapi.netutils.rest;

import java.util.concurrent.Future;

/**
 * Interface for HTTP transport layer responsible of executing an HTTP request asynchronously.
 */
public interface HttpRequestExecutor {

	/**
	 * Submits a request for asynchronous execution.
	 * @param request the request to execute
	 * @return {@link Future} task that will complete when response is received.
	 */
	FutureHttpResponse execute(HttpRequest request);
}
