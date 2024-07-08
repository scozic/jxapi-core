package com.scz.jxapi.netutils.rest.mock;

import com.scz.jxapi.netutils.rest.FutureHttpResponse;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.util.EncodingUtil;

/**
 * This class represents a mock implementation of the FutureHttpResponse class.
 * It is used for testing purposes to simulate a future HTTP response.
 */
public class MockFutureHttpResponse extends FutureHttpResponse {

	private HttpRequest request;

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
		this.setRequest(request);
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
	 * Sets the HttpRequest associated with this response.
	 *
	 * @param request the HttpRequest to be associated with this response
	 */
	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return a string representation of this object
	 */
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
