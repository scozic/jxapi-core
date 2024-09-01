package com.scz.jxapi.netutils.deserialization.json.field;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link TimestampJsonFieldDeserializer}
 */
public class TimestampJsonFieldDeserializerTest {

	@Test
	public void testDeserialize() {
		Assert.assertEquals(Long.valueOf(3L), TimestampJsonFieldDeserializer.getInstance().deserialize("3"));
	}
}
