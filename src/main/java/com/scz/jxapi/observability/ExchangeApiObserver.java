package com.scz.jxapi.observability;

public interface ExchangeApiObserver {

	void handleEvent(ExchangeApiEvent event);
}
