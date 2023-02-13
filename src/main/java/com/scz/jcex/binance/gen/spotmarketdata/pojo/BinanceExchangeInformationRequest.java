package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationRequestSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Binance SpotMarketData API exchangeInformation REST endpointCurrent exchange trading rules and symbol information<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceExchangeInformationRequestSerializer.class)
public class BinanceExchangeInformationRequest {
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
}
