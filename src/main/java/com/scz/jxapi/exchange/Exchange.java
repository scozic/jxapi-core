package com.scz.jxapi.exchange;

import com.scz.jxapi.observability.ExchangeApiObserver;
import com.scz.jxapi.util.HasProperties;

/**
 * Entry point of a REST/WEBSOCKET API wrapper.
 * Every instance has custom name, and id unique for exchange class.
 * Configuration properties are also supplied.
 */
public interface Exchange extends HasProperties {
	
	String getName();
	
	String getId();
	
	void subscribeObserver(ExchangeApiObserver exchangeApiObserver);
	
	boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver);

}
