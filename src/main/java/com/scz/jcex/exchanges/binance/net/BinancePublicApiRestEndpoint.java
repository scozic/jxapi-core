package com.scz.jcex.exchanges.binance.net;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.netutils.rest.javaxnet.JavaxNetRestEndpoint;

public class BinancePublicApiRestEndpoint<R, A> extends JavaxNetRestEndpoint<R, A> {

	public BinancePublicApiRestEndpoint(MessageDeserializer<A> deserializer) {
		super(deserializer);
	}
	
	@Override
	protected String getBody(RestRequest<R> request) {
		return null;
	}

}
