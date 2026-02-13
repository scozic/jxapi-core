package org.jxapi.util;

import org.jxapi.pojo.descriptor.Type;

/**
 * Default {@link ConfigProperty} implementation as regular POJO with setters.
 * @see ConfigProperty
 */
public class DefaultConfigProperty implements ConfigProperty {

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
  public static DefaultConfigProperty create(String name, Type type, String description, Object defaultValue) {
    DefaultConfigProperty p = new DefaultConfigProperty();
    p.setName(name);
    p.setDescription(description);
    p.setType(type);
    p.setDefaultValue(defaultValue);
    return p;
  }

  private String name;
  private String description;
  private Type type = Type.STRING;
  private Object defaultValue;
  
  /**
   * Property name
   * @return Property name
   */
  @Override
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
  @Override
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
   * Property value type, see {@link Type}, should be a primitive type e.g.
   * {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
   * {@link Type#BIGDECIMAL}, {@link Type#LONG}.
   */ 
  @Override
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
  @Override
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
   * Returns a string representation of this object
   * @see EncodingUtil#pojoToString(Object)
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
