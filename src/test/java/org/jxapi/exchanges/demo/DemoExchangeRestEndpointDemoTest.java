package org.jxapi.exchanges.demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import org.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import org.jxapi.exchanges.demo.gen.marketdata.demo.DemoExchangeMarketDataExchangeInfoDemo;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.rest.javanet.MockHttpRequest;
import org.jxapi.netutils.rest.javanet.MockHttpServer;
import org.jxapi.util.DemoUtil;
import org.jxapi.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration test for demo exchange REST endpoint demo snippet.
 */
public class DemoExchangeRestEndpointDemoTest {
  
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeRestEndpointDemoTest.class);
  
  private static final long DEFAULT_WAIT_RESPONSE_TIMEOUT = 1000L;

  private MockHttpServer mockHttpServer;
  private int serverPort;
  private String baseHttpUrl;
  private Properties config;
  
  @Before
  public void setUp() throws IOException {
    mockHttpServer = new MockHttpServer();
    mockHttpServer.start();
    this.serverPort = mockHttpServer.getPort();
    baseHttpUrl = "http://localhost:" + serverPort;
    config = new Properties();
    config.setProperty(DemoExchangeProperties.BASE_HTTP_URL.getName(), baseHttpUrl);
  }
  
  @After
  public void tearDown() {
    mockHttpServer.stop();
  }

  @Test
  public void testDemoExchangeMarketDataExchangeInfoDemo() throws Exception {
    FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> futureResponse = exEcuteExchangeInfoAsync();
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
    Assert.assertEquals(baseHttpUrl + "/marketData/exchangeInfo?symbols=%5B%22BTC_USDT%22%2C%22BNB_USDT%22%5D", 
                        actualRequest.getUrl());
    Assert.assertEquals(HttpMethod.GET, actualRequest.getHttpMethod());
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(200);
    httpResponse.setBody(JsonUtil.pojoToJsonString(expectedResponse));
    mockRequest.complete(httpResponse);
    RestResponse<DemoExchangeMarketDataExchangeInfoResponse> actualResponse = 
        futureResponse.get(DEFAULT_WAIT_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(expectedResponse, actualResponse.getResponse());
  }
  
  private FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exEcuteExchangeInfoAsync() {
    FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> response = new FutureRestResponse<>();
    new Thread(() -> {
      try {
        response.complete(DemoExchangeMarketDataExchangeInfoDemo.execute(
            DemoExchangeMarketDataExchangeInfoDemo.createRequest(config), 
            config, 
            DemoUtil::logRestApiEvent));
      } catch (Exception e) {
        log.error("Request failed", e);
        RestResponse<DemoExchangeMarketDataExchangeInfoResponse> r = new RestResponse<>();
        r.setException(e);
        response.complete(r);
      }
    }).start();
    return response;
  }

}
