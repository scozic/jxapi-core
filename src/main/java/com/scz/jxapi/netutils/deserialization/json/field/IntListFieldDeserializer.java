package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;

public class IntListFieldDeserializer extends AbstractJsonMessageDeserializer<List<Integer>> {
		
		private static final IntListFieldDeserializer INSTANCE = new IntListFieldDeserializer();

		public static final IntListFieldDeserializer getInstance() {
			return INSTANCE;
		}
		
		private IntListFieldDeserializer() {};
		
		@Override
		public List<Integer> deserialize(JsonParser parser) throws IOException {
			if (parser.currentToken() == JsonToken.VALUE_NULL) {
				return null;
			}
			if (parser.currentToken() != JsonToken.START_ARRAY) {
				throw new IllegalStateException("Expecting start array of String, got:" + parser.currentToken() + " with name:" + parser.currentName());
			}
			
			List<Integer> res = new ArrayList<>();
	        for (parser.nextToken(); parser.currentToken() != JsonToken.END_ARRAY; parser.nextToken()) {
	            res.add(Integer.valueOf(parser.getText()));
	        }
			return res;
		}

}
