package com.scz.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import com.scz.jxapi.generator.java.exchange.constants.ConstantsInterfaceGenerator;
import com.scz.jxapi.generator.java.exchange.constants.PropertiesInterfaceGenerator;
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
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor the exchange descriptor to generate classes for
	 */
	public ExchangeClassesGenerator(ExchangeDescriptor exchangeDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		// Generate exchange interface class
		ExchangeInterfaceGenerator exchangeInterfaceGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor);
		exchangeInterfaceGenerator.writeJavaFile(outputFolder);
		
		// Generate exchange interface implementation class
		new ExchangeInterfaceImplementationGenerator(exchangeDescriptor).writeJavaFile(outputFolder);
		
		// Generate classes for each exchange API group
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			new ExchangeApiClassesGenerator(exchangeDescriptor, api).generateClasses(outputFolder);
		}
		
		// Generate constants interface
		List<Constant> constants = exchangeDescriptor.getConstants();
		if (constants != null) {
			ConstantsInterfaceGenerator cgen = new ConstantsInterfaceGenerator(
					ExchangeJavaGenUtil.getExchangeConstantsInterfaceName(exchangeDescriptor), 
					constants); 
			cgen.setDescription("Constants used in {@link " + exchangeInterfaceGenerator.getName() + "} API wrapper");
			cgen.writeJavaFile(outputFolder);
		}
		
		// Generate properties interface
		List<ConfigProperty> properties = exchangeDescriptor.getProperties();
		if (properties != null) {
			PropertiesInterfaceGenerator pgen = new PropertiesInterfaceGenerator(
					ExchangeJavaGenUtil.getExchangePropertiesInterfaceName(exchangeDescriptor), 
					exchangeDescriptor.getName(), 
					properties);
			pgen.writeJavaFile(outputFolder);
		}
	}
}
