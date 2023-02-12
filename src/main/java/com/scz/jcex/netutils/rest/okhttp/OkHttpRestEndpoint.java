package com.scz.jcex.netutils.rest.okhttp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.util.EncodingUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class OkHttpRestEndpoint<R, A> implements RestEndpoint<R, A> {
	
	private static final Logger log = LoggerFactory.getLogger(OkHttpRestEndpoint.class);
	
	private static final MediaType JSON_TYPE = MediaType.parse("application/json");

	protected final OkHttpClient client;

	protected final MessageDeserializer<A> deserializer;

	public OkHttpRestEndpoint(OkHttpClient client, MessageDeserializer<A> deserializer) {
		this.client = client;
		this.deserializer = deserializer;
	}

	@Override
	public A call(RestRequest<R> request) throws IOException {
		Builder builder = new Request.Builder().url(request.getUrl());
		if (!"GET".equalsIgnoreCase(request.getHttpMethod())) {
			builder = builder.method(request.getHttpMethod(), RequestBody.create(JSON_TYPE, EncodingUtil.pojoToJsonString(request)));
		} else {
			builder = builder.get();
		}
		
		if (log.isDebugEnabled())
			log.debug("Sending request:" + request);
		Response response = client.newCall(builder.build()).execute();
		if (log.isDebugEnabled())
			log.debug("Received response:" + response);
		A answer = null;
		if (response != null && response.body() != null) {
		    
		   String msg = new String(response.body().bytes());
		   try {
			   if (log.isDebugEnabled())
				   log.debug("Response body:" + msg);
			   answer = deserializer.deserialize(msg);
		   } finally {
			   response.close();
		   }
        }
		return answer;
	}
	

}
