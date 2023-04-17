package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#subscribeAccountBalanceEvents(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingAccountBalanceEventsDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingAccountBalanceEventsDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinAccountBalanceEventsRequest request = new KucoinAccountBalanceEventsRequest();
      log.info("Subscribing to stream com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.subscribeAccountBalanceEvents() websocket stream with request:" + request);
      api.subscribeAccountBalanceEvents(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
