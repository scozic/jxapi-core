package com.scz.jxapi.exchanges.binance.net;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.netutils.rest.javaxnet.JavaxNetRestEndpoint;

public class BinancePublicApiRestEndpoint<R, A> extends JavaxNetRestEndpoint<R, A> {

	public BinancePublicApiRestEndpoint(MessageDeserializer<A> deserializer) {
		super(deserializer);
	}
	
	@Override
	protected String getBody(RestRequest<R> request) {
		return null;
	}

}
