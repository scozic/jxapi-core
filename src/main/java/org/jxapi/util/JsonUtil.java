package org.jxapi.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jxapi.netutils.deserialization.json.JsonDeserializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Helper methods around JSON serialization/deserialization.
 */
public class JsonUtil {

  private JsonUtil() {
  }
  
  private static final SimpleModule EXCEPTION_SERIALIZATION_MODULE = createExceptionSerializationModule();
  
  /***
   * A default {@link ObjectMapper} instance with default configuration for JSON
   * serialization.
   * <p>
   * The instance is configured with:
   * <ul>
   * <li>{@link SerializationFeature#FAIL_ON_EMPTY_BEANS} disabled</li>
   * <li>{@link SerializationFeature#ORDER_MAP_ENTRIES_BY_KEYS} enabled</li>
   * <li>{@link Include#NON_NULL} serialization inclusion</li>
   * <li>Registration of module to serialize exceptions as readable strings</li>
   * <li>{@link MapperFeature#SORT_PROPERTIES_ALPHABETICALLY} enabled</li>
   * </ul>
   * 
   * Use this mapper as a singleton wherever possible to avoid the overhead of 
   * creating multiple instances.
   * But in case you need a different configuration, use {@link #createDefaultObjectMapper()}
   * to create a new instance with the same default configuration you can overwrite.
   */
  public static final ObjectMapper DEFAULT_OBJECT_MAPPER = createDefaultObjectMapper();
  
  /****
   * A default {@link JsonFactory} instance with default configuration for JSON
   * serialization.
   * <p>
   * The instance is created from {@link #DEFAULT_OBJECT_MAPPER}.
   * 
   * Use this factory as a singleton wherever possible to avoid the overhead of
   * creating multiple instances.
   */
  public static final JsonFactory DEFAULT_JSON_FACTORY = DEFAULT_OBJECT_MAPPER.getFactory();
  
  private static SimpleModule createExceptionSerializationModule() {
    SimpleModule m = new SimpleModule();
    m.addSerializer(new ExceptionToStringJsonSerializer());
    return m;
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
  public static ObjectMapper createDefaultObjectMapper() {

    return JsonMapper.builder()
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
      .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
      .defaultPropertyInclusion(
          JsonInclude.Value.construct(Include.NON_NULL, Include.NON_NULL)
        )
      .addModule(EXCEPTION_SERIALIZATION_MODULE)
      .build();
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
    return pojoToJsonString(pojo, DEFAULT_OBJECT_MAPPER);
  }
  
  /**
   * Converts a POJO to a JSON String using provided {@link ObjectMapper}.  
   * 
   * @param pojo POJO to convert
   * @param objectMapper the {@link ObjectMapper} to use for serialization
   * @return The JSON string representation of the POJO, or <code>null</code> if the POJO is <code>null</code>.
   * @throws IllegalArgumentException If an error occurs during serialization
   */
  public static String pojoToJsonString(Object pojo, ObjectMapper objectMapper) {
    if (pojo == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(pojo);
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
   * String. The parser is expected to be positioned on the value to read. The
   * current token is expected to be a {@link JsonToken#VALUE_STRING} or a
   * {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the next token is {@link JsonToken#VALUE_NULL},
   *         the String value otherwise.
   * @throws IOException Eventually thrown by the parser
   * 
   * @see JsonParser#nextToken()
   * @see #readCurrentString(JsonParser)
   */
  public static String readNextString(JsonParser parser) throws IOException {
    parser.nextToken();
    return readCurrentString(parser);
  }

  /**
   * Reads (and consumes) the next token from the parser and returns it as a
   * String. The parser is expected to be positioned on the value to read. The
   * current token is expected to be a {@link JsonToken#VALUE_STRING} or a
   * {@link JsonToken#VALUE_NULL}.
   * 
   * @param parser The parser to read from
   * @return <code>null</code> if the next token is {@link JsonToken#VALUE_NULL},
   *         the String value otherwise.
   * @throws IOException Eventually thrown by the parser
   * 
   * @see JsonParser#nextToken()
   */
  public static String readCurrentString(JsonParser parser) throws IOException {
    switch (parser.currentToken()) {
    case VALUE_STRING, VALUE_NUMBER_INT, VALUE_NUMBER_FLOAT, VALUE_FALSE, VALUE_TRUE:
      return parser.getText();
    case VALUE_NULL:
      return null;
    default:
      // Array or object, or other token
      // Write a string containing the whole json of array or object
      return DEFAULT_OBJECT_MAPPER.writeValueAsString(new JsonSerializableFromParser(parser));
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

    List<T> res = CollectionUtil.createList();
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

    Map<String, T> res = CollectionUtil.createMap();
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.currentName();
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
      case START_ARRAY, START_OBJECT:
        jsonParser.skipChildren();
        break;
      default:
        break;
    }
  }
  
  public static Object readNextObject(JsonParser parser) throws IOException {
    parser.nextToken();
    return readCurrentObject(parser);
  }
  
  /**
   * Reads the current token from the parser and returns it as a generic Object.
   * The parser is expected to be positioned on the value to read.
   * <p>
   * The returned object type depends on the JSON structure:
   * <ul>
   * <li>JSON number (integer) - returns {@link Integer}</li>
   * <li>JSON number (floating point) - returns {@link Double}</li>
   * <li>JSON string - returns {@link String}</li>
   * <li>JSON object - returns {@link Map}&lt;String, Object&gt;</li>
   * <li>JSON array - returns {@link List}&lt;Object&gt;</li>
   * <li>JSON boolean - returns {@link Boolean}</li>
   * <li>JSON null - returns <code>null</code></li>
   * </ul>
   * <br>
   * In case of JSON object or array, the method is called recursively to
   * construct the full structure.
   * @param parser The parser to read from
   * @return The deserialized object
   * @throws IOException Eventually thrown by the parser
   */
  public static Object readCurrentObject(JsonParser parser) throws IOException {
    JsonToken token = parser.currentToken();
    if (token == null) {
        token = parser.nextToken();
    }
    switch (token) {
        case VALUE_NUMBER_INT:
            return parser.getIntValue();
        case VALUE_NUMBER_FLOAT:
            return parser.getDoubleValue();
        case VALUE_STRING:
            return parser.getText();
        case START_OBJECT:
            Map<String, Object> map = CollectionUtil.createMap();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.currentName();
                map.put(fieldName, readNextObject(parser));
            }
            return map;
        case START_ARRAY:
            List<Object> list = CollectionUtil.createList();
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                list.add(readCurrentObject(parser));
            }
            return list;
        case VALUE_TRUE, VALUE_FALSE:
            return parser.getBooleanValue();
        case VALUE_NULL:
            return null;
        default:
            throw new IllegalArgumentException("Unsupported JSON token: " + token);
    }
  }
  
  public static <T> T readNextObject(JsonParser parser, Class<T> clazz) throws IOException {
    parser.nextToken();
    return readCurrentObject(parser, clazz);
  }
  
  public static <T> T readCurrentObject(JsonParser parser, Class<T> clazz) throws IOException {
    return parser.readValueAs(clazz);
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
    if (value != null) {
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
  
  /**
   * Writes a field with a custom serializer to the JSON generator, only if the
   * value is not null.
   * 
   * @param gen             the JSON generator to write to
   * @param fieldName       the name of the field to write
   * @param value           the value of the field to write
   * @param valueSerializer the custom serializer to use for the value
   * @param provider        the serializer provider
   * @param <T>             the type of the value
   * @throws IOException If an error occurs while writing to the generator
   */
  public static <T> void writeCustomSerializerField(JsonGenerator gen, String fieldName, T value, StdSerializer<T> valueSerializer, SerializerProvider provider) throws IOException {
    if (value != null) {
      gen.writeFieldName(fieldName);
      valueSerializer.serialize(value,  gen,  provider);
    }
  }
  
  /**
   * Writes a BigDecimal value to the JSON generator.
   * 
   * @param gen   the JSON generator to write to
   * @param value the value to write, if <code>null</code> a JSON null value will be written
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeBigDecimalValue(JsonGenerator gen, BigDecimal value) throws IOException {
    if (value != null) {
      gen.writeNumber(value);
    } else {
      gen.writeNull();
    }
  }
  
  /**
   * Writes a Boolean value to the JSON generator.
   * 
   * @param gen   the JSON generator to write to
   * @param value the value to write, if <code>null</code> a JSON null value will
   *              be written
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeBooleanValue(JsonGenerator gen, Boolean value) throws IOException {
    if (value != null) {
      gen.writeBoolean(value);
    } else {
      gen.writeNull();
    }
  }
  
  /**
   * Writes an Object value to the JSON generator.
   * 
   * @param gen   the JSON generator to write to
   * @param value the value to write, if <code>null</code> a JSON null value will
   *              be written
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeObjectValue(JsonGenerator gen, Object value) throws IOException {
    if (value != null) {
      gen.writeObject(value);
    } else {
      gen.writeNull();
    }
  }
  
  /**
   * Writes an Integer value to the JSON generator.
   * 
   * @param gen   the JSON generator to write to
   * @param value the value to write, if <code>null</code> a JSON null value will
   *              be written
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeIntegerValue(JsonGenerator gen, Integer value) throws IOException {
    if (value != null) {
      gen.writeNumber(value);
    } else {
      gen.writeNull();
    }
  }
  
  /**
   * Writes an Long value to the JSON generator.
   * 
   * @param gen   the JSON generator to write to
   * @param value the value to write, if <code>null</code> a JSON null value will
   *              be written
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeLongValue(JsonGenerator gen, Long value) throws IOException {
    if (value != null) {
      gen.writeNumber(value);
    } else {
      gen.writeNull();
    }
  }
  
  /**
   * Writes a String value to the JSON generator.
   * 
   * @param gen   the JSON generator to write to
   * @param value the value to write, if <code>null</code> a JSON null value will
   *              be written
   * @throws IOException If an error occurs while writing to the generator
   */
  public static void writeStringValue(JsonGenerator gen, String value) throws IOException {
    if (value != null) {
      gen.writeString(value);
    } else {
      gen.writeNull();
    }
  }
  
  /**
   * A wrapper for a {@link JsonParser} that can be serialized to JSON. The
   * serializer will copy the current structure of the parser into the JSON
   * output. This is useful when you want to serialize JSON from a parser that is already
   * positioned on a value, and you want to include that value in the JSON output.
   * @see JsonGenerator#copyCurrentStructure(JsonParser)
   */
  @JsonSerialize(using = JsonSerializableFromParserSerializer.class)
  public static class JsonSerializableFromParser {
    
    private final JsonParser parser;
    
    /**
     * Creates a new instance of {@link JsonSerializableFromParser} with the given
     * parser.
     * 
     * @param parser The parser to wrap, must not be <code>null</code>.
     */
    public JsonSerializableFromParser(JsonParser parser) {
      this.parser = parser;
    }
    
    /**
     * Returns the parser wrapped by this instance.
     * 
     * @return The parser wrapped by this instance.
     */
    public JsonParser getParser() {
      return parser;
    }
  }
  
  /**
   * Custom serializer for {@link JsonSerializableFromParser} that copies the
   * current structure of the parser into the JSON output.
   */
  public static class JsonSerializableFromParserSerializer extends StdSerializer<JsonSerializableFromParser> {

    private static final long serialVersionUID = -8453149308374664479L;

	/**
     * Creates a new {@link JsonSerializableFromParserSerializer} instance, suitable
     * for serializing {@link JsonSerializableFromParser} instances.
     */
    public JsonSerializableFromParserSerializer() {
      super(JsonSerializableFromParser.class);
    }

    @Override
    public void serialize(JsonSerializableFromParser value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      if (value.getParser() != null) {
        gen.copyCurrentStructure(value.getParser());
      }
    }
    
  }

}
