package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Binance SpotMarketData API exchangeInformation REST endpoint request
 */
public class BinanceExchangeInformationResponse {
  private long serverTime;
  private List<BinanceExchangeInformationResponseSymbols> symbols;
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
  public List<BinanceExchangeInformationResponseSymbols> getSymbols(){
    return symbols;
  }
  
  /**
   * @param symbols List of market information for each market symbol
   */
  public void setSymbols(List<BinanceExchangeInformationResponseSymbols> symbols) {
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
  public String toString(){
    return EncodingUtil.pojoToString(this);
  }
  
}
