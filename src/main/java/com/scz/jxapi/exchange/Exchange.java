package com.scz.jxapi.exchange;

import java.util.Properties;

import com.scz.jxapi.observability.ExchangeApiObserver;
import com.scz.jxapi.util.HasProperties;

/**
 * Entry point of a REST/WEBSOCKET API wrapper.<br/>
 * Such wrapper is instantiated using specific configuration {@link Properties} and name identifier. Actual implementations will expose wrapper endpoints through one or more {@link ExchangeApi}
 * <p>
 * Every instance has custom name, and id unique for exchange class.
 * Configuration properties are also supplied.
 * <p>
 * Actual implementations will expose getter methods for each {@link ExchangeApi} implementation belonging to this exchange.<br/>
 * The idea of exposing endpoint in separate {@link ExchangeApi} groups helps regrouping APIs related to same business feature. 
 * @see ExchangeApi
 */
public interface Exchange extends HasProperties {

	/**
	 * @return A name identifying an instance of wrapper.
	 */
	String getName();
	
	/**
	 * @return Identifier unique for an actual {@link Exchange} implementation class.
	 */
	String getId();
	
	/**
	 * Subscribes an observer to every {@link ExchangeApi} exposed.
	 * @param exchangeApiObserver observer that will monitor every endpoint of every {@link ExchangeApi} exposed.
	 * @see ExchageApi
	 */
	void subscribeObserver(ExchangeApiObserver exchangeApiObserver);
	
	/**
	 * Unsubscribes an observer from every {@link ExchangeApi} exposed.
	 * @param exchangeApiObserver observer to remove
	 * @see ExchageApi
	 * @return <code>true</code> if observer was actually removed from at least one endpoint, <code>false</code> otherwise.
	 */
	boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver);

}
