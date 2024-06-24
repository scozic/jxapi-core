package com.scz.jxapi.exchange;

import com.scz.jxapi.observability.ExchangeApiObserver;
import com.scz.jxapi.util.HasProperties;

public interface ExchangeApi extends HasProperties {
	
	String getName();
	
	String getExchangeName();
	
	void subscribeObserver(ExchangeApiObserver exchangeApiObserver);
	
	boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver);

}
