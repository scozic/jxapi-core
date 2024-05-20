package com.scz.jxapi.netutils.deserialization;

public class RawIntegerMessageDeserializer implements MessageDeserializer<Integer> {
	
	private static final RawIntegerMessageDeserializer INSTANCE = new RawIntegerMessageDeserializer();
	
	public static RawIntegerMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer deserialize(String msg) {
		return Integer.valueOf(msg);
	}

}
