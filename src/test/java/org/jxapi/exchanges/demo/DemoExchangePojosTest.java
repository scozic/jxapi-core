package org.jxapi.exchanges.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;

/**
 * Tests behavior of methods of a few generated POJOs from exchange wrapper
 * classes for DemoExchange. This is to ensure specific methods for POJOs :
 * <code>builder()</code>, <code>equals()</code>, <code>compareTo()</code>,
 * <code>hashCode()</code>, <code>deepClone()</code> behave as expected.
 */
public class DemoExchangePojosTest {

  
  @Test
  public void testBuildDemoExchangeMarketDataExchangeInfoResponse() {
    DemoExchangeMarketDataExchangeInfoResponse pojo = buildDemoExchangeMarketDataExchangeInfoResponse();
    Assert.assertEquals(200, pojo.getResponseCode().intValue());
    Assert.assertEquals(2, pojo.getPayload().size());
    DemoExchangeMarketDataExchangeInfoResponsePayload payload = pojo.getPayload().get(0);
    Assert.assertEquals("BTC_USDT", payload.getSymbol());
    Assert.assertEquals(new BigDecimal("0.0001"), payload.getMinOrderSize());
    Assert.assertEquals(new BigDecimal("0.1"), payload.getOrderTickSize());
    payload = pojo.getPayload().get(1);
    Assert.assertEquals("ETH_USDT", payload.getSymbol());
    Assert.assertEquals(new BigDecimal("0.001"), payload.getMinOrderSize());
    Assert.assertEquals(new BigDecimal("0.01"), payload.getOrderTickSize());
  }
  
  @Test
  public void testBuildDemoExchangeMarketDataTickersResponse() {
    DemoExchangeMarketDataTickersResponse pojo = buildDemoExchangeMarketDataTickersResponse();
    Assert.assertEquals(200, pojo.getResponseCode().intValue());
    Assert.assertEquals(2, pojo.getPayload().size());
    DemoExchangeMarketDataTickersResponsePayload payload = pojo.getPayload().get("BTC_USDT");
    Assert.assertEquals(new BigDecimal("90417.60"), payload.getLast());
    Assert.assertEquals(new BigDecimal("90933.82"), payload.getHigh());
    Assert.assertEquals(new BigDecimal("863334.53"), payload.getLow());
    Assert.assertEquals(new BigDecimal("3631198541.88"), payload.getVolume());
    Assert.assertEquals(1741207831531L, payload.getTime().longValue());
    payload = pojo.getPayload().get("ETH_USDT");
    Assert.assertEquals(new BigDecimal("2227.05"), payload.getLast());
    Assert.assertEquals(new BigDecimal("2273.51"), payload.getHigh());
    Assert.assertEquals(new BigDecimal("2126.06"), payload.getLow());
    Assert.assertEquals(new BigDecimal("1630680938.25"), payload.getVolume());
    Assert.assertEquals(1741207940214L, payload.getTime().longValue());
  }
  
  @Test
  public void testDemoExchangeMarketDataExchangeInfoResponseGettersAndSetters() {
    DemoExchangeMarketDataExchangeInfoResponse pojo = new DemoExchangeMarketDataExchangeInfoResponse();
    pojo.setResponseCode(200);
    Assert.assertEquals(200, pojo.getResponseCode().intValue());
    DemoExchangeMarketDataExchangeInfoResponsePayload payload = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    payload.setSymbol("BTC_USDT");
    payload.setMinOrderSize(new BigDecimal("0.0001"));
    payload.setOrderTickSize(new BigDecimal("0.1"));
    pojo.setPayload(List.of(payload));
    Assert.assertEquals(1, pojo.getPayload().size());
    Assert.assertEquals("BTC_USDT", pojo.getPayload().get(0).getSymbol());
    Assert.assertEquals(new BigDecimal("0.0001"), pojo.getPayload().get(0).getMinOrderSize());
    Assert.assertEquals(new BigDecimal("0.1"), pojo.getPayload().get(0).getOrderTickSize());
  }
  
  @Test
  public void testDemoExchangeMarketDataTickersResponseGettersAndSetters() {
    DemoExchangeMarketDataTickersResponse pojo = new DemoExchangeMarketDataTickersResponse();
    pojo.setResponseCode(200);
    Assert.assertEquals(200, pojo.getResponseCode().intValue());
    DemoExchangeMarketDataTickersResponsePayload payload = new DemoExchangeMarketDataTickersResponsePayload();
    payload.setLast(new BigDecimal("90417.60"));
    payload.setHigh(new BigDecimal("90933.82"));
    payload.setLow(new BigDecimal("863334.53"));
    payload.setVolume(new BigDecimal("3631198541.88"));
    payload.setTime(1741207831531L);
    pojo.setPayload(Map.of("BTC_USDT", payload));
    Assert.assertEquals(1, pojo.getPayload().size());
    Assert.assertEquals(new BigDecimal("90417.60"), pojo.getPayload().get("BTC_USDT").getLast());
    Assert.assertEquals(new BigDecimal("90933.82"), pojo.getPayload().get("BTC_USDT").getHigh());
    Assert.assertEquals(new BigDecimal("863334.53"), pojo.getPayload().get("BTC_USDT").getLow());
    Assert.assertEquals(new BigDecimal("3631198541.88"), pojo.getPayload().get("BTC_USDT").getVolume());
    Assert.assertEquals(1741207831531L, pojo.getPayload().get("BTC_USDT").getTime().longValue());
  }
  
  @Test
  public void testDeepCloneDemoExchangeMarketDataExchangeInfoResponse() {
    DemoExchangeMarketDataExchangeInfoResponse pojo = buildDemoExchangeMarketDataExchangeInfoResponse();
    DemoExchangeMarketDataExchangeInfoResponse clone = pojo.deepClone();
    Assert.assertEquals(pojo, clone);
    Assert.assertNotSame(pojo, clone);
    Assert.assertNotSame(pojo.getPayload(), clone.getPayload());
  }
  
  @Test
  public void testDeepCloneDemoExchangeMarketDataTickersResponse() {
    DemoExchangeMarketDataTickersResponse pojo =buildDemoExchangeMarketDataTickersResponse();
    DemoExchangeMarketDataTickersResponse clone = pojo.deepClone();
    Assert.assertEquals(pojo, clone);
    Assert.assertNotSame(pojo, clone);
    Assert.assertNotSame(pojo.getPayload(), clone.getPayload());
  }
  
  @Test
  public void testDemoExchangeMarketDataExchangeInfoResponseEquals() {
    DemoExchangeMarketDataExchangeInfoResponse pojo1 = buildDemoExchangeMarketDataExchangeInfoResponse();
    DemoExchangeMarketDataExchangeInfoResponse pojo2 = buildDemoExchangeMarketDataExchangeInfoResponse();
    Assert.assertEquals(pojo1, pojo2);
    Assert.assertEquals(pojo1.hashCode(), pojo2.hashCode());
    pojo2.getPayload().get(0).setSymbol("ETH_USDT");
    Assert.assertNotEquals(pojo1, pojo2);
    Assert.assertNotEquals(pojo1.hashCode(), pojo2.hashCode());
  }
  
  @Test
  public void testDemoExchangeMarketDataTickersResponseEquals() {
    DemoExchangeMarketDataTickersResponse pojo1 = buildDemoExchangeMarketDataTickersResponse();
    DemoExchangeMarketDataTickersResponse pojo2 = buildDemoExchangeMarketDataTickersResponse();
    Assert.assertEquals(pojo1, pojo2);
    Assert.assertEquals(pojo1.hashCode(), pojo2.hashCode());
    pojo2.getPayload().get("BTC_USDT").setLast(new BigDecimal("90417.61"));
    Assert.assertNotEquals(pojo1, pojo2);
    Assert.assertNotEquals(pojo1.hashCode(), pojo2.hashCode());
  }
  
  @Test
  public void testDemoExchangeMarketDataExchangeInfoResponseCompareTo() {
    DemoExchangeMarketDataExchangeInfoResponse pojo1 = buildDemoExchangeMarketDataExchangeInfoResponse();
    DemoExchangeMarketDataExchangeInfoResponse pojo2 = buildDemoExchangeMarketDataExchangeInfoResponse();
    Assert.assertEquals(0, pojo1.compareTo(pojo2));
    pojo2.setResponseCode(201);
    Assert.assertEquals(-1, pojo1.compareTo(pojo2));
    pojo2.setResponseCode(200);
    pojo2.getPayload().get(0).setSymbol("ADA_USDT");
    Assert.assertEquals(1, pojo1.compareTo(pojo2));
  }
  
  @Test
  public void testDemoExchangeMarketDataTickersResponseCompareTo() {
    DemoExchangeMarketDataTickersResponse pojo1 = buildDemoExchangeMarketDataTickersResponse();
    DemoExchangeMarketDataTickersResponse pojo2 = buildDemoExchangeMarketDataTickersResponse();
    Assert.assertEquals(0, pojo1.compareTo(pojo2));
    pojo2.setResponseCode(201);
    Assert.assertEquals(-1, pojo1.compareTo(pojo2));
    pojo2.setResponseCode(200);
    pojo2.getPayload().get("BTC_USDT").setLast(new BigDecimal("90416.61"));
    Assert.assertEquals(1, pojo1.compareTo(pojo2));
  }
  
  private DemoExchangeMarketDataTickersResponse buildDemoExchangeMarketDataTickersResponse() {    
    return DemoExchangeMarketDataTickersResponse.builder()
        .responseCode(200)
        .addToPayload("BTC_USDT",
            DemoExchangeMarketDataTickersResponsePayload.builder()
              .last(new BigDecimal("90417.60"))
              .high(new BigDecimal("90933.82"))
              .low(new BigDecimal("863334.53"))
              .volume(new BigDecimal("3631198541.88"))
              .time(1741207831531L).build())
        .addToPayload("ETH_USDT",
            DemoExchangeMarketDataTickersResponsePayload.builder()
              .last(new BigDecimal("2227.05"))
              .high(new BigDecimal("2273.51"))
              .low(new BigDecimal("2126.06"))
              .volume(new BigDecimal("1630680938.25"))
              .time(1741207940214L).build())
        .build();
  }
  
  private DemoExchangeMarketDataExchangeInfoResponse buildDemoExchangeMarketDataExchangeInfoResponse() {
    return DemoExchangeMarketDataExchangeInfoResponse.builder()
        .responseCode(200)
        .addToPayload(DemoExchangeMarketDataExchangeInfoResponsePayload.builder()
          .symbol("BTC_USDT")
          .minOrderSize(new BigDecimal("0.0001"))
          .orderTickSize(new BigDecimal("0.1"))
          .build())
        .addToPayload(DemoExchangeMarketDataExchangeInfoResponsePayload.builder()
          .symbol("ETH_USDT")
          .minOrderSize(new BigDecimal("0.001"))
          .orderTickSize(new BigDecimal("0.01"))
          .build())
        .build();
  }

}
