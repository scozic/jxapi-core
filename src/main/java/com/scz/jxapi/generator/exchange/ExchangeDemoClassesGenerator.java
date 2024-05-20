package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;

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
