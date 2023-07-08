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
				+ "import com.foo.bar.gen.marketdata.pojo.MyTestCEXExchangeInfoRequest;\n"
				+ "import com.scz.jxapi.util.TestJXApiProperties;\n"
				+ "import java.util.List;\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "/**\n"
				+ " * Snippet to test call to {@link com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi#exchangeInfo(com.foo.bar.gen.marketdata.pojo.MyTestCEXExchangeInfoRequest)}\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class MyTestCEXMarketDataExchangeInfoDemo {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(MyTestCEXMarketDataExchangeInfoDemo.class);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * Sample value for <i>symbols</i> parameter of <i>exchangeInfo</i> API\n"
				+ "   */\n"
				+ "  public static final List<String> SYMBOLS = null;\n"
				+ "  \n"
				+ "  public static void main(String[] args) {\n"
				+ "    try {\n"
				+ "      MyTestCEXMarketDataApi api = new MyTestCEXExchangeImpl(TestJXApiProperties.filterProperties(\"myTestCEX\", true)).getMyTestCEXMarketDataApi();\n"
				+ "      MyTestCEXExchangeInfoRequest request = new MyTestCEXExchangeInfoRequest();\n"
				+ "      request.setSymbols(SYMBOLS);\n"
				+ "      log.info(\"Calling 'com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi.exchangeInfo() API with request:\" + request);\n"
				+ "      log.info(\"Response:\" + api.exchangeInfo(request).get());\n"
				+ "      System.exit(0);\n"
				+ "    } catch (Throwable t) {\n"
				+ "      log.error(\"Exception raised from main()\", t);\n"
				+ "      System.exit(-1);\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n"
				, new RestEndpointDemoGenerator(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor).generate());
	}
}
