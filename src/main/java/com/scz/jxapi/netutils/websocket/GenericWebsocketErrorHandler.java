package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.observability.GenericObserver;

/**
 * Generic implementation of a {@link WebsocketErrorHandler}.
 * @see GenericObserver
 */
public class GenericWebsocketErrorHandler extends GenericObserver<WebsocketException> implements WebsocketErrorHandler {
    
    @Override
    public void handleWebsocketError(WebsocketException error) {
        super.handleEvent(error);
    }

}
