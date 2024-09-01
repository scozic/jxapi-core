package com.scz.jxapi.netutils.deserialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

/**
 * Deserializer for JSON data using a Jackson {@link JsonParser}.
 * 
 * @param <T> the type of the deserialized object
 */
public interface JsonDeserializer<T> {

	T deserialize(JsonParser parser) throws IOException;
}
