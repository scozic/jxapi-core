package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;

@Deprecated
public class DefaultRestEndpoint<R, A> implements RestEndpoint<R, A> {
	
	protected final MessageDeserializer<A> messageDeserializer;
	protected final HttpRequestBuilder<R> requestBuilder;
	protected final HttpRequestExecutor executor;

	public DefaultRestEndpoint(HttpRequestBuilder<R> requestBuilder, HttpRequestExecutor executor, MessageDeserializer<A> responseDeserializer) {
		this.requestBuilder = requestBuilder;
		this.executor = executor;
		this.messageDeserializer = responseDeserializer;
	}
	
	private RestResponse<A> createRestResponse(HttpResponse httpResponse) {
		RestResponse<A> response = new RestResponse<>();
		response.setHttpStatus(httpResponse.getResponseCode());
		Exception ex = httpResponse.getException();
		if (ex != null) {
			response.setException(ex);
		} else {
			try {
				response.setResponse(this.messageDeserializer.deserialize(httpResponse.getBody()));
			} catch (Exception e) {
				response.setException(e);
			}
		}
		
		return response;
	}

	@Override
	public FutureRestResponse<A> call(RestRequest<R> request) {
		FutureRestResponse<A> callback = new FutureRestResponse<>();
		executor.execute(requestBuilder.build(request)).thenAccept(httpResponse -> callback.complete(createRestResponse(httpResponse)));
		return callback;
	}

}
