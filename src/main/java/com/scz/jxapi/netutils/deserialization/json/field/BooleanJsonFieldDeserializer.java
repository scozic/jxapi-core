package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.util.JsonUtil;

public class BooleanJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Boolean> {
	
	private static final BooleanJsonFieldDeserializer INSTANCE = new BooleanJsonFieldDeserializer();
	
	public static BooleanJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private BooleanJsonFieldDeserializer() {}

	@Override
	public Boolean deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readCurrentBoolean(parser);
	}

}
