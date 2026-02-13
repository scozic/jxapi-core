package org.jxapi.exchanges.demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchange.MockExchangeHook;
import org.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import org.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.rest.javanet.MockHttpRequest;
import org.jxapi.netutils.rest.javanet.MockHttpServer;
import org.jxapi.util.JsonUtil;

/**
 * Contains JUnit 4 tests to test issuing calls to rest APIs of {@link DemoExchangeExchange}.
 */
public class DemoExchangeRestRequestTest {
  
  private static final String TEST_EXCHANGE_NAME = "myExchange";
  private static final long DEFAULT_WAIT_RESPONSE_TIMEOUT = 1000L;

  private MockHttpServer mockHttpServer;
  private int serverPort;
  private DemoExchangeExchange exchange;
  private String baseHttpUrl;
  
  private static Long getTime(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(date).getTime();
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date:[" + date + "]");
    }
  }
  
  @Before
  public void setUp() throws IOException {
    mockHttpServer = new MockHttpServer();
    mockHttpServer.start();
    this.serverPort = mockHttpServer.getPort();
    baseHttpUrl = "http://localhost:" + serverPort;
    Properties config = new Properties();
    config.setProperty(DemoExchangeProperties.BASE_HTTP_URL.getName(), String.valueOf(this.baseHttpUrl));
    exchange = new DemoExchangeExchangeImpl(TEST_EXCHANGE_NAME, config);
  }
  
  @After
  public void tearDown() {
        mockHttpServer.stop();
  }
  
  @Test
  public void testAfterInitHookHasBeenCalled() {
    MockExchangeHook exchangeHook = (MockExchangeHook) exchange.getProperties().get(MockExchangeHook.MOCK_EXCHANGE_HOOK_PROPERTY);
    Assert.assertNotNull(exchangeHook);
    Assert.assertEquals(1, exchangeHook.size());
    Assert.assertEquals(exchange, exchangeHook.pop());
  }
  
  @Test
  public void testCallTickers() throws Exception {
    FutureRestResponse<DemoExchangeMarketDataTickersResponse> futureResponse = exchange.getMarketDataApi().tickers();
    DemoExchangeMarketDataTickersResponse expectedResponse = new DemoExchangeMarketDataTickersResponse();
    Map<String, DemoExchangeMarketDataTickersResponsePayload> expectedResponsePayloed = new TreeMap<>();
    expectedResponse.setPayload(expectedResponsePayloed);
    DemoExchangeMarketDataTickersResponsePayload tickers1 = new DemoExchangeMarketDataTickersResponsePayload();
    tickers1.setHigh(new BigDecimal("103486.00"));
    tickers1.setLast(new BigDecimal("103271.20"));
    tickers1.setLow(new BigDecimal("101049.10"));
    tickers1.setVolume(new BigDecimal("153280000.00"));
    tickers1.setTime(getTime("2024-12-16T00:05:14.222"));
    expectedResponsePayloed.put("BTC_USDT", tickers1);
    DemoExchangeMarketDataTickersResponsePayload tickers2 = new DemoExchangeMarketDataTickersResponsePayload();
    tickers2.setHigh(new BigDecimal("3919.71"));
    tickers2.setLast(new BigDecimal("3915.17"));
    tickers2.setLow(new BigDecimal("3832.01"));
    tickers2.setVolume(new BigDecimal("120920000.00"));
    tickers2.setTime(getTime("2024-12-16T00:06:14.333"));
    expectedResponsePayloed.put("ETH_USDT", tickers1);
    
    
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/tickers", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.GET, actualRequest.getHttpMethod());
    Assert.assertNull(actualRequest.getBody());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    
    RestResponse<DemoExchangeMarketDataTickersResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
  
  @Test
  public void testCallExchangeInfoApi() throws Exception {
    DemoExchangeMarketDataExchangeInfoRequest request = new DemoExchangeMarketDataExchangeInfoRequest();
    request.setSymbols(List.of("BTC_USDT", "ETH_USDT"));
    FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> futureResponse = exchange.getMarketDataApi().exchangeInfo(request);
    
    DemoExchangeMarketDataExchangeInfoResponse expectedResponse = new DemoExchangeMarketDataExchangeInfoResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    
    DemoExchangeMarketDataExchangeInfoResponsePayload market1 = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    market1.setSymbol("BTC_USDT");
    market1.setOrderTickSize(new BigDecimal("0.1"));
    market1.setMinOrderSize(new BigDecimal("0.0001"));
    
    DemoExchangeMarketDataExchangeInfoResponsePayload market2 = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    market2.setSymbol("ETH_USDT");
    market2.setOrderTickSize(new BigDecimal("0.01"));
    market2.setMinOrderSize(new BigDecimal("0.005"));
    
    expectedResponse.setPayload(List.of(market1, market2));
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/exchangeInfo?symbols=%5B%22BTC_USDT%22%2C%22ETH_USDT%22%5D", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.GET, actualRequest.getHttpMethod());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    RestResponse<DemoExchangeMarketDataExchangeInfoResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
  
  @Test
  public void testCallPostRequestDataTypeInt() throws Exception {
    Integer request = 123;
    FutureRestResponse<GenericResponse> futureResponse = exchange.getMarketDataApi().postRestRequestDataTypeInt(request);
    
    GenericResponse expectedResponse = new GenericResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/postInt", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.POST, actualRequest.getHttpMethod());
    Assert.assertEquals("123", actualRequest.getBody());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    RestResponse<GenericResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
  
  @Test
  public void testGetRestRequestDataTypePrimitiveWithMsgField() throws Exception {
    Integer request = 123;
    FutureRestResponse<GenericResponse> futureResponse = exchange.getMarketDataApi().getRestRequestDataTypePrimitiveWithMsgField(request);
    
    GenericResponse expectedResponse = new GenericResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/getIntWithMsgField?a=123", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.GET, actualRequest.getHttpMethod());
    Assert.assertNull(actualRequest.getBody());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    RestResponse<GenericResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
  
  @Test
  public void testGetRestRequestDataTypePrimitiveWithMsgFieldNotFound() throws Exception {
    Integer request = 123;
    FutureRestResponse<GenericResponse> futureResponse = exchange.getMarketDataApi().getRestRequestDataTypePrimitiveWithMsgField(request);
    
    GenericResponse expectedResponse = new GenericResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/getIntWithMsgField?a=123", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.GET, actualRequest.getHttpMethod());
    Assert.assertNull(actualRequest.getBody());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(404);
    mockRequest.complete(httpResponse);
    RestResponse<GenericResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertFalse(actualResponse.isOk());
    Assert.assertEquals(404, actualResponse.getHttpStatus());
    Assert.assertEquals(null, actualResponse.getResponse());
  }
  
  @Test
  public void testGetRestRequestDataTypePrimitiveWithMsgFieldServerDown() throws Exception {
    mockHttpServer.stop();
    Integer request = 123;
    FutureRestResponse<GenericResponse> futureResponse = exchange.getMarketDataApi().getRestRequestDataTypePrimitiveWithMsgField(request);
    
    RestResponse<GenericResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertFalse(actualResponse.isOk());
    Assert.assertNotNull(actualResponse.getException());
  }
  
  @Test
  public void testPostRestRequestDataTypeIntList() throws Exception {
    List<Integer> request = List.of(123, 456, 789);
    FutureRestResponse<GenericResponse> futureResponse = exchange.getMarketDataApi().postRestRequestDataTypeIntList(request);
    
    GenericResponse expectedResponse = new GenericResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/postIntList", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.POST, actualRequest.getHttpMethod());
    Assert.assertEquals("[123,456,789]", 
        actualRequest.getBody());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    RestResponse<GenericResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
  
  @Test
  public void testCallPostRestRequestDataTypeObjectListMap() throws Exception {
    Map<String, List<SingleSymbol>> request = new TreeMap<>();
    SingleSymbol singleSymbol1 = new SingleSymbol();
    singleSymbol1.setSymbol("BTC_USDT");
    SingleSymbol singleSymbol2 = new SingleSymbol();
    singleSymbol2.setSymbol("ETH_USDT");
    request.put("spot", List.of(singleSymbol1, singleSymbol2));
    SingleSymbol singleSymbol3 = new SingleSymbol();
    singleSymbol3.setSymbol("SOL_USDT");
    request.put("futures", List.of(singleSymbol3));
    FutureRestResponse<GenericResponse> futureResponse = exchange.getMarketDataApi().postRestRequestDataTypeObjectListMap(request);
    
    GenericResponse expectedResponse = new GenericResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(DEFAULT_WAIT_RESPONSE_TIMEOUT);
    
    HttpRequest actualRequest = mockRequest.getHttpRequest();
    Assert.assertEquals(baseHttpUrl + "/marketData/postObjectListMap", actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.POST, actualRequest.getHttpMethod());
    Assert.assertEquals("{\"futures\":[{\"s\":\"SOL_USDT\"}],\"spot\":[{\"s\":\"BTC_USDT\"},{\"s\":\"ETH_USDT\"}]}", 
              actualRequest.getBody());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    RestResponse<GenericResponse> actualResponse = futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
}
