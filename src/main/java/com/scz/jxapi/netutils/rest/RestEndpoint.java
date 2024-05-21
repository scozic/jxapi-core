package com.scz.jxapi.netutils.rest;

import java.io.IOException;

/**
 * Interface to perform a rest call for a specific api.
 * @param <R> Request
 * @param <A> Answer (Response)
 */
@Deprecated
public interface RestEndpoint<R, A> {

	/**
	 * Submits a REST API call synchronously and returns response.
	 * @param request
	 * @throws IOException 
	 */
	FutureRestResponse<A> call(RestRequest<R> request);
	
}
