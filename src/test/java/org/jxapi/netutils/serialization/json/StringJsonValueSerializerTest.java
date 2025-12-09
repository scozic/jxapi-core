package org.jxapi.netutils.serialization.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for StringJsonValueSerializer.
 */
public class StringJsonValueSerializerTest {

  @Test
  public void testSerialize() {
    Assert.assertNull(StringJsonValueSerializer.getInstance().serialize(null));
    Assert.assertEquals("\"Hello \\\"world\\\"!\"", StringJsonValueSerializer.getInstance().serialize("Hello \"world\"!"));
  }

}
