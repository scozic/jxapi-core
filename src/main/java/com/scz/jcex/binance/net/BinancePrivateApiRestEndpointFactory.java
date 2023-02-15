package com.scz.jcex.binance.net;

import java.util.Properties;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointFactory;

public class BinancePrivateApiRestEndpointFactory implements RestEndpointFactory {
	
	public static final String API_KEY_PROPERTY = "apiKey";
	public static final String API_SECRET_PROPERTY = "apiSecret";
	private Properties properties;
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		BinancePrivateApiRestEndpoint<R, A> endpoint = new BinancePrivateApiRestEndpoint<>(messageDeserializer);
		endpoint.setApiKey(properties.getProperty(API_KEY_PROPERTY));
		endpoint.setApiSecret(API_SECRET_PROPERTY);
		return endpoint;
	}

	
}
