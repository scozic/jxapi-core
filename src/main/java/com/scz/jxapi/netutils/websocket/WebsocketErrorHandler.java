package com.scz.jxapi.netutils.websocket;

import java.io.IOException;

public interface WebsocketErrorHandler {

	void handleWebsocketError(IOException error);
}
