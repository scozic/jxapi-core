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
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link ExchangeDemoPropertiesFileGenerator}
 */
public class ExchangeDemoPropertiesGeneratorTest {
	
	private Path tmpFolder;
	
	@After
	public void tearDown() throws IOException {
		if (tmpFolder != null) {
			JavaCodeGenerationUtil.deletePath(tmpFolder);
			tmpFolder = null;
		}
	}
	
	@Test
	public void testGenerateDemoPropertiesTemplateFile() throws Exception {
		tmpFolder = ClassesGeneratorTestUtil.generateTmpDir();
		Path srcTestResourcesFolder = Paths.get(".", "src", "test", "resources");
		Path exchangeDescriptorFile = srcTestResourcesFolder.resolve("demoExchange.json");
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(exchangeDescriptorFile);
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

}
