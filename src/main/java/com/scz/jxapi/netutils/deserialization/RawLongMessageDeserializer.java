package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain long values.
 */
public class RawLongMessageDeserializer implements MessageDeserializer<Long> {
	
	private static final RawLongMessageDeserializer INSTANCE = new RawLongMessageDeserializer();
	
	public static RawLongMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public Long deserialize(String msg) {
		if (msg == null) {
			return null;
		}
		return Long.valueOf(msg);
	}

}
