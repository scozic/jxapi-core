package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.util.FactoryUtil;

/**
 * Factory interface for creating {@link HttpRequestExecutor} instances.
 * 
 * @see HttpRequestExecutor
 */
public interface HttpRequestExecutorFactory {

	/**
	 * Creates an {@link HttpRequestExecutor} instance for the given exchange API.
	 * Remark: Executor is bound to the {@link ExchangeApi} API group, so it should be used exclusively by that group.
	 * 
	 * @param exchangeApi API group that will use the executor to send requests.
	 * @return {@link HttpRequestExecutor} instance.
	 */
	HttpRequestExecutor createExecutor(ExchangeApi exchangeApi);
	
	/**
	 * Factory method to instantiate {@link HttpRequestExecutorFactory} from its
	 * class name.
	 * 
	 * @param cls Name of {@link HttpRequestExecutorFactory} implementation
	 *            class. Should have a default constructor.
	 * @return factory of <code>cls</code> class.
	 * @throws IllegalArgumentException If provided class cannot be instantiated by
	 *                                  reflection or does not provide a default
	 *                                  constructor.
	 */
	public static HttpRequestExecutorFactory fromClassName(String cls) {
		return FactoryUtil.fromClassName(cls);
	}
}
