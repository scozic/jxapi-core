package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#subscribeTradeOrders(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingTradeOrdersDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingTradeOrdersDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinTradeOrdersRequest request = new KucoinTradeOrdersRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.subscribeTradeOrders() websocket stream with request:" + request);
      api.subscribeTradeOrders(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
