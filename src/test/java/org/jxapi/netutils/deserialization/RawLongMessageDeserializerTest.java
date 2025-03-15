package org.jxapi.netutils.deserialization;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RawLongMessageDeserializer}
 */
public class RawLongMessageDeserializerTest {

	@Test
	public void testDeserialize() {
		Assert.assertEquals(Long.valueOf(3L),  RawLongMessageDeserializer.getInstance().deserialize("3"));
	}
	
	@Test
	public void testDeserializeNull() {
		Assert.assertNull(RawLongMessageDeserializer.getInstance().deserialize(null));
	}
}
