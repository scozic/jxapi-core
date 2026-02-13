package org.jxapi.netutils.deserialization.json.field;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link ListJsonFieldDeserializer}
 */
public class ListJsonFieldDeserializerTest {
  
  private ListJsonFieldDeserializer<String> testDeserializer;
  
  @Before
  public void setUp() {
    testDeserializer = new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance());
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
  public void testDeserializeEmptyArray() {
    Assert.assertEquals(0, testDeserializer.deserialize("[]").size());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testDeserializeNotAJsonArray() {
    testDeserializer.deserialize("\"foo\"");
  }
  
  @Test
  public void testDeserialize() {
    List<String> actual = testDeserializer.deserialize("[\"foo\", \"bar\"]");
    Assert.assertEquals(2, actual.size());
    Assert.assertEquals("foo", actual.get(0));
    Assert.assertEquals("bar", actual.get(1));
  }

}
