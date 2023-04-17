package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListRequest;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#getUntriggeredStopOrderList(com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingGetUntriggeredStopOrderListDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingGetUntriggeredStopOrderListDemo.class);
  
  /**
   * Sample value for <i>currentPage</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final Integer CURRENTPAGE = 1;
  
  /**
   * Sample value for <i>pageSize</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final Integer PAGESIZE = 50;
  
  /**
   * Sample value for <i>status</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final String STATUS = "active";
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final String SYMBOL = null;
  
  /**
   * Sample value for <i>side</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final String SIDE = null;
  
  /**
   * Sample value for <i>type</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final String TYPE = null;
  
  /**
   * Sample value for <i>startAt</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final Long STARTAT = null;
  
  /**
   * Sample value for <i>endAt</i> parameter of <i>GetUntriggeredStopOrderList</i> API
   */
  public static final Long ENDAT = null;
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinGetUntriggeredStopOrderListRequest request = new KucoinGetUntriggeredStopOrderListRequest();
      request.setCurrentPage(CURRENTPAGE);
      request.setPageSize(PAGESIZE);
      request.setStatus(STATUS);
      request.setSymbol(SYMBOL);
      request.setSide(SIDE);
      request.setType(TYPE);
      request.setStartAt(STARTAT);
      request.setEndAt(ENDAT);
      log.info("Calling 'com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.getUntriggeredStopOrderList() API with request:" + request);
      log.info("Response:" + api.getUntriggeredStopOrderList(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
