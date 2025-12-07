package org.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator;
import org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator;
import org.jxapi.netutils.rest.ratelimits.RateLimitManager;
import org.jxapi.util.PlaceHolderResolver;
import org.springframework.util.CollectionUtils;

/**
 * Generates all classes for an {@link ExchangeDescriptor} defined in an
 * exchange descriptor file. This covers:
 * <ul>
 * <li>Java interface for the exchange with a getter method to retrieve each
 * exchange API interface.
 * <li>Implementation of that exchange interface with eventual
 * {@link RateLimitManager} to enforce rate limit rules if any.
 * <li>Java interface for each API listed in {@link ExchangeDescriptor} (see
 * {@link ExchangeApiDescriptor} and generator for its classes
 * {@link ExchangeApiClassesGenerator}), with all classes for every
 * REST/Websocket endpoint.
 * </ul>
 */
public class ExchangeClassesGenerator implements ClassesGenerator {

  private final ExchangeDescriptor exchangeDescriptor;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor to generate classes for
   */
  public ExchangeClassesGenerator(ExchangeDescriptor exchangeDescriptor) {
    this.exchangeDescriptor = exchangeDescriptor;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    PlaceHolderResolver docPlaceHolderResolver = 
      PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null));
    ConstantValuePlaceholderResolverFactory constantValuePlaceholderResolverFactory = 
        new ExchangeConstantValuePlaceholderResolverFactory(exchangeDescriptor);
    // Generate exchange interface class
    ExchangeInterfaceGenerator exchangeInterfaceGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor, docPlaceHolderResolver);
    exchangeInterfaceGenerator.writeJavaFile(outputFolder);
    
    // Generate exchange interface implementation class
    new ExchangeInterfaceImplementationGenerator(exchangeDescriptor).writeJavaFile(outputFolder);
    
    // Generate classes for each exchange API group
    for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
      new ExchangeApiClassesGenerator(exchangeDescriptor, api, docPlaceHolderResolver).generateClasses(outputFolder);
    }
    
    // Generate constants interface
    List<Constant> constants = Constant.fromDescriptors(exchangeDescriptor.getConstants());
    if (!CollectionUtils.isEmpty(constants)) {
      ConstantsClassGenerator cgen = new ConstantsClassGenerator(
          ExchangeGenUtil.getExchangeConstantsClassName(exchangeDescriptor), 
          constants,
          docPlaceHolderResolver);
      cgen.setConstantValuePlaceHolderResolver(constantValuePlaceholderResolverFactory.createConstantValuePlaceholderResolver(cgen.getImports()));
      cgen.setDescription("Constants used in {@link " + exchangeInterfaceGenerator.getName() + "} API wrapper");
      cgen.writeJavaFile(outputFolder);
    }
    
    // Generate properties interface
    List<ConfigPropertyDescriptor> properties = exchangeDescriptor.getProperties();
    if (properties != null) {
      PropertiesClassGenerator pgen = new PropertiesClassGenerator(
          ExchangeGenUtil.getExchangePropertiesClassName(exchangeDescriptor), 
          exchangeDescriptor, 
          properties,
          null);
      pgen.writeJavaFile(outputFolder);
    }
  }
}
