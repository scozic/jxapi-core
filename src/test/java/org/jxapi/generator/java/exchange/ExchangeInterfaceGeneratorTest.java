package org.jxapi.generator.java.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
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
        + " * A sample Exchange descriptor file. Should be provided config properties: {@link com.foo.bar.gen.MyTestExchangeProperties#API_KEY}, {@link com.foo.bar.gen.MyTestExchangeProperties#API_SECRET}. Author: {@link com.foo.bar.gen.MyTestExchangeConstants.Author#FIRST_NAME} {@link com.foo.bar.gen.MyTestExchangeConstants.Author#LAST_NAME}\n"
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
        + "  /**\n"
        + "   * Name of <code>httpDefault</code> HttpClient.\n"
        + "   */\n"
        + "   String HTTP_DEFAULT_HTTP_CLIENT = \"httpDefault\";\n"
        + "  /**\n"
        + "   * Name of <code>wsDefault</code> WebsocketClient.\n"
        + "   */\n"
        + "   String WS_DEFAULT_WEBSOCKET_CLIENT = \"wsDefault\";\n"
        + "  \n"
        + "  // API groups\n"
        + "  \n"
        + "  /**\n"
        + "   * @return The market data API of MyTestExchange. Author: {@link com.foo.bar.gen.MyTestExchangeConstants#AUTHOR_FULL_NAME}\n"
        + "   */\n"
        + "  MyTestExchangeMarketDataApi getMarketDataApi();\n"
        + "}\n"
        + "", 
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
        + " * A sample Exchange descriptor file. Should be provided config properties: {@link com.foo.bar.gen.MyTestExchangeProperties#API_KEY}, {@link com.foo.bar.gen.MyTestExchangeProperties#API_SECRET}. Author: {@link com.foo.bar.gen.MyTestExchangeConstants.Author#FIRST_NAME} {@link com.foo.bar.gen.MyTestExchangeConstants.Author#LAST_NAME}\n"
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
        + "  /**\n"
        + "   * Name of <code>httpDefault</code> HttpClient.\n"
        + "   */\n"
        + "   String HTTP_DEFAULT_HTTP_CLIENT = \"httpDefault\";\n"
        + "  /**\n"
        + "   * Name of <code>wsDefault</code> WebsocketClient.\n"
        + "   */\n"
        + "   String WS_DEFAULT_WEBSOCKET_CLIENT = \"wsDefault\";\n"
        + "}\n"
        + "", 
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
        + "  /**\n"
        + "   * Name of <code>httpDefault</code> HttpClient.\n"
        + "   */\n"
        + "   String HTTP_DEFAULT_HTTP_CLIENT = \"httpDefault\";\n"
        + "  \n"
        + "  // API groups\n"
        + "  \n"
        + "  /**\n"
        + "   * @return An API with both API global and endpoint specific rate limits\n"
        + "   */\n"
        + "  MyTestExchangeFuturesTradingApi getFuturesTradingApi();\n"
        + "  \n"
        + "  // Rate limits\n"
        + "  \n"
        + "  /**\n"
        + "   * @return 'exchangeGlobalRule' rate limit rule.\n"
        + "   */\n"
        + "  public RateLimitRule getExchangeGlobalRuleRateLimit();\n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateExchangeInterface_InvalidRateLimitId_Null() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "exchangeApiInterfaceImplementationGeneratorTestWithRateLimitRulesAtExchangeLevelDescriptor.json"));
    exchangeDescriptor.getRateLimits().get(0).setId(null);
    ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, null);
    exchangeGenerator.generate();
  }
  
  @Test
  public void testGenerateExchangeInterface_ExchangeWithConflictingApiNames() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(Paths.get(".", "src", "test", "resources", "exchangeWithEndpointWithConflictingPropertyNames.yaml"));
    ExchangeInterfaceGenerator exchangeGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, null);
    Assert.assertEquals("package org.jxapi.exchanges.conflict.gen;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.Exchange;\n"
        + "import org.jxapi.exchanges.conflict.gen.a.ConflictAApi;\n"
        + "import org.jxapi.exchanges.conflict.gen.a.ConflictaApi;\n"
        + "import org.jxapi.exchanges.conflict.gen.v1.ConflictV1Api;\n"
        + "\n"
        + "/**\n"
        + " * conflict API<br>\n"
        + " * A dummy exchange where one REST and one websocket endpoint are defined, using parameters with names so that their generated code associated variables or classes would conflict. Will be used for tests that ensure that such conflicts are properly resolved.\n"
        + " * \n"
        + " * @see <a href=\"https://www.example.com/docs/conflict\">Reference documentation</a>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator\")\n"
        + "public interface ConflictExchange extends Exchange {\n"
        + "  \n"
        + "  /**\n"
        + "   * ID of the 'conflict' exchange\n"
        + "   */\n"
        + "  String ID = \"conflict\";\n"
        + "  \n"
        + "  /**\n"
        + "   * Version of the 'conflict' exchange\n"
        + "   */\n"
        + "  String VERSION = \"1.0.0\";\n"
        + "  /**\n"
        + "   * Name of <code>httpDefault</code> HttpClient.\n"
        + "   */\n"
        + "   String HTTP_DEFAULT_HTTP_CLIENT = \"httpDefault\";\n"
        + "  /**\n"
        + "   * Name of <code>wsDefault</code> WebsocketClient.\n"
        + "   */\n"
        + "   String WS_DEFAULT_WEBSOCKET_CLIENT = \"wsDefault\";\n"
        + "  \n"
        + "  // API groups\n"
        + "  \n"
        + "  /**\n"
        + "   * @return Version 1 of the Employee API\n"
        + "   */\n"
        + "  ConflictV1Api getV1Api();\n"
        + "  \n"
        + "  /**\n"
        + "   * @return null\n"
        + "   */\n"
        + "  ConflictaApi getaApi();\n"
        + "  \n"
        + "  /**\n"
        + "   * @return null\n"
        + "   */\n"
        + "  ConflictAApi getAApi();\n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }
}
