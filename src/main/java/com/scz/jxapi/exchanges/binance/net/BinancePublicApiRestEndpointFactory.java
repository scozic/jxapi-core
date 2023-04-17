package com.scz.jxapi.exchanges.binance.net;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestEndpointFactory;

public class BinancePublicApiRestEndpointFactory implements RestEndpointFactory {

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {
		return new BinancePublicApiRestEndpoint<>(messageDeserializer);
	}

}
