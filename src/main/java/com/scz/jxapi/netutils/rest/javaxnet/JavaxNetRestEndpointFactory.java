package com.scz.jxapi.netutils.rest.javaxnet;

import java.util.Properties;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestEndpointFactory;

public class JavaxNetRestEndpointFactory implements RestEndpointFactory {

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		return new JavaxNetRestEndpoint<>(messageDeserializer);
	}

	@Override
	public void setProperties(Properties properties) {
		// Nothing
	}

}
