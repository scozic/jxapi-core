package org.jxapi.exchange;

import org.jxapi.netutils.websocket.WebsocketClient;

/**
 * {@link Exchange} client can implement this interface to receive
 * {@link ExchangeEvent} events triggered when APIs are being called. <br>
 * Such events are raised when a REST endpoint call is submitted, when such call
 * receives a response, when a websocket events for subscription/unsubscription,
 * incoming messages and errors. <br>
 * When an error occur during a REST call it is notified in response and client
 * should perform necessary logic for handling this single error. A websocket
 * error usually need not specific handling because reconnection and
 * resubscription is automatically performed by {@link WebsocketClient}.
 * {@link ExchangeObserver} callback is not meant for individual error
 * handling, but rather to historize them and produce metrics (for instance
 * Micrometer API which can be consumed by Prometheus and Alertmanager). An
 * unusual number of errors in REST calls or websocket connections during a
 * given period of time could trigger alerting <br>
 * 
 * @see ExchangeEvent
 */
public interface ExchangeObserver {

  /**
   * Method called when an event is triggered by an {@link ExchangeApi} instance.
   * 
   * @param event The event dispatched
   */
  void handleEvent(ExchangeEvent event);
}
