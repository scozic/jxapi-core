package org.jxapi.netutils.deserialization;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RawIntegerMessageDeserializer}
 */
public class RawIntegerMessageDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals(Integer.valueOf(3),  RawIntegerMessageDeserializer.getInstance().deserialize("3"));
  }
  
  @Test
  public void testDeserializeNull() {
    Assert.assertNull(RawIntegerMessageDeserializer.getInstance().deserialize(null));
  }
}
