package com.scz.jxapi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.ExchangeApiEvent;
import com.scz.jxapi.exchange.ExchangeApiObserver;

/**
 * {@link ExchangeApiObserver} implementation used in websocket subscription
 * demo snippets: Logs websocket event related messages.
 */
public class WebsocketDemoApiObserver implements ExchangeApiObserver {
	
	private static final Logger log = LoggerFactory.getLogger(WebsocketDemoApiObserver.class);

	@Override
	public void handleEvent(ExchangeApiEvent event) {
		switch (event.getType()) {
		case WEBSOCKET_ERROR:
			log.error("Error raised from websocket{}", event, event.getWebsocketError());
			break;
		case WEBSOCKET_MESSAGE:
			log.debug("Websocket message received:{}", event);
			break;
		case WEBSOCKET_SUBSCRIBE:
			log.info("Subscription event received: {}", event);
			break;
		case WEBSOCKET_UNSUBSCRIBE:
			log.info("Unsubscription event received: {}", event);
			break;
		default:
			break;
		}
		
	}

}
