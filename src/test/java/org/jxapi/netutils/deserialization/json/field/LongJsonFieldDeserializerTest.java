package org.jxapi.netutils.deserialization.json.field;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link LongJsonFieldDeserializer}
 */
public class LongJsonFieldDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals(Long.valueOf(3L), LongJsonFieldDeserializer.getInstance().deserialize("3"));
  }
}
