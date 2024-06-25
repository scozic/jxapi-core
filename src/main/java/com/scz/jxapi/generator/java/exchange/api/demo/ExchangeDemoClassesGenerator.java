package com.scz.jxapi.generator.java.exchange.api.demo;

import java.io.IOException;
import java.nio.file.Path;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;

public class ExchangeDemoClassesGenerator implements ClassesGenerator {
	
	private ExchangeDescriptor exchangeDescriptor;

	public ExchangeDemoClassesGenerator(ExchangeDescriptor exchangeDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
	}

	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {	
			if (api.getRestEndpoints() != null) {
				for (RestEndpointDescriptor restApi: api.getRestEndpoints()) {
					RestEndpointDemoGenerator restEndpointDemoGenerator = new RestEndpointDemoGenerator(exchangeDescriptor, api, restApi);
					restEndpointDemoGenerator.writeJavaFile(outputFolder);
				}
			}
			
			if (api.getWebsocketEndpoints() != null) {
				for (WebsocketEndpointDescriptor websocketApi: api.getWebsocketEndpoints()) {
					WebsocketEndpointDemoGenerator websocketEndpointDemoGenerator = new WebsocketEndpointDemoGenerator(exchangeDescriptor, api, websocketApi);
					websocketEndpointDemoGenerator.writeJavaFile(outputFolder);
				}
			}
		}
	}

}
