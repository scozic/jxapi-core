package com.scz.jxapi.generator.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketEndpointDemoGenerator}
 */
public class WebsocketEndpointDemoGeneratorTest {
	
	@Test
	public void testGenerateWebsocketEndpointDemo() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpointDescriptor = exchangeApiDescriptor.getWebsocketEndpoints().get(0);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestCEXExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickerStreamRequest;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#tickerStream(MyTestCEXMarketDataTickerStreamRequest)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataTickerStreamDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataTickerStreamDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = 60000L;\n"
				+ "  \n"
				+ "  public static MyTestCEXMarketDataTickerStreamRequest createRequest() {\n"
				+ "    MyTestCEXMarketDataTickerStreamRequest request = new MyTestCEXMarketDataTickerStreamRequest();\n"
				+ "    request.setSymbol(\"BTC_USDT\");\n"
				+ "    return request;\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      MyTestCEXMarketDataTickerStreamRequest request = createRequest();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestCEX MarketData tickerStream' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeTickerStream(request, m -> log.info(\"Received message:\" + m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestCEX MarketData tickerStream' stream\");\n"
				+ "      api.unsubscribeTickerStream(subId);\n"
				+ "      Thread.sleep(1000L);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new WebsocketEndpointDemoGenerator(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor).generate());
	}

}
