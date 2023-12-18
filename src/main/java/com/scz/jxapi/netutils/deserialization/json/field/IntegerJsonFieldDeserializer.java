package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.util.JsonUtil;

public class IntegerJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Integer> {
	
	private static final IntegerJsonFieldDeserializer INSTANCE = new IntegerJsonFieldDeserializer();
	
	public static IntegerJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private IntegerJsonFieldDeserializer() {}

	@Override
	public Integer deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readNextInteger(parser);
	} 

}
