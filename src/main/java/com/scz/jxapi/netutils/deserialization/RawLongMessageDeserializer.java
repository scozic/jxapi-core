package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain long values.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 */
public class RawLongMessageDeserializer implements MessageDeserializer<Long> {
	
	private static final RawLongMessageDeserializer INSTANCE = new RawLongMessageDeserializer();
	
	private RawLongMessageDeserializer() {}

	/**
	 * @return the singleton instance of this class
	 */
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
