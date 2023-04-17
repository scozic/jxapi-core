package com.scz.jcex.netutils.rest.javaxnet;

import java.util.Properties;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointFactory;

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
