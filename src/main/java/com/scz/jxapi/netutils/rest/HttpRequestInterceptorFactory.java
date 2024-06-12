package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.util.FactoryUtil;

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
	public static HttpRequestInterceptorFactory fromClassName(String cls) {
		return FactoryUtil.fromClassName(cls);
	}
}
