package com.scz.jxapi.generator.exchange.api;

import java.io.IOException;
import java.nio.file.Path;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.exchange.ClassesGenerator;
import com.scz.jxapi.generator.exchange.api.rest.RestEndpointClassesGenerator;
import com.scz.jxapi.generator.exchange.api.ws.WebsocketEndpointClassesGenerator;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitManager;

/**
 * Generates all classes for an {@link ExchangeApiDescriptor} defined in an
 * exchange descriptor file. This covers:
 * <ul>
 * <li>Java interface for the API with a function for every REST endpoint call,
 * and subscribe and unsubscribe to websocket stream methods for every websocket
 * endpoint.
 * <li>Java class implementating that API interface, with eventual {@link RateLimitManager} to enforce rate limit rules if any.
 * <li>POJOs for every REST and Websocket endpoint request and response
 * <li>JSON deserializers for those POJOs
 * <li>JSON serializers for those POJOs
 * </ul>
 * 
 * @see ExchangeApiDescriptor
 */
public class ExchangeApiClassesGenerator implements ClassesGenerator {

	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	public ExchangeApiClassesGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
		new ExchangeApiInterfaceImplementationGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restEndpointDescriptor: exchangeApiDescriptor.getRestEndpoints()) {
				new RestEndpointClassesGenerator(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor).generateClasses(outputFolder);
			}
		}
		
		if (exchangeApiDescriptor.getWebsocketEndpoints() != null) {
			for (WebsocketEndpointDescriptor websocketEndpointDescriptor: exchangeApiDescriptor.getWebsocketEndpoints()) {
				new WebsocketEndpointClassesGenerator(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor).generateClasses(outputFolder);
			}
		}
	}
}
