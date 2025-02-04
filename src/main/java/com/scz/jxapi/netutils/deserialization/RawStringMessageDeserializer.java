package com.scz.jxapi.netutils.deserialization;

/**
 * Deserializer for plain String values. This deserializer does not perform any conversion.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 */
public class RawStringMessageDeserializer implements MessageDeserializer<String> {
	
	private static final RawStringMessageDeserializer INSTANCE = new RawStringMessageDeserializer();
	
	private RawStringMessageDeserializer() {}

	/**
	 * @return the singleton instance of this class
	 */
	public static final RawStringMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public String deserialize(String msg) {
		return msg;
	}

}
