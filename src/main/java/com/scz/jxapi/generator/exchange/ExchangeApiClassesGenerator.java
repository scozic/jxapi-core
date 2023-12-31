package com.scz.jxapi.generator.exchange;

import java.nio.file.Path;

public class ExchangeApiClassesGenerator {

	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	public ExchangeApiClassesGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
	}
	
	public void generate(Path output) {
		// TODO
	}
}
