package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderRequest;
import com.scz.jxapi.util.TestApiProperties;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#placeAnOrder(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingPlaceAnOrderDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingPlaceAnOrderDemo.class);
  
  /**
   * Sample value for <i>clientOid</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String CLIENTOID = "myClientOrderID";
  
  /**
   * Sample value for <i>side</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String SIDE = "buy";
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String SYMBOL = "XBTUSDM";
  
  /**
   * Sample value for <i>type</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String TYPE = "limit";
  
  /**
   * Sample value for <i>leverage</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final BigDecimal LEVERAGE = new BigDecimal("10.1");;
  
  /**
   * Sample value for <i>remark</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String REMARK = "free text remark";
  
  /**
   * Sample value for <i>stop</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String STOP = null;
  
  /**
   * Sample value for <i>stopPriceType</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String STOPPRICETYPE = null;
  
  /**
   * Sample value for <i>stopPrice</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final BigDecimal STOPPRICE = null;
  
  /**
   * Sample value for <i>reduceOnly</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Boolean REDUCEONLY = false;
  
  /**
   * Sample value for <i>closeOrder</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Boolean CLOSEORDER = false;
  
  /**
   * Sample value for <i>forceHold</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Boolean FORCEHOLD = false;
  
  /**
   * Sample value for <i>price</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final BigDecimal PRICE = new BigDecimal("1.12");;
  
  /**
   * Sample value for <i>size</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Integer SIZE = 100;
  
  /**
   * Sample value for <i>timeInForce</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final String TIMEINFORCE = "GTC";
  
  /**
   * Sample value for <i>postOnly</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Boolean POSTONLY = false;
  
  /**
   * Sample value for <i>hidden</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Boolean HIDDEN = false;
  
  /**
   * Sample value for <i>iceberg</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final Boolean ICEBERG = false;
  
  /**
   * Sample value for <i>visibleSize</i> parameter of <i>PlaceAnOrder</i> API
   */
  public static final BigDecimal VISIBLESIZE = null;
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinPlaceAnOrderRequest request = new KucoinPlaceAnOrderRequest();
      request.setClientOid(CLIENTOID);
      request.setSide(SIDE);
      request.setSymbol(SYMBOL);
      request.setType(TYPE);
      request.setLeverage(LEVERAGE);
      request.setRemark(REMARK);
      request.setStop(STOP);
      request.setStopPriceType(STOPPRICETYPE);
      request.setStopPrice(STOPPRICE);
      request.setReduceOnly(REDUCEONLY);
      request.setCloseOrder(CLOSEORDER);
      request.setForceHold(FORCEHOLD);
      request.setPrice(PRICE);
      request.setSize(SIZE);
      request.setTimeInForce(TIMEINFORCE);
      request.setPostOnly(POSTONLY);
      request.setHidden(HIDDEN);
      request.setIceberg(ICEBERG);
      request.setVisibleSize(VISIBLESIZE);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.placeAnOrder() API with request:" + request);
      log.info("Response:" + api.placeAnOrder(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
