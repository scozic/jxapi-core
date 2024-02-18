package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

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
public class RestEndpointClassesGenerator implements ClassesGenerator {

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
	 * {@inheritDoc}
	 */
	@Override
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
		if (restEndpointDescriptor.getRequestInterfaces() != null) {
			requestInterfaces.addAll(restEndpointDescriptor.getRequestInterfaces());
		}
		
		if (restEndpointDescriptor.getParameters() != null) {
			new EndpointPojoClassesGenerator(
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor), 
					"Request for " + exchangeDescriptor.getName() + " " + apiDescriptor.getName() + " API " 
						+ restEndpointDescriptor.getName() + " REST endpoint<br/>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						restEndpointDescriptor.getParameters(),
						requestInterfaces,
						null).generateClasses(outputFolder);
		}
		
		
		if (restEndpointDescriptor.getResponse() != null) {
			new EndpointPojoClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor), 
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
		if (restEndpointDescriptor.getResponse() != null) {
			new JsonMessageDeserializerClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
					restEndpointDescriptor.getResponse()).generateClasses(outputFolder);
		}
	}
	
	private void generateSerializers(Path ouputFolder) throws IOException {
		if (restEndpointDescriptor.getParameters() != null) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
					restEndpointDescriptor.getParameters()).generateClasses(ouputFolder);
		}
		
		if (restEndpointDescriptor.getResponse() != null) {
			new JsonPojoSerializerClassesGenerator(  
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
					restEndpointDescriptor.getResponse()).generateClasses(ouputFolder);
		}
	}
}
