package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;

public class StringJsonFieldDeserializer extends AbstractJsonMessageDeserializer<String> {
	
	private static final StringJsonFieldDeserializer INSTANCE = new StringJsonFieldDeserializer();
	
	public static StringJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private StringJsonFieldDeserializer() {}

	@Override
	public String deserialize(JsonParser parser) throws IOException {
		return parser.nextTextValue();
	}

}
