package com.scz.jxapi.netutils.rest;

import java.lang.reflect.InvocationTargetException;

import com.scz.jxapi.exchange.ExchangeApi;

/**
 * Factory class for {@link HttpRequestInterceptorFactory}. Class name of such factory implementation can be 
 */
public interface HttpRequestInterceptorFactory {

	/**
	 * @param properties 
	 * @return
	 */
	HttpRequestInterceptor createInterceptor(ExchangeApi exchangeApi);
	
	/**
	 * Factory method to instantiate {@link HttpRequestInterceptorFactory} from its
	 * class name.
	 * 
	 * @param cls Name of {@link HttpRequestInterceptorFactory} implementation
	 *            class. Should have a default constructor.
	 * @return factory of <code>cls</code> class.
	 * @throws IllegalArgumentException If provided class cannot be instantiated by
	 *                                  reflection or does not provide a default
	 *                                  constructor.
	 */
	default HttpRequestInterceptorFactory fromClassName(String cls) {
		try {
			return (HttpRequestInterceptorFactory) Class.forName(cls).getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Failed to instantiate " 
												+ HttpRequestInterceptorFactory.class.getName() + 
												" implementation '" + cls + "'.",
												e);
		}
	}
}
