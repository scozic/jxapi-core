package org.jxapi.generator.java.exchange.api.rest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ConstantValuePlaceholderResolverFactory;
import org.jxapi.generator.java.exchange.ExchangeConstantValuePlaceholderResolverFactory;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.JsonMessageDeserializerClassesGenerator;
import org.jxapi.generator.java.pojo.JsonPojoSerializerClassesGenerator;
import org.jxapi.generator.java.pojo.PojoClassesGenerator;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.util.PlaceHolderResolver;

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
  private final ExchangeDescriptor exchangeDescriptor;
  
  /**
   * API of exchange descriptor defining the REST endpoint
   */
  private final ExchangeApiDescriptor apiDescriptor;
  
  /**
   * REST endpoint descriptor to generate related Java classes for.
   */
  private final RestEndpointDescriptor restEndpointDescriptor;
  
  /**
   * Place holder resolver for documentation generation.
   */
  private final PlaceHolderResolver docPlaceHolderResolver;

  private ConstantValuePlaceholderResolverFactory constantInDefaultValuesPlaceHolderResolver;

  /**
   * @param exchangeDescriptor Exchange descriptor where API with REST endpoint are defined
   * @param apiDescriptor API of exchange descriptor defining the REST endpoint
   * @param restEndpointDescriptor REST endpoint descriptor to generate related Java classes for.
   * @param docPlaceHolderResolver Place holder resolver for resolution of placeholders in descriptions.
   */
  public RestEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
                    ExchangeApiDescriptor apiDescriptor, 
                    RestEndpointDescriptor restEndpointDescriptor,
                    PlaceHolderResolver docPlaceHolderResolver) {
    this.exchangeDescriptor = exchangeDescriptor;
    this.apiDescriptor = apiDescriptor;
    this.restEndpointDescriptor = restEndpointDescriptor;
    this.docPlaceHolderResolver = Optional.ofNullable(docPlaceHolderResolver)
                                          .orElse(PlaceHolderResolver.NO_OP);
    this.constantInDefaultValuesPlaceHolderResolver = new ExchangeConstantValuePlaceholderResolverFactory(exchangeDescriptor);
    
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
          getRestEndpointRequestOrResponseDescription("Request", request),
          request.getProperties(),
          request.getImplementedInterfaces(),
          docPlaceHolderResolver,
          constantInDefaultValuesPlaceHolderResolver
          ).generateClasses(outputFolder);
    }
    
    Field response = restEndpointDescriptor.getResponse();
    if (response != null && response.getProperties() != null) {
      new PojoClassesGenerator( 
          ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
              exchangeDescriptor, 
              apiDescriptor, 
              restEndpointDescriptor), 
            getRestEndpointRequestOrResponseDescription("Response", response),
            response.getProperties(),
            response.getImplementedInterfaces(),
            docPlaceHolderResolver,
            constantInDefaultValuesPlaceHolderResolver
          ).generateClasses(outputFolder);
    }
  }
  
  private String getRestEndpointRequestOrResponseDescription(String prefix, Field objField) {
    return Optional
      .ofNullable(PojoGenUtil.getObjectDescription(objField))
      .orElse(new StringBuilder().append(prefix)
          .append(" object for ")
          .append(exchangeDescriptor.getId())
          .append(" ")
          .append(apiDescriptor.getName())
          .append(" API ")
          .append(restEndpointDescriptor.getName())
          .append(" REST endpoint<br>\n")
          .append(StringUtils.defaultString(restEndpointDescriptor.getDescription()))
          .toString());
    
  }
  
  private void generateDeserializers(Path outputFolder) throws IOException {
    Field request = restEndpointDescriptor.getRequest();
    List<Field> requestParams = request == null? null : request.getProperties();
    if (requestParams != null) {
      new JsonMessageDeserializerClassesGenerator(
          ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
              exchangeDescriptor, 
              apiDescriptor, 
              restEndpointDescriptor), 
          requestParams).generateClasses(outputFolder);
    }
    
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
