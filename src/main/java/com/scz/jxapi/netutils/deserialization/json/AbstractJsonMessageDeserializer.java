package com.scz.jxapi.netutils.deserialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;

public abstract class AbstractJsonMessageDeserializer<T> implements MessageDeserializer<T>, JsonDeserializer<T> {
	
	private final JsonFactory jsonFactory = new JsonFactory();
	
	@Override
	public T deserialize(String msg) {
		try {
			JsonParser parser = jsonFactory.createParser(msg.getBytes());
			parser.nextToken();
			return deserialize(parser);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error parsing JSON:[" + msg + "]", e);
		} 
	}
	
}
