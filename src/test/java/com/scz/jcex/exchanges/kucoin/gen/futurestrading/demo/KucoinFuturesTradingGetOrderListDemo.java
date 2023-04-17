package com.scz.jcex.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#getOrderList(com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingGetOrderListDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingGetOrderListDemo.class);
  
  /**
   * Sample value for <i>currentPage</i> parameter of <i>GetOrderList</i> API
   */
  public static final Integer CURRENTPAGE = 1;
  
  /**
   * Sample value for <i>pageSize</i> parameter of <i>GetOrderList</i> API
   */
  public static final Integer PAGESIZE = 50;
  
  /**
   * Sample value for <i>status</i> parameter of <i>GetOrderList</i> API
   */
  public static final String STATUS = "active";
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>GetOrderList</i> API
   */
  public static final String SYMBOL = null;
  
  /**
   * Sample value for <i>side</i> parameter of <i>GetOrderList</i> API
   */
  public static final String SIDE = null;
  
  /**
   * Sample value for <i>type</i> parameter of <i>GetOrderList</i> API
   */
  public static final String TYPE = null;
  
  /**
   * Sample value for <i>startAt</i> parameter of <i>GetOrderList</i> API
   */
  public static final Long STARTAT = null;
  
  /**
   * Sample value for <i>endAt</i> parameter of <i>GetOrderList</i> API
   */
  public static final Long ENDAT = null;
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetOrderListRequest request = new KucoinGetOrderListRequest();
      request.setCurrentPage(CURRENTPAGE);
      request.setPageSize(PAGESIZE);
      request.setStatus(STATUS);
      request.setSymbol(SYMBOL);
      request.setSide(SIDE);
      request.setType(TYPE);
      request.setStartAt(STARTAT);
      request.setEndAt(ENDAT);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.getOrderList() API with request:" + request);
      log.info("Response:" + api.getOrderList(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
