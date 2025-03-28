package org.jxapi.generator.java.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;

/**
 * Unit test for {@link ExchangeInterfaceGenerator}
 */
public class ExchangeInterfaceGeneratorTest {

	@Test
	public void testGenerateExchangeInterface() throws Exception {
		ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.foo.bar.gen;\n"
		    + "\n"
		    + "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
		    + "import javax.annotation.processing.Generated;\n"
		    + "import org.jxapi.exchange.Exchange;\n"
		    + "\n"
		    + "/**\n"
		    + " * MyTestExchange API<br>\n"
		    + " * A sample Exchange descriptor file\n"
		    + " * @see <a href=\"https://docs.myexchange.com/api\">Reference documentation</a>\n"
		    + " */\n"
		    + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator\")\n"
		    + "public interface MyTestExchangeExchange extends Exchange {\n"
		    + "  \n"
		    + "  /**\n"
		    + "   * ID of the 'MyTestExchange' exchange\n"
		    + "   */\n"
		    + "  String ID = \"MyTestExchange\";\n"
		    + "  /**\n"
		    + "   * Version of the 'MyTestExchange' exchange\n"
		    + "   */\n"
		    + "  String VERSION = \"1.0.0\";\n"
		    + "  \n"
		    + "  /**\n"
		    + "   * @return The market data API of MyTestExchange\n"
		    + "   */\n"
		    + "  MyTestExchangeMarketDataApi getMyTestExchangeMarketDataApi();\n"
		    + "}\n", 
				exchangeGenerator.generate());
	}
}
