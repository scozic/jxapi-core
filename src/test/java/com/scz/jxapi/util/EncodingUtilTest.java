package com.scz.jxapi.util;

import java.math.BigDecimal;
import java.util.List;

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

	@Test
	public void testCreateUrlQueryParametersEmpty() {
		Assert.assertEquals("", EncodingUtil.createUrlQueryParameters());
	}
	
	@Test
	public void testPojoToString() {
		Bar bar = new Bar();
		bar.setName("babar");
		bar.setActive(true);
		bar.setScore(new BigDecimal("1.23"));
		Assert.assertEquals("Bar{\"active\":true,\"name\":\"babar\",\"score\":1.23}", EncodingUtil.pojoToString(bar));
	}
	
	@Test
	public void testPojoToJsonStringWithNestedPojoAttribute() {
		Bar bar = new Bar();
		bar.setName("babar");
		bar.setActive(true);
		bar.setScore(new BigDecimal("1.23"));
		
		Foo foo = new Foo();
		foo.setHello("Hi");
		foo.setBar(bar);
		
		Assert.assertEquals("Foo{\"bar\":{\"active\":true,\"name\":\"babar\",\"score\":1.23},\"hello\":\"Hi\"}", EncodingUtil.pojoToString(foo));
	}
	
	@Test
	public void testSubstituteArguments() {
		Assert.assertEquals("Nothing to substitute!", EncodingUtil.substituteArguments("Nothing to substitute!"));
		Assert.assertEquals("Hello Bob, I am Roger", EncodingUtil.substituteArguments("Hello ${stranger}, I am ${me}", "stranger", "Bob", "me", "Roger"));
	}

	@Test
	public void testListToStringEmptyList() {
		Assert.assertEquals("", EncodingUtil.listToString(List.of(), ";"));
	}

	@Test
	public void testListToStringOneElement() {
		Assert.assertEquals("Hello", EncodingUtil.listToString(List.of("Hello"), ";"));
	}

	@Test
	public void testListToStringTwoElements() {
		Assert.assertEquals("Hello;World", EncodingUtil.listToString(List.of("Hello", "World"), ";"));
	}

	@Test
	public void testBigDecimalToString() {
		Assert.assertEquals("1.23", EncodingUtil.bigDecimalToString(new BigDecimal("1.23")));
	}

	@Test
	public void testBigDecimalToStringNull() {
		Assert.assertEquals("0", EncodingUtil.bigDecimalToString(null));
	}

	@Test
	public void testToBigDecimal() {
		Assert.assertEquals(new BigDecimal("1.23"), EncodingUtil.toBigDecimal("1.23"));
	}

	@Test
	public void testToBigDecimalNull() {
		Assert.assertNull(EncodingUtil.toBigDecimal(null));
	}

	@Test
	public void testToBigDecimalEmpty() {
		Assert.assertNull(EncodingUtil.toBigDecimal(""));
	}
	
	@Test(expected = NumberFormatException.class)
	public void testToBigDecimalInvalid() {
		EncodingUtil.toBigDecimal("1.23.45");
	}

	@Test
	public void testUrlEncode() {
		Assert.assertEquals("Hello+World%21", EncodingUtil.urlEncode("Hello World!"));
	}

	@Test
	public void testUrlEncodeNull() {
		Assert.assertNull(EncodingUtil.urlEncode(null));
	}

	@Test
	public void testPrettyPrintLongString_StringThatNeedsNotBeShortened() {
		Assert.assertEquals("Hello World!", EncodingUtil.prettyPrintLongString("Hello World!", 12));
	}

	@Test
	public void testPrettyPrintLongString_StringThatNeedsToBeShortened() {
		Assert.assertEquals("Hell....you?", EncodingUtil.prettyPrintLongString("Hello World! How are you?", 12));
	}

	@Test
	public void testPrettyPrintLongString_NullString() {
		Assert.assertNull(EncodingUtil.prettyPrintLongString(null, 12));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPrettyPrintLongString_NullString_MaxLengthTooSmall() {
		EncodingUtil.prettyPrintLongString("Hello World!", 3);
	}
	
	private class Foo {
		private String hello;
		private Bar bar;
		@SuppressWarnings("unused")
		public String getHello() {
			return hello;
		}
		public void setHello(String hello) {
			this.hello = hello;
		}
		@SuppressWarnings("unused")
		public Bar getBar() {
			return bar;
		}
		public void setBar(Bar bar) {
			this.bar = bar;
		}
	}
	
	private class Bar {
		private String name;
		private boolean active;
		private BigDecimal score;
		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@SuppressWarnings("unused")
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		@SuppressWarnings("unused")
		public BigDecimal getScore() {
			return score;
		}
		public void setScore(BigDecimal score) {
			this.score = score;
		}
	}
	
}
