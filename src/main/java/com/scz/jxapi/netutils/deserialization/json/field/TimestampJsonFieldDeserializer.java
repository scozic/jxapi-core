package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.util.JsonUtil;

public class TimestampJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Long> {
	
	private static final TimestampJsonFieldDeserializer INSTANCE = new TimestampJsonFieldDeserializer();
	
	public static TimestampJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private TimestampJsonFieldDeserializer() {}

	@Override
	public Long deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readCurrentLong(parser);
	}

}
