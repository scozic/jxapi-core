package com.scz.jxapi.generator.properties.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

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
		List<ConfigProperty> configProperties = new ArrayList<>();
		configProperties.addAll(exchangeDescriptor.getProperties());
		new ExchangeDemoPropertiesFileGenerator(exchangeDescriptor.getName(), 
												configProperties)
			.writeJavaFile(actualFilePath);
		String expected = Files.readString(srcTestResourcesFolder.resolve(fileName));
		String actual = Files.readString(actualFilePath);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGenerateDemoPropertiesTemplateFile_EmptyConfigProperties() {
		List<ConfigProperty> configProperties = List.of();
		String content = new ExchangeDemoPropertiesFileGenerator("DemoExchange", configProperties).generate();
		Assert.assertEquals("# Demo configuration properties file for DemoExchange exchange.\n"
				+ "# You should create a copy of this file without the '.dist' extension and add that .properties file\n"
				+ "# to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
				+ "# The resulting file will be read as default configuration file for demo snippets.\n"
				+ "# Uncomment and set following properties and adjust their values per your needs.\n"
				+ "\n"
				+ "\n"
				+ "# Common configuration properties\n"
				+ "\n"
				+ "# The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)\n"
				+ "# jxapi.httpRequestTimeout=\n"
				+ "\n"
				+ "# Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum com.scz.jxapi.netutils.rest.ratelimits.RequestThrottlingMode\n"
				+ "# jxapi.requestThrottlingMode=\n"
				+ "\n"
				+ "# Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.\n"
				+ "# jxapi.maxRequestThrottleDelay=\n"
				+ "\n"
				+ "\n"
				+ "# Demo REST/WEBSOCKET snippets configuration properties\n"
				+ "\n"
				+ "# The duration in ms of the subscription in websocket endpoint demo classes\n"
				+ "# jxapi.demo.ws.subscriptionDuration=30000\n"
				+ "\n"
				+ "# Delay in ms before exiting program after unsubscribing in websocked endpoint demo classes.\n"
				+ "# jxapi.demo.ws.delayBeforeExitAfterUnsubscription=1000\n", content);
	}
}
