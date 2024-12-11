package com.scz.jxapi.generator.java.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;

/**
 * Unit test for {@link ExchangeInterfaceGenerator}
 */
public class ExchangeInterfaceGeneratorTest {

	@Test
	public void testGenerateExchangeInterface() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.foo.bar.gen;\n"
				+ "\n"
				+ "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
				+ "import com.scz.jxapi.exchange.Exchange;\n"
				+ "\n"
				+ "/**\n"
				+ " * MyTestExchange API<br>\n"
				+ " * A sample Exchange descriptor file\n"
				+ " * @see <a href=\"https://docs.myexchange.com/api\">Reference documentation</a>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public interface MyTestExchangeExchange extends Exchange {\n"
				+ "  \n"
				+ "  String ID = \"MyTestExchange\";\n"
				+ "  \n"
				+ "  MyTestExchangeMarketDataApi getMyTestExchangeMarketDataApi();\n"
				+ "}\n", 
				exchangeGenerator.generate());
	}
}
