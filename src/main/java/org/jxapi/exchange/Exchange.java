package org.jxapi.exchange;

import java.util.List;
import java.util.Properties;

import org.jxapi.netutils.Network;
import org.jxapi.util.Disposable;
import org.jxapi.util.HasProperties;

/**
 * Entry point of a REST/WEBSOCKET API wrapper.<br>
 * Such wrapper is instantiated using specific configuration {@link Properties}
 * and name identifier. Actual implementations will expose wrapper endpoints
 * through one or more {@link ExchangeApi}
 * <p>
 * Every instance has custom name, and id unique for exchange class.
 * Configuration properties are also supplied.
 * <p>
 * Actual implementations will expose getter methods for each
 * {@link ExchangeApi} implementation belonging to this exchange.<br>
 * The idea of exposing endpoint in separate {@link ExchangeApi} groups helps
 * regrouping APIs related to same business feature.
 * 
 * @see ExchangeApi
 */
public interface Exchange extends Disposable, HasProperties {

  /**
   * @return A name identifying an instance of wrapper.
   */
  String getName();

  /**
   * @return Identifier unique for an actual {@link Exchange} implementation
   *         class.
   */
  String getId();
  
  /**
   * @return The version of the exchange
   */
  String getVersion();
  
  /**
   * Returns the base HTTP URL prefix for all REST endpoints of API groups of this
   * exchange. This prefix is used to build the full URL of each endpoint. It is
   * unused when either API group or REST endpoint defines an absolute URL.
   * 
   * @return The HTTP URL prefix for REST endpoints of this exchange.
   */
  String getHttpUrl();

  /**
   * Subscribes an observer to every {@link ExchangeApi} exposed.
   * 
   * @param exchangeApiObserver observer that will monitor every endpoint of every
   *                            {@link ExchangeApi} exposed.
   * @see ExchangeApi
   */
  void subscribeObserver(ExchangeObserver exchangeApiObserver);

  /**
   * Unsubscribes an observer from every {@link ExchangeApi} exposed.
   * 
   * @param exchangeApiObserver observer to remove
   * @see ExchangeApi
   * @return <code>true</code> if observer was actually removed from at least one
   *         endpoint, <code>false</code> otherwise.
   */
  boolean unsubscribeObserver(ExchangeObserver exchangeApiObserver);

  /**
   * Returns list of {@link ExchangeApi} instances exposed by this exchange.
   * A getter method for each {@link ExchangeApi} implementation belonging to this
   * exchange will be exposed in actual implementations.
   * 
   * @return List of {@link ExchangeApi} instances exposed by this exchange.
   */
  List<ExchangeApi> getApis();
  
  /**.
   * @return the network used by this exchange
   * @see Network
   */
  Network getNetwork();

  

}
