package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toBinance SpotMarketData API AllMarketTickersStream REST endpoint
 */
public class BinanceAllMarketTickersStreamRequest implements WebsocketSubscribeParameters {
  
  @Override
  public String toString(){
    return EncodingUtil.pojoToString(this);
  }

  	@Override
  	public String getTopic() {
  		return EncodingUtil.substituteArguments("!ticker@arr");
  	}
  
}
