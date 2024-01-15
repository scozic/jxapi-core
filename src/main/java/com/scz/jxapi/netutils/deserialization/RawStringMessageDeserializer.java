package com.scz.jxapi.netutils.deserialization;

/**
 * 
 */
public class RawStringMessageDeserializer implements MessageDeserializer<String> {
	
	public static final RawStringMessageDeserializer INSTANCE = new RawStringMessageDeserializer();

	@Override
	public String deserialize(String msg) {
		return msg;
	}

}
