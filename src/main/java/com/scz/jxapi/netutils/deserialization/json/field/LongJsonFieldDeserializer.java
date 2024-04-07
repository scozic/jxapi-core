package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.util.JsonUtil;

public class LongJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Long> {
	
	private static final LongJsonFieldDeserializer INSTANCE = new LongJsonFieldDeserializer();
	
	public static LongJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private LongJsonFieldDeserializer() {}

	@Override
	public Long deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readCurrentLong(parser);
	}

}
