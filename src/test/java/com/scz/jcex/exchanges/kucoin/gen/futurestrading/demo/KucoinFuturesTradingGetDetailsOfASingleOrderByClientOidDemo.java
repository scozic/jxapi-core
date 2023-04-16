package com.scz.jcex.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#getDetailsOfASingleOrderByClientOid(com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingGetDetailsOfASingleOrderByClientOidDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingGetDetailsOfASingleOrderByClientOidDemo.class);
  
  /**
   * Sample value for <i>clientOid</i> parameter of <i>GetDetailsOfASingleOrderByClientOid</i> API
   */
  public static final String CLIENTOID = "KCFUT_B_DOGEUSDT_62360000000002";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetDetailsOfASingleOrderByClientOidRequest request = new KucoinGetDetailsOfASingleOrderByClientOidRequest();
      request.setClientOid(CLIENTOID);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.getDetailsOfASingleOrderByClientOid() API with request:" + request);
      log.info("Response:" + api.getDetailsOfASingleOrderByClientOid(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
