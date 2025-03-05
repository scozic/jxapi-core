package com.scz.jxapi.netutils.deserialization;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RawStringMessageDeserializer}
 */
public class RawStringMessageDeserializerTest {

	@Test
	public void testDeserialize() {
		Assert.assertEquals("foo", RawStringMessageDeserializer.getInstance().deserialize("foo"));
	}
	
	@Test
	public void testDeserializeNull() {
		Assert.assertNull(RawStringMessageDeserializer.getInstance().deserialize(null));
	}
}
