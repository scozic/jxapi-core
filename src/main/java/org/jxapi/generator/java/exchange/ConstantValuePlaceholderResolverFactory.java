package org.jxapi.generator.java.exchange;

import org.jxapi.generator.java.Imports;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Factory for creating {@link PlaceHolderResolver} instances that resolve placeholders for
 * values in descriptor constant or default values definitions.<br>
 * This means replacements are likely to contain calls to substitution instructions.<br>
 * The created resolver should use the provided {@link Imports} instance to register any required imports.
 * 
 * @see PlaceHolderResolver
 * @see Imports
 */
public interface ConstantValuePlaceholderResolverFactory {

  /**
   * Creates a {@link PlaceHolderResolver} for resolving placeholders in constant or default value definitions.
   * @param imports the imports instance to use for registering any required imports
   * @return the created PlaceHolderResolver
   */
  PlaceHolderResolver createConstantValuePlaceholderResolver(Imports imports);
  
  /**
   * A no-operation implementation of ConstantValuePlaceholderResolverFactory that
   * returns a no-op PlaceHolderResolver.
   * @see PlaceHolderResolver#NO_OP
   */
  static ConstantValuePlaceholderResolverFactory NO_OP = i -> PlaceHolderResolver.NO_OP;
}
