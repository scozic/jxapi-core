package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.util.FactoryUtil;

/**
 * Factory class for {@link Websocket} implementations.
 * Actual implementations are expected to provide a public constructor with no argument so they can be instantiated by reflection.
 * Name of such implementation class can be specified as value of {@link ExchangeApiDescriptor#}
 */
public interface WebsocketFactory {

	Websocket createWebsocket(ExchangeApi exchangeApi);
	
	/**
	 * Factory method to instantiate {@link WebsocketFactory} from its
	 * class name.
	 * 
	 * @param cls Name of {@link WebsocketFactory} implementation
	 *            class. Should have a default constructor.
	 * @return factory of <code>cls</code> class.
	 * @throws IllegalArgumentException If provided class cannot be instantiated by
	 *                                  reflection or does not provide a default
	 *                                  constructor.
	 */
	public static WebsocketFactory fromClassName(String cls) {
		return FactoryUtil.fromClassName(cls);
	}
}
