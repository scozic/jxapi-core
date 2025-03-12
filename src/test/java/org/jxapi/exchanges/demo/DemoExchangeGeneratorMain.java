package org.jxapi.exchanges.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGeneratorMain;

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
		ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(exchangeDescriptorFile);
		Path srcTestJavaFolder = srcTestFolder.resolve(Paths.get("java"));
		Path mainPackagePath = Paths.get(StringUtils.replace(exchangeDescriptor.getBasePackage(), ".", "/"));
		Path genMainPackagesFolder = srcTestJavaFolder.resolve(mainPackagePath);
		JavaCodeGenUtil.deletePath(genMainPackagesFolder);
		ExchangeGeneratorMain.generateExchangeWrapper(exchangeDescriptor, srcTestJavaFolder);
		ExchangeGeneratorMain.generateExchangeWrapperDemos(exchangeDescriptor, srcTestJavaFolder);
		ExchangeGeneratorMain.generateDemoPropertiesFileTemplate(exchangeDescriptor, srcTestResourcesFolder);
		ExchangeGeneratorMain.generateExchangeWrapperReadme(exchangeDescriptor, Paths.get("."), "./doc/javadoc/", ".");
		log.info("Generating demo wrapper in current project:DONE");
	}

	public static void main(String[] args) {
		try {
			generateDemoExchange();
			System.exit(0);
		} catch (Throwable t) {
			log.error("Error in " + DemoExchangeGeneratorMain.class.getName() + " main", t);
			System.exit(-1);
		}
	}
}
