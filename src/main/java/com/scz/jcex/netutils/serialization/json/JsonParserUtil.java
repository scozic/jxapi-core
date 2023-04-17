package com.scz.jcex.netutils.serialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

/**
 * Helper static methods around {@link JsonParser}
 */
public class JsonParserUtil {

	private JsonParserUtil() {}
	
	/**
	 * When {@link JsonParser#currentName()} stands for an unkown/unsupported type during deserialization, this method can be used to skip its value.
	 * Parser will be moved to token next to value found after calling {@link JsonParser#nextToken()} on provided instance.
	 * @throws IOException 
	 * 
	 */
	public static void skipNextValue(JsonParser jsonParser) throws IOException {
		switch (jsonParser.nextToken()) {
		case START_ARRAY:
		case START_OBJECT:
			jsonParser = jsonParser.skipChildren();
			break;
		case VALUE_FALSE:
		case VALUE_NULL:
		case VALUE_NUMBER_FLOAT:
		case VALUE_NUMBER_INT:
		case VALUE_STRING:
		case VALUE_TRUE:
			break;
		default:
			throw new IllegalStateException("Unexpected JsonToken:" + jsonParser.currentToken());
		}
	}
}
