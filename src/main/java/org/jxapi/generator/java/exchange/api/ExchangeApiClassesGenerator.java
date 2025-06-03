package org.jxapi.generator.java.exchange.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import org.jxapi.generator.java.exchange.api.rest.RestEndpointClassesGenerator;
import org.jxapi.generator.java.exchange.api.ws.WebsocketEndpointClassesGenerator;
import org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator;
import org.jxapi.netutils.rest.ratelimits.RateLimitManager;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Generates all Java classes of an API Wrapper for an {@link ExchangeApiDescriptor} defined in an
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
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor    the exchange descriptor where the API is defined
   * @param exchangeApiDescriptor the API descriptor to generate classes for
   */
  public ExchangeApiClassesGenerator(ExchangeDescriptor exchangeDescriptor,
                                     ExchangeApiDescriptor exchangeApiDescriptor) {
    this.exchangeDescriptor = exchangeDescriptor;
    this.exchangeApiDescriptor = exchangeApiDescriptor;
  }
  
  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    PlaceHolderResolver docPlaceHolderResolver = 
      PlaceHolderResolver.create(ExchangeJavaGenUtil.getDescriptionReplacements(exchangeDescriptor, exchangeApiDescriptor.getName()));
    ExchangeApiInterfaceGenerator exchangeApiInterfaceGenerator = 
      new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor, docPlaceHolderResolver);
    exchangeApiInterfaceGenerator.writeJavaFile(outputFolder); 
    
    new ExchangeApiInterfaceImplementationGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
    for (RestEndpointDescriptor restEndpointDescriptor: CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints())) {
      new RestEndpointClassesGenerator(exchangeDescriptor, 
                                         exchangeApiDescriptor, 
                                         restEndpointDescriptor, 
                                         docPlaceHolderResolver).generateClasses(outputFolder);
    }
    
    for (WebsocketEndpointDescriptor websocketEndpointDescriptor: CollectionUtil.emptyIfNull(exchangeApiDescriptor.getWebsocketEndpoints())) {
      new WebsocketEndpointClassesGenerator(exchangeDescriptor, 
                                            exchangeApiDescriptor, 
                                            websocketEndpointDescriptor,
                                            docPlaceHolderResolver).generateClasses(outputFolder);
    }
    
    // Generate constants interface
    List<Constant> constants = exchangeApiDescriptor.getConstants();
    if (!CollectionUtil.isEmpty(constants)) {
      ConstantsClassGenerator cgen = new ConstantsClassGenerator(
          ExchangeJavaGenUtil.getExchangeApiConstantsInterfaceName(exchangeDescriptor, exchangeApiDescriptor), 
          constants,
          docPlaceHolderResolver); 
      cgen.setConstantValuePlaceHolderResolver(s -> ExchangeJavaGenUtil.generateSubstitutionInstructionDeclaration(
                                                      s, 
                                                      exchangeDescriptor, 
                                                      exchangeApiDescriptor, 
                                                      null,
                                                      cgen.getImports()));
      cgen.setDescription("Constants used in "
                + exchangeDescriptor.getId() 
                + " exchange API wrapper {@link " 
                + exchangeApiInterfaceGenerator.getName() 
                + "} API group");
      cgen.writeJavaFile(outputFolder);
    }
  }
}
