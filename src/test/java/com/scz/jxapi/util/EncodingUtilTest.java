package com.scz.jxapi.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link EncodingUtil}
 */
public class EncodingUtilTest {

	@Test
	public void testSplitJsonArrayStrEmpty() {
		List<String> actual = EncodingUtil.splitJsonArrayStr("");
		Assert.assertTrue(actual.isEmpty());
	}
	
	@Test
	public void testSplitJsonArrayStrWithDeepJsonArray() {
		List<String> actual = EncodingUtil.splitJsonArrayStr("[\n"
				+ "    {\n"
				+ "        \"a\": \"aval\",\n"
				+ "        \"b\": {\n"
				+ "            \"c\": \"cval\"\n"
				+ "        }\n"
				+ "    },\n"
				+ "    {\n"
				+ "        \"d\": [\n"
				+ "            {\n"
				+ "                \"e\": \"eval\"\n"
				+ "            },\n"
				+ "            {\n"
				+ "                \"f\": \"fval\",\n"
				+ "                \"g\": [1, 2, 3, 4]\n"
				+ "            }\n"
				+ "        ]\n"
				+ "    },\n"
				+ "    {\n"
				+ "        \"h\": \"a string with escaped single quote \\\" and inner incomplete json {\\\"x\\\": [\\\"y\\\", \\\"z\\\"]\"\n"
				+ "    }\n"
				+ "]");
		
		Assert.assertEquals(3, actual.size());
		Assert.assertEquals("\n"
				+ "    {\n"
				+ "        \"a\": \"aval\",\n"
				+ "        \"b\": {\n"
				+ "            \"c\": \"cval\"\n"
				+ "        }\n"
				+ "    }", actual.get(0));
		Assert.assertEquals("\n"
				+ "    {\n"
				+ "        \"d\": [\n"
				+ "            {\n"
				+ "                \"e\": \"eval\"\n"
				+ "            },\n"
				+ "            {\n"
				+ "                \"f\": \"fval\",\n"
				+ "                \"g\": [1, 2, 3, 4]\n"
				+ "            }\n"
				+ "        ]\n"
				+ "    }", actual.get(1));
		Assert.assertEquals("\n"
				+ "    {\n"
				+ "        \"h\": \"a string with escaped single quote \\\" and inner incomplete json {\\\"x\\\": [\\\"y\\\", \\\"z\\\"]\"\n"
				+ "    }\n", actual.get(2));
	}
	
	@Test
	public void testSplitJsonArrayStrWithIntArray() {
		List<String> actual = EncodingUtil.splitJsonArrayStr("[1,2,3]");
		Assert.assertEquals(3, actual.size());
		Assert.assertEquals("1", actual.get(0));
		Assert.assertEquals("2", actual.get(1));
		Assert.assertEquals("3", actual.get(2));
	}
	
	
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
