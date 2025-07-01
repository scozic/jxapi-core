package org.jxapi.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxapi.netutils.deserialization.json.JsonDeserializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Helper methods around JSON serialization/deserialization.
 */
public class JsonUtil {

  private JsonUtil() {
  }
  
  private static final SimpleModule EXCEPTION_SERIALIZATION_MODULE = createExceptionSerializationModule();
  
  private static final ObjectMapper DEFAULT_OBJECT_MAPPER = createDefaultJsonToStringObjectMapper();
  
  private static SimpleModule createExceptionSerializationModule() {
    SimpleModule m = new SimpleModule();
    m.addSerializer(new ExceptionSerializer());
    return m;
  }

  /**
   * Custom serializer for {@link Exception} objects, that serializes them as a
   * readable string.
   * <p>
   * This serializer is registered in the default {@link ObjectMapper} returned by
   * {@link JsonUtil#createDefaultJsonToStringObjectMapper()}.
   */
  public static class ExceptionSerializer extends StdSerializer<Exception> {

    /**
     * Creates a new {@link ExceptionSerializer} instance.
     */
    public ExceptionSerializer() {
      super(Exception.class);
    }

    @Override
    public void serialize(Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeString(String.valueOf(value));
    }
  }
  
  /**
   * @return A new {@link ObjectMapper} instance with default configuration for
   *         JSON serialization. The returned instance is configured with:
   *         <ul>
   *         <li>{@link SerializationFeature#FAIL_ON_EMPTY_BEANS} disabled</li>
   *         <li>{@link SerializationFeature#ORDER_MAP_ENTRIES_BY_KEYS} enabled</li>
   *         <li>{@link Include#NON_NULL} serialization inclusion</li>
   *         <li>Registration of module to serialize exceptions as readable strings</li>
   *         <li>{@link MapperFeature#SORT_PROPERTIES_ALPHABETICALLY} enabled</li>
   *         </ul>
   */
  public static ObjectMapper createDefaultJsonToStringObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    om.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    om.setSerializationInclusion(Include.NON_NULL);
    om.registerModule(EXCEPTION_SERIALIZATION_MODULE);
    om.setConfig(om.getSerializationConfig().with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY));
    return om;
  }

  /**
   * @param pojo The pojo to serialize as a JSON string.
   * @return <code>null</code> if <code>pojo</code> is <code>null</code> or a JSON
   *         string representation of this pojo using
   *         {@link ObjectMapper#writeValueAsString(Object)}
   * @throws IllegalArgumentException wrapping {@link JsonProcessingException}
   *                                  eventually thrown by
   *                                  {@link ObjectMapper#writeValueAsString(Object)}
   */
  public static String pojoToJsonString(Object pojo) {
    if (pojo == null) {
      return null;
    }
    try {
      return DEFAULT_OBJECT_MAPPER.writeValueAsString(pojo);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(
          "Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
    }
  }

  /**
   * @param pojo The pojo to serialize as a pretty-printed JSON string.
   * @return <code>null</code> if <code>pojo</code> is <code>null</code> or a
   *         pretty-printed JSON string representation of this pojo using
   *         {@link ObjectMapper#writeValueAsString(Object)}. The pretty-printed
   *         JSON string is formatted with a line break after each property. The
   *         line breaks are system independent, using only Unix line breaks \n.
   * @throws IllegalArgumentException wrapping {@link JsonProcessingException}
   *                                  eventually thrown by
   *                                  {@link ObjectMapper#writeValueAsString(Object)}
   */
  public static String pojoToPrettyPrintJson(Object pojo) {
    if (pojo == null) {
      return null;
    }
    try {
      return DEFAULT_OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(pojo).replace("\r", "");
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(
          "Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
    }
  }

  /**
   * Reads (and consumes) the next token from the parser and returns it as a
   * BigDecimal. The parser is expected to be positioned on the value to read. The
   * current token is expected to be a {@link JsonToken#VALUE_NUMBER_FLOAT},
   * {@link JsonToken#VALUE_NUMBER_INT}, a {@link JsonToken#VALUE_STRING}
   * containing a number or a {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the next token is
   *         {@link JsonToken#VALUE_NULL}, the BigDecimal value otherwise.
   * @throws IOException           Eventually thrown by the parser
   * @throws NumberFormatException If the next token is a string that is not a
   *                               valid number
   * @see JsonParser#nextToken()
   */
  public static BigDecimal readNextBigDecimal(JsonParser parser) throws IOException {
    parser.nextToken();
    return readCurrentBigDecimal(parser);
  }

  /**
   * Reads the current token from the parser and returns it as a BigDecimal. The
   * parser is expected to be positioned on the value to read. The current token
   * is expected to be a {@link JsonToken#VALUE_NUMBER_FLOAT},
   * {@link JsonToken#VALUE_NUMBER_INT}, a {@link JsonToken#VALUE_STRING}
   * containing a number or a {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the current token is
   *         {@link JsonToken#VALUE_NULL}, the BigDecimal value otherwise.
   * @throws IOException           Eventually thrown by the parser
   * @throws NumberFormatException If the current token is a string that is not a
   *                               valid number
   */
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

  /**
   * Reads (and consumes) the next token from the parser and returns it as an
   * Integer. The parser is expected to be positioned on the value to read. The
   * current token is expected to be a {@link JsonToken#VALUE_NUMBER_INT}, a
   * {@link JsonToken#VALUE_STRING} containing a number or a
   * {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the next token is
   *         {@link JsonToken#VALUE_NULL}, the Integer value otherwise.
   * @throws IOException Eventually thrown by the parser
   * 
   * @see JsonParser#nextToken()
   * @throws IOException           Eventually thrown by the parser
   * @throws NumberFormatException If the next token is a string that is not a
   *                               valid integer
   */
  public static Integer readNextInteger(JsonParser parser) throws IOException {
    parser.nextToken();
    return readCurrentInteger(parser);
  }

  /**
   * Reads the current token from the parser and returns it as an Integer. The
   * parser is expected to be positioned on the value to read. The current token
   * is expected to be a {@link JsonToken#VALUE_NUMBER_INT}, a
   * {@link JsonToken#VALUE_STRING} containing a number or a
   * {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the current token is
   *         {@link JsonToken#VALUE_NULL}, the Integer value otherwise.
   * @throws IOException           Eventually thrown by the parser
   * @throws NumberFormatException If the current token is a string that is not a
   *                               valid integer
   */
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

  /**
   * Reads (and consumes) the next token from the parser and returns it as a
   * Long. The parser is expected to be positioned on the value to read. The
   * current token is expected to be a {@link JsonToken#VALUE_NUMBER_INT}, a
   * {@link JsonToken#VALUE_STRING} containing a number or a
   * {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the next token is
   *         {@link JsonToken#VALUE_NULL}, the Long value otherwise.
   * @throws IOException Eventually thrown by the parser
   * 
   * @see JsonParser#nextToken()
   * @throws IOException           Eventually thrown by the parser
   * @throws NumberFormatException If the next token is a string that is not a
   *                               valid long
   */
  public static Long readNextLong(JsonParser parser) throws IOException {
    parser.nextToken();
    return readCurrentLong(parser);
  }

  /**
   * Reads the current token from the parser and returns it as a Long. The parser
   * is expected to be positioned on the value to read. The current token is
   * expected to be a {@link JsonToken#VALUE_NUMBER_INT}, a
   * {@link JsonToken#VALUE_STRING} containing a number or a
   * {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the current token is
   *         {@link JsonToken#VALUE_NULL}, the Long value otherwise.
   * @throws IOException           Eventually thrown by the parser
   * @throws NumberFormatException If the current token is a string that is not a
   *                               valid long
   */
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

  /**
   * Reads (and consumes) the next token from the parser and returns it as a
   * Boolean. The parser is expected to be positioned on the value to read. The
   * current token is expected to be a {@link JsonToken#VALUE_TRUE},
   * {@link JsonToken#VALUE_FALSE}, a {@link JsonToken#VALUE_STRING} containing a
   * boolean or a {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the next token is
   *         {@link JsonToken#VALUE_NULL}, the Boolean value otherwise.
   * @throws IOException Eventually thrown by the parser
   * 
   * @see JsonParser#nextToken()
   */
  public static Boolean readNextBoolean(JsonParser parser) throws IOException {
    parser.nextToken();
    return readCurrentBoolean(parser);
  }

  /**
   * Reads the current token from the parser and returns it as a Boolean. The
   * parser is expected to be positioned on the value to read. The current token
   * is expected to be a {@link JsonToken#VALUE_TRUE},
   * {@link JsonToken#VALUE_FALSE}, a {@link JsonToken#VALUE_STRING} containing a
   * boolean or a {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the current token is
   *         {@link JsonToken#VALUE_NULL}, the Boolean value otherwise.
   * @throws IOException Eventually thrown by the parser
   */
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
        throw new JsonParseException(parser, "Expected boolean value, but got " + parser.currentToken());
    }
  }

  /**
   * Reads (and consumes) the next token from the parser and returns it as a
   * generic List of items. The parser is expected to be positioned on the start
   * of the list to read.
   * 
   * @param parser           The parser to read from
   * @param itemDeserializer The deserializer to use to read each item of the list
   * @param <T>              The type of items in the list
   * @return <code>null</code> if the next token is {@link JsonToken#VALUE_NULL},
   *         the List of items otherwise.
   * @throws IOException           Eventually thrown by the parser
   * @throws IllegalStateException If the current token is not
   *                               {@link JsonToken#START_ARRAY}
   */
  public static <T> List<T> readCurrentList(JsonParser parser, JsonDeserializer<T> itemDeserializer)
      throws IOException {
    if (parser.currentToken() == JsonToken.VALUE_NULL) {
      return null;
    }
    if (parser.currentToken() != JsonToken.START_ARRAY) {
      throw new IllegalStateException(
          "Expecting start array of items to be deserialized using " + itemDeserializer);
    }

    List<T> res = new ArrayList<>();
    while (parser.nextToken() != JsonToken.END_ARRAY) {
      res.add(itemDeserializer.deserialize(parser));
    }

    return res;
  }

  /**
   * Reads (and consumes) the next token from the parser and returns it as a
   * generic Map of items. The parser is expected to be positioned on the start of
   * the map to read.
   * 
   * @param parser           The parser to read from
   * @param itemDeserializer The deserializer to use to read each item of the map
   * @param <T>              The type of the values in the map
   * @return <code>null</code> if the next token is {@link JsonToken#VALUE_NULL},
   *         the Map of items otherwise.
   * @throws IOException           Eventually thrown by the parser
   * @throws IllegalStateException If the current token is not
   *                               {@link JsonToken#START_OBJECT}
   */
  public static <T> Map<String, T> readMap(JsonParser parser, JsonDeserializer<T> itemDeserializer)
      throws IOException {
    if (parser.currentToken() == JsonToken.VALUE_NULL) {
      return null;
    }
    if (parser.currentToken() != JsonToken.START_OBJECT) {
      throw new IllegalStateException(
          "Expecting start object of map of string keys, and values objects to be deserialized using "
              + itemDeserializer
              + " but found:"
              + parser.currentToken());
    }

    Map<String, T> res = new HashMap<>();
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      parser.nextToken();
      res.put(key, itemDeserializer.deserialize(parser));
    }

    return res;
  }

  /**
   * When {@link JsonParser#currentName()} stands for an unkown/unsupported type
   * during deserialization, this method can be used to skip its value.
   * Parser will be moved to token next to value found after calling
   * {@link JsonParser#nextToken()} on provided instance.
   * 
   * @param jsonParser The parser to read from
   * @throws IOException Eventually thrown by the parser
   * 
   */
  public static void skipNextValue(JsonParser jsonParser) throws IOException {
    switch (jsonParser.nextToken()) {
      case START_ARRAY:
      case START_OBJECT:
        jsonParser.skipChildren();
        break;
      default:
        break;
    }
  }
  
  /**
   * Writes a string field to the JSON generator, only if the value is not null.
   * 
   * @param gen       The JSON generator to write to
   * @param fieldName The name of the field to write
   * @param value     The value of the field to write
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeStringField(JsonGenerator gen, String fieldName, String value) throws IOException {
    if (value != null) {
      gen.writeStringField(fieldName, value);
    }
  }
  
  /**
   * Writes a string field to the JSON generator, only if the value is not null.
   * 
   * @param gen       The JSON generator to write to
   * @param fieldName The name of the field to write
   * @param value     The value of the field to write
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeObjectField(JsonGenerator gen, String fieldName, Object value) throws IOException {
    if (value != null) {
      gen.writeObjectField(fieldName, value);
    }
  }
  
  /**
   * Writes a boolean field to the JSON generator, only if the value is true.
   * @param gen the JSON generator to write to
   * @param fieldName the name of the field to write
   * @param value the value of the field to write
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeBooleanField(JsonGenerator gen, String fieldName, Boolean value) throws IOException {
    if (value != null && value) {
      gen.writeBooleanField(fieldName, value);
    }
  }
  
  /**
   * Writes a Long field to the JSON generator, only if the value is not null.
   * @param gen the JSON generator to write to
   * @param fieldName the name of the field to write
   * @param value the value of the field to write
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeLongField(JsonGenerator gen, String fieldName, Long value) throws IOException {
    if (value != null) {
      gen.writeNumberField(fieldName, value);
    }
  }
  
  /**
   * Writes a BigDecimal field to the JSON generator, only if the value is not null.
   * @param gen the JSON generator to write to
   * @param fieldName the name of the field to write
   * @param value the value of the field to write
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeBigDecimalField(JsonGenerator gen, String fieldName, BigDecimal value) throws IOException {
    if (value != null) {
      gen.writeNumberField(fieldName, value);
    }
  }
  
  /**
   * Writes a BigDecimal field to the JSON generator, only if the value is not null.
   * @param gen the JSON generator to write to
   * @param fieldName the name of the field to write
   * @param value the value of the field to write
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeIntField(JsonGenerator gen, String fieldName, Integer value) throws IOException {
    if (value != null) {
      gen.writeNumberField(fieldName, value);
    }
  }

}
