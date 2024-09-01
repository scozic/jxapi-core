package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain boolean values.
 */
public class RawBooleanMessageDeserializer implements MessageDeserializer<Boolean> {
	
	private static final RawBooleanMessageDeserializer INSTANCE = new RawBooleanMessageDeserializer();
	
	public static RawBooleanMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public Boolean deserialize(String msg) {
		if (msg == null) {
			return null;
		}
		return Boolean.valueOf(msg);
	}

}
