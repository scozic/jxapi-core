package com.scz.jxapi.exchanges.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.exchange.ExchangeGeneratorMain;

/**
 * Static helper methods for triggering generation of DemoExchange java wrapper and demos code.
 */
public class DemoExchangeGeneratorMain {
	
	public static void generateDemoExchange() throws IOException {
		generateDemoExchange(Paths.get(".", "src", "test"));
	}

	public static void generateDemoExchange(Path srcTestFolder) throws IOException {
		Path exchangeDescriptorFile = srcTestFolder.resolve(Paths.get("resources", "demoExchange.json"));
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(exchangeDescriptorFile);
		Path srcTestJavaFolder = srcTestFolder.resolve(Paths.get("java"));
		ExchangeGeneratorMain.generateExchangeWrapper(exchangeDescriptor, srcTestJavaFolder);
		ExchangeGeneratorMain.generateExchangeWrapperDemos(exchangeDescriptor, srcTestJavaFolder);
	}

}
