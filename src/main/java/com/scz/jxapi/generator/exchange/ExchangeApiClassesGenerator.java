package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;

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
public class ExchangeApiClassesGenerator {

	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	public ExchangeApiClassesGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
	}
	
	public void generateClasses(Path outputFolder) throws IOException {
		new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
		new ExchangeApiInterfaceImplementationGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
		for (RestEndpointDescriptor restEndpointDescriptor: exchangeApiDescriptor.getRestEndpoints()) {
			new RestEndpointClassesGenerator(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor).generateClasses(outputFolder);
		}
		for (WebsocketEndpointDescriptor websocketEndpointDescriptor: exchangeApiDescriptor.getWebsocketEndpoints()) {
			new WebsocketEndpointClassesGenerator(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor).generateClasses(outputFolder);
		}
	}
}
