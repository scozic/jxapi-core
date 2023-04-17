package com.scz.jxapi.exchanges.kucoin.net;

import java.io.IOException;

/**
 * Interface for API to retrieve token for applying websocket connection. See <a href="https://docs.kucoin.com/#apply-connect-token">Kucoin API</a>.
 */
public interface KucoinWebsocketListenKeyApi {

	KucoinWebsocketTokenInfo requestToken() throws IOException;
}
