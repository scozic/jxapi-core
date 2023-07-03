package com.scz.jxapi.generator.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class ExchangeInterfaceGeneratorTest {

	@Test
	public void testGenerateExchangeInterface() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.foo.bar.gen;\n"
				+ "\n"
				+ "import com.foo.bar.gen.marketdata.MyTestCEXMarketDataApi;\n"
				+ "\n"
				+ "/**\n"
				+ " * MyTestCEX API</br>\n"
				+ " * A sample CEX descriptor file\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public interface MyTestCEXExchange {\n"
				+ "  \n"
				+ "  MyTestCEXMarketDataApi getMyTestCEXMarketDataApi();\n"
				+ "}\n", 
				exchangeGenerator.generate());
	}
}
