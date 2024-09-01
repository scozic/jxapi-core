package com.scz.jxapi.netutils.deserialization;

import java.math.BigDecimal;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Deserializer for plain BigDecimal values.
 */
public class RawBigDecimalMessageDeserializer implements MessageDeserializer<BigDecimal> {
	
	private static final RawBigDecimalMessageDeserializer INSTANCE = new RawBigDecimalMessageDeserializer();
	
	public static RawBigDecimalMessageDeserializer getInstance() {
		return INSTANCE;
	}

	@Override
	public BigDecimal deserialize(String msg) {
		return EncodingUtil.toBigDecimal(msg);
	}

}
