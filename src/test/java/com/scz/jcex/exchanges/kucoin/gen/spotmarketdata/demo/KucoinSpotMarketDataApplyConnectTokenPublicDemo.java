package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#applyConnectTokenPublic(com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataApplyConnectTokenPublicDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataApplyConnectTokenPublicDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinApplyConnectTokenPublicRequest request = new KucoinApplyConnectTokenPublicRequest();
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.applyConnectTokenPublic() API with request:" + request);
      log.info("Response:" + api.applyConnectTokenPublic(request));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
