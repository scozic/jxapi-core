package org.jxapi.netutils.deserialization.json.field;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link MapJsonFieldDeserializer}
 */
public class MapJsonFieldDeserializerTest {
  
  private MapJsonFieldDeserializer<Integer> testDeserializer;
  
  @Before
  public void setUp() {
    testDeserializer = new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance());
  }
  
  @After
  public void tearDown() {
    testDeserializer = null;
  }
  
  @Test
  public void testDeserializeNull() {
    Assert.assertNull(testDeserializer.deserialize((String) null));
  }
  
  @Test
  public void testDeserializeNullJsonString() {
    Assert.assertNull(testDeserializer.deserialize("null"));
  }
  
  @Test
  public void testDeserializeEmptyMap() {
    Assert.assertEquals(0, testDeserializer.deserialize("{}").size());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testDeserializeNotAJsonMap() {
    testDeserializer.deserialize("\"foo\"");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDeserializeInvalidJsonMapValue() {
    testDeserializer.deserialize("{\"foo\":\"2\", \"name\":[\"Bob\", \"Dylan\"]}");
  }

  @Test
  public void testDeserialize() {
    Map<String, Integer> actual = testDeserializer.deserialize("{\"foo\":\"1\", \"bar\":25}");
    Assert.assertEquals(2, actual.size());
    Assert.assertEquals(Integer.valueOf(1), actual.get("foo"));
    Assert.assertEquals(Integer.valueOf(25), actual.get("bar"));
  }
  
}
