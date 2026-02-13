package org.jxapi.util;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Unit test for {@link JsonUtil}
 */
public class JsonUtilTest {
  
  @Test
  public void testPojoToJsonString_NullPojo() {
    Assert.assertNull(JsonUtil.pojoToJsonString(null));
  }

  @Test
  public void testPojoToJsonString() {
    Foo foo = new Foo("foo", List.of(new Bar(1), new Bar(2)));
    foo.setException(new Exception("My exception!"));
    String expected = "{\"bars\":[{\"id\":1},{\"id\":2}],\"exception\":\"java.lang.Exception: My exception!\",\"name\":\"foo\"}";
    String result = JsonUtil.pojoToJsonString(foo);
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPojoToJsonString_InvalidPojo() {
    CursedBar cbar = new CursedBar(1);
    Foo foo = new Foo("foo", List.of(cbar));
    cbar.setFoo(foo);
    JsonUtil.pojoToJsonString(foo);
  }
  
  @Test
  public void testPojoToPrettyPrintJson_NullPojo() {
    Assert.assertNull(JsonUtil.pojoToPrettyPrintJson(null));
  }

  @Test
  public void testPojoToPrettyPrintJson() {
    Foo foo = new Foo("foo", List.of(new Bar(1), new Bar(2)));
    String expected = "{\n"
        + "  \"bars\" : [ {\n"
        + "    \"id\" : 1\n"
        + "  }, {\n"
        + "    \"id\" : 2\n"
        + "  } ],\n"
        + "  \"name\" : \"foo\"\n"
        + "}";
    String result = JsonUtil.pojoToPrettyPrintJson(foo);
    Assert.assertEquals(expected, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testPojoToPrettyPrintJson_InvalidPojo() {
    CursedBar cbar = new CursedBar(1);
    Foo foo = new Foo("foo", List.of(cbar));
    cbar.setFoo(foo);
    JsonUtil.pojoToPrettyPrintJson(foo);
  }
  
  // Read next / current String tests
  @Test
  public void testReadNextString_ValueTrueAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("true".getBytes());
    Assert.assertEquals(Boolean.TRUE.toString(), JsonUtil.readNextString(parser));
  }
  
  @Test
  public void testReadNextString_ValueStringAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"bar\"".getBytes());
    Assert.assertEquals("bar", JsonUtil.readNextString(parser));
  }
  
  @Test
  public void testReadNextString_ValueBigDecimalAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("123.45".getBytes());
    Assert.assertEquals("123.45", JsonUtil.readNextString(parser));
  }
  
  @Test
  public void testReadNextString_ValueIntAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("123".getBytes());
    Assert.assertEquals("123", JsonUtil.readNextString(parser));
  }
  
  @Test
  public void testReadNextString_ValueNullAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertNull(JsonUtil.readNextString(parser));
  }
  
  @Test
  public void testReadCurrentString_ValueTrueAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("true".getBytes());
    Assert.assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
    Assert.assertEquals(Boolean.TRUE.toString(), JsonUtil.readCurrentString(parser));
  }
  
  @Test
  public void testReadCurrentString_ValueStringAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"bar\"".getBytes());
    Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
    Assert.assertEquals("bar", JsonUtil.readCurrentString(parser));
  }
  
  @Test
  public void testReadCurrentString_ValueBigDecimalAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("123.45".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
    Assert.assertEquals("123.45", JsonUtil.readCurrentString(parser));
  }
  
  @Test
  public void testReadCurrentString_ValueIntAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("123".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
    Assert.assertEquals("123", JsonUtil.readCurrentString(parser));
  }
  
  @Test
  public void testReadCurrentString_ValueNullAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readCurrentString(parser));
  }
  
  @Test
  public void testReadCurrentString_ValueObjectAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("{\"x\":{\"foo\":\"bar\"}, \"y\":123}".getBytes());
    Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
    Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    Assert.assertEquals("{\"foo\":\"bar\"}", JsonUtil.readCurrentString(parser));
  }
  
  // Read next / current BigDecimal tests
  
  @Test
  public void testReadNextBigDecimal() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"12.34\"".getBytes());
    Assert.assertEquals(new BigDecimal("12.34"), JsonUtil.readNextBigDecimal(parser));
  }
  
  @Test
  public void testReadCurrentBigDecimal_ValueString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"12.34\"".getBytes());
    Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
    Assert.assertEquals(new BigDecimal("12.34"), JsonUtil.readCurrentBigDecimal(parser));
  }
  
  @Test
  public void testReadCurrentBigDecimal_ValueFloat() throws Exception {
    JsonParser parser = new JsonFactory().createParser("12.34".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
    Assert.assertEquals(new BigDecimal("12.34"), JsonUtil.readCurrentBigDecimal(parser));
  }
  
  @Test
  public void testReadCurrentBigDecimal_ValueInt() throws Exception {
    JsonParser parser = new JsonFactory().createParser("12".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
    Assert.assertEquals(new BigDecimal("12"), JsonUtil.readCurrentBigDecimal(parser));
  }
  
  @Test
  public void testReadCurrentBigDecimal_ValueNull() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readCurrentBigDecimal(parser));
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadCurrentBigDecimal_InvalidValue() throws Exception {
    JsonParser parser = new JsonFactory().createParser("[]".getBytes());
    parser.nextToken();
    JsonUtil.readCurrentBigDecimal(parser);
  }
  
  
  // Read next / current int tests
  
  @Test
  public void testReadNextInteger() throws Exception {
    JsonParser parser = new JsonFactory().createParser("12".getBytes());
    Assert.assertEquals(Integer.valueOf("12"), JsonUtil.readNextInteger(parser));
  }
  
  @Test
  public void testReadCurrentInteger_ValueString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"12\"".getBytes());
    Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
    Assert.assertEquals(Integer.valueOf("12"), JsonUtil.readCurrentInteger(parser));
  }
  
  @Test
  public void testReadCurrentInteger_ValueInt() throws Exception {
    JsonParser parser = new JsonFactory().createParser("12".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
    Assert.assertEquals(Integer.valueOf("12"), JsonUtil.readCurrentInteger(parser));
  }
  
  @Test
  public void testReadCurrentInteger_ValueNull() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readCurrentInteger(parser));
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadCurrentInteger_InvalidValue() throws Exception {
    JsonParser parser = new JsonFactory().createParser("[]".getBytes());
    parser.nextToken();
    JsonUtil.readCurrentInteger(parser);
  }
  
  
  // Read next / current Long tests
  
  @Test
  public void testReadNextLong() throws Exception {
    JsonParser parser = new JsonFactory().createParser("12".getBytes());
    Assert.assertEquals(Long.valueOf("12"), JsonUtil.readNextLong(parser));
  }
  
  @Test
  public void testReadCurrentLong_ValueString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"12\"".getBytes());
    Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
    Assert.assertEquals(Long.valueOf("12"), JsonUtil.readCurrentLong(parser));
  }
  
  @Test
  public void testReadCurrentLong_ValueInt() throws Exception {
    JsonParser parser = new JsonFactory().createParser("12".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
    Assert.assertEquals(Long.valueOf("12"), JsonUtil.readCurrentLong(parser));
  }
  
  @Test
  public void testReadCurrentLong_ValueNull() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readCurrentLong(parser));
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadCurrentLong_InvalidValue() throws Exception {
    JsonParser parser = new JsonFactory().createParser("[]".getBytes());
    parser.nextToken();
    JsonUtil.readCurrentLong(parser);
  }
  
  // Read next / current Boolean tests
  
  @Test
  public void testReadNextBoolean_ValueTrueAsString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("true".getBytes());
    Assert.assertEquals(Boolean.TRUE, JsonUtil.readNextBoolean(parser));
  }
  
  @Test
  public void testReadCurrentBoolean_ValueString() throws Exception {
    JsonParser parser = new JsonFactory().createParser("\"any\"".getBytes());
    Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
    Assert.assertEquals(Boolean.FALSE, JsonUtil.readCurrentBoolean(parser));
  }
  
  @Test
  public void testReadCurrentBoolean_ValueTrue() throws Exception {
    JsonParser parser = new JsonFactory().createParser("true".getBytes());
    Assert.assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
    Assert.assertEquals(Boolean.TRUE, JsonUtil.readCurrentBoolean(parser));
  }
  
  @Test
  public void testReadCurrentBoolean_ValueFalse() throws Exception {
    JsonParser parser = new JsonFactory().createParser("false".getBytes());
    Assert.assertEquals(JsonToken.VALUE_FALSE, parser.nextToken());
    Assert.assertEquals(Boolean.FALSE, JsonUtil.readCurrentBoolean(parser));
  }
  
  @Test
  public void testReadCurrentBoolean_ValueNull() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readCurrentBoolean(parser));
  }
  
  @Test(expected = JsonParseException.class)
  public void testReadCurrentBoolean_InvalidValue() throws Exception {
    JsonParser parser = new JsonFactory().createParser("[]".getBytes());
    parser.nextToken();
    JsonUtil.readCurrentBoolean(parser);
  }
  
  
  // Read current list tests
  
  @Test
  public void testReadCurrentList_NullList() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readCurrentList(parser, null));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testReadCurrentList_InvalidList() throws Exception {
    JsonParser parser = new JsonFactory().createParser("false".getBytes());
    parser.nextToken();
    JsonUtil.readCurrentList(parser, null);
  }
  
  @Test
  public void testReadCurrentList_IntegerList() throws Exception {
    JsonParser parser = new JsonFactory().createParser("[null, 1, 2, 5]".getBytes());
    Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
    List<Integer> l = JsonUtil.readCurrentList(parser, IntegerJsonFieldDeserializer.getInstance());
    Assert.assertEquals(4, l.size());
    Assert.assertNull(l.get(0));
    Assert.assertEquals(Integer.valueOf(1), l.get(1));
    Assert.assertEquals(Integer.valueOf(2), l.get(2));
    Assert.assertEquals(Integer.valueOf(5), l.get(3));
  }
  
  @Test
  public void testReadMap_NullMap() throws Exception {
    JsonParser parser = new JsonFactory().createParser("null".getBytes());
    Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    Assert.assertNull(JsonUtil.readMap(parser, null));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testReadMap_NotAnObject() throws Exception {
    JsonParser parser = new JsonFactory().createParser("[]".getBytes());
    parser.nextToken();
    JsonUtil.readMap(parser, null);
  }
  
  @Test
  public void testReadMap_EmptyMap() throws Exception {
    JsonParser parser = new JsonFactory().createParser("{}".getBytes());
    Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    Assert.assertEquals(0, JsonUtil.readMap(parser, null).size());
  }

  @Test
  public void testReadMap_IntegerMap() throws Exception {
    JsonParser parser = new JsonFactory().createParser("{\"a\": \"1\", \"b\": \"2\"}".getBytes());
    Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    Map<String, Integer> m = JsonUtil.readMap(parser, IntegerJsonFieldDeserializer.getInstance());
    Assert.assertEquals(2, m.size());
    Assert.assertEquals(Integer.valueOf(1), m.get("a"));
    Assert.assertEquals(Integer.valueOf(2), m.get("b"));    
  }
  
  @Test
  public void testSkipNextValue() throws Exception {
    doTestSkipNextPrimitiveValue("false");
    doTestSkipNextPrimitiveValue("true");
    doTestSkipNextPrimitiveValue("null");
    doTestSkipNextPrimitiveValue("3");
    doTestSkipNextPrimitiveValue("3.45");
    doTestSkipNextPrimitiveValue("\"foo\"");
    doTestSkipNextPrimitiveValue("[1, 2, 3]");
    doTestSkipNextPrimitiveValue("{\"x\": 1, \"y\": 0}");
  }
  
  @Test
  public void testCreateDefaultJsonToStringObjectMapper() {
    Assert.assertNotNull(JsonUtil.createDefaultObjectMapper());
  }
  
  @Test
  public void testWriteCustomSerializedField() {
    Foo foo = new Foo("afoo", List.of(new Bar(1), new Bar(2)));
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeCustomSerializerField(gen, "theFooNull", null, new FooToStringJsonSerializer(), null);
      JsonUtil.writeCustomSerializerField(gen, "theFoo", foo, new FooToStringJsonSerializer(), null);
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"theFoo\":{\"name\":\"afoo\",\"bars\":[{\"id\":1},{\"id\":2}]}}", writer.toString());
  }
  
  @Test
  public void testReadNextObject_Primitives() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
      JsonParser parser = objectMapper.getFactory().createParser("123");
      Assert.assertEquals(123, JsonUtil.readNextObject(parser));

      parser = objectMapper.getFactory().createParser("123.45");
      Assert.assertEquals(123.45, JsonUtil.readNextObject(parser));

      parser = objectMapper.getFactory().createParser("\"test\"");
      Assert.assertEquals("test", JsonUtil.readNextObject(parser));

      parser = objectMapper.getFactory().createParser("true");
      Assert.assertEquals(true, JsonUtil.readNextObject(parser));

      parser = objectMapper.getFactory().createParser("null");
      Assert.assertEquals(null, JsonUtil.readNextObject(parser));
  }
  
  @Test
  public void testWriteIntField() {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeIntField(gen, "fooNull", null);
      JsonUtil.writeIntField(gen, "foo", 42);
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"foo\":42}", writer.toString());
  }
  
  @Test
  public void testWriteLongField() {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeLongField(gen, "fooNull", null);
      JsonUtil.writeLongField(gen, "foo", 42L);
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"foo\":42}", writer.toString());
  }
  
  @Test
  public void testWriteBigDecimalField() {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeBigDecimalField(gen, "fooNull", null);
      JsonUtil.writeBigDecimalField(gen, "foo", new BigDecimal("42.78"));
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"foo\":42.78}", writer.toString());
  }
  
  @Test
  public void testWriteBooleanField() {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeBooleanField(gen, "fooNull", null);
      JsonUtil.writeBooleanField(gen, "foo", true);
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"foo\":true}", writer.toString());
  }
  
  @Test
  public void testWriteStringField() {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeStringField(gen, "fooNull", null);
      JsonUtil.writeStringField(gen, "foo", "bar");
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"foo\":\"bar\"}", writer.toString());
  }
  
  @Test
  public void testWriteObjectField() {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      gen.writeStartObject();
      JsonUtil.writeObjectField(gen, "fooNull", null);
      JsonUtil.writeObjectField(gen, "foo", new Foo("bar", List.of(new Bar(1))));
      gen.writeEndObject();
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"foo\":{\"bars\":[{\"id\":1}],\"name\":\"bar\"}}", writer.toString());
  }


  @SuppressWarnings("unchecked")
  @Test
  public void testReadNextObject_ComplexTypes() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
      JsonParser parser = objectMapper.getFactory().createParser("{\"key\":\"value\",\"number\":42}");
      Map<String, Object> map = (Map<String, Object>) JsonUtil.readNextObject(parser);
      Assert.assertEquals(2, map.size());
      Assert.assertEquals("value", map.get("key"));
      Assert.assertEquals(42, map.get("number"));

      parser = objectMapper.getFactory().createParser("[1,2,3]");
      List<Object> list = (List<Object>) JsonUtil.readNextObject(parser);
      Assert.assertEquals(3, list.size());
      Assert.assertEquals(1, list.get(0));
      Assert.assertEquals(2, list.get(1));
      Assert.assertEquals(3, list.get(2));
  }
  
  @Test
  public void testWriteStringValue() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeStringValue(gen, "bar");
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("\"bar\"", writer.toString());
  }
  
  @Test
  public void testWriteStringValue_Null() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeStringValue(gen, null);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("null", writer.toString());
  }
  
  @Test
  public void testWriteIntValue() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeIntegerValue(gen, 123);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("123", writer.toString());
  }
  
  @Test
  public void testWriteIntValue_Null() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeIntegerValue(gen, null);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("null", writer.toString());
  }
  
  @Test
  public void testWriteLongValue() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeLongValue(gen, 1377785454545L);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("1377785454545", writer.toString());
  }
  
  @Test
  public void testWriteLongValue_Null() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeLongValue(gen, null);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("null", writer.toString());
  }
  
  @Test
  public void testWriteBigDecimalValue() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeBigDecimalValue(gen, new BigDecimal("12345.6789"));
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("12345.6789", writer.toString());
  }
  
  @Test
  public void testWriteBigDecimalValue_Null() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeBigDecimalValue(gen, null);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("null", writer.toString());
  }
  
  @Test
  public void testWriteBooleanValue() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeBooleanValue(gen, true);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("true", writer.toString());
  }
  
  @Test
  public void testWriteBooleanValue_Null() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeBooleanValue(gen, null);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("null", writer.toString());
  }
  
  @Test
  public void testWriteObjectValue() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeObjectValue(gen, new Foo("bar", List.of(new Bar(1))));
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("{\"bars\":[{\"id\":1}],\"name\":\"bar\"}", writer.toString());
  }
  
  @Test
  public void testWriteObjectValue_Null() throws IOException {
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      JsonUtil.writeObjectValue(gen, null);
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }

    Assert.assertEquals("null", writer.toString());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testReadCurrentObject_NestedStructures() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
      JsonParser parser = objectMapper.getFactory().createParser("{\"list\":[1,2,{\"nestedKey\":\"nestedValue\"}]}");
      Map<String, Object> map = (Map<String, Object>) JsonUtil.readCurrentObject(parser);
      Assert.assertTrue(map.containsKey("list"));

      List<Object> list = (List<Object>) map.get("list");
      Assert.assertEquals(3, list.size());
      Assert.assertEquals(1, list.get(0));
      Assert.assertEquals(2, list.get(1));

      Map<String, Object> nestedMap = (Map<String, Object>) list.get(2);
      Assert.assertEquals("nestedValue", nestedMap.get("nestedKey"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadNextObject_NestedStructures() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
      JsonParser parser = objectMapper.getFactory().createParser("{\"list\":[1,2,{\"nestedKey\":\"nestedValue\"}]}");
      Map<String, Object> map = (Map<String, Object>) JsonUtil.readNextObject(parser);
      Assert.assertTrue(map.containsKey("list"));

      List<Object> list = (List<Object>) map.get("list");
      Assert.assertEquals(3, list.size());
      Assert.assertEquals(1, list.get(0));
      Assert.assertEquals(2, list.get(1));

      Map<String, Object> nestedMap = (Map<String, Object>) list.get(2);
      Assert.assertEquals("nestedValue", nestedMap.get("nestedKey"));
  }
  
  @Test
  public void testReadNextObject() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String json = "{\"name\":\"TestFoo\",\"bars\":[{\"id\":1},{\"id\":2}],\"id\":123,\"price\":99.99,\"timestamp\":1633024800000,\"active\":true}";
    JsonParser parser = objectMapper.getFactory().createParser(json);

    Foo result = JsonUtil.readNextObject(parser, Foo.class);

    Assert.assertNotNull(result);
    Assert.assertEquals("TestFoo", result.getName());
    Assert.assertEquals(2, result.getBars().size());
    Assert.assertEquals(Integer.valueOf(123), result.getId());
    Assert.assertEquals(new BigDecimal("99.99"), result.getPrice());
    Assert.assertEquals(Long.valueOf(1633024800000L), result.getTimestamp());
    Assert.assertEquals(Boolean.TRUE, result.getActive());
  }

  @Test
  public void testReadCurrentObject() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String json = "{\"name\":\"TestFoo\",\"bars\":[{\"id\":1},{\"id\":2}],\"id\":123,\"price\":99.99,\"timestamp\":1633024800000,\"active\":true}";
    JsonParser parser = objectMapper.getFactory().createParser(json);
    parser.nextToken(); // Move to the first token

    Foo result = JsonUtil.readCurrentObject(parser, Foo.class);

    Assert.assertNotNull(result);
    Assert.assertEquals("TestFoo", result.getName());
    Assert.assertEquals(2, result.getBars().size());
    Assert.assertEquals(Integer.valueOf(123), result.getId());
    Assert.assertEquals(new BigDecimal("99.99"), result.getPrice());
    Assert.assertEquals(Long.valueOf(1633024800000L), result.getTimestamp());
    Assert.assertEquals(Boolean.TRUE, result.getActive());
  }
  
  private void doTestSkipNextPrimitiveValue(String primitiveValue) throws Exception{
    String s = "{\"a\": " + primitiveValue + ", \"b\": true}";
    JsonParser parser = new JsonFactory().createParser(s.getBytes());
    Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
    Assert.assertEquals("a", parser.currentName());
    JsonUtil.skipNextValue(parser);
    Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
    Assert.assertEquals("b", parser.currentName());  
  }
  

  public static class Foo {
    
    private String name;
    private List<Bar> bars;
    private Exception exception;
    private Integer id;
    private BigDecimal price;
    private Long timestamp;
    private Boolean active;
    
    public Foo() {
    }
    
    public Foo(String name, List<Bar> bars) {
      this.setName(name);
      this.setBars(bars);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<Bar> getBars() {
      return bars;
    }

    public void setBars(List<Bar> bars) {
      this.bars = bars;
    }

    public Exception getException() {
      return exception;
    }

    public void setException(Exception exception) {
      this.exception = exception;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public BigDecimal getPrice() {
      return price;
    }

    public void setPrice(BigDecimal price) {
      this.price = price;
    }

    public Long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
    }

    public Boolean getActive() {
      return active;
    }

    public void setActive(Boolean active) {
      this.active = active;
    }
    

  }
  
  public static class FooToStringJsonSerializer extends StdSerializer<Foo> {

    private static final long serialVersionUID = -6845087478891618280L;

	public FooToStringJsonSerializer() {
      super(Foo.class);
    }

    @Override
    public void serialize(Foo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeStartObject();
      JsonUtil.writeStringField(gen, "name", value.getName());
      JsonUtil.writeObjectField(gen, "bars", value.getBars());
      if (value.getException() != null) {
        JsonUtil.writeStringField(gen, "exception", value.getException().toString());
      }
      JsonUtil.writeIntField(gen, "id", value.getId());
      JsonUtil.writeBigDecimalField(gen, "price", value.getPrice());
      JsonUtil.writeLongField(gen, "timestamp", value.getTimestamp());
      JsonUtil.writeBooleanField(gen, "active", value.getActive());
      gen.writeEndObject();
    }
  }
  
  public static class Bar {
    
    private int id;
    
    public Bar() {
      this(0);
    }
    
    public Bar(int id) {
      this.setId(id);
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }    
  }
  
  public static class CursedBar extends Bar {
    private Foo foo;
    
    public CursedBar() {
      this(0);
    }

    public CursedBar(int id) {
      super(id);
    }

    public Foo getFoo() {
      return foo;
    }

    public void setFoo(Foo foo) {
      this.foo = foo;
    } 
  }
  
  
}
