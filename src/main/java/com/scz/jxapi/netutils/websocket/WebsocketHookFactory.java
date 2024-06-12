package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.util.FactoryUtil;

public interface WebsocketHookFactory {

	WebsocketHook createWebsocketHook(ExchangeApi exchangeApi);
	
	/**
	 * Factory method to instantiate {@link WebsocketHookFactory} from its
	 * class name.
	 * 
	 * @param cls Name of {@link WebsocketHookFactory} implementation
	 *            class. Should have a default constructor.
	 * @return factory of <code>cls</code> class.
	 * @throws IllegalArgumentException If provided class cannot be instantiated by
	 *                                  reflection or does not provide a default
	 *                                  constructor.
	 */
	public static WebsocketHookFactory fromClassName(String cls) {
		return FactoryUtil.fromClassName(cls);
	}
}
