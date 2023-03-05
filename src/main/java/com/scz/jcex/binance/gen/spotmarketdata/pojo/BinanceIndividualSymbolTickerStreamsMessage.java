package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceIndividualSymbolTickerStreamsMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Message disseminated upon subscription to Binance SpotMarketData API IndividualSymbolTickerStreams websocket endpoint request<br/>24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs, see <a href="https://binance-docs.github.io/apidocs/spot/en/#individual-symbol-ticker-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceIndividualSymbolTickerStreamsMessageSerializer.class)
public class BinanceIndividualSymbolTickerStreamsMessage {
  private long C;
  private long E;
  private long F;
  private long L;
  private long O;
  private BigDecimal P;
  private BigDecimal c;
  private String e;
  private BigDecimal h;
  private BigDecimal l;
  private long n;
  private BigDecimal o;
  private BigDecimal p;
  private BigDecimal q;
  private String s;
  private BigDecimal v;
  private BigDecimal w;
  
  /**
   * @return Statistics close time
   */
  public long getC(){
    return C;
  }
  
  /**
   * @param C Statistics close time
   */
  public void setC(long C) {
    this.C = C;
  }
  
  /**
   * @return Event time
   */
  public long getE(){
    return E;
  }
  
  /**
   * @param E Event time
   */
  public void setE(long E) {
    this.E = E;
  }
  
  /**
   * @return First trade ID
   */
  public long getF(){
    return F;
  }
  
  /**
   * @param F First trade ID
   */
  public void setF(long F) {
    this.F = F;
  }
  
  /**
   * @return Last trade ID
   */
  public long getL(){
    return L;
  }
  
  /**
   * @param L Last trade ID
   */
  public void setL(long L) {
    this.L = L;
  }
  
  /**
   * @return Statistics open time
   */
  public long getO(){
    return O;
  }
  
  /**
   * @param O Statistics open time
   */
  public void setO(long O) {
    this.O = O;
  }
  
  /**
   * @return Price change percent
   */
  public BigDecimal getP(){
    return P;
  }
  
  /**
   * @param P Price change percent
   */
  public void setP(BigDecimal P) {
    this.P = P;
  }
  
  /**
   * @return Last price
   */
  public BigDecimal getc(){
    return c;
  }
  
  /**
   * @param c Last price
   */
  public void setc(BigDecimal c) {
    this.c = c;
  }
  
  /**
   * @return Event type
   */
  public String gete(){
    return e;
  }
  
  /**
   * @param e Event type
   */
  public void sete(String e) {
    this.e = e;
  }
  
  /**
   * @return High price
   */
  public BigDecimal getH(){
    return h;
  }
  
  /**
   * @param h High price
   */
  public void setH(BigDecimal h) {
    this.h = h;
  }
  
  /**
   * @return Low price
   */
  public BigDecimal getl(){
    return l;
  }
  
  /**
   * @param l Low price
   */
  public void setl(BigDecimal l) {
    this.l = l;
  }
  
  /**
   * @return Total number of trades
   */
  public long getN(){
    return n;
  }
  
  /**
   * @param n Total number of trades
   */
  public void setN(long n) {
    this.n = n;
  }
  
  /**
   * @return Open price
   */
  public BigDecimal geto(){
    return o;
  }
  
  /**
   * @param o Open price
   */
  public void seto(BigDecimal o) {
    this.o = o;
  }
  
  /**
   * @return Price change
   */
  public BigDecimal getp(){
    return p;
  }
  
  /**
   * @param p Price change
   */
  public void setp(BigDecimal p) {
    this.p = p;
  }
  
  /**
   * @return Total traded quote asset volume
   */
  public BigDecimal getQ(){
    return q;
  }
  
  /**
   * @param q Total traded quote asset volume
   */
  public void setQ(BigDecimal q) {
    this.q = q;
  }
  
  /**
   * @return Symbol
   */
  public String getS(){
    return s;
  }
  
  /**
   * @param s Symbol
   */
  public void setS(String s) {
    this.s = s;
  }
  
  /**
   * @return Total traded base asset volume
   */
  public BigDecimal getV(){
    return v;
  }
  
  /**
   * @param v Total traded base asset volume
   */
  public void setV(BigDecimal v) {
    this.v = v;
  }
  
  /**
   * @return Weighted average price
   */
  public BigDecimal getW(){
    return w;
  }
  
  /**
   * @param w Weighted average price
   */
  public void setW(BigDecimal w) {
    this.w = w;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
