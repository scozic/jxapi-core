package com.scz.jxapi.exchanges.kucoin.net;

import java.util.Properties;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestEndpointFactory;

import okhttp3.OkHttpClient;

public class KucoinOkHttpPrivateApiRestEndpointFactory implements RestEndpointFactory {
	
	public static final String API_KEY_PROPERTY = "apiKey";
	public static final String API_SECRET_PROPERTY = "apiSecret";
	public static final String API_PASSPHRASE_PROPERTY = "apiPassphrase";
	private Properties properties;
	private final OkHttpClient client = new OkHttpClient();
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		KucoinOkHttpPrivateApiRestEndpoint<R, A> endpoint = new KucoinOkHttpPrivateApiRestEndpoint<>(client, messageDeserializer);
		endpoint.setApiKey(properties.getProperty(API_KEY_PROPERTY));
		endpoint.setApiSecret(properties.getProperty(API_SECRET_PROPERTY));
		endpoint.setApiPassphrase(properties.getProperty(API_PASSPHRASE_PROPERTY));
		return endpoint;
	}

}
