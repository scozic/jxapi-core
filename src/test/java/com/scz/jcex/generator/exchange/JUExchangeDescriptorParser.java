package com.scz.jcex.generator.exchange;

import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ExchangeDescriptorParser}
 */
public class JUExchangeDescriptorParser {
	
	@Test
	public void testParseExchangeDescriptor() throws Exception {
		ExchangeDescriptor ex = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		Assert.assertEquals("MyTestCEX", ex.getName());
		Assert.assertEquals("A sample CEX descriptor file", ex.getDescription());
		Assert.assertEquals("com.foo.bar.gen", ex.getBasePackage());
		List<ExchangeApiDescriptor> apis = ex.getApis(); 
		Assert.assertEquals(1, apis.size());
		checkMarketDataApi(apis.get(0));
	}
	
	private void checkMarketDataApi(ExchangeApiDescriptor marketDataApi) {
		Assert.assertEquals("MarketData", marketDataApi.getName());
		Assert.assertEquals("The market data API of MyTestCEX", marketDataApi.getDescription());
		Assert.assertEquals("com.foo.bar.BarRestEndpointFactory", marketDataApi.getRestEndpointFactory());
		List<RestEndpointDescriptor> restEndpoints = marketDataApi.getRestEndpoints();
		Assert.assertEquals(1, restEndpoints.size());
		checkExchangeInfoRestEndpoint(restEndpoints.get(0));
		Assert.assertEquals("com.foo.bar.BarWebsocketEndpointFactory", marketDataApi.getWebsocketEndpointFactory());
		List<WebsocketEndpointDescriptor> websocketEndpoints = marketDataApi.getWebsocketEndpoints();
		Assert.assertEquals(1, websocketEndpoints.size());
		checkTickerStreamWebsocketEndpoint(websocketEndpoints.get(0));
	}

	private void checkExchangeInfoRestEndpoint(RestEndpointDescriptor exchangeInfoEndPoint) {
		Assert.assertEquals("exchangeInfo", exchangeInfoEndPoint.getName());
		Assert.assertEquals("Fetch market information of symbols that can be traded", exchangeInfoEndPoint.getDescription());
		Assert.assertEquals("GET", exchangeInfoEndPoint.getHttpMethod());
		Assert.assertEquals("https://com.sample.mycex/exchangeInfo", exchangeInfoEndPoint.getUrl());
		List<EndpointParameter> exchangeInfoParameters = exchangeInfoEndPoint.getParameters();
		
		Assert.assertEquals(1, exchangeInfoParameters.size());
		EndpointParameter symbolsParameter = exchangeInfoParameters.get(0);
		Assert.assertEquals("symbols", symbolsParameter.getName());
		Assert.assertEquals("The list of symbol to fetch market information for. Leave empty to fetch all markets", symbolsParameter.getDescription());
		Assert.assertEquals(EndpointParameterType.STRING_LIST, symbolsParameter.getType());
		Assert.assertEquals("", symbolsParameter.getSampleValue());
		
		checkExchangeInfoResponse(exchangeInfoEndPoint.getResponse());
	}
	
	private void checkExchangeInfoResponse(List<EndpointParameter> exchangeInfoResponse) {
		Assert.assertEquals(2, exchangeInfoResponse.size());
		EndpointParameter responseCode = exchangeInfoResponse.get(0);
		Assert.assertEquals("responseCode", responseCode.getName());
		Assert.assertEquals("Request response code", responseCode.getDescription());
		Assert.assertEquals(EndpointParameterType.INT, responseCode.getType());
		Assert.assertEquals("0", responseCode.getSampleValue());
		
		EndpointParameter payload = exchangeInfoResponse.get(1);
		Assert.assertEquals("payload", payload.getName());
		Assert.assertEquals("List of market information for each requested symbol", payload.getDescription());
		Assert.assertEquals(EndpointParameterType.STRUCT_LIST, payload.getType());
		List<EndpointParameter> payloadParameters = payload.getParameters();
		Assert.assertEquals(2, payloadParameters.size());
		
		EndpointParameter symbol = payloadParameters.get(0);
		Assert.assertEquals("symbol", symbol.getName());
		Assert.assertEquals("Market symbol", symbol.getDescription());
		Assert.assertEquals(EndpointParameterType.STRING, symbol.getType());
		Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
		
		EndpointParameter minOrderSize = payloadParameters.get(1);
		Assert.assertEquals("minOrderSize", minOrderSize.getName());
		Assert.assertEquals("Minimum order amount", minOrderSize.getDescription());
		Assert.assertEquals(EndpointParameterType.BIGDECIMAL, minOrderSize.getType());
		Assert.assertEquals("0.0001", minOrderSize.getSampleValue());
	}
	

	private void checkTickerStreamWebsocketEndpoint(WebsocketEndpointDescriptor tickerStreamEndpoint) {
		Assert.assertEquals("tickerStream", tickerStreamEndpoint.getName());
		Assert.assertEquals("Subscribe to ticker stream", tickerStreamEndpoint.getDescription());
		Assert.assertEquals("${symbol}@ticker", tickerStreamEndpoint.getTopic());
		Assert.assertEquals("|", tickerStreamEndpoint.getTopicParametersListSeparator());
		
		List<EndpointParameter> parameters = tickerStreamEndpoint.getParameters();
		EndpointParameter symbols = parameters.get(0);
		Assert.assertEquals("symbol", symbols.getName());
		Assert.assertEquals("Symbol to subscribe to ticker stream of", symbols.getDescription());
		Assert.assertEquals(EndpointParameterType.STRING, symbols.getType());
		Assert.assertEquals("BTC_USDT", symbols.getSampleValue());
		
		List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields = tickerStreamEndpoint.getMessageTopicMatcherFields();
		Assert.assertEquals(2, messageTopicMatcherFields.size());
		Assert.assertEquals("topic", messageTopicMatcherFields.get(0).getName());
		Assert.assertEquals("ticker", messageTopicMatcherFields.get(0).getValue());
		Assert.assertEquals("symbol", messageTopicMatcherFields.get(1).getName());
		Assert.assertEquals("${symbol}", messageTopicMatcherFields.get(1).getValue());
		
		List<EndpointParameter> response = tickerStreamEndpoint.getResponse();
		Assert.assertEquals(3, response.size());
		
		EndpointParameter topic = response.get(0);
		Assert.assertEquals("topic", topic.getName());
		Assert.assertEquals("Topic", topic.getDescription());
		Assert.assertEquals(EndpointParameterType.STRING, topic.getType());
		Assert.assertEquals("ticker", topic.getSampleValue());
		
		EndpointParameter symbol = response.get(1);
		Assert.assertEquals("symbol", symbol.getName());
		Assert.assertEquals("Symbol name", symbol.getDescription());
		Assert.assertEquals(EndpointParameterType.STRING, symbol.getType());
		Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
		
		EndpointParameter last = response.get(2);
		Assert.assertEquals("last", last.getName());
		Assert.assertEquals("Last traded price", last.getDescription());
		Assert.assertEquals(EndpointParameterType.BIGDECIMAL, last.getType());
		Assert.assertEquals("16000.00", last.getSampleValue());
	}
}
