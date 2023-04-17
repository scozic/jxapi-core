package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetAllTickersResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;
import java.util.List;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinGetAllTickersResponseDataSerializer.class)
public class KucoinGetAllTickersResponseData {
  private List<KucoinGetAllTickersResponseDataTicker> ticker;
  private Long time;
  
  /**
   * @return List of market information for each market symbol
   */
  public List<KucoinGetAllTickersResponseDataTicker> getTicker(){
    return ticker;
  }
  
  /**
   * @param ticker List of market information for each market symbol
   */
  public void setTicker(List<KucoinGetAllTickersResponseDataTicker> ticker) {
    this.ticker = ticker;
  }
  
  /**
   * @return Timestamp
   */
  public Long getTime(){
    return time;
  }
  
  /**
   * @param time Timestamp
   */
  public void setTime(Long time) {
    this.time = time;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
