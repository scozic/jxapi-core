package org.jxapi.generator.java.exchange;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Specific implementation of ConstantValuePlaceholderResolverFactory, in the context of wrapper generation.
 * This implementation creates PlaceHolderResolver instances that generate substitution instruction declarations
 * for constants defined in the ExchangeDescriptor.
 * 
 * @see ExchangeDescriptor
 * @see Constant
 */
public class ExchangeConstantValuePlaceholderResolverFactory implements ConstantValuePlaceholderResolverFactory {

  private final ExchangeDescriptor exchangeDescriptor;
  
  /**
   * Constructor.
   * @param exchangeDescriptor the exchange descriptor containing the constants
   */
  public ExchangeConstantValuePlaceholderResolverFactory(ExchangeDescriptor exchangeDescriptor) {
    this.exchangeDescriptor = exchangeDescriptor;
  }

  @Override
  public PlaceHolderResolver createConstantValuePlaceholderResolver(Imports imports) {
    return s -> ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        s, 
        exchangeDescriptor, 
        null,
        null,
        imports);
  }

}
