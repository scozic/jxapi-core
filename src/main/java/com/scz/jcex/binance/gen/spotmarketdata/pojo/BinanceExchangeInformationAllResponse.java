package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationAllResponseSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Binance SpotMarketData API exchangeInformationAll REST endpoint request<br/>Current exchange trading rules and symbol information for all spot trading pairs<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceExchangeInformationAllResponseSerializer.class)
public class BinanceExchangeInformationAllResponse {
  private long serverTime;
  private List<BinanceExchangeInformationAllResponseSymbols> symbols;
  private String timezone;
  
  /**
   * @return Server timezone
   */
  public long getServerTime(){
    return serverTime;
  }
  
  /**
   * @param serverTime Server timezone
   */
  public void setServerTime(long serverTime) {
    this.serverTime = serverTime;
  }
  
  /**
   * @return List of market information for each market symbol
   */
  public List<BinanceExchangeInformationAllResponseSymbols> getSymbols(){
    return symbols;
  }
  
  /**
   * @param symbols List of market information for each market symbol
   */
  public void setSymbols(List<BinanceExchangeInformationAllResponseSymbols> symbols) {
    this.symbols = symbols;
  }
  
  /**
   * @return Server timezone
   */
  public String getTimezone(){
    return timezone;
  }
  
  /**
   * @param timezone Server timezone
   */
  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
}
