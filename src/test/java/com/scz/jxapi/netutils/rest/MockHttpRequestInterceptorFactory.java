package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.exchange.ExchangeApi;

public class MockHttpRequestInterceptorFactory implements HttpRequestInterceptorFactory {

	@Override
	public HttpRequestInterceptor createInterceptor(ExchangeApi exchangeApi) {
		return new MockHttpRequestInterceptor();
	}

}
