package org.jxapi.generator.java.exchange;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;

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
    exchangeDescriptor.setAfterInitHookFactory("com.xxz.foo.gen.FooAfterInitExchangeHookFactory");
    
    ConstantDescriptor apiVersion = ConstantDescriptor.builder()
        .name("apiVersion")
        .value("1")
        .build();
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
    exchangeDescriptor.setNetwork(NetworkDescriptor.builder().build());
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
        + "          false);\n"
        + "    // Network\n"
        + "    \n"
        + "     // APIs\n"
        + "    this.api1Api = addApi(FooApi1Api.ID, new FooApi1ApiImpl(this, exchangeObserver));\n"
        + "    this.api2Api = addApi(FooApi2Api.ID, new FooApi2ApiImpl(this, exchangeObserver));\n"
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
    exchangeDescriptor.setNetwork(NetworkDescriptor.builder()
      .addToHttpClients(HttpClientDescriptor.builder()
        .name("httpDefault")
        .build())
      .build());
    RateLimitRuleDescriptor exchangeGlobalRateLimit = RateLimitRuleDescriptor.builder()
        .id("exchangeGlobalRateLimit")
        .timeFrame(60000L)
        .maxRequestCount(1000)
        .build();
    exchangeDescriptor.setRateLimits(List.of(exchangeGlobalRateLimit));
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
        + "  private final FooApi3Api api3Api;\n"
        + "  \n"
        + "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
        + "    super(ID,\n"
        + "          VERSION,\n"
        + "          exchangeName,\n"
        + "          properties,\n"
        + "          null,\n"
        + "          true);\n"
        + "    // Network\n"
        + "    createHttpClient(\"httpDefault\",\n"
        + "      null,\n"
        + "      null,\n"
        + "      null);\n"
        + "    \n"
        + "     // APIs\n"
        + "    this.api1Api = addApi(FooApi1Api.ID, new FooApi1ApiImpl(this, exchangeObserver));\n"
        + "    this.api2Api = addApi(FooApi2Api.ID, new FooApi2ApiImpl(this, exchangeObserver));\n"
        + "    this.api3Api = addApi(FooApi3Api.ID, new FooApi3ApiImpl(this, exchangeObserver));\n"
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
    exchangeDescriptor.setNetwork(NetworkDescriptor.builder().build());
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
        + "          false);\n"
        + "    // Network\n"
        + "    \n"
        + "     // APIs\n"
        + "    this.api1Api = addApi(FooApi1Api.ID, new FooApi1ApiImpl(this, exchangeObserver));\n"
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
    RateLimitRuleDescriptor exchangeGlobalRateLimit = RateLimitRuleDescriptor.builder()
        .id("exchangeGlobalRateLimit")
        .timeFrame(60000L)
        .maxRequestCount(1000)
        .build();
    exchangeDescriptor.setRateLimits(List.of(exchangeGlobalRateLimit));
    exchangeDescriptor.setNetwork(NetworkDescriptor.builder()
        .addToHttpClients(HttpClientDescriptor.builder()
            .name("httpDefault")
            .httpRequestInterceptorFactory("com.xyz.foo.gen.FooHttpRequestInterceptorFactory")
            .httpRequestExecutorFactory("com.xyz.foo.gen.FooHttpRequestExecutorFactory")
            .httpRequestTimeout(15000L)
            .build())
        .addToWebsocketClients(WebsocketClientDescriptor.builder()
            .name("wsDefault")
            .websocketFactory("com.xyz.foo.gen.FooWebsocketFactory")
            .websocketUrl("wss://foo.example.com/ws")
            .websocketHookFactory("com.xyz.foo.gen.FooWebsocketHookFactory")
            .build())
        .build());
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
        + "          true);\n"
        + "    // Network\n"
        + "    createHttpClient(\"httpDefault\",\n"
        + "      \"com.xyz.foo.gen.FooHttpRequestInterceptorFactory\",\n"
        + "      \"com.xyz.foo.gen.FooHttpRequestExecutorFactory\",\n"
        + "      15000);\n"
        + "    createWebsocketClient(\"wsDefault\",\n"
        + "      \"wss://foo.example.com/ws\",\n"
        + "      \"com.xyz.foo.gen.FooWebsocketFactory\",\n"
        + "      \"com.xyz.foo.gen.FooWebsocketHookFactory\");\n"
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
    RateLimitRuleDescriptor exchangeGlobalRateLimit = RateLimitRuleDescriptor.builder()
        .id("exchangeGlobalRateLimit")
        .timeFrame(60000L)
        .maxRequestCount(1000)
        .build();
    exchangeDescriptor.setRateLimits(List.of(exchangeGlobalRateLimit));
    exchangeDescriptor.setNetwork(NetworkDescriptor.builder()
        .addToWebsocketClients(WebsocketClientDescriptor.builder()
            .name("wsDefault")
            .websocketFactory("com.xyz.foo.gen.FooWebsocketFactory")
            .websocketUrl("wss://foo.example.com/ws")
            .websocketHookFactory("com.xyz.foo.gen.FooWebsocketHookFactory")
            .build())
        .build());
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
        + "          true);\n"
        + "    // Network\n"
        + "    createWebsocketClient(\"wsDefault\",\n"
        + "      \"wss://foo.example.com/ws\",\n"
        + "      \"com.xyz.foo.gen.FooWebsocketFactory\",\n"
        + "      \"com.xyz.foo.gen.FooWebsocketHookFactory\");\n"
        + "    \n"
        + "     // APIs\n"
        + "    this.api1Api = addApi(FooApi1Api.ID, new FooApi1ApiImpl(this, exchangeObserver));\n"
        + "    this.api2Api = addApi(FooApi2Api.ID, new FooApi2ApiImpl(this, exchangeObserver));\n"
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
    RateLimitRuleDescriptor exchangeGlobalRateLimit = RateLimitRuleDescriptor.builder()
        .timeFrame(60000L)
        .maxRequestCount(1000)
        .build();
    exchangeDescriptor.setRateLimits(List.of(exchangeGlobalRateLimit));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    exchangeGenerator.generate();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateExchangeApiWithDuplicateExchangeRateLimitName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    RateLimitRuleDescriptor exchangeGlobalRateLimit = RateLimitRuleDescriptor.builder()
        .id("exchangeGlobalRateLimit")
        .timeFrame(60000L)
        .maxRequestCount(1000)
        .build();
    exchangeDescriptor.setRateLimits(List.of(exchangeGlobalRateLimit, exchangeGlobalRateLimit.deepClone()));
    ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
    exchangeGenerator.generate();
  }
  
  @Test
  public void testGenerateExchangeApiWithWeightedExchangeRateLimitRule() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Foo");
    exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
    exchangeDescriptor.setDescription("Foo exchange description");
    RateLimitRuleDescriptor exchangeGlobalRateLimit = RateLimitRuleDescriptor.builder()
        .id("exchangeGlobalRateLimit")
        .timeFrame(60000L)
        .maxTotalWeight(1000)
        .build();
    exchangeDescriptor.setRateLimits(List.of(exchangeGlobalRateLimit));
    exchangeDescriptor.setNetwork(NetworkDescriptor.builder()
      .addToHttpClients(HttpClientDescriptor.builder()
        .name("httpDefault")
        .httpRequestInterceptorFactory("com.xyz.foo.gen.FooHttpRequestInterceptorFactory")
        .httpRequestExecutorFactory("com.xyz.foo.gen.FooHttpRequestExecutorFactory")
        .build())
      .build());
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
        + "          true);\n"
        + "    // Network\n"
        + "    createHttpClient(\"httpDefault\",\n"
        + "      \"com.xyz.foo.gen.FooHttpRequestInterceptorFactory\",\n"
        + "      \"com.xyz.foo.gen.FooHttpRequestExecutorFactory\",\n"
        + "      null);\n"
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
        + "import org.jxapi.util.EncodingUtil;\n"
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
        + "          false);\n"
        + "    // Network\n"
        + "    createHttpClient(\"httpDefault\",\n"
        + "      \"org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory\",\n"
        + "      null,\n"
        + "      null);\n"
        + "    createWebsocketClient(\"wsDefault\",\n"
        + "      EncodingUtil.substituteArguments(\"${config.server.baseWebsocketUrl}\", ),\n"
        + "      null,\n"
        + "      \"org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory\");\n"
        + "    \n"
        + "     // APIs\n"
        + "    this.v1Api = addApi(ConflictV1Api.ID, new ConflictV1ApiImpl(this, exchangeObserver));\n"
        + "    this.aApi = addApi(ConflictaApi.ID, new ConflictaApiImpl(this, exchangeObserver));\n"
        + "    this.AApi = addApi(ConflictAApi.ID, new ConflictAApiImpl(this, exchangeObserver));\n"
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
