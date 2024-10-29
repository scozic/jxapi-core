package com.scz.jxapi.exchange.descriptor.parser;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketMessageTopicMatcherFieldDescriptor;
import com.scz.jxapi.netutils.rest.HttpMethod;

/**
 * Unit test for {@link ExchangeDescriptorParser}
 */
public class ExchangeDescriptorParserTest {
	
	@Test
	public void testParseExchangeDescriptor() throws Exception {
		ExchangeDescriptor ex = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		Assert.assertEquals("MyTestExchange", ex.getName());
		Assert.assertEquals("A sample Exchange descriptor file", ex.getDescription());
		Assert.assertEquals("com.foo.bar.gen", ex.getBasePackage());
		List<ExchangeApiDescriptor> apis = ex.getApis(); 
		Assert.assertEquals(1, apis.size());
		checkMarketDataApi(apis.get(0));
	}
	
	private void checkMarketDataApi(ExchangeApiDescriptor marketDataApi) {
		Assert.assertEquals("MarketData", marketDataApi.getName());
		Assert.assertEquals("The market data API of MyTestExchange", marketDataApi.getDescription());
		Assert.assertEquals("com.foo.bar.BarHttpRequestInterceptorFactory", marketDataApi.getHttpRequestInterceptorFactory());
		List<RestEndpointDescriptor> restEndpoints = marketDataApi.getRestEndpoints();
		Assert.assertEquals(2, restEndpoints.size());
		checkExchangeInfoRestEndpoint(restEndpoints.get(0));
		checkTickersRestEndpooint(restEndpoints.get(1));
		Assert.assertEquals("wss://com.foo.exchange/ws", marketDataApi.getWebsocketUrl());
		Assert.assertEquals("com.foo.bar.BarWebsocketFactory", marketDataApi.getWebsocketFactory());
		Assert.assertEquals("com.foo.bar.BarWebsocketHookFactory", marketDataApi.getWebsocketHookFactory());
		List<WebsocketEndpointDescriptor> websocketEndpoints = marketDataApi.getWebsocketEndpoints();
		Assert.assertEquals(1, websocketEndpoints.size());
		checkTickerStreamWebsocketEndpoint(websocketEndpoints.get(0));
	}

	private void checkTickersRestEndpooint(RestEndpointDescriptor tickersEndPoint) {
		Assert.assertEquals("tickers", tickersEndPoint.getName());
		Assert.assertEquals("Fetch current tickers", tickersEndPoint.getDescription());
		Assert.assertEquals(HttpMethod.GET, tickersEndPoint.getHttpMethod());
		Assert.assertEquals("https://com.sample.mycex/tickers", tickersEndPoint.getUrl());
		List<Field> exchangeInfoParameters = tickersEndPoint.getRequest().getProperties();
		Assert.assertEquals(0, exchangeInfoParameters.size());
		
		checkTickersResponse(tickersEndPoint.getResponse().getProperties());
		
	}

	private void checkTickersResponse(List<Field> tickersResponse) {
		Assert.assertEquals(2, tickersResponse.size());
		Field responseCode = tickersResponse.get(0);
		Assert.assertEquals("responseCode", responseCode.getName());
		Assert.assertEquals("Request response code", responseCode.getDescription());
		Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
		Assert.assertEquals("0", responseCode.getSampleValue());
		
		Field payload = tickersResponse.get(1);
		Assert.assertEquals("payload", payload.getName());
		Assert.assertEquals("Tickers for each symbol", payload.getDescription());
		Assert.assertEquals(CanonicalType.MAP, payload.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
		List<Field> payloadParameters = payload.getProperties();
		Assert.assertEquals(1, payloadParameters.size());
		
		Field last = payloadParameters.get(0);
		Assert.assertEquals("last", last.getName());
		Assert.assertEquals("Last traded price", last.getDescription());
		Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
		Assert.assertEquals(Double.valueOf("10.0"), last.getSampleValue());
		
	}

	private void checkExchangeInfoRestEndpoint(RestEndpointDescriptor exchangeInfoEndPoint) {
		Assert.assertEquals("exchangeInfo", exchangeInfoEndPoint.getName());
		Assert.assertEquals("Fetch market information of symbols that can be traded", exchangeInfoEndPoint.getDescription());
		Assert.assertEquals(HttpMethod.GET, exchangeInfoEndPoint.getHttpMethod());
		Assert.assertEquals("https://com.sample.mycex/exchangeInfo", exchangeInfoEndPoint.getUrl());
		List<Field> exchangeInfoParameters = exchangeInfoEndPoint.getRequest().getProperties();
		
		Assert.assertEquals(1, exchangeInfoParameters.size());
		Field symbolsParameter = exchangeInfoParameters.get(0);
		Assert.assertEquals("symbols", symbolsParameter.getName());
		Assert.assertEquals("The list of symbol to fetch market information for. Leave empty to fetch all markets", symbolsParameter.getDescription());
		Assert.assertEquals(CanonicalType.LIST, symbolsParameter.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.STRING, symbolsParameter.getType().getSubType().getCanonicalType());
		Assert.assertEquals("[\"BTC\", \"ETH\"]", symbolsParameter.getSampleValue());
		
		checkExchangeInfoResponse(exchangeInfoEndPoint.getResponse().getProperties());
	}
	
	private void checkExchangeInfoResponse(List<Field> exchangeInfoResponse) {
		Assert.assertEquals(2, exchangeInfoResponse.size());
		Field responseCode = exchangeInfoResponse.get(0);
		Assert.assertEquals("responseCode", responseCode.getName());
		Assert.assertEquals("Request response code", responseCode.getDescription());
		Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
		Assert.assertEquals("0", responseCode.getSampleValue());
		
		Field payload = exchangeInfoResponse.get(1);
		Assert.assertEquals("payload", payload.getName());
		Assert.assertEquals("List of market information for each requested symbol", payload.getDescription());
		Assert.assertEquals(CanonicalType.LIST, payload.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
		List<Field> payloadParameters = payload.getProperties();
		Assert.assertEquals(3, payloadParameters.size());
		
		Field symbol = payloadParameters.get(0);
		Assert.assertEquals("symbol", symbol.getName());
		Assert.assertEquals("Market symbol", symbol.getDescription());
		Assert.assertEquals(CanonicalType.STRING, symbol.getType().getCanonicalType());
		Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
		
		Field minOrderSize = payloadParameters.get(1);
		Assert.assertEquals("minOrderSize", minOrderSize.getName());
		Assert.assertEquals("Minimum order amount", minOrderSize.getDescription());
		Assert.assertEquals(CanonicalType.BIGDECIMAL, minOrderSize.getType().getCanonicalType());
		Assert.assertEquals("0.0001", minOrderSize.getSampleValue());
		
		Field levels = payloadParameters.get(2);
		Assert.assertEquals("levels", levels.getName());
		Assert.assertEquals("Amount precision levels", levels.getDescription());
		Assert.assertEquals(CanonicalType.LIST, levels.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.INT, levels.getType().getSubType().getCanonicalType());
		Assert.assertEquals(List.of(1, 10, 500).toString(), levels.getSampleValue().toString());
	}
	

	private void checkTickerStreamWebsocketEndpoint(WebsocketEndpointDescriptor tickerStreamEndpoint) {
		Assert.assertEquals("tickerStream", tickerStreamEndpoint.getName());
		Assert.assertEquals("Subscribe to ticker stream", tickerStreamEndpoint.getDescription());
		Assert.assertEquals("${symbol}@ticker", tickerStreamEndpoint.getTopic());
		Assert.assertEquals("|", tickerStreamEndpoint.getTopicParametersListSeparator());
		
		List<Field> parameters = tickerStreamEndpoint.getRequest().getProperties();
		Field symbols = parameters.get(0);
		Assert.assertEquals("symbol", symbols.getName());
		Assert.assertEquals("Symbol to subscribe to ticker stream of", symbols.getDescription());
		Assert.assertEquals(CanonicalType.STRING, symbols.getType().getCanonicalType());
		Assert.assertEquals("BTC_USDT", symbols.getSampleValue());
		
		List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields = tickerStreamEndpoint.getMessageTopicMatcherFields();
		Assert.assertEquals(2, messageTopicMatcherFields.size());
		Assert.assertEquals("topic", messageTopicMatcherFields.get(0).getName());
		Assert.assertEquals("ticker", messageTopicMatcherFields.get(0).getValue());
		Assert.assertEquals("symbol", messageTopicMatcherFields.get(1).getName());
		Assert.assertEquals("${symbol}", messageTopicMatcherFields.get(1).getValue());
		
		List<Field> response = tickerStreamEndpoint.getMessage().getProperties();
		Assert.assertEquals(3, response.size());
		
		Field topic = response.get(0);
		Assert.assertEquals("topic", topic.getName());
		Assert.assertEquals("Topic", topic.getDescription());
		Assert.assertEquals(CanonicalType.STRING, topic.getType().getCanonicalType());
		Assert.assertEquals("ticker", topic.getSampleValue());
		
		Field symbol = response.get(1);
		Assert.assertEquals("symbol", symbol.getName());
		Assert.assertEquals("Symbol name", symbol.getDescription());
		Assert.assertEquals(CanonicalType.STRING, symbol.getType().getCanonicalType());
		Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
		
		Field last = response.get(2);
		Assert.assertEquals("last", last.getName());
		Assert.assertEquals("Last traded price", last.getDescription());
		Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
		Assert.assertEquals("16000.00", last.getSampleValue());
	}
}
