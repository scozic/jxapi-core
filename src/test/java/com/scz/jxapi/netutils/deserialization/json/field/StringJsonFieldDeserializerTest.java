package com.scz.jxapi.netutils.deserialization.json.field;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link StringJsonFieldDeserializer}
 */
public class StringJsonFieldDeserializerTest {

	@Test
	public void testDeserialize() {
		Assert.assertEquals("foo", StringJsonFieldDeserializer.getInstance().deserialize("\"foo\""));
	}
}
