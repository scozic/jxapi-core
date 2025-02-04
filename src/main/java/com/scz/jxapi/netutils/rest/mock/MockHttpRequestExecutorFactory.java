package com.scz.jxapi.netutils.rest.mock;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.rest.HttpRequestExecutorFactory;

/**
 * Factory for creating {@link MockHttpRequestExecutor} instances.
 */
public class MockHttpRequestExecutorFactory implements HttpRequestExecutorFactory {

	@Override
	public MockHttpRequestExecutor createExecutor(ExchangeApi exchangeApi) {
		return new MockHttpRequestExecutor();
	}

}
