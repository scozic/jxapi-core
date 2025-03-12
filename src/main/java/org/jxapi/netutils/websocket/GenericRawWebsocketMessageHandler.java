package org.jxapi.netutils.websocket;

import org.jxapi.observability.GenericObserver;

/**
 * Generic implementation of a {@link RawWebsocketMessageHandler}.
 * @see GenericObserver
 */
public class GenericRawWebsocketMessageHandler extends GenericObserver<String> implements RawWebsocketMessageHandler {
    
    @Override
    public void handleWebsocketMessage(String message) {
        super.handleEvent(message);
    }

}
