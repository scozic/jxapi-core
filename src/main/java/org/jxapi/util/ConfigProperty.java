package org.jxapi.util;

import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.pojo.descriptor.Type;

/**
 * Represents a configuration property of an exchange, as described in a
 * {@link ConfigPropertyDescriptor} of exchange descriptor. Such objects will be
 * created as static constants in the generated Java wrapper class exposing
 * properties.
 * 
 */
public interface ConfigProperty {

  /**
   * Property name
   * @return Property name
   */
  String getName();

  /**
   * Description of the property
   * @return Description of the property
   */
  String getDescription();

  /**
   * Property value type, see {@link Type}, should be a primitive type e.g.
   * {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
   * {@link Type#BIGDECIMAL}, {@link Type#LONG}.
   * 
   * @return Property value type
   */
  Type getType();

  /**
   * Default value of the property. Should be of the type specified in 'type' or
   * string representation of the value of that type. Can be <code>null</code> if
   * no default value.
   * @return Default value of the property
   */
  Object getDefaultValue();

}