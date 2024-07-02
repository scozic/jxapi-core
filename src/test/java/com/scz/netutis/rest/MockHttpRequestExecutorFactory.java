package com.scz.netutis.rest;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpRequestExecutorFactory;

public class MockHttpRequestExecutorFactory implements HttpRequestExecutorFactory {

	@Override
	public HttpRequestExecutor createExecutor(ExchangeApi exchangeApi) {
		return new MockHttpRequestExecutor();
	}

}
