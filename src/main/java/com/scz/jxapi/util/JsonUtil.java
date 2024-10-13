package com.scz.jxapi.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.netutils.deserialization.json.JsonDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;

/**
 * Helper methods around JSON serialization/deserialization.
 */
public class JsonUtil {
	
	private JsonUtil() {}
	
	private static class ExceptionSerializer extends StdSerializer<Exception> {
		
		public ExceptionSerializer() {
			super(Exception.class);
		}

		@Override
		public void serialize(Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeString(String.valueOf(value));
		}
	}

	public static String pojoToJsonString(Object pojo) { 
		if (pojo == null) {
			return null;
		}
		try {
			ObjectMapper om = new ObjectMapper();
			om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			om.setSerializationInclusion(Include.NON_NULL);
			SimpleModule exceptionSerialization = new SimpleModule(); 
			exceptionSerialization.addSerializer(new ExceptionSerializer());
			om.registerModule(exceptionSerialization);
			return om.writeValueAsString(pojo);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
		}
	}

	public static String pojoToPrettyPrintJson(Object pojo) {
		if (pojo == null) {
			return null;
		}
		try {
			ObjectMapper om = new ObjectMapper();
			om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			return om.writerWithDefaultPrettyPrinter().writeValueAsString(pojo).replaceAll("\\r", "");
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
		}
	}
	
	public static BigDecimal readNextBigDecimal(JsonParser parser) throws IOException {
		parser.nextToken();
		return readCurrentBigDecimal(parser);
	}
	
	public static BigDecimal readCurrentBigDecimal(JsonParser parser) throws IOException {
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
		return readCurrentInteger(parser);
	}
	
	public static Integer readCurrentInteger(JsonParser parser) throws IOException {
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
		return readCurrentLong(parser);
	}
	
	public static Long readCurrentLong(JsonParser parser) throws IOException {
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
	
	public static Boolean readNextBoolean(JsonParser parser) throws IOException {
		parser.nextToken();
		return readCurrentBoolean(parser);
	}
	
	public static Boolean readCurrentBoolean(JsonParser parser) throws IOException {
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
	
	public static <T> List<T> readCurrentList(JsonParser parser, JsonDeserializer<T> itemDeserializer) throws IOException {
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
	
	@Deprecated
	public List<Integer> parseIntegerJsonArray(String jsonArray) {
		return parseJsonArray(jsonArray, IntegerJsonFieldDeserializer.getInstance());
	}
	
	@Deprecated
	public List<BigDecimal> parseBigDecimalJsonArray(String jsonArray) {
		return parseJsonArray(jsonArray, BigDecimalJsonFieldDeserializer.getInstance());
	}
	
	@Deprecated
	public List<Boolean> parseBooleanJsonArray(String jsonArray) {
		return parseJsonArray(jsonArray, BooleanJsonFieldDeserializer.getInstance());
	}
	
	@Deprecated
	public List<Long> parseLongJsonArray(String jsonArray) {
		return parseJsonArray(jsonArray, LongJsonFieldDeserializer.getInstance());
	}
	
	@Deprecated
	public List<String> parseStringJsonArray(String jsonArray) {
		return parseJsonArray(jsonArray, StringJsonFieldDeserializer.getInstance());
	}
	
	private static <T> List<T> parseJsonArray(String jsonArray, JsonDeserializer<T> itemDeserializer) {
		ListJsonFieldDeserializer<T> deserializer = new ListJsonFieldDeserializer<>(itemDeserializer);
		return deserializer.deserialize(jsonArray);
	}

}
