package org.jxapi.netutils.rest.mock;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.jxapi.netutils.rest.HttpRequestInterceptorFactory;

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
