package com.scz.jcex.binance.gen.spotmarketdata;

import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsMessage;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import java.io.IOException;

/**
 * SpotMarketData CEX SpotMarketData API</br>
 * Binance spot market data API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  BinanceSpotMarketData {
  /**
   * Current exchange trading rules and symbol information for all spot trading pairs
   */
  BinanceExchangeInformationAllResponse exchangeInformationAll(BinanceExchangeInformationAllRequest request) throws IOException;
  /**
   * Current exchange trading rules and symbol information for a list of spot trading pairs
   */
  BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException;
  
  /**
   * Subscribe to AllMarketTickersStream stream.<br/>
   * All Market Tickers Stream, see <a href="https://binance-docs.github.io/apidocs/spot/en/#all-market-tickers-stream">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeAllMarketTickersStream(BinanceAllMarketTickersStreamRequest request, WebsocketListener<BinanceAllMarketTickersStreamMessage> listener);
  
  /**
   * Unsubscribe from AllMarketTickersStream stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeAllMarketTickersStream()
   */
  boolean unsubscribeAllMarketTickersStream(String subscriptionId);
  
  /**
   * Subscribe to IndividualSymbolTickerStreams stream.<br/>
   * 24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs, see <a href="https://binance-docs.github.io/apidocs/spot/en/#individual-symbol-ticker-streams">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeIndividualSymbolTickerStreams(BinanceIndividualSymbolTickerStreamsRequest request, WebsocketListener<BinanceIndividualSymbolTickerStreamsMessage> listener);
  
  /**
   * Unsubscribe from IndividualSymbolTickerStreams stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeIndividualSymbolTickerStreams()
   */
  boolean unsubscribeIndividualSymbolTickerStreams(String subscriptionId);
}
