package com.scz.jxapi.generator.java.exchange.api.demo;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link WebsocketEndpointDemoGenerator}
 */
public class WebsocketEndpointDemoGeneratorTest {
	
	@Test
	public void testGenerateWebsocketEndpointDemoObjectRequest() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpointDescriptor = exchangeApiDescriptor.getWebsocketEndpoints().get(0);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestExchangeMarketDataTickerStreamRequest;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#tickerStream(MyTestExchangeMarketDataTickerStreamRequest)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataTickerStreamDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataTickerStreamDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static MyTestExchangeMarketDataTickerStreamRequest createRequest() {\n"
				+ "    MyTestExchangeMarketDataTickerStreamRequest request = new MyTestExchangeMarketDataTickerStreamRequest();\n"
				+ "    request.setSymbol(\"BTC_USDT\");\n"
				+ "    return request;\n"
				+ "  }\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      MyTestExchangeMarketDataTickerStreamRequest request = createRequest();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestExchange MarketData tickerStream' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeTickerStream(request, m -> DemoUtil.logWsMessage(m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestExchange MarketData tickerStream' stream\");\n"
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
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#streamWithStringRequestDataType(String)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataStreamWithStringRequestDataTypeDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataStreamWithStringRequestDataTypeDemo.class);\n"
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
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      String request = createSymbol();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestExchange MarketData streamWithStringRequestDataType' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeStreamWithStringRequestDataType(request, m -> DemoUtil.logWsMessage(m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestExchange MarketData streamWithStringRequestDataType' stream\");\n"
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
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#streamWithObjectRequestDataTypeZeroParameters()}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataStreamWithObjectRequestDataTypeZeroParametersDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataStreamWithObjectRequestDataTypeZeroParametersDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestExchange MarketData streamWithObjectRequestDataTypeZeroParameters' for \" + SUBSCRIPTION_DURATION + \"ms\");\n"
				+ "      String subId = api.subscribeStreamWithObjectRequestDataTypeZeroParameters(m -> DemoUtil.logWsMessage(m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestExchange MarketData streamWithObjectRequestDataTypeZeroParameters' stream\");\n"
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
				+ "import java.util.List;\n"
				+ "import java.util.Map;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MultiSymbol;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.SingleSymbol;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#streamWithObjectListMapRequestDataType(Map<String, List<MultiSymbol>>)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataStreamWithObjectListMapRequestDataTypeDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataStreamWithObjectListMapRequestDataTypeDemo.class);\n"
				+ "  \n"
				+ "  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;\n"
				+ "  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n"
				+ "  \n"
				+ "  public static Map<String, List<MultiSymbol>> createRequest() {\n"
				+ "    MultiSymbol requestItem = new MultiSymbol();\n"
				+ "    requestItem.setSymbolList(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[BTC_USDT, ETH_USDT]\"));\n"
				+ "    requestItem.setSymbolMap(new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance())).deserialize(\"{USDT=[BTC_USDT, ETH]}\"));\n"
				+ "    SingleSymbol requestItem_symbolObject = new SingleSymbol();\n"
				+ "    requestItem_symbolObject.setSymbol(\"BTC_USDT\");\n"
				+ "    requestItem.setSymbolObject(requestItem_symbolObject);\n"
				+ "    return Map.of(\"spot\", List.of(requestItem));\n"
				+ "  }\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      Map<String, List<MultiSymbol>> request = createRequest();\n"
				+ "      log.info(\"Subscribing to websocket API 'MyTestExchange MarketData streamWithObjectListMapRequestDataType' for \" + SUBSCRIPTION_DURATION + \"ms with request:\" + request);\n"
				+ "      String subId = api.subscribeStreamWithObjectListMapRequestDataType(request, m -> DemoUtil.logWsMessage(m));\n"
				+ "      Thread.sleep(SUBSCRIPTION_DURATION);\n"
				+ "      log.info(\"Unubscribing from 'MyTestExchange MarketData streamWithObjectListMapRequestDataType' stream\");\n"
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
