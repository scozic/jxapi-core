package com.scz.jxapi.generator.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ExchangeApiInterfaceImplementationGenerator}
 */
public class ExchangeApiInterfaceImplementationGeneratorTest {

	@Test
	public void testGenerateExchangeApi() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		ExchangeApiInterfaceImplementationGenerator apiInterfaceGenerator = new ExchangeApiInterfaceImplementationGenerator(exchangeDescriptor, exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata;\n"
				+ "\n"
				+ "import com.foo.bar.BarRestEndpointFactory;\n"
				+ "import com.foo.bar.BarWebsocketEndpointFactory;\n"
				+ "import com.foo.bar.gen.marketdata.deserializers.MyTestCEXExchangeInfoResponseDeserializer;\n"
				+ "import com.foo.bar.gen.marketdata.deserializers.MyTestCEXTickerStreamMessageDeserializer;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXExchangeInfoRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXExchangeInfoResponse;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXTickerStreamMessage;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXTickerStreamRequest;\n"
				+ "import com.scz.jxapi.netutils.rest.FutureRestResponse;\n"
				+ "import com.scz.jxapi.netutils.rest.RestEndpoint;\n"
				+ "import com.scz.jxapi.netutils.rest.RestRequest;\n"
				+ "import com.scz.jxapi.netutils.websocket.DefaultWebsocketMessageTopicMatcher;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketListener;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketMessageTopicMatcherField;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;\n"
				+ "import java.util.Properties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Actual implementation of {@link MyTestCEXMarketDataApi}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class  MyTestCEXMarketDataApiImpl implements MyTestCEXMarketDataApi {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataApiImpl.class);\n"
				+ "  \n"
				+ "  \n"
				+ "  private final BarRestEndpointFactory restEndpointFactory = new BarRestEndpointFactory();\n"
				+ "  \n"
				+ "  private final BarWebsocketEndpointFactory websocketEndpointFactory = new BarWebsocketEndpointFactory();\n"
				+ "  \n"
				+ "  private final RestEndpoint<MyTestCEXExchangeInfoRequest, MyTestCEXExchangeInfoResponse> exchangeInfoApi;\n"
				+ "  \n"
				+ "  private final WebsocketEndpoint<MyTestCEXTickerStreamRequest, MyTestCEXTickerStreamMessage> tickerStreamWs;\n"
				+ "  \n"
				+ "  public MyTestCEXMarketDataApiImpl(Properties properties) {\n"
				+ "    this.restEndpointFactory.setProperties(properties);\n"
				+ "    this.websocketEndpointFactory.setProperties(properties);\n"
				+ "    this.exchangeInfoApi = restEndpointFactory.createRestEndpoint(new MyTestCEXExchangeInfoResponseDeserializer());\n"
				+ "    this.tickerStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new MyTestCEXTickerStreamMessageDeserializer());\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public FutureRestResponse<MyTestCEXExchangeInfoResponse> exchangeInfo(MyTestCEXExchangeInfoRequest request) {\n"
				+ "    if (log.isDebugEnabled())\n"
				+ "      log.debug(\"GET exchangeInfo > \" + request);\n"
				+ "     return exchangeInfoApi.call(RestRequest.create(\"https://com.sample.mycex/exchangeInfo\", \"GET\", request));\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String subscribeTickerStream(MyTestCEXTickerStreamRequest request, WebsocketListener<MyTestCEXTickerStreamMessage> listener) {\n"
				+ "    if (log.isDebugEnabled())\n"
				+ "      log.debug(\"subscribeTickerStream:request:\" + request);\n"
				+ "    WebsocketSubscribeRequest<MyTestCEXTickerStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();\n"
				+ "    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList(\"topic\", \"ticker\", \"symbol\", \"\" + request.getSymbol() + \"\")));\n"
				+ "    websocketSubscribeRequest.setParameters(request);\n"
				+ "    return tickerStreamWs.subscribe(websocketSubscribeRequest, listener);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean unsubscribeTickerStream(String subscriptionId) {\n"
				+ "    if (log.isDebugEnabled())\n"
				+ "      log.debug(\"unsubscribeTickerStream: subscriptionId:\" + subscriptionId);\n"
				+ "    return tickerStreamWs.unsubscribe(subscriptionId);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n",	
				apiInterfaceGenerator.generate());
	}
}
