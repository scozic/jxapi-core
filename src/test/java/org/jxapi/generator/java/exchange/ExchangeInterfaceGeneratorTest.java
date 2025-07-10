package org.jxapi.generator.java.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ExchangeInterfaceGenerator}
 */
public class ExchangeInterfaceGeneratorTest {

  @Test
  public void testGenerateExchangeInterface() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    PlaceHolderResolver docPlaceHolderResolver = 
        PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null));
    ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, docPlaceHolderResolver);
    Assert.assertEquals("package com.foo.bar.gen;\n"
        + "\n"
        + "import com.foo.bar.gen.marketdata.MyTestExchangeMarketDataApi;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.Exchange;\n"
        + "\n"
        + "/**\n"
        + " * MyTestExchange API<br>\n"
        + " * A sample Exchange descriptor file. Should be provided config properties: {@link com.foo.bar.gen.MyTestExchangeProperties#API_KEY}, {@link com.foo.bar.gen.MyTestExchangeProperties#API_SECRET}. Author: {@link com.foo.bar.gen.MyTestExchangeConstants#AUTHOR}\n"
        + " * @see <a href=\"https://docs.myexchange.com/api\">Reference documentation</a>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator\")\n"
        + "public interface MyTestExchangeExchange extends Exchange {\n"
        + "  \n"
        + "  /**\n"
        + "   * ID of the 'MyTestExchange' exchange\n"
        + "   */\n"
        + "  String ID = \"MyTestExchange\";\n"
        + "  \n"
        + "  /**\n"
        + "   * Version of the 'MyTestExchange' exchange\n"
        + "   */\n"
        + "  String VERSION = \"1.0.0\";\n"
        + "  \n"
        + "  // API groups\n"
        + "  \n"
        + "  /**\n"
        + "   * @return The market data API of MyTestExchange. Author: {@link com.foo.bar.gen.MyTestExchangeConstants#AUTHOR}\n"
        + "   */\n"
        + "  MyTestExchangeMarketDataApi getMyTestExchangeMarketDataApi();\n"
        + "}\n", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeInterface_NoApiGroups() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    PlaceHolderResolver docPlaceHolderResolver = 
        PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null));
    exchangeDescriptor.setApis(null);
    ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, docPlaceHolderResolver);
    Assert.assertEquals("package com.foo.bar.gen;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.Exchange;\n"
        + "\n"
        + "/**\n"
        + " * MyTestExchange API<br>\n"
        + " * A sample Exchange descriptor file. Should be provided config properties: {@link com.foo.bar.gen.MyTestExchangeProperties#API_KEY}, {@link com.foo.bar.gen.MyTestExchangeProperties#API_SECRET}. Author: {@link com.foo.bar.gen.MyTestExchangeConstants#AUTHOR}\n"
        + " * @see <a href=\"https://docs.myexchange.com/api\">Reference documentation</a>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator\")\n"
        + "public interface MyTestExchangeExchange extends Exchange {\n"
        + "  \n"
        + "  /**\n"
        + "   * ID of the 'MyTestExchange' exchange\n"
        + "   */\n"
        + "  String ID = \"MyTestExchange\";\n"
        + "  \n"
        + "  /**\n"
        + "   * Version of the 'MyTestExchange' exchange\n"
        + "   */\n"
        + "  String VERSION = \"1.0.0\";\n"
        + "}\n", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeInterface_WithExchangeLevelRateLimitRule() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "exchangeApiInterfaceImplementationGeneratorTestWithRateLimitRulesAtExchangeLevelDescriptor.json"));
    ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, null);
    Assert.assertEquals("package com.foo.bar.gen;\n"
        + "\n"
        + "import com.foo.bar.gen.futurestrading.MyTestExchangeFuturesTradingApi;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.Exchange;\n"
        + "import org.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
        + "\n"
        + "/**\n"
        + " * MyTestExchange API<br>\n"
        + " * A sample exchange descriptor file used in ExchangeApiInterfaceImplementationGeneratorTest unit test, to test generation of API interface implementation with rateLimits rules defined at both exchange, API and REST endpoint levels\n"
        + " * \n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator\")\n"
        + "public interface MyTestExchangeExchange extends Exchange {\n"
        + "  \n"
        + "  /**\n"
        + "   * ID of the 'MyTestExchange' exchange\n"
        + "   */\n"
        + "  String ID = \"MyTestExchange\";\n"
        + "  \n"
        + "  /**\n"
        + "   * Version of the 'MyTestExchange' exchange\n"
        + "   */\n"
        + "  String VERSION = null;\n"
        + "  \n"
        + "  // API groups\n"
        + "  \n"
        + "  /**\n"
        + "   * @return An API with both API global and endpoint specific rate limits\n"
        + "   */\n"
        + "  MyTestExchangeFuturesTradingApi getMyTestExchangeFuturesTradingApi();\n"
        + "  \n"
        + "  // Rate limits\n"
        + "  \n"
        + "  /**\n"
        + "   * @return 'exchangeGlobalRule' rate limit rule.\n"
        + "   */\n"
        + "  public RateLimitRule getExchangeGlobalRuleRateLimit();\n"
        + "}\n", 
        exchangeGenerator.generate());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateExchangeInterface_InvalidRateLimitId_Null() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "exchangeApiInterfaceImplementationGeneratorTestWithRateLimitRulesAtExchangeLevelDescriptor.json"));
    exchangeDescriptor.getRateLimits().get(0).setId(null);
    ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, null);
    exchangeGenerator.generate();
  }
}
