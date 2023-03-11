package com.scz.jcex.exchanges.binance.net;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointFactory;
import com.scz.jcex.netutils.rest.javaxnet.JavaxNetRestEndpoint;

public class BinancePublicApiRestEndpointFactory implements RestEndpointFactory {

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		return new JavaxNetRestEndpoint<>(messageDeserializer);
	}

}
