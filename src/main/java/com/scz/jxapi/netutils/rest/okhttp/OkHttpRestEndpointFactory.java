package com.scz.jxapi.netutils.rest.okhttp;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestEndpointFactory;

import okhttp3.OkHttpClient;

public class OkHttpRestEndpointFactory implements RestEndpointFactory {
	
	private final OkHttpClient okHttpClient;
	
	public OkHttpRestEndpointFactory() {
		this(new OkHttpClient());
	}
	
	public OkHttpRestEndpointFactory(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		return new OkHttpRestEndpoint<>(okHttpClient, messageDeserializer);
	}
 
}
