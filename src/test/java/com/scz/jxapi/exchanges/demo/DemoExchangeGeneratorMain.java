package com.scz.jxapi.exchanges.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeGeneratorMain;

/**
 * Static helper methods for triggering generation of DemoExchange java wrapper and demos code.
 */
public class DemoExchangeGeneratorMain {
	
	private static Logger log = LoggerFactory.getLogger(DemoExchangeGeneratorMain.class);
	
	public static void generateDemoExchange() throws IOException {
		generateDemoExchange(Paths.get(".", "src", "test"));
	}

	public static void generateDemoExchange(Path srcTestFolder) throws IOException {
		log.info("Generating demo wrapper in current project");
		Path srcTestResourcesFolder = srcTestFolder.resolve(Paths.get("resources"));
		Path exchangeDescriptorFile = srcTestResourcesFolder.resolve(Paths.get("demoExchange.json"));
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(exchangeDescriptorFile);
		Path srcTestJavaFolder = srcTestFolder.resolve(Paths.get("java"));
		Path mainPackagePath = Paths.get(StringUtils.replace(exchangeDescriptor.getBasePackage(), ".", "/"));
		Path genMainPackagesFolder = srcTestJavaFolder.resolve(mainPackagePath);
		JavaCodeGenerationUtil.deletePath(genMainPackagesFolder);
		ExchangeGeneratorMain.generateExchangeWrapper(exchangeDescriptor, srcTestJavaFolder);
		ExchangeGeneratorMain.generateExchangeWrapperDemos(exchangeDescriptor, srcTestJavaFolder);
		ExchangeGeneratorMain.generateDemoPropertiesFileTemplate(exchangeDescriptor, srcTestResourcesFolder);
		ExchangeGeneratorMain.generateExchangeWrapperReadme(exchangeDescriptor, Paths.get("."), "./doc/javadoc/", "./src/main/java/");
		log.info("Generating demo wrapper in current project:DONE");
	}

	public static void main(String[] args) {
		try {
			generateDemoExchange();
		} catch (Throwable t) {
			log.error("Error in " + DemoExchangeGeneratorMain.class.getName() + " main", t);
		}
	}
}
