package com.scz.jcex.exchanges.kucoin.generator;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.generator.exchange.ExchangeGeneratorMain;

public class KucoinCEXGeneratorMain {
	
	private static final Logger log = LoggerFactory.getLogger(KucoinCEXGeneratorMain.class);
	
	public static void main(String[] args) {
		try {
			ExchangeGeneratorMain.generateExchangeApi(Paths.get(".", "src", "main", "resources", "KucoinCEXDescriptor.json"));
			System.exit(0);
		} catch (Throwable t) {
			log.error("Error in " + KucoinCEXGeneratorMain.class.getName() + " main", t);
			System.exit(-1);
		}
	}

}
