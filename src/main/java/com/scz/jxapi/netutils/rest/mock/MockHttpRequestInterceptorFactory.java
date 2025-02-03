package com.scz.jxapi.netutils.rest.mock;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptorFactory;

/**
 * Factory for creating {@link MockHttpRequestInterceptor} instances.
 * 
 * @see MockHttpRequestInterceptor
 */
public class MockHttpRequestInterceptorFactory implements HttpRequestInterceptorFactory {

	@Override
	public HttpRequestInterceptor createInterceptor(ExchangeApi exchangeApi) {
		return new MockHttpRequestInterceptor();
	}

}
