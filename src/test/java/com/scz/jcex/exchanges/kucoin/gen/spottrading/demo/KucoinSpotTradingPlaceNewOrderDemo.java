package com.scz.jcex.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#placeNewOrder(com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingPlaceNewOrderDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingPlaceNewOrderDemo.class);
  
  /**
   * Sample value for <i>clientOid</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String CLIENTOID = "testKCOrderID";
  
  /**
   * Sample value for <i>side</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String SIDE = "buy";
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String SYMBOL = "ETH-BTC";
  
  /**
   * Sample value for <i>type</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String TYPE = "limit";
  
  /**
   * Sample value for <i>remark</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String REMARK = "Default remark";
  
  /**
   * Sample value for <i>stp</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String STP = "CN";
  
  /**
   * Sample value for <i>tradeType</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String TRADETYPE = "TRADE";
  
  /**
   * Sample value for <i>price</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String PRICE = "1.234";
  
  /**
   * Sample value for <i>size</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String SIZE = "10.0";
  
  /**
   * Sample value for <i>timeInForce</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String TIMEINFORCE = "GTC";
  
  /**
   * Sample value for <i>cancelAfter</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final long CANCELAFTER = 120L;
  
  /**
   * Sample value for <i>postOnly</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final boolean POSTONLY = false;
  
  /**
   * Sample value for <i>hidden</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final boolean HIDDEN = false;
  
  /**
   * Sample value for <i>iceberg</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final boolean ICEBERG = false;
  
  /**
   * Sample value for <i>visibleSize</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String VISIBLESIZE = "100.00";
  
  /**
   * Sample value for <i>funds</i> parameter of <i>PlaceNewOrder</i> API
   */
  public static final String FUNDS = "22.333";
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinPlaceNewOrderRequest request = new KucoinPlaceNewOrderRequest();
      request.setClientOid(CLIENTOID);
      request.setSide(SIDE);
      request.setSymbol(SYMBOL);
      request.setType(TYPE);
      request.setRemark(REMARK);
      request.setStp(STP);
      request.setTradeType(TRADETYPE);
      request.setPrice(PRICE);
      request.setSize(SIZE);
      request.setTimeInForce(TIMEINFORCE);
      request.setCancelAfter(CANCELAFTER);
      request.setPostOnly(POSTONLY);
      request.setHidden(HIDDEN);
      request.setIceberg(ICEBERG);
      request.setVisibleSize(VISIBLESIZE);
      request.setFunds(FUNDS);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.placeNewOrder() API with request:" + request);
      log.info("Response:" + api.placeNewOrder(request));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
