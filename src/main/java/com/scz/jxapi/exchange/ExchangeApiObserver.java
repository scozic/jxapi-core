package com.scz.jxapi.exchange;

public interface ExchangeApiObserver {

	void handleEvent(ExchangeApiEvent event);
}
