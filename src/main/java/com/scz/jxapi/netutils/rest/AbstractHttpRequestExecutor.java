package com.scz.jxapi.netutils.rest;

/**
 * Abstract implementation of the {@link HttpRequestExecutor} interface.
 * <p>
 * This class provides a default implementation for the {@link #setRequestTimeout(long)} and {@link #getRequestTimeout()} methods.
 * <p>
 */
public abstract class AbstractHttpRequestExecutor implements HttpRequestExecutor {

	private long requestTimeout = DEFAULT_REQUEST_TIMEOUT;

	@Override
	public void setRequestTimeout(long requestTimeout) {
		this.requestTimeout = requestTimeout;
		
	}

	@Override
	public long getRequestTimeout() {
		return this.requestTimeout;
	}

}
