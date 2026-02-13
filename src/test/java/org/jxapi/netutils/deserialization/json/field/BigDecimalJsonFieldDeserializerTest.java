package org.jxapi.netutils.deserialization.json.field;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link BigDecimalJsonFieldDeserializer}
 */
public class BigDecimalJsonFieldDeserializerTest {

  @Test
  public void testDeserialize() {
    Assert.assertEquals(new BigDecimal("3.14"), BigDecimalJsonFieldDeserializer.getInstance().deserialize("3.14"));
  }
}
