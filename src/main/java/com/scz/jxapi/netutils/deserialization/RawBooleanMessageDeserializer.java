package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain boolean values.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 */
public class RawBooleanMessageDeserializer implements MessageDeserializer<Boolean> {
	
	private static final RawBooleanMessageDeserializer INSTANCE = new RawBooleanMessageDeserializer();
	
	private RawBooleanMessageDeserializer() {}

	/**
	 * @return the singleton instance of this class
	 */
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
