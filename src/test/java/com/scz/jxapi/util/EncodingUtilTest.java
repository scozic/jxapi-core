package com.scz.jxapi.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link EncodingUtil}
 */
public class EncodingUtilTest {
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateUrlQueryParametersNullKeyThrows() {
		EncodingUtil.createUrlQueryParameters(null, "val");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateUrlQueryParametersOddParametersLengthThrows() {
		EncodingUtil.createUrlQueryParameters("key");
	}
	
	@Test
	public void testCreateUrlQueryParametersWithOneNullValueAndOneValueEscapedInUrlEncoding() {
		Assert.assertEquals("?key0=val0&key2=%26val2%5B", 
							EncodingUtil.createUrlQueryParameters("key0", "val0", "ke1", null, "key2", "&val2["));
	}
	
}
