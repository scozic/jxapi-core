package com.scz.jxapi.exchanges.kucoin.net;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestEndpointFactory;
import com.scz.jxapi.netutils.rest.javaxnet.JavaxNetRestEndpoint;

public class KucoinPublicApiRestEndpointFactory  implements RestEndpointFactory {

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		return new JavaxNetRestEndpoint<>(messageDeserializer);
	}

	
}
