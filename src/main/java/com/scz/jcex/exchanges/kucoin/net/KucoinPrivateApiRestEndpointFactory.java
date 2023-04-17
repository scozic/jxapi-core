package com.scz.jcex.exchanges.kucoin.net;

import java.util.Properties;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointFactory;

public class KucoinPrivateApiRestEndpointFactory implements RestEndpointFactory {
	
	public static final String API_KEY_PROPERTY = "apiKey";
	public static final String API_SECRET_PROPERTY = "apiSecret";
	public static final String API_PASSPHRASE_PROPERTY = "apiPassphrase";
	private Properties properties;
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		KucoinPrivateApiRestEndpoint<R, A> endpoint = new KucoinPrivateApiRestEndpoint<>(messageDeserializer);
		endpoint.setApiKey(properties.getProperty(API_KEY_PROPERTY));
		endpoint.setApiSecret(properties.getProperty(API_SECRET_PROPERTY));
		endpoint.setApiPassphrase(properties.getProperty(API_PASSPHRASE_PROPERTY));
		return endpoint;
	}

}
