package org.jxapi.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;

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
  
  @Test()
  public void testCreateDefaultJsonToStringObjectMapper() {
    Assert.assertNotNull(JsonUtil.createDefaultJsonToStringObjectMapper());
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
