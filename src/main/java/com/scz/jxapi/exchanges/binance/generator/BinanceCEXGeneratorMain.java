package com.scz.jxapi.exchanges.binance.generator;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.generator.exchange.ExchangeGeneratorMain;

public class BinanceCEXGeneratorMain {
	
	private static final Logger log = LoggerFactory.getLogger(BinanceCEXGeneratorMain.class);

	public static void main(String[] args) {
		try {
			ExchangeGeneratorMain.generateExchangeApi(Paths.get(".", "src", "main", "resources", "BinanceCEXDescriptor.json"));
			System.exit(0);
		} catch (Throwable t) {
			log.error("Error in " + BinanceCEXGeneratorMain.class.getName() + " main", t);
			System.exit(-1);
		}
	}
}
