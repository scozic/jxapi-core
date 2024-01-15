package com.scz.jxapi.netutils.deserialization;

public class RawBooleanMessageDeserializer implements MessageDeserializer<Boolean> {
	
	private static final RawBooleanMessageDeserializer INSTANCE = new RawBooleanMessageDeserializer();
	
	public static RawBooleanMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public Boolean deserialize(String msg) {
		return Boolean.valueOf(msg);
	}

}
