package com.scz.jxapi.netutils.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.observability.GenericObserver;

/**
 * Generic implementation of a {@link WebsocketErrorHandler}.
 * @see GenericObserver
 */
public class GenericWebsocketErrorHandler extends GenericObserver<WebsocketException> implements WebsocketErrorHandler {
	
	private static final Logger log = LoggerFactory.getLogger(GenericWebsocketErrorHandler.class);
    
    @Override
    public void handleWebsocketError(WebsocketException error) {
    	if (log.isDebugEnabled())
    		log.debug("Received websocket error", error);
        super.handleEvent(error);
    }

}
