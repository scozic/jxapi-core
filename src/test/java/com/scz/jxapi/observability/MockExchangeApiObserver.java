package com.scz.jxapi.observability;

import java.util.ArrayList;
import java.util.List;

public class MockExchangeApiObserver implements ExchangeApiObserver {
	private List<ExchangeApiEvent> receivedEvents = new ArrayList<>();


	public List<ExchangeApiEvent> getReceivedEvents() {
		return receivedEvents;
	}

	@Override
	public void handleEvent(ExchangeApiEvent event) {
		receivedEvents.add(event);	
	}
	
	public int size() {
		return receivedEvents.size();
	}
	
	public ExchangeApiEvent popEvent() {
		if (receivedEvents.size() < 1) {
			throw new IllegalStateException("No event received");
		}
		return receivedEvents.remove(0);
	}

}
