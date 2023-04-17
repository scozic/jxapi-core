package com.scz.jcex.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#getAccountOverview(com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingGetAccountOverviewDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingGetAccountOverviewDemo.class);
  
  /**
   * Sample value for <i>currency</i> parameter of <i>GetAccountOverview</i> API
   */
  public static final String CURRENCY = "USDT";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetAccountOverviewRequest request = new KucoinGetAccountOverviewRequest();
      request.setCurrency(CURRENCY);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.getAccountOverview() API with request:" + request);
      log.info("Response:" + api.getAccountOverview(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
