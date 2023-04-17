package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

import java.util.List;

/**
 * Request for Binance SpotMarketData API exchangeInformation REST endpointCurrent exchange trading rules and symbol information for a list of spot trading pairs.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#exchange-information">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
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
    return EncodingUtil.createUrlQueryParameters("symbols", EncodingUtil.listToUrlParamString(symbols));
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
