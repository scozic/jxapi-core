package com.scz.jcex.netutils.websocket;

import java.io.IOException;

public interface WebsocketErrorHandler {

	void handleWebsocketError(IOException error);
}
