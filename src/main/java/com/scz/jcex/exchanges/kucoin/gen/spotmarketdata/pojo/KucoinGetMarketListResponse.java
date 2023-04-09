package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetMarketListResponseSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Kucoin SpotMarketData API getMarketList REST endpoint request<br/>Request via this endpoint to get the transaction currency for the entire trading market.<br/>See <a href="https://docs.kucoin.com/#get-market-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetMarketListResponseSerializer.class)
public class KucoinGetMarketListResponse {
  private String code;
  private List<String> data;
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
   * @return Transaction currency list
   */
  public List<String> getData(){
    return data;
  }
  
  /**
   * @param data Transaction currency list
   */
  public void setData(List<String> data) {
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
