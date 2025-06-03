package org.jxapi.util;

import java.util.Map;

/**
 * Interface for resolving placeholders in a template string. Implementations
 * should provide logic to replace placeholders with actual values.
 */
public interface PlaceHolderResolver {

  /**
   * Resolves the placeholders in the given template string.
   *
   * @param template the template string containing placeholders
   * @return the resolved string with placeholders replaced
   */
  String resolve(String template);
  
  /**
   * A no-operation implementation of PlaceHolderResolver that returns the input
   * string unchanged.
   */
  static final PlaceHolderResolver NO_OP = s -> s;
  
  /**
   * Creates a PlaceHolderResolver that substitutes placeholders in the input
   * string with the provided map of keys and values.
   *
   * @param map a map containing keys and values for substitution
   * @return a PlaceHolderResolver that performs the substitution
   * @see EncodingUtil#substituteArguments(String, Map)
   */
  static PlaceHolderResolver create(Map<String, Object>  map) {
    if (CollectionUtil.isEmptyMap(map)) {
      return NO_OP;
    }
    return s -> EncodingUtil.substituteArguments(s, map);
  }
}
