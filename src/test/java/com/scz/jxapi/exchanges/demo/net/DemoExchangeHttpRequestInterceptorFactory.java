package com.scz.jxapi.exchanges.demo.net;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptorFactory;

/**
 * {@link HttpRequestInterceptorFactory} implementation for
 * {@link DemoExchangeExchange}. Will create DemoExchangeHttpRequestInterceptor
 * instances.
 */
public class DemoExchangeHttpRequestInterceptorFactory implements HttpRequestInterceptorFactory {

	@Override
	public HttpRequestInterceptor createInterceptor(ExchangeApi exchangeApi) {
		return new DemoExchangeHttpRequestInterceptor(exchangeApi.getProperties());
	}

}
