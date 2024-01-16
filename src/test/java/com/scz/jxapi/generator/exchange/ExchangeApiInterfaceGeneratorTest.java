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
	
	@Test
	public void testGenerateExchangeApiSpecificResponseDataTypes() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		ExchangeApiInterfaceGenerator apiInterfaceGenerator = new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata;\n"
				+ "\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeBigDecimalRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeBooleanRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeIntListRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeIntRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeLongRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeObjectListMapRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeObjectListMapResponse;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeObjectRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeObjectResponse;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataGetRestResponseDataTypeStringRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataSubscribeTickerMessage;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataSubscribeTickerRequest;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataSubscribeTickertMapListStreamMessage;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataSubscribeTickertMapListStreamRequest;\n"
				+ "import com.scz.jxapi.netutils.rest.FutureRestResponse;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketListener;\n"
				+ "import com.scz.jxapi.util.HasProperties;\n"
				+ "import java.math.BigDecimal;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Map;\n"
				+ "\n"
				+ "/**\n"
				+ " * MarketData CEX MarketData API</br>\n"
				+ " * The market data API of MyTestCEX, , with different response data types for websocket and REST endpoints. Remark: Only OBJECT (default) and STRING are usually used, but any data type is supported\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public interface MyTestCEXMarketDataApi extends HasProperties {\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using INT response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<Integer> getRestResponseDataTypeInt(MyTestCEXMarketDataGetRestResponseDataTypeIntRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using LONG response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<Long> getRestResponseDataTypeLong(MyTestCEXMarketDataGetRestResponseDataTypeLongRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using BIGDECIMAL response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<BigDecimal> getRestResponseDataTypeBigDecimal(MyTestCEXMarketDataGetRestResponseDataTypeBigDecimalRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using STRING response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<String> getRestResponseDataTypeString(MyTestCEXMarketDataGetRestResponseDataTypeStringRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using BIGDECIMAL response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<BigDecimal> getRestResponseDataTypeBigDecimal(MyTestCEXMarketDataGetRestResponseDataTypeBigDecimalRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using BOOLEAN response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<Boolean> getRestResponseDataTypeBoolean(MyTestCEXMarketDataGetRestResponseDataTypeBooleanRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using OBJECT response data type (default type, explicit here)\n"
				+ "   */\n"
				+ "  FutureRestResponse<MyTestCEXMarketDataGetRestResponseDataTypeObjectResponse> getRestResponseDataTypeObject(MyTestCEXMarketDataGetRestResponseDataTypeObjectRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using INT_LIST response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<List<Integer>> getRestResponseDataTypeIntList(MyTestCEXMarketDataGetRestResponseDataTypeIntListRequest request);\n"
				+ "  /**\n"
				+ "   * A sample REST endpoint using OBJECT_LIST_MAP response data type\n"
				+ "   */\n"
				+ "  FutureRestResponse<Map<String, List<MyTestCEXMarketDataGetRestResponseDataTypeObjectListMapResponse>>> getRestResponseDataTypeObjectListMap(MyTestCEXMarketDataGetRestResponseDataTypeObjectListMapRequest request);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Subscribe to subscribeTicker stream.<br/>\n"
				+ "   * Subscribe to stream with OBJECT message type (default, explicit here)\n"
				+ "   * \n"
				+ "   * @return client subscriptionId to use for unsubscription\n"
				+ "   */\n"
				+ "  String subscribeSubscribeTicker(MyTestCEXMarketDataSubscribeTickerRequest request, WebsocketListener<MyTestCEXMarketDataSubscribeTickerMessage> listener);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Unsubscribe from subscribeTicker stream.\n"
				+ "   * \n"
				+ "   * @param subscriptionId ID of subscription returned by #subscribeSubscribeTicker()\n"
				+ "   */\n"
				+ "  boolean unsubscribeSubscribeTicker(String subscriptionId);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Subscribe to subscribeTickertMapListStream stream.<br/>\n"
				+ "   * Subscribe to stream with OBJECT_MAP_LIST message type\n"
				+ "   * \n"
				+ "   * @return client subscriptionId to use for unsubscription\n"
				+ "   */\n"
				+ "  String subscribeSubscribeTickertMapListStream(MyTestCEXMarketDataSubscribeTickertMapListStreamRequest request, WebsocketListener<List<Map<String, MyTestCEXMarketDataSubscribeTickertMapListStreamMessage>>> listener);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Unsubscribe from subscribeTickertMapListStream stream.\n"
				+ "   * \n"
				+ "   * @param subscriptionId ID of subscription returned by #subscribeSubscribeTickertMapListStream()\n"
				+ "   */\n"
				+ "  boolean unsubscribeSubscribeTickertMapListStream(String subscriptionId);\n"
				+ "}\n"
				+ "", 
				apiInterfaceGenerator.generate());
	}
}
