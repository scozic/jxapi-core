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
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXExchangeInfoRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXExchangeInfoResponse;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXTickerStreamMessage;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXTickerStreamRequest;\n"
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
				+ "  FutureRestResponse<MyTestCEXExchangeInfoResponse> exchangeInfo(MyTestCEXExchangeInfoRequest request);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Subscribe to tickerStream stream.<br/>\n"
				+ "   * Subscribe to ticker stream\n"
				+ "   * \n"
				+ "   * @return client subscriptionId to use for unsubscription\n"
				+ "   */\n"
				+ "  String subscribeTickerStream(MyTestCEXTickerStreamRequest request, WebsocketListener<MyTestCEXTickerStreamMessage> listener);\n"
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
