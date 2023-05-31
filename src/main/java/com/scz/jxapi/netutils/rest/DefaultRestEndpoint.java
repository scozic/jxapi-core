package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;

public class DefaultRestEndpoint<R, A> implements RestEndpoint<R, A> {
	
	protected final MessageDeserializer<A> messageDeserializer;
	protected final HttpRequestBuilder requestBuilder;
	protected final HttpRequestExecutor executor;

	public DefaultRestEndpoint(HttpRequestBuilder requestBuilder, HttpRequestExecutor executor, MessageDeserializer<A> responseDeserializer) {
		this.requestBuilder = requestBuilder;
		this.executor = executor;
		this.messageDeserializer = responseDeserializer;
	}
	
	private RestResponse<A> createRestResponse(HttpResponse httpResponse) {
		RestResponse<A> response = new RestResponse<>();
		response.setHttpResponseCode(httpResponse.getResponseCode());
		response.setException(httpResponse.getException());
		response.setResponse(this.messageDeserializer.deserialize(httpResponse.getBody()));
		return response;
	}

	@Override
	public void call(RestRequest<R> request, Callback<RestResponse<A>> callback) {
		executor.execute(requestBuilder.build(request), httpResponse -> callback.handle(createRestResponse(httpResponse)));
	}

}
