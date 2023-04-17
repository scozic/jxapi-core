package com.scz.jcex.netutils.deserialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

public interface JsonDeserializer<T> {

	T deserialize(JsonParser parser) throws IOException;
}
