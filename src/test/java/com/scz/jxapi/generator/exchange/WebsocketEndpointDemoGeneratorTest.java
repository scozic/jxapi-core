package com.scz.jxapi.generator.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketEndpointDemoGenerator}
 */
public class WebsocketEndpointDemoGeneratorTest {
	
	@Test
	public void testGenerateWebsocketEndpointDemoObjectRequest() throws Exception {
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
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static MyTestCEXMarketDataTickerStreamRequest createRequest() {\n"
				+ "    MyTestCEXMarketDataTickerStreamRequest request = new MyTestCEXMarketDataTickerStreamRequest();\n"
				+ "    request.setSymbol(\"BTC_USDT\");\n"
				+ "    return request;\n"
				+ "  }\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      MyTestCEXMarketDataTickerStreamRequest request = createRequest();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestCEX MarketData tickerStream' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeTickerStream(request, m -> log.info(\"Received message:\" + m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestCEX MarketData tickerStream' stream\");\n"
				+ "      api.unsubscribeTickerStream(subId);\n"
				+ "      Thread.sleep(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new WebsocketEndpointDemoGenerator(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor).generate());
	}
	
	@Test
	public void testGenerateWebsocketEndpointDemoSpecificRequesTypePrimitiveStringWithNamedArg() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchange.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpointDescriptor = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithStringRequestDataType", exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestCEXExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#streamWithStringRequestDataType(String)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataStreamWithStringRequestDataTypeDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataStreamWithStringRequestDataTypeDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static String createSymbol() {\n"
				+ "    return \"BTC_USDT\";\n"
				+ "  }\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      String request = createSymbol();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestCEX MarketData streamWithStringRequestDataType' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeStreamWithStringRequestDataType(request, m -> log.info(\"Received message:\" + m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestCEX MarketData streamWithStringRequestDataType' stream\");\n"
				+ "      api.unsubscribeStreamWithStringRequestDataType(subId);\n"
				+ "      Thread.sleep(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new WebsocketEndpointDemoGenerator(exchange, exchangeApiDescriptor, websocketEndpointDescriptor).generate());
	}
	
	@Test
	public void testGenerateWebsocketEndpointDemoZeroArgs() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchange.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpointDescriptor = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithObjectRequestDataTypeZeroParameters", exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestCEXExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#streamWithObjectRequestDataTypeZeroParameters()}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataStreamWithObjectRequestDataTypeZeroParametersDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataStreamWithObjectRequestDataTypeZeroParametersDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestCEX MarketData streamWithObjectRequestDataTypeZeroParameters' for \" + SUBSCRIPTION_DURATION + \"ms\");\n"
				+ "      String subId = api.subscribeStreamWithObjectRequestDataTypeZeroParameters(m -> log.info(\"Received message:\" + m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestCEX MarketData streamWithObjectRequestDataTypeZeroParameters' stream\");\n"
				+ "      api.unsubscribeStreamWithObjectRequestDataTypeZeroParameters(subId);\n"
				+ "      Thread.sleep(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new WebsocketEndpointDemoGenerator(exchange, exchangeApiDescriptor, websocketEndpointDescriptor).generate());
	}
	
	@Test
	public void testGenerateWebsocketEndpointSpecificRequestTypeObjectListMapWithObjectDefinedInDifferentAPI() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchange.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpointDescriptor = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithObjectListMapRequestDataType", exchangeApiDescriptor);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestCEXExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.SingleSymbol;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Map;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#streamWithObjectListMapRequestDataType(Map<String, List<SingleSymbol>>)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataStreamWithObjectListMapRequestDataTypeDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataStreamWithObjectListMapRequestDataTypeDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static Map<String, List<SingleSymbol>> createRequest() {\n"
				+ "    SingleSymbol requestItem = new SingleSymbol();\n"
				+ "    requestItem.setSymbol(\"BTC_USDT\");\n"
				+ "    return Map.of(\"spot\", List.of(requestItem));\n"
				+ "  }\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      Map<String, List<SingleSymbol>> request = createRequest();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestCEX MarketData streamWithObjectListMapRequestDataType' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeStreamWithObjectListMapRequestDataType(request, m -> log.info(\"Received message:\" + m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestCEX MarketData streamWithObjectListMapRequestDataType' stream\");\n"
				+ "      api.unsubscribeStreamWithObjectListMapRequestDataType(subId);\n"
				+ "      Thread.sleep(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new WebsocketEndpointDemoGenerator(exchange, exchangeApiDescriptor, websocketEndpointDescriptor).generate());
	}

}
