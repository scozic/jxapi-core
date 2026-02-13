package org.jxapi.generator.properties.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.api.demo.EndpointDemoGenUtil;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ExchangeDemoPropertiesTemplateGenerator}
 */
public class ExchangeDemoPropertiesTemplateGeneratorTest {
  
  private Path tmpFolder;
  
  @After
  public void tearDown() throws IOException {
    if (tmpFolder != null) {
      JavaCodeGenUtil.deletePath(tmpFolder);
      tmpFolder = null;
    }
  }
  
  @Test
  public void testGenerateDemoExchangePropertiesTemplateFile() throws Exception {
    tmpFolder = ClassesGeneratorTestUtil.generateTmpDir();
    Path srcTestResourcesFolder = Paths.get(".", "src", "test", "resources");
    Path exchangeDescriptorFile = srcTestResourcesFolder.resolve("demoExchange.json");
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(exchangeDescriptorFile);
    String fileName = "demo-DemoExchange.properties.dist";
    Path actualFilePath = tmpFolder.resolve(fileName);
    String expected = Files.readString(srcTestResourcesFolder.resolve(fileName));
    ExchangeDemoPropertiesTemplateGenerator gen = doTestGenerateDemoPropertiesFileForExchange(exchangeDescriptor, expected);
    gen.writeJavaFile(actualFilePath);
    String actual = Files.readString(actualFilePath);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGenerateConflictExchangePropertiesTemplateFile() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(Paths.get(".", "src", "test", "resources", "exchangeWithEndpointWithConflictingPropertyNames.yaml"));
    doTestGenerateDemoPropertiesFileForExchange(exchangeDescriptor, 
          "# Demo configuration properties file for conflict exchange.\n"
          + "# You should create a copy of this file without the '.dist' extension and add that .properties file\n"
          + "# to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
          + "# The resulting file will be read as default configuration file for demo snippets.\n"
          + "# Uncomment and set following properties and adjust their values per your needs.\n"
          + "\n"
          + "# conflict specific configuration properties\n"
          + "\n"
          + "# p1 property.\n"
          + "# p1=12340\n"
          + "\n"
          + "# P1 property (note the uppercase 'P'). Base URL for websocket endpoints of the Conflict Exchange API\n"
          + "# P1=wss://ws.api.example.com/conflict\n"
          + "\n"
          + "\n"
          + "# Common configuration properties\n"
          + "\n"
          + "# The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)\n"
          + "# jxapi.httpRequestTimeout=\n"
          + "\n"
          + "# Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode\n"
          + "# jxapi.requestThrottlingMode=\n"
          + "\n"
          + "# Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.\n"
          + "# jxapi.maxRequestThrottleDelay=\n"
          + "\n"
          + "\n"
          + "# Demo REST/WEBSOCKET snippets common configuration properties\n"
          + "\n"
          + "# The duration in ms of the subscription in websocket endpoint demo classes\n"
          + "# jxapi.demo.ws.subscriptionDuration=30000\n"
          + "\n"
          + "# Delay in ms before exiting program after unsubscribing in websocked endpoint demo classes.\n"
          + "# jxapi.demo.ws.delayBeforeExitAfterUnsubscription=1000\n"
          + "\n"
          + "\n"
          + "# Demo REST/WEBSOCKET specific configuration properties\n"
          + "\n"
          + "# # Configuration properties for v1 API group endpoints demo snippets\n"
          + "# ## Configuration properties for REST endpoints demo snippets of v1 API group\n"
          + "# ### Configuration properties for REST myRestEndpoint endpoint of v1 API group\n"
          + "# Demo configuration property for myRestEndpoint.request field as raw JSON string value.\n"
          + "# demo.v1.rest.myRestEndpoint.request=\n"
          + "\n"
          + "# #### Demo configuration properties for myRestEndpoint.request field object instance.\n"
          + "# Demo configuration property for request.p1 field.<p>\n"
          + "# p1 parameter\n"
          + "# demo.v1.rest.myRestEndpoint.request.p1=12340\n"
          + "\n"
          + "# Demo configuration property for request.P1 field.<p>\n"
          + "# P1 parameter (note the uppercase 'P'). Its  value should be one of the values of the {@link org.jxapi.exchanges.conflict.gen.ConflictConstants.C1___} constant group\n"
          + "# demo.v1.rest.myRestEndpoint.request.P1=barVal\n"
          + "\n"
          + "# Demo configuration property for request.p1_ field.<p>\n"
          + "# p1_ parameter (note the underscore)\n"
          + "# demo.v1.rest.myRestEndpoint.request.p1_=1234567890\n"
          + "\n"
          + "# Demo configuration property for request.P1_ field as raw JSON string value.<p>\n"
          + "# P1_ parameter (note the uppercase 'P' and underscore)\n"
          + "# demo.v1.rest.myRestEndpoint.request.P1_=\n"
          + "\n"
          + "# ##### Demo configuration properties for request.P1_ field object instance.<p>\n"
          + "# P1_ parameter (note the uppercase 'P' and underscore)\n"
          + "# Demo configuration property for P1_.subParam field.<p>\n"
          + "# subParam parameter\n"
          + "# demo.v1.rest.myRestEndpoint.request.P1_.subParam=12.35\n"
          + "\n"
          + "# ### Configuration properties for REST a endpoint of v1 API group\n"
          + "# Demo configuration property for a.request field.\n"
          + "# demo.v1.rest.a.request=\n"
          + "\n"
          + "# ### Configuration properties for REST A endpoint of v1 API group\n"
          + "# Demo configuration property for A.request field.\n"
          + "# demo.v1.rest.A.request=\n"
          + "\n"
          + "# ## Configuration properties for websocket endpoints demo snippets of v1 API group\n"
          + "# ### Configuration properties for websocket myWsEndpoint endpoint of v1 API group\n"
          + "# Demo configuration property for myWsEndpoint.request field as raw JSON string value.\n"
          + "# demo.v1.ws.myWsEndpoint.request=\n"
          + "\n"
          + "# #### Demo configuration properties for myWsEndpoint.request field object instance.\n"
          + "# Demo configuration property for request.p1 field.<p>\n"
          + "# p1 parameter\n"
          + "# demo.v1.ws.myWsEndpoint.request.p1=12340\n"
          + "\n"
          + "# Demo configuration property for request.P1 field.<p>\n"
          + "# P1 parameter (note the uppercase 'P'). Its  value should be one of the values of the {@link org.jxapi.exchanges.conflict.gen.ConflictConstants.C1___} constant group\n"
          + "# demo.v1.ws.myWsEndpoint.request.P1=barVal\n"
          + "\n"
          + "# Demo configuration property for request.p1_ field.<p>\n"
          + "# p1_ parameter (note the underscore)\n"
          + "# demo.v1.ws.myWsEndpoint.request.p1_=1234567890\n"
          + "\n"
          + "# Demo configuration property for request.P1_ field as raw JSON string value.<p>\n"
          + "# P1_ parameter (note the uppercase 'P' and underscore)\n"
          + "# demo.v1.ws.myWsEndpoint.request.P1_=\n"
          + "\n"
          + "# ##### Demo configuration properties for request.P1_ field object instance.<p>\n"
          + "# P1_ parameter (note the uppercase 'P' and underscore)\n"
          + "# Demo configuration property for P1_.subParam field.<p>\n"
          + "# subParam parameter\n"
          + "# demo.v1.ws.myWsEndpoint.request.P1_.subParam=12.35\n"
          + "\n"
          + "# ### Configuration properties for websocket a endpoint of v1 API group\n"
          + "# Demo configuration property for a.request field.\n"
          + "# demo.v1.ws.a.request=\n"
          + "\n"
          + "# ### Configuration properties for websocket A endpoint of v1 API group\n"
          + "# Demo configuration property for A.request field.\n"
          + "# demo.v1.ws.A.request=\n"
          + "\n"
          + "");
  }
  
  private ExchangeDemoPropertiesTemplateGenerator doTestGenerateDemoPropertiesFileForExchange(
      ExchangeDescriptor exchangeDescriptor,
      String expectedContent) {
    PlaceHolderResolver valuesPlaceHolderResolver = PlaceHolderResolver.create(ExchangeGenUtil.getValuesReplacements(exchangeDescriptor));
    PlaceHolderResolver descriptionPlaceHolderResolver = PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor));
    List<ConfigPropertyDescriptor> configProperties = CollectionUtil.emptyIfNull(exchangeDescriptor.getProperties());
    List<ConfigPropertyDescriptor> demoProperties = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    ExchangeDemoPropertiesTemplateGenerator generator = new ExchangeDemoPropertiesTemplateGenerator(
        exchangeDescriptor.getId(),
        configProperties, 
        demoProperties, 
        valuesPlaceHolderResolver, 
        descriptionPlaceHolderResolver);
    String content = generator.generate();
    Assert.assertEquals(expectedContent, content);
    return generator;
  }

  @Test
  public void testGenerateDemoPropertiesTemplateFile_EmptyConfigProperties() {
    List<ConfigPropertyDescriptor> configProperties = List.of();
    List<ConfigPropertyDescriptor> demoProperties = List.of();
    ExchangeDemoPropertiesTemplateGenerator generator = new ExchangeDemoPropertiesTemplateGenerator("DemoExchange", configProperties, demoProperties, null, null);
    String content = generator.generate();
    Assert.assertEquals("# Demo configuration properties file for DemoExchange exchange.\n"
        + "# You should create a copy of this file without the '.dist' extension and add that .properties file\n"
        + "# to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
        + "# The resulting file will be read as default configuration file for demo snippets.\n"
        + "# Uncomment and set following properties and adjust their values per your needs.\n"
        + "\n"
        + "# Common configuration properties\n"
        + "\n"
        + "# The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)\n"
        + "# jxapi.httpRequestTimeout=\n"
        + "\n"
        + "# Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode\n"
        + "# jxapi.requestThrottlingMode=\n"
        + "\n"
        + "# Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.\n"
        + "# jxapi.maxRequestThrottleDelay=\n"
        + "\n"
        + "\n"
        + "# Demo REST/WEBSOCKET snippets common configuration properties\n"
        + "\n"
        + "# The duration in ms of the subscription in websocket endpoint demo classes\n"
        + "# jxapi.demo.ws.subscriptionDuration=30000\n"
        + "\n"
        + "# Delay in ms before exiting program after unsubscribing in websocked endpoint demo classes.\n"
        + "# jxapi.demo.ws.delayBeforeExitAfterUnsubscription=1000\n"
        + "\n"
        + "", content);
  }
  
  @Test
  public void testGeneratePropertiesFileComment() {
    Assert.assertEquals("", ExchangeDemoPropertiesTemplateGenerator.generatePropertiesFileComment(null));
    String comment = "This is a test comment.\n" + "It should be indented with '# ' prefix.";
    String expected = "# This is a test comment.\n" + "# It should be indented with '# ' prefix.\n";
    String actual = ExchangeDemoPropertiesTemplateGenerator.generatePropertiesFileComment(comment);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGettersAndSetters() {
    ExchangeDemoPropertiesTemplateGenerator generator = new ExchangeDemoPropertiesTemplateGenerator();
    Assert.assertNull(generator.getExchangeId());

    String exchangeId = "TestExchange";
    generator.setExchangeId(exchangeId);
    Assert.assertEquals(exchangeId, generator.getExchangeId());

    List<ConfigPropertyDescriptor> configProperties = new ArrayList<>();
    generator.setExchangeProperties(configProperties);
    Assert.assertSame(configProperties, generator.getExchangeProperties());

    List<ConfigPropertyDescriptor> demoProperties = new ArrayList<>();
    generator.setDemoProperties(demoProperties);
    Assert.assertSame(demoProperties, generator.getDemoProperties());
    
    PlaceHolderResolver constantsPlaceHolderResolver = PlaceHolderResolver.create(Map.of("key", "value"));
    generator.setConstantsResolver(constantsPlaceHolderResolver);
    Assert.assertSame(constantsPlaceHolderResolver, generator.getConstantsResolver());
    
    PlaceHolderResolver descriptionPlaceHolderResolver = PlaceHolderResolver.create(Map.of("keyDescr", "valueDescr"));
    generator.setDescriptionResolver(descriptionPlaceHolderResolver);
    Assert.assertSame(descriptionPlaceHolderResolver, generator.getDescriptionResolver());
  }
  
  @Test
  public void testGeneratePropertiesFileComment_NoDescriptionInProperties() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.INT.toString())
        .defaultValue(159).build();

    ConfigPropertyDescriptor group = ConfigPropertyDescriptor.builder()
        .name("myGroup")
        .addToProperties(property)
        .build();

    List<ConfigPropertyDescriptor> properties = List.of(group);
    List<ConfigPropertyDescriptor> demoProperties = List.of();
    ExchangeDemoPropertiesTemplateGenerator generator = new ExchangeDemoPropertiesTemplateGenerator("TestExchange", properties, demoProperties, null, null);
    Assert.assertEquals(
        "# Demo configuration properties file for TestExchange exchange.\n"
        + "# You should create a copy of this file without the '.dist' extension and add that .properties file\n"
        + "# to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
        + "# The resulting file will be read as default configuration file for demo snippets.\n"
        + "# Uncomment and set following properties and adjust their values per your needs.\n"
        + "\n"
        + "# TestExchange specific configuration properties\n"
        + "\n"
        + "# myGroup.myProp=159\n"
        + "\n"
        + "\n"
        + "# Common configuration properties\n"
        + "\n"
        + "# The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)\n"
        + "# jxapi.httpRequestTimeout=\n"
        + "\n"
        + "# Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode\n"
        + "# jxapi.requestThrottlingMode=\n"
        + "\n"
        + "# Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.\n"
        + "# jxapi.maxRequestThrottleDelay=\n"
        + "\n"
        + "\n"
        + "# Demo REST/WEBSOCKET snippets common configuration properties\n"
        + "\n"
        + "# The duration in ms of the subscription in websocket endpoint demo classes\n"
        + "# jxapi.demo.ws.subscriptionDuration=30000\n"
        + "\n"
        + "# Delay in ms before exiting program after unsubscribing in websocked endpoint demo classes.\n"
        + "# jxapi.demo.ws.delayBeforeExitAfterUnsubscription=1000\n"
        + "\n"
        + "",
        generator.generate());
  }
  
}
