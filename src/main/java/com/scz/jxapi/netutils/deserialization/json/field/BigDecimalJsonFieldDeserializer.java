package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.JsonDeserializer;
import com.scz.jxapi.util.JsonUtil;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link BigDecimal} fields in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class BigDecimalJsonFieldDeserializer extends AbstractJsonMessageDeserializer<BigDecimal> {

	private static final BigDecimalJsonFieldDeserializer INSTANCE = new BigDecimalJsonFieldDeserializer();

	/**
	 * @return the singleton instance of this class
	 */
	public static BigDecimalJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}

	private BigDecimalJsonFieldDeserializer() {
	}

	@Override
	public BigDecimal deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readCurrentBigDecimal(parser);
	}
}
