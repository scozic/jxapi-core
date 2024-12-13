package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Properties;

import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#subscribeTickerStream(com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest, com.scz.jxapi.netutils.websocket.WebsocketListener)}.
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataTickerStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataTickerStreamDemo.class);
  
  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;
  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;
  
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
   * @param subscriptionDuration               Delay to wait for after subscription before unsubscribing.
   * @param delayBeforeExitAfterUnsubscription Delay to wait before returning after unsubscribing.
   * @throws InterruptedException eventually thrown while sleeping for <code>subscriptionDuration</code> or <code>delayBeforeExitAfterUnsubscription</code> delays
   */
  public static void subscribe(DemoExchangeMarketDataTickerStreamRequest request,
                               WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> messageListener,
                               Properties configProperties,
                               ExchangeApiObserver apiObserver,
                               long subscriptionDuration,
                               long delayBeforeExitAfterUnsubscription) throws InterruptedException {
    DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties).getDemoExchangeMarketDataApi();
    log.info("Subscribing to websocket API 'DemoExchange MarketData tickerStream' for {} ms with request:{}", SUBSCRIPTION_DURATION, request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    String subId = api.subscribeTickerStream(request, messageListener);
    Thread.sleep(subscriptionDuration);
    log.info("Unubscribing from 'DemoExchange MarketData tickerStream' stream");
    api.unsubscribeTickerStream(subId);
    Thread.sleep(delayBeforeExitAfterUnsubscription);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
  }
  
  /**
   * Runs websocket endpoint 'DemoExchange MarketData tickerStream' subscription demo.
   * @param args no arguments expected
   */
  public static void main(String[] args) {
    try {
      subscribe(createRequest(),
                DemoUtil::logWsMessage,
                TestJXApiProperties.filterProperties(DemoExchangeExchange.ID, true),
                DemoUtil::logWsApiEvent,
                SUBSCRIPTION_DURATION,
                DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
