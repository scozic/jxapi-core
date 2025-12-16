package org.jxapi.netutils.serialization.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for IntegerValueJsonSerializer.
 */
public class IntegerValueJsonSerializerTest {

  @Test
  public void testSerialize() {
    Assert.assertNull(IntegerJsonValueSerializer.getInstance().serialize(null));
    Assert.assertEquals("123", IntegerJsonValueSerializer.getInstance().serialize(123));
  }

}
