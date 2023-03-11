package com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers.BinanceIndividualSymbolTickerStreamsMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Message disseminated upon subscription to Binance SpotMarketData API IndividualSymbolTickerStreams websocket endpoint request<br/>24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#individual-symbol-ticker-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceIndividualSymbolTickerStreamsMessageSerializer.class)
public class BinanceIndividualSymbolTickerStreamsMessage {
  private BigDecimal baseAssetVolume;
  private long closeTime;
  private long eventTime;
  private String eventType;
  private long firstTradeID;
  private BigDecimal highPrice;
  private BigDecimal lastPrice;
  private long lastTradeID;
  private BigDecimal lowPrice;
  private BigDecimal openPrice;
  private long openTime;
  private BigDecimal priceChange;
  private BigDecimal priceChangePercent;
  private BigDecimal quoteAssetVolume;
  private String symbol;
  private long tradeCount;
  private BigDecimal weightedAvgPrice;
  
  /**
   * @return Total traded base asset volume. Message field <strong>v</strong>
   */
  public BigDecimal getBaseAssetVolume(){
    return baseAssetVolume;
  }
  
  /**
   * @param baseAssetVolume Total traded base asset volume. Message field <strong>v</strong>
   */
  public void setBaseAssetVolume(BigDecimal baseAssetVolume) {
    this.baseAssetVolume = baseAssetVolume;
  }
  
  /**
   * @return Statistics close time. Message field <strong>C</strong>
   */
  public long getCloseTime(){
    return closeTime;
  }
  
  /**
   * @param closeTime Statistics close time. Message field <strong>C</strong>
   */
  public void setCloseTime(long closeTime) {
    this.closeTime = closeTime;
  }
  
  /**
   * @return Event time. Message field <strong>E</strong>
   */
  public long getEventTime(){
    return eventTime;
  }
  
  /**
   * @param eventTime Event time. Message field <strong>E</strong>
   */
  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }
  
  /**
   * @return Event type. Message field <strong>e</strong>
   */
  public String getEventType(){
    return eventType;
  }
  
  /**
   * @param eventType Event type. Message field <strong>e</strong>
   */
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  /**
   * @return First trade ID. Message field <strong>F</strong>
   */
  public long getFirstTradeID(){
    return firstTradeID;
  }
  
  /**
   * @param firstTradeID First trade ID. Message field <strong>F</strong>
   */
  public void setFirstTradeID(long firstTradeID) {
    this.firstTradeID = firstTradeID;
  }
  
  /**
   * @return High price. Message field <strong>h</strong>
   */
  public BigDecimal getHighPrice(){
    return highPrice;
  }
  
  /**
   * @param highPrice High price. Message field <strong>h</strong>
   */
  public void setHighPrice(BigDecimal highPrice) {
    this.highPrice = highPrice;
  }
  
  /**
   * @return Last price Message field <strong>c</strong>
   */
  public BigDecimal getLastPrice(){
    return lastPrice;
  }
  
  /**
   * @param lastPrice Last price Message field <strong>c</strong>
   */
  public void setLastPrice(BigDecimal lastPrice) {
    this.lastPrice = lastPrice;
  }
  
  /**
   * @return Last trade ID. Message field <strong>L</strong>
   */
  public long getLastTradeID(){
    return lastTradeID;
  }
  
  /**
   * @param lastTradeID Last trade ID. Message field <strong>L</strong>
   */
  public void setLastTradeID(long lastTradeID) {
    this.lastTradeID = lastTradeID;
  }
  
  /**
   * @return Low price. Message field <strong>l</strong>
   */
  public BigDecimal getLowPrice(){
    return lowPrice;
  }
  
  /**
   * @param lowPrice Low price. Message field <strong>l</strong>
   */
  public void setLowPrice(BigDecimal lowPrice) {
    this.lowPrice = lowPrice;
  }
  
  /**
   * @return Open price. Message field <strong>o</strong>
   */
  public BigDecimal getOpenPrice(){
    return openPrice;
  }
  
  /**
   * @param openPrice Open price. Message field <strong>o</strong>
   */
  public void setOpenPrice(BigDecimal openPrice) {
    this.openPrice = openPrice;
  }
  
  /**
   * @return Statistics open time. Message field <strong>O</strong>
   */
  public long getOpenTime(){
    return openTime;
  }
  
  /**
   * @param openTime Statistics open time. Message field <strong>O</strong>
   */
  public void setOpenTime(long openTime) {
    this.openTime = openTime;
  }
  
  /**
   * @return Price change. Message field <strong>p</strong>
   */
  public BigDecimal getPriceChange(){
    return priceChange;
  }
  
  /**
   * @param priceChange Price change. Message field <strong>p</strong>
   */
  public void setPriceChange(BigDecimal priceChange) {
    this.priceChange = priceChange;
  }
  
  /**
   * @return Price change percent. Message field <strong>P</strong>
   */
  public BigDecimal getPriceChangePercent(){
    return priceChangePercent;
  }
  
  /**
   * @param priceChangePercent Price change percent. Message field <strong>P</strong>
   */
  public void setPriceChangePercent(BigDecimal priceChangePercent) {
    this.priceChangePercent = priceChangePercent;
  }
  
  /**
   * @return Total traded quote asset volume. Message field <strong>q</strong>
   */
  public BigDecimal getQuoteAssetVolume(){
    return quoteAssetVolume;
  }
  
  /**
   * @param quoteAssetVolume Total traded quote asset volume. Message field <strong>q</strong>
   */
  public void setQuoteAssetVolume(BigDecimal quoteAssetVolume) {
    this.quoteAssetVolume = quoteAssetVolume;
  }
  
  /**
   * @return Symbol. Message field <strong>s</strong>
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Symbol. Message field <strong>s</strong>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Total number of trades. Message field <strong>n</strong>
   */
  public long getTradeCount(){
    return tradeCount;
  }
  
  /**
   * @param tradeCount Total number of trades. Message field <strong>n</strong>
   */
  public void setTradeCount(long tradeCount) {
    this.tradeCount = tradeCount;
  }
  
  /**
   * @return Weighted average price. Message field <strong>w</strong>
   */
  public BigDecimal getWeightedAvgPrice(){
    return weightedAvgPrice;
  }
  
  /**
   * @param weightedAvgPrice Weighted average price. Message field <strong>w</strong>
   */
  public void setWeightedAvgPrice(BigDecimal weightedAvgPrice) {
    this.weightedAvgPrice = weightedAvgPrice;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
