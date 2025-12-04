package org.jxapi.netutils.deserialization;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link MessageDeserializer}
 */
public class MessageDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals("foo", MessageDeserializer.NO_OP.deserialize("foo"));
  }
  
  @Test
  public void testDeserializeNull() {
    Assert.assertNull(MessageDeserializer.NO_OP.deserialize(null));
  }
}
