package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.exchange.ExchangeApi;

public interface HttpRequestExecutorFactory {

	HttpRequestExecutor createExecutor(ExchangeApi exchangeApi);
}
