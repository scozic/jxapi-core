package org.jxapi.generator.properties.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.generator.java.exchange.api.demo.EndpointDemoGenUtil;

/**
 * Unit test for {@link ExchangeDemoPropertiesFileGenerator}
 */
public class ExchangeDemoPropertiesGeneratorTest {
  
  private Path tmpFolder;
  
  @After
  public void tearDown() throws IOException {
    if (tmpFolder != null) {
      JavaCodeGenUtil.deletePath(tmpFolder);
      tmpFolder = null;
    }
  }
  
  @Test
  public void testGenerateDemoPropertiesTemplateFile() throws Exception {
    tmpFolder = ClassesGeneratorTestUtil.generateTmpDir();
    Path srcTestResourcesFolder = Paths.get(".", "src", "test", "resources");
    Path exchangeDescriptorFile = srcTestResourcesFolder.resolve("demoExchange.json");
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(exchangeDescriptorFile);
    String fileName = "demo-DemoExchange.properties.dist";
    Path actualFilePath = tmpFolder.resolve(fileName);
    List<ConfigPropertyDescriptor> configProperties = new ArrayList<>();
    configProperties.addAll(exchangeDescriptor.getProperties());
    List<ConfigPropertyDescriptor> demoProperties = new ArrayList<>();
    demoProperties.addAll(EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor));
    new ExchangeDemoPropertiesFileGenerator(exchangeDescriptor.getId(), 
                        configProperties,
                        demoProperties)
      .writeJavaFile(actualFilePath);
    String expected = Files.readString(srcTestResourcesFolder.resolve(fileName));
    String actual = Files.readString(actualFilePath);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGenerateDemoPropertiesTemplateFile_EmptyConfigProperties() {
    List<ConfigPropertyDescriptor> configProperties = List.of();
    List<ConfigPropertyDescriptor> demoProperties = List.of();
    ExchangeDemoPropertiesFileGenerator generator = new ExchangeDemoPropertiesFileGenerator("DemoExchange", configProperties, demoProperties);
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
    Assert.assertEquals("", ExchangeDemoPropertiesFileGenerator.generatePropertiesFileComment(null));
    String comment = "This is a test comment.\n" + "It should be indented with '# ' prefix.";
    String expected = "# This is a test comment.\n" + "# It should be indented with '# ' prefix.\n";
    String actual = ExchangeDemoPropertiesFileGenerator.generatePropertiesFileComment(comment);
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGettersAndSetters() {
    ExchangeDemoPropertiesFileGenerator generator = new ExchangeDemoPropertiesFileGenerator();
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
  }
  
  @Test
  public void testGeneratePropertiesFileComment_NoDescriptionInProperties() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProp", Type.INT, null, 159);
    ConfigPropertyDescriptor group = ConfigPropertyDescriptor.createGroup("myGroup", null, List.of(property));
    List<ConfigPropertyDescriptor> properties = List.of(group);
    List<ConfigPropertyDescriptor> demoProperties = List.of();
    ExchangeDemoPropertiesFileGenerator generator = new ExchangeDemoPropertiesFileGenerator("TestExchange", properties, demoProperties);
    Assert.assertEquals("# Demo configuration properties file for TestExchange exchange.\n"
        + "# You should create a copy of this file without the '.dist' extension and add that .properties file\n"
        + "# to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
        + "# The resulting file will be read as default configuration file for demo snippets.\n"
        + "# Uncomment and set following properties and adjust their values per your needs.\n"
        + "\n"
        + "# TestExchange specific configuration properties\n"
        + "\n"
        + "# myProp=159\n"
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
