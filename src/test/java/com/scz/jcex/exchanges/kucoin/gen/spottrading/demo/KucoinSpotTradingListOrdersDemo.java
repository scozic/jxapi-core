package com.scz.jcex.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#listOrders(com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingListOrdersDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingListOrdersDemo.class);
  
  /**
   * Sample value for <i>currentPage</i> parameter of <i>ListOrders</i> API
   */
  public static final int CURRENTPAGE = 1;
  
  /**
   * Sample value for <i>pageSize</i> parameter of <i>ListOrders</i> API
   */
  public static final int PAGESIZE = 50;
  
  /**
   * Sample value for <i>status</i> parameter of <i>ListOrders</i> API
   */
  public static final String STATUS = "active";
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>ListOrders</i> API
   */
  public static final String SYMBOL = null;
  
  /**
   * Sample value for <i>side</i> parameter of <i>ListOrders</i> API
   */
  public static final String SIDE = null;
  
  /**
   * Sample value for <i>type</i> parameter of <i>ListOrders</i> API
   */
  public static final String TYPE = null;
  
  /**
   * Sample value for <i>tradeType</i> parameter of <i>ListOrders</i> API
   */
  public static final String TRADETYPE = "TRADE";
  
  /**
   * Sample value for <i>startAt</i> parameter of <i>ListOrders</i> API
   */
  public static final long STARTAT = 0L;
  
  /**
   * Sample value for <i>endAt</i> parameter of <i>ListOrders</i> API
   */
  public static final long ENDAT = System.currentTimeMillis();
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinListOrdersRequest request = new KucoinListOrdersRequest();
      request.setCurrentPage(CURRENTPAGE);
      request.setPageSize(PAGESIZE);
      request.setStatus(STATUS);
      request.setSymbol(SYMBOL);
      request.setSide(SIDE);
      request.setType(TYPE);
      request.setTradeType(TRADETYPE);
      request.setStartAt(STARTAT);
      request.setEndAt(ENDAT);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.listOrders() API with request:" + request);
      log.info("Response:" + api.listOrders(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
