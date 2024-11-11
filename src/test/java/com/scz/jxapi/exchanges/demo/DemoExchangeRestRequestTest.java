package com.scz.jxapi.exchanges.demo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpResponse;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.netutils.rest.javanet.MockHttpRequest;
import com.scz.jxapi.netutils.rest.javanet.MockHttpServer;
import com.scz.jxapi.util.JsonUtil;

/**
 * Contains JUnit 4 tests to test issuing calls to rest APIs of {@link DemoExchangeExchange}.
 */
public class DemoExchangeRestRequestTest {
	
	private static final String TEST_EXCHANGE_NAME = "myExchange";
	private static final long DEFAULT_WAIT_RESPONSE_TIMEOUT = 1000L;

	private MockHttpServer mockHttpServer;
	private int serverPort;
	private DemoExchangeExchange exchange;
	
	@Before
	public void setUp() throws IOException {
		mockHttpServer = new MockHttpServer();
		mockHttpServer.start();
		this.serverPort = mockHttpServer.getPort();
		Properties config = new Properties();
		config.setProperty(DemoExchangeProperties.HTTP_PORT_PROPERTY, String.valueOf(this.serverPort));
		config.setProperty(DemoExchangeProperties.HOST_PROPERTY, DemoExchangeProperties.HOST_DEFAULT_VALUE);
		exchange = new DemoExchangeExchangeImpl(TEST_EXCHANGE_NAME, config);
	}
	
	@Test
	public void testCallExchangeInfoApi() throws Exception {
		DemoExchangeMarketDataExchangeInfoRequest request = new DemoExchangeMarketDataExchangeInfoRequest();
		request.setSymbols(List.of("BTC_USDT", "ETH_USDT"));
		FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> futureResponse = exchange.getDemoExchangeMarketDataApi().exchangeInfo(request);
		
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
		Assert.assertEquals("http://localhost:8081/marketData/exchangeInfo?symbols=%5B%22BTC_USDT%22%2C%22ETH_USDT%22%5D", actualRequest.getUrl());
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
}
