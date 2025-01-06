package com.scz.jxapi.generator.properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeGeneratorMain;

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
		ExchangeGeneratorMain.generateDemoPropertiesFileTemplate(exchangeDescriptor, tmpFolder);
		String expectedFileName = "demo-DemoExchange.properties.dist";
		String expected = Files.readString(srcTestResourcesFolder.resolve(expectedFileName));
		String actual = Files.readString(tmpFolder.resolve("demo-DemoExchange.properties.dist"));
		Assert.assertEquals(expected, actual);
	}

}
