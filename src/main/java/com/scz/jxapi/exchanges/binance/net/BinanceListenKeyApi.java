package com.scz.jxapi.exchanges.binance.net;

import java.io.IOException;

/**
 * Interface for Binance specific websocket listen key api management.
 * See <ul>
 * <li>https://binance-docs.github.io/apidocs/spot/en/#user-data-streams</li>
 * <li>https://binance-docs.github.io/apidocs/futures/en/#user-data-streams</li>
 * </ul>
 * It will be used {@link BinancePrivateWebsocketManager} to peform required operations using this API to subscribe to private streams. 
 */
public interface BinanceListenKeyApi {

	/**
	 * @return New listenKey
	 */
	String getListenKey() throws IOException;
	
	/**
	 * @param listenKey The listen key to keep alive for 60mn
	 * @throws IOException
	 */
	void keepAliveListenKey(String listenKey) throws IOException;
	
	/**
	 * @param listenKey The listeny key to discard
	 */
	void deleteListenKey(String listenKey) throws IOException;
}
