package org.jxapi.netutils.rest;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.util.FactoryUtil;

/**
 * Factory class for {@link HttpRequestInterceptorFactory}. Class name of such
 * factory implementation can be provided in exchange descriptor to create API
 * specific interceptor.
 * 
 * @see HttpRequestInterceptor
 */
public interface HttpRequestInterceptorFactory {

	/**
	 * @param exchangeApi {@link ExchangeApi} instance to configure the interceptor
	 *                    for. It can be used to access configuration properties.
	 * @return new instance of {@link HttpRequestInterceptor} with properties set.
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
