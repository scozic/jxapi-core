package com.scz.jxapi.generator.exchange.api.rest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.EndpointParameter;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.generator.exchange.ClassesGenerator;
import com.scz.jxapi.generator.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.exchange.api.pojo.EndpointPojoClassesGenerator;
import com.scz.jxapi.generator.exchange.api.pojo.JsonMessageDeserializerClassesGenerator;
import com.scz.jxapi.generator.exchange.api.pojo.JsonPojoSerializerClassesGenerator;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;

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
		EndpointParameter request = restEndpointDescriptor.getRequest();
		if (request != null && request.getParameters() != null) {
			new EndpointPojoClassesGenerator(
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor), 
					"Request for " + exchangeDescriptor.getName() + " " + apiDescriptor.getName() + " API " 
						+ restEndpointDescriptor.getName() + " REST endpoint<br/>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						request.getParameters(),
						request.getImplementedInterfaces(),
						null).generateClasses(outputFolder);
		}
		
		EndpointParameter response = restEndpointDescriptor.getResponse();
		if (response != null && response.getParameters() != null) {
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
						response.getParameters(),
						response.getImplementedInterfaces(),
					null).generateClasses(outputFolder);
		}
	}
	
	private void generateDeserializers(Path outputFolder) throws IOException {
		EndpointParameter response = restEndpointDescriptor.getResponse();
		if (response != null && response.getParameters() != null) {
			new JsonMessageDeserializerClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
							response.getParameters()).generateClasses(outputFolder);
		}
	}
	
	private void generateSerializers(Path ouputFolder) throws IOException {
		 EndpointParameter request = restEndpointDescriptor.getRequest();
		 List<EndpointParameter> requestParams = request == null? null : request.getParameters();
		 if (requestParams != null) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
							requestParams).generateClasses(ouputFolder);
		}
		EndpointParameter response = restEndpointDescriptor.getResponse();
		List<EndpointParameter> responseParams = response == null? null : response.getParameters();
		if (responseParams != null) {
			new JsonPojoSerializerClassesGenerator(  
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
							responseParams).generateClasses(ouputFolder);
		}
	}
}
