package com.scz.jcex.netutils.deserialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.scz.jcex.netutils.deserialization.MessageDeserializer;

public abstract class AbstractJsonMessageDeserializer<T> implements MessageDeserializer<T>, JsonDeserializer<T> {
	
	@Override
	public T deserialize(String msg) {
		try {
			JsonParser parser = new JsonFactory().createParser(msg.getBytes());
			return deserialize(parser);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error parsing JSON:[" + msg + "]", e);
		} 
	}
	
}
