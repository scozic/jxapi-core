package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceExecutionReportUserDataStreamMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Message disseminated upon subscription to Binance SpotTrading API executionReportUserDataStream websocket endpoint request<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceExecutionReportUserDataStreamMessageSerializer.class)
public class BinanceExecutionReportUserDataStreamMessage {
  private BigDecimal A;
  private BigDecimal B;
  private String C;
  private long D;
  private long E;
  private BigDecimal F;
  private long I;
  private long J;
  private BigDecimal L;
  private boolean M;
  private String N;
  private long O;
  private BigDecimal P;
  private BigDecimal Q;
  private String S;
  private long T;
  private long U;
  private String V;
  private long W;
  private String X;
  private BigDecimal Y;
  private BigDecimal Z;
  private String c;
  private int d;
  private String e;
  private String f;
  private int g;
  private String i;
  private long j;
  private BigDecimal l;
  private boolean m;
  private int n;
  private String o;
  private BigDecimal p;
  private BigDecimal q;
  private String r;
  private String s;
  private long t;
  private long u;
  private long v;
  private boolean w;
  private String x;
  private BigDecimal z;
  
  /**
   * @return Prevented Quantity; This is only visible if the order expired due to STP trigger.
   */
  public BigDecimal getA(){
    return A;
  }
  
  /**
   * @param A Prevented Quantity; This is only visible if the order expired due to STP trigger.
   */
  public void setA(BigDecimal A) {
    this.A = A;
  }
  
  /**
   * @return Last Prevented Quantity; This is only visible if the order expired due to STP trigger.
   */
  public BigDecimal getB(){
    return B;
  }
  
  /**
   * @param B Last Prevented Quantity; This is only visible if the order expired due to STP trigger.
   */
  public void setB(BigDecimal B) {
    this.B = B;
  }
  
  /**
   * @return Original client order ID; This is the ID of the order being canceled
   */
  public String getC(){
    return C;
  }
  
  /**
   * @param C Original client order ID; This is the ID of the order being canceled
   */
  public void setC(String C) {
    this.C = C;
  }
  
  /**
   * @return Trailing Time; This is only visible if the trailing stop order has been activated.
   */
  public long getD(){
    return D;
  }
  
  /**
   * @param D Trailing Time; This is only visible if the trailing stop order has been activated.
   */
  public void setD(long D) {
    this.D = D;
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
   * @return Iceberg quantity
   */
  public BigDecimal getF(){
    return F;
  }
  
  /**
   * @param F Iceberg quantity
   */
  public void setF(BigDecimal F) {
    this.F = F;
  }
  
  /**
   * @return Ignore
   */
  public long getI(){
    return I;
  }
  
  /**
   * @param I Ignore
   */
  public void setI(long I) {
    this.I = I;
  }
  
  /**
   * @return Strategy Type; This is only visible if the strategyType parameter was provided upon order placement
   */
  public long getJ(){
    return J;
  }
  
  /**
   * @param J Strategy Type; This is only visible if the strategyType parameter was provided upon order placement
   */
  public void setJ(long J) {
    this.J = J;
  }
  
  /**
   * @return Last executed price
   */
  public BigDecimal getL(){
    return L;
  }
  
  /**
   * @param L Last executed price
   */
  public void setL(BigDecimal L) {
    this.L = L;
  }
  
  /**
   * @return Ignore
   */
  public boolean isM(){
    return M;
  }
  
  /**
   * @param M Ignore
   */
  public void setM(boolean M) {
    this.M = M;
  }
  
  /**
   * @return Commission asset
   */
  public String getN(){
    return N;
  }
  
  /**
   * @param N Commission asset
   */
  public void setN(String N) {
    this.N = N;
  }
  
  /**
   * @return Order creation time
   */
  public long getO(){
    return O;
  }
  
  /**
   * @param O Order creation time
   */
  public void setO(long O) {
    this.O = O;
  }
  
  /**
   * @return Stop price
   */
  public BigDecimal getP(){
    return P;
  }
  
  /**
   * @param P Stop price
   */
  public void setP(BigDecimal P) {
    this.P = P;
  }
  
  /**
   * @return Quote Order Quantity
   */
  public BigDecimal getQ(){
    return Q;
  }
  
  /**
   * @param Q Quote Order Quantity
   */
  public void setQ(BigDecimal Q) {
    this.Q = Q;
  }
  
  /**
   * @return Side
   */
  public String getS(){
    return S;
  }
  
  /**
   * @param S Side
   */
  public void setS(String S) {
    this.S = S;
  }
  
  /**
   * @return Transaction time
   */
  public long getT(){
    return T;
  }
  
  /**
   * @param T Transaction time
   */
  public void setT(long T) {
    this.T = T;
  }
  
  /**
   * @return CounterOrderId; This is only visible if the order expired due to STP trigger.
   */
  public long getU(){
    return U;
  }
  
  /**
   * @param U CounterOrderId; This is only visible if the order expired due to STP trigger.
   */
  public void setU(long U) {
    this.U = U;
  }
  
  /**
   * @return selfTradePreventionMode
   */
  public String getV(){
    return V;
  }
  
  /**
   * @param V selfTradePreventionMode
   */
  public void setV(String V) {
    this.V = V;
  }
  
  /**
   * @return Working Time; This is only visible if the order has been placed on the book.
   */
  public long getW(){
    return W;
  }
  
  /**
   * @param W Working Time; This is only visible if the order has been placed on the book.
   */
  public void setW(long W) {
    this.W = W;
  }
  
  /**
   * @return Current order status
   */
  public String getX(){
    return X;
  }
  
  /**
   * @param X Current order status
   */
  public void setX(String X) {
    this.X = X;
  }
  
  /**
   * @return Last quote asset transacted quantity (i.e. lastPrice * lastQty)
   */
  public BigDecimal getY(){
    return Y;
  }
  
  /**
   * @param Y Last quote asset transacted quantity (i.e. lastPrice * lastQty)
   */
  public void setY(BigDecimal Y) {
    this.Y = Y;
  }
  
  /**
   * @return Cumulative quote asset transacted quantity
   */
  public BigDecimal getZ(){
    return Z;
  }
  
  /**
   * @param Z Cumulative quote asset transacted quantity
   */
  public void setZ(BigDecimal Z) {
    this.Z = Z;
  }
  
  /**
   * @return Client
   */
  public String getc(){
    return c;
  }
  
  /**
   * @param c Client
   */
  public void setc(String c) {
    this.c = c;
  }
  
  /**
   * @return Trailing Delta; This is only visible if the order was a trailing stop order
   */
  public int getd(){
    return d;
  }
  
  /**
   * @param d Trailing Delta; This is only visible if the order was a trailing stop order
   */
  public void setd(int d) {
    this.d = d;
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
   * @return Time in force
   */
  public String getf(){
    return f;
  }
  
  /**
   * @param f Time in force
   */
  public void setf(String f) {
    this.f = f;
  }
  
  /**
   * @return OrderListId
   */
  public int getG(){
    return g;
  }
  
  /**
   * @param g OrderListId
   */
  public void setG(int g) {
    this.g = g;
  }
  
  /**
   * @return Order ID
   */
  public String geti(){
    return i;
  }
  
  /**
   * @param i Order ID
   */
  public void seti(String i) {
    this.i = i;
  }
  
  /**
   * @return Strategy ID; This is only visible if the strategyId parameter was provided upon order placement
   */
  public long getj(){
    return j;
  }
  
  /**
   * @param j Strategy ID; This is only visible if the strategyId parameter was provided upon order placement
   */
  public void setj(long j) {
    this.j = j;
  }
  
  /**
   * @return Last executed quantity
   */
  public BigDecimal getl(){
    return l;
  }
  
  /**
   * @param l Last executed quantity
   */
  public void setl(BigDecimal l) {
    this.l = l;
  }
  
  /**
   * @return Is this trade the maker side?
   */
  public boolean ism(){
    return m;
  }
  
  /**
   * @param m Is this trade the maker side?
   */
  public void setm(boolean m) {
    this.m = m;
  }
  
  /**
   * @return Commission amount
   */
  public int getn(){
    return n;
  }
  
  /**
   * @param n Commission amount
   */
  public void setn(int n) {
    this.n = n;
  }
  
  /**
   * @return Order type
   */
  public String geto(){
    return o;
  }
  
  /**
   * @param o Order type
   */
  public void seto(String o) {
    this.o = o;
  }
  
  /**
   * @return Order price
   */
  public BigDecimal getp(){
    return p;
  }
  
  /**
   * @param p Order price
   */
  public void setp(BigDecimal p) {
    this.p = p;
  }
  
  /**
   * @return Order quantity
   */
  public BigDecimal getq(){
    return q;
  }
  
  /**
   * @param q Order quantity
   */
  public void setq(BigDecimal q) {
    this.q = q;
  }
  
  /**
   * @return Order reject reason, ; will be an error code
   */
  public String getR(){
    return r;
  }
  
  /**
   * @param r Order reject reason, ; will be an error code
   */
  public void setR(String r) {
    this.r = r;
  }
  
  /**
   * @return Symbol
   */
  public String gets(){
    return s;
  }
  
  /**
   * @param s Symbol
   */
  public void sets(String s) {
    this.s = s;
  }
  
  /**
   * @return Trade ID
   */
  public long gett(){
    return t;
  }
  
  /**
   * @param t Trade ID
   */
  public void sett(long t) {
    this.t = t;
  }
  
  /**
   * @return TradeGroupId; This is only visible if the account is part of a trade group and the order expired due to STP trigger.
   */
  public long getu(){
    return u;
  }
  
  /**
   * @param u TradeGroupId; This is only visible if the account is part of a trade group and the order expired due to STP trigger.
   */
  public void setu(long u) {
    this.u = u;
  }
  
  /**
   * @return Prevented Match Id; This is only visible if the order expire due to STP trigger.
   */
  public long getv(){
    return v;
  }
  
  /**
   * @param v Prevented Match Id; This is only visible if the order expire due to STP trigger.
   */
  public void setv(long v) {
    this.v = v;
  }
  
  /**
   * @return Is the order on the book?
   */
  public boolean isw(){
    return w;
  }
  
  /**
   * @param w Is the order on the book?
   */
  public void setw(boolean w) {
    this.w = w;
  }
  
  /**
   * @return Current execution type
   */
  public String getx(){
    return x;
  }
  
  /**
   * @param x Current execution type
   */
  public void setx(String x) {
    this.x = x;
  }
  
  /**
   * @return Cumulative filled quantity
   */
  public BigDecimal getz(){
    return z;
  }
  
  /**
   * @param z Cumulative filled quantity
   */
  public void setz(BigDecimal z) {
    this.z = z;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
