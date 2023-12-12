package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.JsonDeserializer;

public class ListFieldDeserializer<T> extends AbstractJsonMessageDeserializer<List<T>> {
	
	protected final JsonDeserializer<T> itemDeserializer;
	
	public ListFieldDeserializer(JsonDeserializer<T> itemDeserializer) {
		this.itemDeserializer = itemDeserializer;
	}

	@Override
	public List<T> deserialize(JsonParser parser) throws IOException {
		if (parser.currentToken() == JsonToken.VALUE_NULL) {
			return null;
		}
		if (parser.currentToken() != JsonToken.START_ARRAY) {
			throw new IllegalStateException("Expecting start array of String, got:" + parser.currentToken() + " with name:" + parser.currentName());
		}
		
		List<T> res = new ArrayList<>();
        for (parser.nextToken(); parser.currentToken() != JsonToken.END_ARRAY; parser.nextToken()) {
            res.add(itemDeserializer.deserialize(parser));
        }
		return res;
	}

}
