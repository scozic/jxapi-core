package com.scz.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitManager;

/**
 * Generates all classes for an {@link ExchangeDescriptor} defined in an
 * exchange descriptor file. This covers:
 * <ul>
 * <li>Java interface for the exchange with a getter method to retrieve each
 * exchange API interface.
 * <li>Implementation of that exchange interface with eventual
 * {@link RateLimitManager} to enforce rate limit rules if any.
 * <li>Java interface for each API listed in {@link ExchangeDescriptor} (see
 * {@link ExchangeApiDescriptor} and generator for its classes
 * {@link ExchangeApiClassesGenerator}), with all classes for every
 * REST/Websocket endpoint.
 * </ul>
 */
public class ExchangeClassesGenerator implements ClassesGenerator {

	private final ExchangeDescriptor exchangeDescriptor;
	
	public ExchangeClassesGenerator(ExchangeDescriptor exchangeDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		new ExchangeInterfaceGenerator(exchangeDescriptor).writeJavaFile(outputFolder);
		new ExchangeInterfaceImplementationGenerator(exchangeDescriptor).writeJavaFile(outputFolder);
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			new ExchangeApiClassesGenerator(exchangeDescriptor, api).generateClasses(outputFolder);
		}
	}
}
