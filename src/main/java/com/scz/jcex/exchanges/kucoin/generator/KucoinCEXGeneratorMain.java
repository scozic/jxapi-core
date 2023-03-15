package com.scz.jcex.exchanges.kucoin.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.exchanges.binance.generator.BinanceCEXGeneratorMain;
import com.scz.jcex.generator.JavaCodeGenerationUtil;
import com.scz.jcex.generator.exchange.ExchangeDescriptor;
import com.scz.jcex.generator.exchange.ExchangeDescriptorParser;
import com.scz.jcex.generator.exchange.ExchangeJavaWrapperGeneratorUtil;

public class KucoinCEXGeneratorMain {
	
	private static final Logger log = LoggerFactory.getLogger(KucoinCEXGeneratorMain.class);
	
	public static void main(String[] args) {
		try {
			ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "main", "resources", "KucoinCEXDescriptor.json"));
			Path outputSrcMainFolder = Paths.get(".", "src", "main", "java");
			Path packagePath = Paths.get("com", "scz", "jcex", "exchanges", "kucoin", "gen");
			Path genPackagesFolder = outputSrcMainFolder.resolve(packagePath);
			JavaCodeGenerationUtil.deletePath(genPackagesFolder);
			ExchangeJavaWrapperGeneratorUtil.generateCEX(exchangeDescriptor, outputSrcMainFolder);
			
			Path outputSrcTestFolder = Paths.get(".", "src", "test", "java");
			Path genTestPackagesFolder = outputSrcTestFolder.resolve(packagePath);
			JavaCodeGenerationUtil.deletePath(genTestPackagesFolder);
			
			ExchangeJavaWrapperGeneratorUtil.generateCEXDemos(exchangeDescriptor, outputSrcTestFolder);
			log.info("Done generating KucoinCEXDescriptor java code in:" + outputSrcMainFolder);
			System.exit(0);
		} catch (Throwable t) {
			log.error("Error in " + BinanceCEXGeneratorMain.class.getName() + " main", t);
			System.exit(-1);
		}
	}

}
