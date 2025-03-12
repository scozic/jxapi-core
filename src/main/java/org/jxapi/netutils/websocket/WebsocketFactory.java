package org.jxapi.netutils.websocket;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.util.FactoryUtil;

/**
 * Factory class for {@link Websocket} implementations.
 * Actual implementations of this factory are expected to provide a public constructor with no
 * argument so they can be instantiated by reflection.
 * Name of such implementation class can be specified as value of
 * {@link ExchangeApiDescriptor#getWebsocketFactory()}
 */
public interface WebsocketFactory {

	/**
	 * Create a new instance of a {@link Websocket} implementation.
	 * 
	 * @param exchangeApi the {@link ExchangeApi} instance that can be used to
	 *                    retrieve configuration properties.
	 * @return a new instance of a {@link Websocket} implementation, initially in
	 *         'disconnected' state.
	 */
	Websocket createWebsocket(ExchangeApi exchangeApi);

	/**
	 * Static method to instantiate {@link WebsocketFactory} from its
	 * class name, assuming it has a default constructor.
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
