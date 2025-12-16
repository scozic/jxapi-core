package org.jxapi.netutils.serialization.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for LongJsonValueSerializer.
 */
public class LongValueJsonSerializerTest {

  @Test
  public void testSerialize() {
    Assert.assertNull(LongJsonValueSerializer.getInstance().serialize(null));
    Assert.assertEquals("1234565768901", LongJsonValueSerializer.getInstance().serialize(1234565768901L));
  }

}
