package com.scz.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.ExchangeApi;

/**
 * Unit test for {@link HttpRequestExecutorFactory}
 */
public class HttpRequestExceutorFactoryTest {

	@Test
	public void testFromClassName() {
		HttpRequestExecutorFactory fac = HttpRequestExecutorFactory.fromClassName(TestHttpRequestExceutorFactory.class.getName());
		Assert.assertNotNull(fac);
		Assert.assertTrue(fac instanceof TestHttpRequestExceutorFactory);
	}
	
	public static class TestHttpRequestExceutorFactory implements HttpRequestExecutorFactory {

		@Override
		public HttpRequestExecutor createExecutor(ExchangeApi exchangeApi) {
			return null;
		}
		
	}
 }
