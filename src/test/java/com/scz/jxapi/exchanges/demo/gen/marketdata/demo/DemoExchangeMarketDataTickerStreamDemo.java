package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Properties;

import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.util.DemoProperties;
import com.scz.jxapi.util.DemoUtil;
import javax.annotation.processing.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#subscribeTickerStream(com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest, com.scz.jxapi.netutils.websocket.WebsocketListener)}.
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.demo.WebsocketEndpointDemoGenerator")
public class DemoExchangeMarketDataTickerStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataTickerStreamDemo.class);
  
  public static DemoExchangeMarketDataTickerStreamRequest createRequest() {
    DemoExchangeMarketDataTickerStreamRequest request = new DemoExchangeMarketDataTickerStreamRequest();
    request.setSymbol("BTC_USDT");
    return request;
  }
  
  /**
   * {@link DemoExchangeMarketDataApi#subscribeTickerStream(com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest, com.scz.jxapi.netutils.websocket.WebsocketListener)}.
   * <br>Websocket endpoint subscription will be performed using given websocket listener then after waiting for <code>subscriptionDuration</code> delay, unsubscription is performed.
   * Finally waits for <code>delayBeforeExitAfterUnsubscription</code> delay before returning to make sure no more messages are dispatched.
   * @param request                            The subscription request
   * @param messageListener                    The listener that will receive messages dispatched while subscription is active
   * @param configProperties                   Exchange configuration properties.
   * @param apiObserver                       {@link ExchangeApiObserver} (optional, ignored if <code>null</code>) observer will be subscribed to Exchange API exposing websocket endpoint that will be notifed of received websocket events.Useful in particular to get notified of websocket errors.
   * @throws InterruptedException eventually thrown while sleeping for <code>subscriptionDuration</code> or <code>delayBeforeExitAfterUnsubscription</code> delays
   */
  public static void subscribe(DemoExchangeMarketDataTickerStreamRequest request,
                               WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> messageListener,
                               Properties configProperties,
                               ExchangeApiObserver apiObserver) throws InterruptedException {
    DemoExchangeExchange exchange = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties);
    DemoExchangeMarketDataApi api = exchange.getDemoExchangeMarketDataApi();
    long subscriptionDuration = DemoProperties.getWebsocketSubscriptionDuration(configProperties);
    long delayBeforeExit = DemoProperties.getWebsocketDelayBeforeExit(configProperties);
    log.info("Subscribing to websocket API 'DemoExchange MarketData tickerStream' for {} ms with request:{}", subscriptionDuration, request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    String subId = api.subscribeTickerStream(request, messageListener);
    DemoUtil.sleep(subscriptionDuration);
    log.info("Unubscribing from 'DemoExchange MarketData tickerStream' stream");
    api.unsubscribeTickerStream(subId);
    DemoUtil.sleep(delayBeforeExit);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    exchange.dispose();
  }
  
  /**
   * Runs websocket endpoint 'DemoExchange MarketData tickerStream' subscription demo.
   * @param args no arguments expected
   */
  public static void main(String[] args) {
    try {
      subscribe(createRequest(),
                DemoUtil::logWsMessage,
                DemoUtil.loadDemoExchangeProperties(DemoExchangeExchange.ID),
                DemoUtil::logWsApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
