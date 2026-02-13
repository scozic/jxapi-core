package org.jxapi.netutils.deserialization.json.field;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link BooleanJsonFieldDeserializer}
 */
public class BooleanJsonFieldDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertTrue(BooleanJsonFieldDeserializer.getInstance().deserialize("true"));
  }
}
