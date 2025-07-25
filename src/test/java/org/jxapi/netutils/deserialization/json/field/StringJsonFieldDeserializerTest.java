package org.jxapi.netutils.deserialization.json.field;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link StringJsonFieldDeserializer}
 */
public class StringJsonFieldDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals("foo", StringJsonFieldDeserializer.getInstance().deserialize("\"foo\""));
    Assert.assertEquals("123", StringJsonFieldDeserializer.getInstance().deserialize("123"));
    Assert.assertEquals("123.45", StringJsonFieldDeserializer.getInstance().deserialize("123.45"));
    Assert.assertEquals("true", StringJsonFieldDeserializer.getInstance().deserialize("true"));
    Assert.assertNull(StringJsonFieldDeserializer.getInstance().deserialize("null"));
  }

}
