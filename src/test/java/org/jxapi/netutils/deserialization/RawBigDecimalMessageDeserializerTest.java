package org.jxapi.netutils.deserialization;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RawBigDecimalMessageDeserializer}
 */
public class RawBigDecimalMessageDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals(new BigDecimal("3.14"),  RawBigDecimalMessageDeserializer.getInstance().deserialize("3.14"));
  }
  
  @Test
  public void testDeserializeNull() {
    Assert.assertNull(RawBigDecimalMessageDeserializer.getInstance().deserialize(null));
  }
}
