package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Request for Binance SpotMarketData API exchangeInformation REST endpointCurrent exchange trading rules and symbol information for a list of spot trading pairs<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceExchangeInformationRequestSerializer.class)
public class BinanceExchangeInformationRequest implements RestEndpointUrlParameters {
  private List<String> symbols;
  
  /**
   * @return List of market symbols to retrieve exchange information for.
   */
  public List<String> getSymbols(){
    return symbols;
  }
  
  /**
   * @param symbols List of market symbols to retrieve exchange information for.
   */
  public void setSymbols(List<String> symbols) {
    this.symbols = symbols;
  }
  
  
  @Override
  public String getUrlParameters() {
    return com.scz.jcex.util.EncodingUtil.substituteArguments("symbols=%5B%22${symbols}%22%5D", "symbols", com.scz.jcex.util.EncodingUtil.listToString(symbols, "%22,%22"));
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
}
