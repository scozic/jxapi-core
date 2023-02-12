package com.scz.jcex.netutils.deserialization.json.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;

public class StringListFieldDeserializer extends AbstractJsonMessageDeserializer<List<String>> {
	
	private static final StringListFieldDeserializer INSTANCE = new StringListFieldDeserializer();

	public static final StringListFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private StringListFieldDeserializer() {};
	
	@Override
	public List<String> deserialize(JsonParser parser) throws IOException {
		parser.nextToken();
		if (parser.currentToken() != JsonToken.START_ARRAY) {
			throw new IllegalStateException("Expecting start array of String, got:" + parser.currentToken() + " with name:" + parser.currentName());
		}
		
		List<String> res = new ArrayList<>();
        for (parser.nextToken(); parser.currentToken() != JsonToken.END_ARRAY; parser.nextToken()) {
            res.add(parser.getText());
        }
		return res;
	}

}
