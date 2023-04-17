package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.JsonDeserializer;

public class StructListFieldDeserializer<T> extends AbstractJsonMessageDeserializer<List<T>> {

	private final JsonDeserializer<T> itemDeserializer;
	
	public StructListFieldDeserializer(JsonDeserializer<T> structDeserializer) {
		this.itemDeserializer = structDeserializer;
	}
	@Override
	public List<T> deserialize(JsonParser parser) throws IOException {
		if (parser.currentToken() != JsonToken.START_ARRAY) {
			throw new IllegalStateException("Expecting start array of items to be deserialized using " + this.itemDeserializer);
		}
		
		List<T> res = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            res.add(itemDeserializer.deserialize(parser));
        }
        
		return res;
	}

}
