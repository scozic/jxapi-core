package org.jxapi.netutils.serialization.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link BooleanJsonValueSerializer}.
 */
public class BooleanValueJsonSerializerTest {

  @Test
  public void testSerialize() {
    Assert.assertNull(BooleanJsonValueSerializer.getInstance().serialize(null));
    Assert.assertEquals(Boolean.TRUE.toString(), BooleanJsonValueSerializer.getInstance().serialize(Boolean.TRUE));
    Assert.assertEquals(Boolean.FALSE.toString(), BooleanJsonValueSerializer.getInstance().serialize(Boolean.FALSE));
  }

}
