package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#subscribeStopOrderLifecycleEvent(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingStopOrderLifecycleEventDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingStopOrderLifecycleEventDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinStopOrderLifecycleEventRequest request = new KucoinStopOrderLifecycleEventRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.subscribeStopOrderLifecycleEvent() websocket stream with request:" + request);
      api.subscribeStopOrderLifecycleEvent(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
