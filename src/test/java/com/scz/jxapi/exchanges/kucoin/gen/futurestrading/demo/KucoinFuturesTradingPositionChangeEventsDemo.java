package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#subscribePositionChangeEvents(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingPositionChangeEventsDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingPositionChangeEventsDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>PositionChangeEvents</i> API
   */
  public static final String SYMBOL = "";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinPositionChangeEventsRequest request = new KucoinPositionChangeEventsRequest();
      request.setSymbol(SYMBOL);
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.subscribePositionChangeEvents() websocket stream with request:" + request);
      api.subscribePositionChangeEvents(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
