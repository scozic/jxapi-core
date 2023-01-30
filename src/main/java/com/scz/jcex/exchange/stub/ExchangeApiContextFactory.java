package com.scz.jcex.exchange.stub;

import java.util.Properties;

/**
 * Factory for {@link ExchangeApiContext}. One {@link ExchangeApiContext}
 * instance will be created for a given connection to a crypto exchange.
 * <p>
 * Actual exchange implementations should supply an public implementation of
 * this interface, with a public default constructor.
 * Such implementation 
 */
public interface ExchangeApiContextFactory {

	ExchangeApiContext createContext(Properties properties);
}
