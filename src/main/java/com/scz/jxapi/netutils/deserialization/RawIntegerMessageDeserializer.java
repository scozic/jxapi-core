package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain integer values.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 */
public class RawIntegerMessageDeserializer implements MessageDeserializer<Integer> {
	
	private static final RawIntegerMessageDeserializer INSTANCE = new RawIntegerMessageDeserializer();
	
	public static RawIntegerMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public Integer deserialize(String msg) {
		if (msg == null) {
			return null;
		}
		return Integer.valueOf(msg);
	}

}
