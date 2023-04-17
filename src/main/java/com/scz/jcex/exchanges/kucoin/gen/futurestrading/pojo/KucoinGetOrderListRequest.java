package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetOrderListRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin FuturesTrading API GetOrderList REST endpointList your current orders. <br/>See <a href="https://docs.kucoin.com/futures/#get-order-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetOrderListRequestSerializer.class)
public class KucoinGetOrderListRequest implements RestEndpointUrlParameters {
  private Integer currentPage;
  private Long endAt;
  private Integer pageSize;
  private String side;
  private Long startAt;
  private String status;
  private String symbol;
  private String type;
  
  /**
   * @return [Optional] Current request page.
   */
  public Integer getCurrentPage(){
    return currentPage;
  }
  
  /**
   * @param currentPage [Optional] Current request page.
   */
  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }
  
  /**
   * @return [Optional] End time (milisecond).
   */
  public Long getEndAt(){
    return endAt;
  }
  
  /**
   * @param endAt [Optional] End time (milisecond).
   */
  public void setEndAt(Long endAt) {
    this.endAt = endAt;
  }
  
  /**
   * @return [Optional] pageSize, The default pageSize is 50, The maximum cannot exceed 1000
   */
  public Integer getPageSize(){
    return pageSize;
  }
  
  /**
   * @param pageSize [Optional] pageSize, The default pageSize is 50, The maximum cannot exceed 1000
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
  
  /**
   * @return [Optional] <strong>buy</strong> or <strong>sell</strong>.
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side [Optional] <strong>buy</strong> or <strong>sell</strong>.
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return [Optional] Start time (milisecond).
   */
  public Long getStartAt(){
    return startAt;
  }
  
  /**
   * @param startAt [Optional] Start time (milisecond).
   */
  public void setStartAt(Long startAt) {
    this.startAt = startAt;
  }
  
  /**
   * @return [Optional] <strong>active</strong> or <strong>done</strong>(<strong>done</strong> as default), Only list orders with a specific status.
   */
  public String getStatus(){
    return status;
  }
  
  /**
   * @param status [Optional] <strong>active</strong> or <strong>done</strong>(<strong>done</strong> as default), Only list orders with a specific status.
   */
  public void setStatus(String status) {
    this.status = status;
  }
  
  /**
   * @return [Optional] Symbol of the contract
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol [Optional] Symbol of the contract
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return [Optional] <strong>limit</strong>, <strong>market</strong>, <strong>limit_stop</strong> or <strong>market_stop</strong>
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type [Optional] <strong>limit</strong>, <strong>market</strong>, <strong>limit_stop</strong> or <strong>market_stop</strong>
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.createUrlQueryParameters("currentPage", currentPage,"pageSize", pageSize,"status", status,"symbol", symbol,"side", side,"type", type,"startAt", startAt,"endAt", endAt);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
