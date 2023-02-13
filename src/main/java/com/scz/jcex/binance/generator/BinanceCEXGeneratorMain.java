package com.scz.jcex.binance.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.generator.JavaCodeGenerationUtil;
import com.scz.jcex.generator.exchange.ExchangeDescriptor;
import com.scz.jcex.generator.exchange.ExchangeDescriptorParser;
import com.scz.jcex.generator.exchange.ExchangeJavaWrapperGeneratorUtil;

public class BinanceCEXGeneratorMain {
	
	private static final Logger log = LoggerFactory.getLogger(BinanceCEXGeneratorMain.class);

	public static void main(String[] args) {
		try {
			ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "main", "resources", "BinanceCEXDescriptor.json"));
			Path outputFolder = Paths.get(".", "src", "main", "java");
			Path genPackagesFolder = outputFolder.resolve(Paths.get("com", "scz", "jcex", "binance", "gen"));
			JavaCodeGenerationUtil.deletePath(genPackagesFolder);
			ExchangeJavaWrapperGeneratorUtil.generateCEX(exchangeDescriptor, outputFolder);
			log.info("Done generating BinanceCEXDescriptor java code in:" + outputFolder);
		} catch (Throwable t) {
			log.error("Error in " + BinanceCEXGeneratorMain.class.getName() + " main", t);
		}
	}
}
