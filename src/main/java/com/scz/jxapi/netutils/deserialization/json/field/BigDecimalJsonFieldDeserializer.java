package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.util.JsonUtil;

public class BigDecimalJsonFieldDeserializer extends AbstractJsonMessageDeserializer<BigDecimal> {

	private static final BigDecimalJsonFieldDeserializer INSTANCE = new BigDecimalJsonFieldDeserializer();
	
	public static BigDecimalJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private BigDecimalJsonFieldDeserializer() {}

	@Override
	public BigDecimal deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readNextBigDecimal(parser);
	}
}
