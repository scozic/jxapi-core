package org.jxapi.exchange.descriptor;

import java.util.List;

import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;
/**
 * Represents a configuration property or a group of properties of an exchange like authentication credentials (API Key,secret) the wraooer client should provide to instantiate a wrapper.br>
 * Exchange descriptor may contain a list of such properties as value of
 * 'properties' property of exchange.<p>
 * The name of a property should be spelled 'camelCase' like a Java variable
 * name.<p>
 * The value of a property can be a 'primitive' type e.g. {@link Type#STRING},
 * {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
 * {@link Type#LONG}. It can't be a list, map, or object.<p>
 * The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. That class wlll list constants for
 * property names and default values, and default 'getter' methods for
 * retrieving there values from properties<p>
 * <p>
 * The properties can be grouped together. For example, authentication
 * credentials can be grouped into a 'group' property called 'auth' with
 * sub-properties for API key, secret, etc listed. Those properties can be
 * referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain
 * other groups, so the structure is hierarchical.<p>
 * 
 */
public class ConfigPropertyDescriptor {
  
  /**
   * Factory method to create a property instance
   * 
   * @param name         Property name
   * @param type         Property value type, see {@link Type}, should be a
   *                     primitive type e.g. {@link Type#STRING}, {@link Type#INT},
   *                     {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
   *                     {@link Type#LONG}.
   * @param description  Description of the property
   * @param defaultValue Default value of the property. Should be of the type
   *                     specified in 'type' or string representation of the value
   *                     of that type. Can be <code>null</code> if no default value.
   * @return Property instance
   */
  public static ConfigPropertyDescriptor create(String name, Type type, String description, Object defaultValue) {
    ConfigPropertyDescriptor p = new ConfigPropertyDescriptor();
    p.setName(name);
    p.setDescription(description);
    p.setType(type);
    p.setDefaultValue(defaultValue);
    return p;
  }
  
  /**
   * Factory method to create a group of properties instance
   * 
   * @param name        Group name
   * @param description Description of the group
   * @param properties  List of sub-properties of this group
   * @return Group of properties instance
   */
  public static ConfigPropertyDescriptor createGroup(String name, String description, List<ConfigPropertyDescriptor> properties) {
    ConfigPropertyDescriptor p = new ConfigPropertyDescriptor();
    p.setName(name);
    p.setDescription(description);
    p.setProperties(properties);
    return p;
  }

  private String name;
  private String description;
  private Type type = Type.STRING;
  private Object defaultValue;
  private List<ConfigPropertyDescriptor> properties;
  
  /**
   * Property name
   * @return Property name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Property name
   * @param name Property name
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Description of the property
   * @return Description of the property
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * Description of the property
   * @param description Description of the property
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /*
   * Property value type, see {@link Type}, should be a primitive type e.g. {@link
   * Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link
   * Type#BIGDECIMAL}, {@link Type#LONG}. 
   * <p> Not relevant for groups of properties, i.e. when {@link #isGroup()} 
   * returns <code>true</code>. 
   * <p>
   * Default value is {@link Type#STRING} (therefore type property can be ommitted
   * in descriptor when it values are of String type).
   */
  public Type getType() {
    return type;
  }
  
  /**
   * Property value type, see {@link Type}, should be a primitive type e.g.
   * {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
   * {@link Type#BIGDECIMAL}, {@link Type#LONG}.
   * @param type Property value type
   */
  public void setType(Type type) {
    this.type = type;
  }
  
  /**
   * Shortcut for <code>setType(Type.fromTypeName(type))</code>
   * @param type The type name see {@link Type#toString()}
   * 
   * @see #setType(Type)
   */
  public void setType(String type) {
    setType(Type.fromTypeName(type));
  }
  
  /**
   * Default value of the property. Should be of the type specified in 'type' or
   * string representation of the value of that type. Can be <code>null</code> if
   * no default value.
   * @return Default value of the property
   */
  public Object getDefaultValue() {
    return defaultValue;
  }
  
  /**
   * Default value of the property. Should be of the type specified in 'type' or
   * string representation of the value of that type. Can be <code>null</code> if
   * no default value.
   * @param defaultValue Default value of the property
   */
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  /**
   * Returns the sub-properties of this property. If the property is a group of
   * properties, i.e. it has sub-properties defined in 'properties' property.
   * 
   * @return List of sub-properties
   */
  public List<ConfigPropertyDescriptor> getProperties() {
    return properties;
  }

  /**
   * Sets the sub-properties of this property. If the property is a group of
   * properties, i.e. it has sub-properties defined in 'properties' property.
   * 
   * @param properties List of sub-properties
   */
  public void setProperties(List<ConfigPropertyDescriptor> properties) {
    this.properties = properties;
  }
  
  /**
   * Returns <code>true</code> if this property is a group of properties, i.e. it
   * has sub-properties defined in 'properties' property.
   * 
   * @return <code>true</code> if this property is a group of properties
   */
  public boolean isGroup() {
    return !CollectionUtil.isEmpty(properties);
  }
  
  /**
   * Returns a string representation of this object
   * @see EncodingUtil#pojoToString(Object)
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
