package com.scz.jxapi.generator.java.exchange.api.rest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import com.scz.jxapi.generator.java.exchange.api.pojo.PojoClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerClassesGenerator;

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

	/**
	 * Exchange descriptor where API with REST endpoint are defined
	 */
	protected final ExchangeDescriptor exchangeDescriptor;
	
	/**
	 * API of exchange descriptor defining the REST endpoint
	 */
	protected final ExchangeApiDescriptor apiDescriptor;
	
	/**
	 * REST endpoint descriptor to generate related Java classes for.
	 */
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
		Field request = restEndpointDescriptor.getRequest();
		if (request != null && request.getProperties() != null) {
			new PojoClassesGenerator(
					ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor), 
					"Request for " + exchangeDescriptor.getName() + " " + apiDescriptor.getName() + " API " 
						+ restEndpointDescriptor.getName() + " REST endpoint<br>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						request.getProperties(),
						request.getImplementedInterfaces()
						).generateClasses(outputFolder);
		}
		
		Field response = restEndpointDescriptor.getResponse();
		if (response != null && response.getProperties() != null) {
			new PojoClassesGenerator( 
					ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor), 
					"Response to " + exchangeDescriptor.getName() 
						+ " " + apiDescriptor.getName() + " API <br>\n" 
						+ restEndpointDescriptor.getName() 
						+ " REST endpoint request<br>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						response.getProperties(),
						response.getImplementedInterfaces()
					).generateClasses(outputFolder);
		}
	}
	
	private void generateDeserializers(Path outputFolder) throws IOException {
		Field response = restEndpointDescriptor.getResponse();
		if (response != null && response.getProperties() != null) {
			new JsonMessageDeserializerClassesGenerator( 
					ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
							response.getProperties()).generateClasses(outputFolder);
		}
	}
	
	private void generateSerializers(Path ouputFolder) throws IOException {
		 Field request = restEndpointDescriptor.getRequest();
		 List<Field> requestParams = request == null? null : request.getProperties();
		 if (requestParams != null) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
							requestParams).generateClasses(ouputFolder);
		}
		Field response = restEndpointDescriptor.getResponse();
		List<Field> responseParams = response == null? null : response.getProperties();
		if (responseParams != null) {
			new JsonPojoSerializerClassesGenerator(  
					ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor),
							responseParams).generateClasses(ouputFolder);
		}
	}
}
