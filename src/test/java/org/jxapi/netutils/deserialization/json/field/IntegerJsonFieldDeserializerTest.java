package org.jxapi.netutils.deserialization.json.field;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link IntegerJsonFieldDeserializer}
 */
public class IntegerJsonFieldDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals(Integer.valueOf(3), IntegerJsonFieldDeserializer.getInstance().deserialize("3"));
    Assert.assertEquals(Integer.valueOf(3), IntegerJsonFieldDeserializer.getInstance().deserialize("\"3\""));
  }
}
