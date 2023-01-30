package com.scz.jcex.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;

import com.scz.jcex.generator.JavaCodeGenerationUtil;

public class CEXGenerator {
	
	public void generateCEX(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		generateCEXPojos(exchangeDescriptor, ouputFolder);
		
		
	}
	
	private void generateCEXPojos(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		String pojoPackage = exchangeDescriptor.getBasePackage() + ".pojo";
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
										pojoPackage + "." 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
											+ "Request", 
										"Request for " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
											+ restEndpointDescriptor.getName() + " REST endpoint",
										restEndpointDescriptor.getParameters());
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
						pojoPackage + "." 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
							+ "Response", 
						"Response to " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
							+ restEndpointDescriptor.getName() + " REST endpoint request",
						restEndpointDescriptor.getResponse());
			}
			
			for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
										pojoPackage + "." 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
											+ "Request", 
										"Subscription request to" + exchangeDescriptor.getName() + " " + api.getName() + " API " 
											+ wsEndpointDescriptor.getName() + " REST endpoint",
											wsEndpointDescriptor.getParameters());
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
						pojoPackage + "." 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
							+ "Response", 
						"Response to " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
							+ wsEndpointDescriptor.getName() + " REST endpoint request",
							wsEndpointDescriptor.getResponse());
			}
		}
		
		
	}

}
