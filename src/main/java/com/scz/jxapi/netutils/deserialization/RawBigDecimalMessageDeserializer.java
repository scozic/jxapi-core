package com.scz.jxapi.netutils.deserialization;

import java.math.BigDecimal;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Deserializer for plain BigDecimal values.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 */
public class RawBigDecimalMessageDeserializer implements MessageDeserializer<BigDecimal> {
	
	private static final RawBigDecimalMessageDeserializer INSTANCE = new RawBigDecimalMessageDeserializer();
	
	private RawBigDecimalMessageDeserializer() {}
	
	/**
	 * @return the singleton instance of this class
	 */
	public static RawBigDecimalMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public BigDecimal deserialize(String msg) {
		return EncodingUtil.toBigDecimal(msg);
	}

}
