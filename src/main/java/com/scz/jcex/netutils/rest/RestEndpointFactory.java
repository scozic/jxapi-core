package com.scz.jcex.netutils.rest;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;

public interface RestEndpointFactory {

	<R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer); 
}
