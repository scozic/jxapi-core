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
  
  private MapJsonFieldDeserializer<String> testDeserializer;
  
  @Before
  public void setUp() {
    testDeserializer = new MapJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance());
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
  
  @Test(expected = IllegalStateException.class)
  public void testDeserializeInvalidJsonMapValue() {
    testDeserializer.deserialize("{\"foo\":\"bar\", \"name\":[\"Bob\", \"Dylan\"]}");
  }

  @Test
  public void testDeserialize() {
    Map<String, String> actual = testDeserializer.deserialize("{\"foo\":\"bar\", \"name\":\"Bob\"}");
    Assert.assertEquals(2, actual.size());
    Assert.assertEquals("bar", actual.get("foo"));
    Assert.assertEquals("Bob", actual.get("name"));
  }
  
}
