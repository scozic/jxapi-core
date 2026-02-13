package org.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Optional;
import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchanges.demo.gen.DemoExchangeDemoProperties;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickerStreamRequestDeserializer;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.util.DemoProperties;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#subscribeTickerStream(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest, org.jxapi.netutils.websocket.WebsocketListener)}.
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.WebsocketEndpointDemoGenerator")
public class DemoExchangeMarketDataTickerStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataTickerStreamDemo.class);
  
  /**
   * Creates a sample value for the request field of type DemoExchangeMarketDataTickerStreamRequest using sample value(s) defined in demo configuration properties.
   * 
   * @param properties the configuration properties to use for the sample value generation.
   */
  public static DemoExchangeMarketDataTickerStreamRequest createRequest(Properties properties) {
    return Optional
      .ofNullable(new DemoExchangeMarketDataTickerStreamRequestDeserializer().deserialize(DemoExchangeDemoProperties.MarketData.Ws.TickerStream.getRequest(properties)))
      .orElse(DemoExchangeMarketDataTickerStreamRequest.builder()  
        .symbol(DemoExchangeDemoProperties.MarketData.Ws.TickerStream.Request.getSymbol(properties))
        .build());
  }
  
  /**
   * {@link DemoExchangeMarketDataApi#subscribeTickerStream(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest, org.jxapi.netutils.websocket.WebsocketListener)}.
   * <br>Websocket endpoint subscription will be performed using given websocket listener then after waiting for <code>subscriptionDuration</code> delay, unsubscription is performed.
   * Finally waits for <code>delayBeforeExitAfterUnsubscription</code> delay before returning to make sure no more messages are dispatched.
   * @param request          The subscription request
   * @param messageListener  The listener that will receive messages dispatched while subscription is active
   * @param configProperties Exchange configuration properties.
   * @param observer      {@link ExchangeObserver} (optional, ignored if <code>null</code>) observer will be subscribed to Exchange API exposing websocket endpoint that will be notifed of received websocket events.Useful in particular to get notified of websocket errors.
   * @throws InterruptedException eventually thrown while sleeping for <code>subscriptionDuration</code> or <code>delayBeforeExitAfterUnsubscription</code> delays
   */
  public static void subscribe(DemoExchangeMarketDataTickerStreamRequest request,
                               WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> messageListener,
                               Properties configProperties,
                               ExchangeObserver observer) throws InterruptedException {
    DemoExchangeExchange exchange = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties);
    DemoExchangeMarketDataApi api = exchange.getMarketDataApi();
    long subscriptionDuration = DemoProperties.getWebsocketSubscriptionDuration(configProperties);
    long delayBeforeExit = DemoProperties.getWebsocketDelayBeforeExit(configProperties);
    log.info("Subscribing to websocket API 'DemoExchange MarketData tickerStream' for {} ms with request:{}", subscriptionDuration, request);
    if (observer != null) {
      exchange.subscribeObserver(observer);
    }
    String subId = api.subscribeTickerStream(request, messageListener);
    DemoUtil.sleep(subscriptionDuration);
    log.info("Unubscribing from 'DemoExchange MarketData tickerStream' stream");
    api.unsubscribeTickerStream(subId);
    DemoUtil.sleep(delayBeforeExit);
    if (observer != null) {
      exchange.unsubscribeObserver(observer);
    }
    exchange.dispose();
  }
  
  /**
   * Runs websocket endpoint 'DemoExchange MarketData tickerStream' subscription demo.
   * @param args no arguments expected
   */
  public static void main(String[] args) {
    try {
      Properties properties = DemoUtil.loadDemoExchangeProperties(DemoExchangeExchange.ID);
      subscribe(createRequest(properties),
                DemoUtil::logWsMessage,
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
