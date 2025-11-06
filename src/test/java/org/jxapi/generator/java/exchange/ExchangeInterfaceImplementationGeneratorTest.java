package org.jxapi.generator.java.exchange;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;

/**
 * Unit test for {@link ExchangeInterfaceImplementationGenerator}
 */
public class ExchangeInterfaceImplementationGeneratorTest {
  
  @Test
  public void testGenerateExchangeApi() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    exchangeDescriptor.setHttpUrl("${config.serverHttpUrl}/api/v${constants.apiVersion}");
    exchangeDescriptor.setWebsocketUrl("${config.serverWsUrl}/ws");
    exchangeDescriptor.setAfterInitHookFactory("com.xxz.foo.gen.FooAfterInitExchangeHookFactory");
    
    Constant apiVersion = new Constant();
    apiVersion.setName("apiVersion");
    apiVersion.setValue("1");
    exchangeDescriptor.setConstants(List.of(apiVersion));
    
    ConfigPropertyDescriptor serverHttpUrl = new ConfigPropertyDescriptor();
    serverHttpUrl.setName("serverHttpUrl");
    
    ConfigPropertyDescriptor serverWsUrl = new ConfigPropertyDescriptor();
    serverWsUrl.setName("serverWsUrl");
    
    exchangeDescriptor.setProperties(List.of(serverHttpUrl, serverWsUrl));
    
    List<ExchangeApiDescriptor> apis = new ArrayList<>();
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("api1");
    apis.add(api1);
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("api2");
    apis.add(api2);
    exchangeDescriptor.setApis(apis);
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package com.xyz.foo.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import com.xyz.foo.gen.api1.FooApi1Api;\n"
        + "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
        + "import com.xyz.foo.gen.api2.FooApi2Api;\n"
        + "import com.xyz.foo.gen.api2.FooApi2ApiImpl;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "import org.jxapi.util.EncodingUtil;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link FooExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
        + "  \n"
        + "  private final FooApi1Api api1Api;\n"
        + "  private final FooApi2Api api2Api;\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          EncodingUtil.substituteArguments(\"${config.serverHttpUrl}/api/v${constants.apiVersion}\", \"config.serverHttpUrl\", FooProperties.getServerHttpUrl(properties), \"constants.apiVersion\", FooConstants.API_VERSION),\n"
        + "          EncodingUtil.substituteArguments(\"${config.serverWsUrl}/ws\", \"config.serverWsUrl\", FooProperties.getServerWsUrl(properties)));\n"
        + "    this.api1Api = addApi(new FooApi1ApiImpl(this));\n"
        + "    this.api2Api = addApi(new FooApi2ApiImpl(this));\n"
        + "    afterInit(\"com.xxz.foo.gen.FooAfterInitExchangeHookFactory\");\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi1Api getApi1Api() {\n"
        + "    return this.api1Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi2Api getApi2Api() {\n"
        + "    return this.api2Api;\n"
        + "  }\n"
        + "  \n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeApiWithExchangeRateLimits() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    List<ExchangeApiDescriptor> apis = new ArrayList<>();
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("api1");
    api1.setRestEndpoints(List.of(new RestEndpointDescriptor()));
    apis.add(api1);
    // api2 has 0 REST endpoints and should not be provided request throttler in constructor.
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("api2");
    api2.setRestEndpoints(List.of());
    apis.add(api2);
    ExchangeApiDescriptor api3 = new ExchangeApiDescriptor();
    api3.setName("api3");
    apis.add(api3);
    exchangeDescriptor.setApis(apis);
    exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("exchangeGlobalRateLimit", 60000, 1000)));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package com.xyz.foo.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import com.xyz.foo.gen.api1.FooApi1Api;\n"
        + "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
        + "import com.xyz.foo.gen.api2.FooApi2Api;\n"
        + "import com.xyz.foo.gen.api2.FooApi2ApiImpl;\n"
        + "import com.xyz.foo.gen.api3.FooApi3Api;\n"
        + "import com.xyz.foo.gen.api3.FooApi3ApiImpl;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "import org.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
        + "import org.jxapi.netutils.rest.ratelimits.RequestThrottler;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link FooExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
        + "  \n"
        + "  private final RateLimitRule rateLimitExchangeGlobalRateLimit = RateLimitRule.createRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
        + "  \n"
        + "  private final RequestThrottler requestThrottler = new RequestThrottler(\"Foo\");\n"
        + "  private final FooApi1Api api1Api;\n"
        + "  private final FooApi2Api api2Api;\n"
        + "  private final FooApi3Api api3Api;\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          null,\n"
        + "          null);\n"
        + "    this.api1Api = addApi(new FooApi1ApiImpl(this, requestThrottler));\n"
        + "    this.api2Api = addApi(new FooApi2ApiImpl(this));\n"
        + "    this.api3Api = addApi(new FooApi3ApiImpl(this));\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi1Api getApi1Api() {\n"
        + "    return this.api1Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi2Api getApi2Api() {\n"
        + "    return this.api2Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi3Api getApi3Api() {\n"
        + "    return this.api3Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public RateLimitRule getExchangeGlobalRateLimitRateLimit() {\n"
        + "    return this.rateLimitExchangeGlobalRateLimit;\n"
        + "  }\n"
        + "  \n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeApiWithEmptyExchangeRateLimits() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    List<ExchangeApiDescriptor> apis = new ArrayList<>();
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("api1");
    api1.setRestEndpoints(List.of(new RestEndpointDescriptor()));
    apis.add(api1);
    exchangeDescriptor.setApis(apis);
    exchangeDescriptor.setRateLimits(List.of());
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package com.xyz.foo.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import com.xyz.foo.gen.api1.FooApi1Api;\n"
        + "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link FooExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
        + "  \n"
        + "  private final FooApi1Api api1Api;\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          null,\n"
        + "          null);\n"
        + "    this.api1Api = addApi(new FooApi1ApiImpl(this));\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi1Api getApi1Api() {\n"
        + "    return this.api1Api;\n"
        + "  }\n"
        + "  \n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeApiWithExchangeRateLimitsButNoApis() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("exchangeGlobalRateLimit", 60000, 1000)));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package com.xyz.foo.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "import org.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link FooExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
        + "  \n"
        + "  private final RateLimitRule rateLimitExchangeGlobalRateLimit = RateLimitRule.createRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          null,\n"
        + "          null);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public RateLimitRule getExchangeGlobalRateLimitRateLimit() {\n"
        + "    return this.rateLimitExchangeGlobalRateLimit;\n"
        + "  }\n"
        + "  \n"
        + "}\n", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeApiWithExchangeRateLimitsButApisHaveNoRestEndpoint() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    List<ExchangeApiDescriptor> apis = new ArrayList<>();
    // api2 has 0 REST endpoints and should not be provided request throttler in constructor.
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("api1");
    api1.setRestEndpoints(List.of());
    apis.add(api1);
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("api2");
    apis.add(api2);
    exchangeDescriptor.setApis(apis);
    exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("exchangeGlobalRateLimit", 60000, 1000)));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package com.xyz.foo.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import com.xyz.foo.gen.api1.FooApi1Api;\n"
        + "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
        + "import com.xyz.foo.gen.api2.FooApi2Api;\n"
        + "import com.xyz.foo.gen.api2.FooApi2ApiImpl;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "import org.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link FooExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
        + "  \n"
        + "  private final RateLimitRule rateLimitExchangeGlobalRateLimit = RateLimitRule.createRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
        + "  private final FooApi1Api api1Api;\n"
        + "  private final FooApi2Api api2Api;\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          null,\n"
        + "          null);\n"
        + "    this.api1Api = addApi(new FooApi1ApiImpl(this));\n"
        + "    this.api2Api = addApi(new FooApi2ApiImpl(this));\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi1Api getApi1Api() {\n"
        + "    return this.api1Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public FooApi2Api getApi2Api() {\n"
        + "    return this.api2Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public RateLimitRule getExchangeGlobalRateLimitRateLimit() {\n"
        + "    return this.rateLimitExchangeGlobalRateLimit;\n"
        + "  }\n"
        + "  \n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateExchangeApiWithExchangeRateLimitNullRateLimitName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule(null, 60000, 1000)));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    exchangeGenerator.generate();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateExchangeApiWithDuplicateExchangeRateLimitName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("myReule", 60000, 1000), 
                         RateLimitRule.createRule("myReule", 60000, 1000)));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    exchangeGenerator.generate();
  }
  
  @Test
  public void testGenerateExchangeApiWithWeightedExchangeRateLimitRule() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createWeightedRule("exchangeGlobalRateLimit", 60000, 1000)));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package com.xyz.foo.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "import org.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link FooExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
        + "  \n"
        + "  private final RateLimitRule rateLimitExchangeGlobalRateLimit = RateLimitRule.createWeightedRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          null,\n"
        + "          null);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public RateLimitRule getExchangeGlobalRateLimitRateLimit() {\n"
        + "    return this.rateLimitExchangeGlobalRateLimit;\n"
        + "  }\n"
        + "  \n"
        + "}\n", 
        exchangeGenerator.generate());
  }
  
  @Test
  public void testGenerateExchangeApiWithConflictingApiGroupNames() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(Paths.get(".", "src", "test", "resources", "exchangeWithEndpointWithConflictingPropertyNames.yaml"));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    Assert.assertEquals("package org.jxapi.exchanges.conflict.gen;\n"
        + "\n"
        + "import java.util.Properties;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.exchange.AbstractExchange;\n"
        + "import org.jxapi.exchanges.conflict.gen.a.ConflictAApi;\n"
        + "import org.jxapi.exchanges.conflict.gen.a.ConflictAApiImpl;\n"
        + "import org.jxapi.exchanges.conflict.gen.a.ConflictaApi;\n"
        + "import org.jxapi.exchanges.conflict.gen.a.ConflictaApiImpl;\n"
        + "import org.jxapi.exchanges.conflict.gen.v1.ConflictV1Api;\n"
        + "import org.jxapi.exchanges.conflict.gen.v1.ConflictV1ApiImpl;\n"
        + "\n"
        + "/**\n"
        + " * Actual implementation of {@link ConflictExchange}<br>\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator\")\n"
        + "public class ConflictExchangeImpl extends AbstractExchange implements ConflictExchange {\n"
        + "  \n"
        + "  private final ConflictV1Api v1Api;\n"
        + "  private final ConflictaApi aApi;\n"
        + "  private final ConflictAApi AApi;\n"
        + "  \n"
        + "  public ConflictExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          \"https://api.example.com/conflict\",\n"
        + "          null);\n"
        + "    this.v1Api = addApi(new ConflictV1ApiImpl(this));\n"
        + "    this.aApi = addApi(new ConflictaApiImpl(this));\n"
        + "    this.AApi = addApi(new ConflictAApiImpl(this));\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public ConflictV1Api getV1Api() {\n"
        + "    return this.v1Api;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public ConflictaApi getaApi() {\n"
        + "    return this.aApi;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public ConflictAApi getAApi() {\n"
        + "    return this.AApi;\n"
        + "  }\n"
        + "  \n"
        + "}\n"
        + "", 
        exchangeGenerator.generate());
  }

}
