package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#getListOfOrdersCompletedIn24h(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingGetListOfOrdersCompletedIn24hDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingGetListOfOrdersCompletedIn24hDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>GetListOfOrdersCompletedIn24h</i> API
   */
  public static final String SYMBOL = null;
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinGetListOfOrdersCompletedIn24hRequest request = new KucoinGetListOfOrdersCompletedIn24hRequest();
      request.setSymbol(SYMBOL);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.getListOfOrdersCompletedIn24h() API with request:" + request);
      log.info("Response:" + api.getListOfOrdersCompletedIn24h(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
