package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;

/**
 * Generates all classes used by a particular REST endpoint defined in an exchange descriptor 
 * exchange descriptor file. This covers:
 * <ul>
 * <li>POJOs for endpoint request and response
 * <li>JSON deserializers for those POJOs
 * <li>JSON serializers for those POJOs
 * </ul>
 * @see RestEndpointDescriptor
 */
public class RestEndpointClassesGenerator {

	protected final ExchangeDescriptor exchangeDescriptor;
	protected final ExchangeApiDescriptor apiDescriptor;
	protected final RestEndpointDescriptor restEndpointDescriptor;

	/**
	 * @param exchangeDescriptor Exchange descriptor where API with REST endpoint are defined
	 * @param apiDescriptor API of exchange descriptor defining the REST endpoint
	 * @param restEndpointDescriptor REST endpoint descriptor to generate related Java classes for.
	 */
	public RestEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
										ExchangeApiDescriptor apiDescriptor, 
										RestEndpointDescriptor restEndpointDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.apiDescriptor = apiDescriptor;
		this.restEndpointDescriptor = restEndpointDescriptor;
	}
	
	/**
	 * Triggers generation of all java files bases in main source folder.
	 * @param outputFolder source folder, for instance <code>src/main/java</code>
	 * @throws IOException
	 */
	public void generateClasses(Path outputFolder) throws IOException {
		// Generate POJOs for request and response
		generatePojos(outputFolder);
		
		// Generate deserializers for request/response pojos
		generateDeserializers(outputFolder);
		
		// Generate serializers
		generateSerializers(outputFolder);
	}

	private void generatePojos(Path outputFolder) throws IOException {
		// Generate POJOs for request and response
		List<String> requestInterfaces = new ArrayList<>();
		requestInterfaces.add(RestEndpointUrlParameters.class.getName());
		if (restEndpointDescriptor.getRequestInterfaces() != null) {
			requestInterfaces.addAll(restEndpointDescriptor.getRequestInterfaces());
		}
		String additionalBody = null;
		additionalBody = ExchangeJavaWrapperGeneratorUtil.generateRestEndpointGetUrlParametersMethod(restEndpointDescriptor);
		
		
		new EndpointPojoClassesGenerator(
				ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor), 
				"Request for " + exchangeDescriptor.getName() + " " + apiDescriptor.getName() + " API " 
						+ restEndpointDescriptor.getName() + " REST endpoint<br/>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						restEndpointDescriptor.getParameters(),
						requestInterfaces,
						additionalBody).generateClasses(outputFolder);
		
		
		if (restEndpointDescriptor.getResponse() != null) {
			new EndpointPojoClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor), 
					"Response to " + exchangeDescriptor.getName() 
						+ " " + apiDescriptor.getName() + " API <br/>\n" 
						+ restEndpointDescriptor.getName() 
						+ " REST endpoint request<br/>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
					restEndpointDescriptor.getResponse(),
					restEndpointDescriptor.getResponseInterfaces(),
					null).generateClasses(outputFolder);
		}
	}
	
	private void generateDeserializers(Path outputFolder) throws IOException {
		ExchangeJavaWrapperGeneratorUtil.generateDeserializer(outputFolder, 
						ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor),
						restEndpointDescriptor.getResponse());
	}
	
	private void generateSerializers(Path ouputFolder) throws IOException {
		for (RestEndpointDescriptor restEndpointDescriptor: apiDescriptor.getRestEndpoints()) {
			ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
						ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor),
						restEndpointDescriptor.getParameters());
			if (restEndpointDescriptor.getResponse() != null) {
				ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
						ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor),
						restEndpointDescriptor.getResponse());
			}
		}
		
	}
}
