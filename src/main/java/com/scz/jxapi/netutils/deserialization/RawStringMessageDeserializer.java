package com.scz.jxapi.netutils.deserialization;

import com.scz.jxapi.generator.exchange.ResponseDataType;

/**
 * For endpoints using {@link ResponseDataType#STRING} raw message type, this
 * deserializer is used, which does not actually deserialze raw message body and
 * returns it as is.
 */
public class RawStringMessageDeserializer implements MessageDeserializer<String> {
	
	public static final RawStringMessageDeserializer INSTANCE = new RawStringMessageDeserializer();

	@Override
	public String deserialize(String msg) {
		return msg;
	}

}
