package com.scz.jxapi.netutils.deserialization;

public class RawLongMessageDeserializer implements MessageDeserializer<Long> {
	
	private static final RawLongMessageDeserializer INSTANCE = new RawLongMessageDeserializer();
	
	public static RawLongMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public Long deserialize(String msg) {
		return Long.valueOf(msg);
	}

}
