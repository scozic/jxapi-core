package com.scz.jxapi.generator.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for {@link ExchangeApiInterfaceGenerator}
 */
public class ExchangeApiInterfaceGeneratorTest {

	@Test
	public void testGenerateExchangeApi() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		ExchangeApiInterfaceGenerator apiInterfaceGenerator = new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata;\n"
				+ "\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataExchangeInfoRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataExchangeInfoResponse;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickerStreamMessage;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickerStreamRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickersRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickersResponse;\n"
				+ "import com.scz.jxapi.netutils.rest.FutureRestResponse;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketListener;\n"
				+ "import com.scz.jxapi.util.HasProperties;\n"
				+ "\n"
				+ "/**\n"
				+ " * MarketData CEX MarketData API</br>\n"
				+ " * The market data API of MyTestCEX\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public interface MyTestCEXMarketDataApi extends HasProperties {\n"
				+ "  /**\n"
				+ "   * Fetch market information of symbols that can be traded\n"
				+ "   */\n"
				+ "  FutureRestResponse<MyTestCEXMarketDataExchangeInfoResponse> exchangeInfo(MyTestCEXMarketDataExchangeInfoRequest request);\n"
				+ "  /**\n"
				+ "   * Fetch current tickers\n"
				+ "   */\n"
				+ "  FutureRestResponse<MyTestCEXMarketDataTickersResponse> tickers(MyTestCEXMarketDataTickersRequest request);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Subscribe to tickerStream stream.<br/>\n"
				+ "   * Subscribe to ticker stream\n"
				+ "   * \n"
				+ "   * @return client subscriptionId to use for unsubscription\n"
				+ "   */\n"
				+ "  String subscribeTickerStream(MyTestCEXMarketDataTickerStreamRequest request, WebsocketListener<MyTestCEXMarketDataTickerStreamMessage> listener);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Unsubscribe from tickerStream stream.\n"
				+ "   * \n"
				+ "   * @param subscriptionId ID of subscription returned by #subscribeTickerStream()\n"
				+ "   */\n"
				+ "  boolean unsubscribeTickerStream(String subscriptionId);\n"
				+ "}\n"
				+ "", 
				apiInterfaceGenerator.generate());
	}
	
	@Test
	public void testGenerateExchangeApiNoWsEndpoint() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorNoWebsocketEndpoint.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		ExchangeApiInterfaceGenerator apiInterfaceGenerator = new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata;\n"
				+ "\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataExchangeInfoRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataExchangeInfoResponse;\n"
				+ "import com.scz.jxapi.netutils.rest.FutureRestResponse;\n"
				+ "import com.scz.jxapi.util.HasProperties;\n"
				+ "\n"
				+ "/**\n"
				+ " * MarketData CEX MarketData API</br>\n"
				+ " * The market data API of MyTestCEX, , with only 1 REST endpoint and no websocket endpoint\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public interface MyTestCEXMarketDataApi extends HasProperties {\n"
				+ "  /**\n"
				+ "   * Fetch market information of symbols that can be traded\n"
				+ "   */\n"
				+ "  FutureRestResponse<MyTestCEXMarketDataExchangeInfoResponse> exchangeInfo(MyTestCEXMarketDataExchangeInfoRequest request);\n"
				+ "}\n", 
				apiInterfaceGenerator.generate());
	}
	
	@Test
	public void testGenerateExchangeApiNoRestEndpoint() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorNoRestEndpoint.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		ExchangeApiInterfaceGenerator apiInterfaceGenerator = new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata;\n"
				+ "\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickerStreamMessage;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataTickerStreamRequest;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketListener;\n"
				+ "import com.scz.jxapi.util.HasProperties;\n"
				+ "\n"
				+ "/**\n"
				+ " * MarketData CEX MarketData API</br>\n"
				+ " * The market data API of MyTestCEX\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public interface MyTestCEXMarketDataApi extends HasProperties {\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Subscribe to tickerStream stream.<br/>\n"
				+ "   * Subscribe to ticker stream\n"
				+ "   * \n"
				+ "   * @return client subscriptionId to use for unsubscription\n"
				+ "   */\n"
				+ "  String subscribeTickerStream(MyTestCEXMarketDataTickerStreamRequest request, WebsocketListener<MyTestCEXMarketDataTickerStreamMessage> listener);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Unsubscribe from tickerStream stream.\n"
				+ "   * \n"
				+ "   * @param subscriptionId ID of subscription returned by #subscribeTickerStream()\n"
				+ "   */\n"
				+ "  boolean unsubscribeTickerStream(String subscriptionId);\n"
				+ "}\n", 
				apiInterfaceGenerator.generate());
	}
}
