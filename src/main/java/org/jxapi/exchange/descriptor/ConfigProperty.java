package org.jxapi.exchange.descriptor;

import org.jxapi.exchange.Exchange;

/**
 * Represents a configuration property of an exchange, that client should
 * provide in constructor of an {@link Exchange} implementation to instantiate a
 * wrapper. For instance when an API requires authentication, the client should
 * provide the authentication credentials (API Key,secret) as properties<br>
 * Exchange descriptor may contain a list of such properties as value of
 * 'properties' property of exchange.<br>
 * The name of a property should be spelled 'camelCase' like a Java variable
 * name.<br>
 * The value of a property can be a 'primitive' type e.g. {@link Type#STRING},
 * {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
 * {@link Type#LONG}. It can't be a list, map, or object.<br>
 * The properties will be exposed in a generated Java interface in the generated
 * Java wrapper class for the exchange. That interface wlll list constants for
 * property names and default values, and default 'getter' methods for
 * retrieving there values from properties<br>
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