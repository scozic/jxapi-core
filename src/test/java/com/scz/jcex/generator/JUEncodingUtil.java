package com.scz.jcex.generator;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jcex.util.EncodingUtil;

/**
 * Unit test for {@link EncodingUtil}
 */
public class JUEncodingUtil {
	
	@Test
	public void testPojoToString() {
		Bar bar = new Bar();
		bar.setName("babar");
		bar.setActive(true);
		bar.setScore(new BigDecimal("1.23"));
		Assert.assertEquals("Bar{\"name\":\"babar\",\"active\":true,\"score\":1.23}", EncodingUtil.pojoToString(bar));
	}
	
	@Test
	public void testPojoToStringWithNestedPojoAttribute() {
		Bar bar = new Bar();
		bar.setName("babar");
		bar.setActive(true);
		bar.setScore(new BigDecimal("1.23"));
		
		Foo foo = new Foo();
		foo.setHello("Hi");
		foo.setBar(bar);
		
		Assert.assertEquals("Foo{\"hello\":\"Hi\",\"bar\":{\"name\":\"babar\",\"active\":true,\"score\":1.23}}", EncodingUtil.pojoToString(foo));
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
