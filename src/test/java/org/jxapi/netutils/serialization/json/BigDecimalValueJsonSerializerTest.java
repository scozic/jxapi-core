package org.jxapi.netutils.serialization.json;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link BigDecimalJsonValueSerializer}.
 */
public class BigDecimalValueJsonSerializerTest {

  @Test
  public void testSerialize() {
    Assert.assertNull(BigDecimalJsonValueSerializer.getInstance().serialize(null));
    Assert.assertEquals("12345.6789", BigDecimalJsonValueSerializer.getInstance().serialize(new BigDecimal("12345.6789")));
  }

}
