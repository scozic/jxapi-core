package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelAllOrdersRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API CancelAllOrders REST endpointRequest via this endpoint to cancel all open orders. The response is a list of ids of the canceled orders.<br/>See <a href="https://docs.kucoin.com/#cancel-all-orders">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinCancelAllOrdersRequestSerializer.class)
public class KucoinCancelAllOrdersRequest implements RestEndpointUrlParameters {
  private String symbol;
  private String tradeType;
  
  /**
   * @return [Optional] symbol, cancel the orders for the specified trade pair.
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol [Optional] symbol, cancel the orders for the specified trade pair.
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
  
  @Override
  public String getUrlParameters() {
    return "";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
