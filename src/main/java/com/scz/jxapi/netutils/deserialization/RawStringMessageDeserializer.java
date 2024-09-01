package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain String values. This deserializer does not perform any conversion.
 */
public class RawStringMessageDeserializer implements MessageDeserializer<String> {
	
	private static final RawStringMessageDeserializer INSTANCE = new RawStringMessageDeserializer();
	
	public static final RawStringMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public String deserialize(String msg) {
		return msg;
	}

}
