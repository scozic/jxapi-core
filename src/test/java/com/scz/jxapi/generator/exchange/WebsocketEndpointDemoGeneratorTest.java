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
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#subscribeTickerStream(com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickerStreamRequest)}\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataTickerStreamDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataTickerStreamDemo.class);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Sample value for <i>symbol</i> parameter of <i>tickerStream</i> API\n"
				+ "   */\n"
				+ "  public static final String SYMBOL = \"BTC_USDT\";\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      MyTestCEXMarketDataTickerStreamRequest request = new MyTestCEXMarketDataTickerStreamRequest();\n"
				+ "      request.setSymbol(SYMBOL);\n"
				+ "      log.info(\"Subscribing to stream com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi.subscribeTickerStream() websocket stream with request:\" + request);\n"
				+ "      api.subscribeTickerStream(request, m -> log.info(\"Received message:\" + m));\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new WebsocketEndpointDemoGenerator(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor).generate());
	}

}
