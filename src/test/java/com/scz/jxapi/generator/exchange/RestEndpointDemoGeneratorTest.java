package com.scz.jxapi.generator.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RestEndpointDemoGenerator} 
 */
public class RestEndpointDemoGeneratorTest {

	@Test
	public void testGenerateRestEndpointDemo() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor exchangeApiDescriptor = exchangeDescriptor.getApis().get(0);
		RestEndpointDescriptor restEndpointDescriptor = exchangeApiDescriptor.getRestEndpoints().get(0);
		Assert.assertEquals("package com.foo.bar.gen.marketdata.demo;\n"
				+ "\n"
				+ "import com.foo.bar.gen.MyTestCEXExchangeImpl;\n"
				+ "import com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi;\n"
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXMarketDataExchangeInfoRequest;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.util.DemoUtil;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#exchangeInfo(MyTestCEXMarketDataExchangeInfoRequest)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataExchangeInfoDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataExchangeInfoDemo.class);\n"
				+ "  \n"
				+ "  public static MyTestCEXMarketDataExchangeInfoRequest createRequest() {\n"
				+ "    MyTestCEXMarketDataExchangeInfoRequest request = new MyTestCEXMarketDataExchangeInfoRequest();\n"
				+ "    request.setSymbols(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"BTC\\\", \\\"ETH\\\"]\"));\n"
				+ "    return request;\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      MyTestCEXMarketDataExchangeInfoRequest request = createRequest();\n"
				+ "      log.info(\"Calling 'com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi.exchangeInfo() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.exchangeInfo(request);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				new RestEndpointDemoGenerator(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor).generate());
	}
	
	@Test
	public void testGenerateRestEndpointDemoSpecificRequesTypePrimitiveInt() throws Exception {
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("postRestRequestDataTypeInt", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
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
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#postRestRequestDataTypeInt(Integer)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataPostRestRequestDataTypeIntDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataPostRestRequestDataTypeIntDemo.class);\n"
				+ "  \n"
				+ "  public static Integer createRequest() {\n"
				+ "    return Integer.valueOf(12345);\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      Integer request = createRequest();\n"
				+ "      log.info(\"Calling 'com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi.postRestRequestDataTypeInt() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeInt(request);\n"
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
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("postRestRequestDataTypeString", api);
		RestEndpointDemoGenerator generator = new RestEndpointDemoGenerator(exchange, api, restEndpoint);
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
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#postRestRequestDataTypeString(String)}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataPostRestRequestDataTypeStringDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataPostRestRequestDataTypeStringDemo.class);\n"
				+ "  \n"
				+ "  public static String createHello() {\n"
				+ "    return \"Hello world!\";\n"
				+ "  }\n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      String request = createHello();\n"
				+ "      log.info(\"Calling 'com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi.postRestRequestDataTypeString() API with request:\" + request);\n"
				+ "      DemoUtil.checkResponse(api.postRestRequestDataTypeString(request);\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	private RestEndpointDescriptor findRestEndpointByName(String name, ExchangeApiDescriptor exchangeDescriptor) {
		for (RestEndpointDescriptor api: exchangeDescriptor.getRestEndpoints()) {
			if (api.getName().equals(name)) {
				return api;
			}
		}
		throw new AssertionError("No such API:" + name + " in:" + exchangeDescriptor);
	}
}
