package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.exchange.ExchangeApi;

public class MockHttpRequestExecutorFactory implements HttpRequestExecutorFactory {

	@Override
	public HttpRequestExecutor createExecutor(ExchangeApi exchangeApi) {
		return new MockHttpRequestExecutor();
	}

}
