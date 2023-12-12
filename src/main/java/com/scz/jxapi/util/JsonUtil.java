package com.scz.jxapi.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scz.jxapi.netutils.deserialization.json.JsonDeserializer;

/**
 * Helper methods around JSON serialization/deserialization.
 */
public class JsonUtil {
	
	private JsonUtil() {}

	public static String pojoToString(Object pojo) {
		return pojo.getClass().getSimpleName() + JsonUtil.pojoToJsonString(pojo); 
	}

	public static String pojoToJsonString(Object pojo) { 
		try {
			ObjectMapper om = new ObjectMapper();
			om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			return om.writeValueAsString(pojo);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
		}
	}

	public static BigDecimal readNextBigDecimal(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_NUMBER_FLOAT:
			return parser.getDecimalValue();
		case VALUE_STRING:
			return EncodingUtil.toBigDecimal(parser.getText());
		case VALUE_NULL:
			return null;
		case VALUE_NUMBER_INT:
			return new BigDecimal(parser.getLongValue());
		default:
			throw new JsonParseException(parser, "Expected floating point value, but got " + parser.currentToken());
		}
	}

	public static Integer readNextInteger(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_STRING:
			return Integer.valueOf(parser.getText());
		case VALUE_NULL:
			return null;
		case VALUE_NUMBER_INT:
			return Integer.valueOf(parser.getIntValue());
		default:
			throw new JsonParseException(parser, "Expected integer value, but got " + parser.currentToken());
		}
	}

	public static Long readNextLong(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_STRING:
			return Long.valueOf(parser.getText());
		case VALUE_NULL:
			return null;
		case VALUE_NUMBER_INT:
			return Long.valueOf(parser.getLongValue());
		default:
			throw new JsonParseException(parser, "Expected long value, but got " + parser.currentToken());
		}
	}
	
	public static <T> List<T> readNextList(JsonParser parser, JsonDeserializer<T> itemDeserializer) throws IOException {
		if (parser.currentToken() == JsonToken.VALUE_NULL) {
			return null;
		}
		if (parser.currentToken() != JsonToken.START_ARRAY) {
			throw new IllegalStateException("Expecting start array of items to be deserialized using " + itemDeserializer);
		}
		
		List<T> res = new ArrayList<>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            res.add(itemDeserializer.deserialize(parser));
        }
        
		return res;
	}
	
	public static <T> Map<String, T> readMap(JsonParser parser, JsonDeserializer<T> itemDeserializer) throws IOException {
		if (parser.currentToken() == JsonToken.VALUE_NULL) {
			return null;
		}
		if (parser.currentToken() != JsonToken.START_OBJECT) {
			throw new IllegalStateException("Expecting start object of map of string keys, and values objects to be deserialized using " 
											+ itemDeserializer 
											+ " but found:" 
											+ parser.currentToken());
		}
		
		Map<String, T> res = new HashMap<>();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
        	if (parser.currentToken() != JsonToken.FIELD_NAME) {
        		throw new IllegalStateException("Expecting field name in map of objects to be deserialized using " 
						+ itemDeserializer 
						+ " but found:" 
						+ parser.currentToken());
        	}
            String key = parser.getCurrentName();
            parser.nextToken();
            res.put(key, itemDeserializer.deserialize(parser));
        }
        
		return res;
	}

	public static Boolean readNextBoolean(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_TRUE:
			return Boolean.TRUE;
		case VALUE_FALSE:
			return Boolean.FALSE;
		case VALUE_NULL:
			return null;
		case VALUE_STRING:
			return Boolean.valueOf(parser.getText());
		default:
			throw new JsonParseException(parser, "Expected long value, but got " + parser.currentToken());
		}
	}

}
