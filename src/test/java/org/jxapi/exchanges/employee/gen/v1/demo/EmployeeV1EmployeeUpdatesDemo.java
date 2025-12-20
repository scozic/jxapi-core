package org.jxapi.exchanges.employee.gen.v1.demo;

import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.util.DemoProperties;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link EmployeeV1Api#subscribeEmployeeUpdates(org.jxapi.netutils.websocket.WebsocketListener)}.
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.WebsocketEndpointDemoGenerator")
public class EmployeeV1EmployeeUpdatesDemo {
  private static final Logger log = LoggerFactory.getLogger(EmployeeV1EmployeeUpdatesDemo.class);
  
  /**
   * {@link EmployeeV1Api#subscribeEmployeeUpdates(org.jxapi.netutils.websocket.WebsocketListener)}.
   * <br>Websocket endpoint subscription will be performed using given websocket listener then after waiting for <code>subscriptionDuration</code> delay, unsubscription is performed.
   * Finally waits for <code>delayBeforeExitAfterUnsubscription</code> delay before returning to make sure no more messages are dispatched.
   * @param messageListener  The listener that will receive messages dispatched while subscription is active
   * @param configProperties Exchange configuration properties.
   * @param observer      {@link ExchangeObserver} (optional, ignored if <code>null</code>) observer will be subscribed to Exchange API exposing websocket endpoint that will be notifed of received websocket events.Useful in particular to get notified of websocket errors.
   * @throws InterruptedException eventually thrown while sleeping for <code>subscriptionDuration</code> or <code>delayBeforeExitAfterUnsubscription</code> delays
   */
  public static void subscribe(WebsocketListener<EmployeeV1EmployeeUpdatesMessage> messageListener,
                               Properties configProperties,
                               ExchangeObserver observer) throws InterruptedException {
    EmployeeExchange exchange = new EmployeeExchangeImpl("test-" + EmployeeExchange.ID, configProperties);
    EmployeeV1Api api = exchange.getV1Api();
    long subscriptionDuration = DemoProperties.getWebsocketSubscriptionDuration(configProperties);
    long delayBeforeExit = DemoProperties.getWebsocketDelayBeforeExit(configProperties);
    log.info("Subscribing to websocket API 'Employee v1 employeeUpdates' for {} ms", subscriptionDuration);
    if (observer != null) {
      exchange.subscribeObserver(observer);
    }
    String subId = api.subscribeEmployeeUpdates(messageListener);
    DemoUtil.sleep(subscriptionDuration);
    log.info("Unubscribing from 'Employee v1 employeeUpdates' stream");
    api.unsubscribeEmployeeUpdates(subId);
    DemoUtil.sleep(delayBeforeExit);
    if (observer != null) {
      exchange.unsubscribeObserver(observer);
    }
    exchange.dispose();
  }
  
  /**
   * Runs websocket endpoint 'Employee v1 employeeUpdates' subscription demo.
   * @param args no arguments expected
   */
  public static void main(String[] args) {
    try {
      Properties properties = DemoUtil.loadDemoExchangeProperties(EmployeeExchange.ID);
      subscribe(DemoUtil::logWsMessage,
                properties,
                DemoUtil::logWsApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
