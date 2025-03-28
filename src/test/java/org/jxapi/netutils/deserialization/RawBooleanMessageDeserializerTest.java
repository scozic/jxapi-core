package org.jxapi.netutils.deserialization;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RawBooleanMessageDeserializer}
 */
public class RawBooleanMessageDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertTrue(RawBooleanMessageDeserializer.getInstance().deserialize("true"));
  }
  
  @Test
  public void testDeserializeNull() {
    Assert.assertNull(RawBooleanMessageDeserializer.getInstance().deserialize(null));
  }
}
