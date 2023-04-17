package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata;

import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerRequest;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponse;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Request;
import com.scz.jxapi.netutils.websocket.WebsocketListener;

import java.io.IOException;

/**
 * FuturesMarketData CEX FuturesMarketData API</br>
 * Kucoin Futures market data API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinFuturesMarketDataApi {
  /**
   * Submit request to get the info of all open contracts.<br/>See <a href="https://docs.kucoin.com/futures/#get-open-contract-list">API</a>
   */
  KucoinGetOpenContractListResponse getOpenContractList(KucoinGetOpenContractListRequest request) throws IOException;
  /**
   * The real-time ticker includes the last traded price, the last traded size, transaction ID, the side of liquidity taker, the best bid price and size, the best ask price and size as well as the transaction time of the orders. These messages can also be obtained through Websocket. The Sequence Number is used to judge whether the messages pushed by Websocket is continuous.<br/>See <a href="https://docs.kucoin.com/futures/#get-real-time-ticker">API</a>
   */
  KucoinGetRealTimeTickerResponse getRealTimeTicker(KucoinGetRealTimeTickerRequest request) throws IOException;
  
  /**
   * Subscribe to RealTimeSymbolTickerV2 stream.<br/>
   * 24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://docs.kucoin.com/#symbol-ticker">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeRealTimeSymbolTickerV2(KucoinRealTimeSymbolTickerV2Request request, WebsocketListener<KucoinRealTimeSymbolTickerV2Message> listener);
  
  /**
   * Unsubscribe from RealTimeSymbolTickerV2 stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeRealTimeSymbolTickerV2()
   */
  boolean unsubscribeRealTimeSymbolTickerV2(String subscriptionId);
}
