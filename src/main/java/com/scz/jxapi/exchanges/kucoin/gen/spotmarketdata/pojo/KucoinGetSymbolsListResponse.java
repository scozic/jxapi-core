package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetSymbolsListResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.util.List;

/**
 * Response to Kucoin SpotMarketData API getSymbolsList REST endpoint request<br/>Request via this endpoint to get a list of available currency pairs for trading. If you want to get the market information of the trading symbol, please use Get All Tickers.<br/>See <a href="https://docs.kucoin.com/#get-symbols-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetSymbolsListResponseSerializer.class)
public class KucoinGetSymbolsListResponse {
  private String code;
  private List<KucoinGetSymbolsListResponseData> data;
  private String msg;
  
  /**
   * @return System error code, see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public String getCode(){
    return code;
  }
  
  /**
   * @param code System error code, see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * @return List of market information for each market symbol
   */
  public List<KucoinGetSymbolsListResponseData> getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(List<KucoinGetSymbolsListResponseData> data) {
    this.data = data;
  }
  
  /**
   * @return System error code description, provided in error response when code is not '0', see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public String getMsg(){
    return msg;
  }
  
  /**
   * @param msg System error code description, provided in error response when code is not '0', see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
