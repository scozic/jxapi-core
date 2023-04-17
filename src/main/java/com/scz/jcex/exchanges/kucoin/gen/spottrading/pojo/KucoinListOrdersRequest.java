package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinListOrdersRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API ListOrders REST endpointRequest via this endpoint to get your current order list. Items are paginated and sorted to show the latest first. See the <a href="https://docs.kucoin.com/#pagination">Pagination</a> section for retrieving additional entries after the first page.</p> <br/>See <a href="https://docs.kucoin.com/#list-orders">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinListOrdersRequestSerializer.class)
public class KucoinListOrdersRequest implements RestEndpointUrlParameters {
  private Integer currentPage;
  private Long endAt;
  private Integer pageSize;
  private String side;
  private Long startAt;
  private String status;
  private String symbol;
  private String tradeType;
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
   * @return [Optional] Number of results per request. Minimum is 10, maximum is 500.
   */
  public Integer getPageSize(){
    return pageSize;
  }
  
  /**
   * @param pageSize [Optional] Number of results per request. Minimum is 10, maximum is 500.
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
   * @return [Optional] Only list orders for a specific symbol.
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol [Optional] Only list orders for a specific symbol.
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return [Optional] the type of trading:<strong>TRADE</strong> (Spot Trading), <strong>MARGIN_TRADE</strong> (Cross Margin Trading), <strong>MARGIN_ISOLATED_TRADE</strong>(Isolated Margin Trading), and the default is <strong>TRADE</strong> to cancel the spot trading orders.
   */
  public String getTradeType(){
    return tradeType;
  }
  
  /**
   * @param tradeType [Optional] the type of trading:<strong>TRADE</strong> (Spot Trading), <strong>MARGIN_TRADE</strong> (Cross Margin Trading), <strong>MARGIN_ISOLATED_TRADE</strong>(Isolated Margin Trading), and the default is <strong>TRADE</strong> to cancel the spot trading orders.
   */
  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
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
    return EncodingUtil.createUrlQueryParameters("currentPage", currentPage,"pageSize", pageSize,"status", status,"symbol", symbol,"side", side,"type", type,"tradeType", tradeType,"startAt", startAt,"endAt", endAt);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
