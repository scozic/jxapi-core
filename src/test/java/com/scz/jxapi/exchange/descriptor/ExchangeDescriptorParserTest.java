package com.scz.jxapi.exchange.descriptor;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.HttpMethod;

/**
 * Unit test for ExchangeDescriptorParser
 */
public class ExchangeDescriptorParserTest {

    @Test
    public void testCreateExchangeDescriptorParser() {
        ExchangeDescriptorParser parser = new ExchangeDescriptorParser();
        Assert.assertNotNull(parser);
    }

    @Test
    public void testParse() throws Exception {
        ExchangeDescriptorParser parser = new ExchangeDescriptorParser();
        ExchangeDescriptor exchangeDescriptor = parser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
        Assert.assertNotNull(exchangeDescriptor);
        Assert.assertEquals("MyTestExchange", exchangeDescriptor.getName());
        Assert.assertEquals("A sample Exchange descriptor file", exchangeDescriptor.getDescription());
        Assert.assertEquals("com.foo.bar.gen", exchangeDescriptor.getBasePackage());
        Assert.assertEquals(1, exchangeDescriptor.getApis().size());

        ExchangeApiDescriptor marketDataApi = exchangeDescriptor.getApis().get(0);
        Assert.assertEquals("MarketData", marketDataApi.getName());
        Assert.assertEquals("The market data API of MyTestExchange", marketDataApi.getDescription());
        Assert.assertEquals("com.foo.bar.BarHttpRequestInterceptorFactory", marketDataApi.getHttpRequestInterceptorFactory());
        Assert.assertEquals(2, marketDataApi.getRestEndpoints().size());

        RestEndpointDescriptor exchangeInfoEndpoint = marketDataApi.getRestEndpoints().get(0);
        Assert.assertEquals("exchangeInfo", exchangeInfoEndpoint.getName());
        Assert.assertEquals(HttpMethod.GET, exchangeInfoEndpoint.getHttpMethod());
        Assert.assertEquals("Fetch market information of symbols that can be traded", exchangeInfoEndpoint.getDescription());
        Assert.assertEquals("https://com.sample.mycex/exchangeInfo", exchangeInfoEndpoint.getUrl());
        Assert.assertEquals(1, exchangeInfoEndpoint.getRequest().getParameters().size());
        Assert.assertEquals(2, exchangeInfoEndpoint.getResponse().getParameters().size());

        RestEndpointDescriptor tickersEndpoint = marketDataApi.getRestEndpoints().get(1);
        Assert.assertEquals("tickers", tickersEndpoint.getName());
        Assert.assertEquals(HttpMethod.GET, tickersEndpoint.getHttpMethod());
        Assert.assertEquals("Fetch current tickers", tickersEndpoint.getDescription());
        Assert.assertEquals("https://com.sample.mycex/tickers", tickersEndpoint.getUrl());
        Assert.assertEquals(0, tickersEndpoint.getRequest().getParameters().size());
        Assert.assertEquals(2, tickersEndpoint.getResponse().getParameters().size());

        Assert.assertEquals("wss://com.foo.exchange/ws", marketDataApi.getWebsocketUrl());
        Assert.assertEquals("com.foo.bar.BarWebsocketFactory", marketDataApi.getWebsocketFactory());
        Assert.assertEquals("com.foo.bar.BarWebsocketHookFactory", marketDataApi.getWebsocketHookFactory());
        Assert.assertEquals(1, marketDataApi.getWebsocketEndpoints().size());

        WebsocketEndpointDescriptor tickerStreamEndpoint = marketDataApi.getWebsocketEndpoints().get(0);
        Assert.assertEquals("tickerStream", tickerStreamEndpoint.getName());
        Assert.assertEquals("${symbol}@ticker", tickerStreamEndpoint.getTopic());
        Assert.assertEquals("Subscribe to ticker stream", tickerStreamEndpoint.getDescription());
        Assert.assertEquals(1, tickerStreamEndpoint.getRequest().getParameters().size());
        Assert.assertEquals("|", tickerStreamEndpoint.getTopicParametersListSeparator());
        Assert.assertEquals(2, tickerStreamEndpoint.getMessageTopicMatcherFields().size());
        Assert.assertEquals(3, tickerStreamEndpoint.getMessage().getParameters().size());
    }
}
