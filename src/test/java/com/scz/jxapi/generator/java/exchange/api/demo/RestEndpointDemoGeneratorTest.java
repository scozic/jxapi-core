package com.scz.jxapi.generator.java.exchange.api.demo;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link RestEndpointDemoGenerator} 
 */
public class RestEndpointDemoGeneratorTest {

	@Test
	public void testGenerateRestEndpointDemo() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0); 
		RestEndpointDescriptor restEndpointDescriptor = ClassesGeneratorTestUtil.findRestEndpointByName("exchangeInfo", api);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestExchangeMarketDataExchangeInfoRequest;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#exchangeInfo(MyTestExchangeMarketDataExchangeInfoRequest)}<br>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataExchangeInfoDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataExchangeInfoDemo.class);\n"
				+ "  \n"
				+ "  public static MyTestExchangeMarketDataExchangeInfoRequest createRequest() {\n"
				+ "    MyTestExchangeMarketDataExchangeInfoRequest request = new MyTestExchangeMarketDataExchangeInfoRequest();\n"
				+ "    request.setSymbols(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"BTC\\\", \\\"ETH\\\"]\"));\n"
				+ "    return request;\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      MyTestExchangeMarketDataExchangeInfoRequest request = createRequest();\n"
				+ "      log.info(\"Calling com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi.exchangeInfo() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.exchangeInfo(request));\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n"
				+ "", 
				new RestEndpointDemoGenerator(exchange, api, restEndpointDescriptor).generate());
	}
	
	@Test
	public void testGenerateRestEndpointDemoSpecificRequesTypePrimitiveInt() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeInt", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
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
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#postRestRequestDataTypeInt(Integer)}<br>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataPostRestRequestDataTypeIntDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataPostRestRequestDataTypeIntDemo.class);\n"
				+ "  \n"
				+ "  public static Integer createRequest() {\n"
				+ "    return Integer.valueOf(12345);\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      Integer request = createRequest();\n"
				+ "      log.info(\"Calling com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi.postRestRequestDataTypeInt() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeInt(request));\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGenerateRestEndpointDemoSpecificRequesTypePrimitiveStringWithNamedArg() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeString", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
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
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#postRestRequestDataTypeString(String)}<br>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataPostRestRequestDataTypeStringDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataPostRestRequestDataTypeStringDemo.class);\n"
				+ "  \n"
				+ "  public static String createHello() {\n"
				+ "    return \"Hello world!\";\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      String request = createHello();\n"
				+ "      log.info(\"Calling com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi.postRestRequestDataTypeString() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeString(request));\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGenerateRestEndpointDemoNoArgEndpoint() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeObjectNoParameters", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
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
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#postRestRequestDataTypeObjectNoParameters()}<br>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataPostRestRequestDataTypeObjectNoParametersDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataPostRestRequestDataTypeObjectNoParametersDemo.class);\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      log.info(\"Calling com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi.postRestRequestDataTypeObjectNoParameters() API\");\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeObjectNoParameters());\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n"
				+ "", 
				generator.generate());
	}
	
	@Test
	public void testGenerateRestEndpointSpecificRequestTypeObjectListMapWithReferencedObject() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeObjectListMap", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import java.util.List;\n"
				+ "import java.util.Map;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.SingleSymbol;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>>)}<br>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataPostRestRequestDataTypeObjectListMapDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataPostRestRequestDataTypeObjectListMapDemo.class);\n"
				+ "  \n"
				+ "  public static Map<String, List<SingleSymbol>> createRequest() {\n"
				+ "    SingleSymbol requestItem = new SingleSymbol();\n"
				+ "    requestItem.setSymbol(\"BTC_USDT\");\n"
				+ "    return Map.of(\"spot\", List.of(requestItem));\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      Map<String, List<SingleSymbol>> request = createRequest();\n"
				+ "      log.info(\"Calling com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi.postRestRequestDataTypeObjectListMap() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeObjectListMap(request));\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGenerateRestEndpointRequestTypeIntList() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeIntList", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import java.util.List;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestExchangeExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi#postRestRequestDataTypeIntList(List<Integer>)}<br>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestExchangeMarketDataPostRestRequestDataTypeIntListDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestExchangeMarketDataPostRestRequestDataTypeIntListDemo.class);\n"
				+ "  \n"
				+ "  public static List<Integer> createRequest() {\n"
				+ "    return new ListJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(\"[1, 3, 5]\");\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestExchangeMarketDataApi api = new MyTestExchangeExchangeImpl(\"test-myTestExchange\", TestJXApiProperties.filterProperties(\"myTestExchange\", true)).getMyTestExchangeMarketDataApi();\n"
				+ "      List<Integer> request = createRequest();\n"
				+ "      log.info(\"Calling com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi.postRestRequestDataTypeIntList() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeIntList(request));\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
}
