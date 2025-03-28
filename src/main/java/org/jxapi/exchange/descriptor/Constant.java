package org.jxapi.exchange.descriptor;

import org.jxapi.util.EncodingUtil;

/**
 * Represents a constant value used across APIs of an exchange.<br>
 * Exchange descriptor may contain a list of such constants as value of
 * 'constants' property of exchange or exchange API descriptors.<br>
 * Such constants will be provided as static fields in a generated Java
 * interface in the generated Java wrapper class for the exchange.<br>
 * The name of a constant should be a valid Java identifier.<br>
 * The value of a constant must be a 'primitive' type e.g. {@link Type#STRING},
 * {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
 * {@link Type#LONG}. It can't be a list, map, or object.<br>
 * The name of a constant should provide a more readable name for the value. The
 * description allows to provide semantic details.<br>
 */
public class Constant {

  /**
   * Factory method to create a constant instance
   * 
   * @param name        Constant name
   * @param type        Constant value type, see {@link Type}, should be a
   *                    primitive type e.g. {@link Type#STRING}, {@link Type#INT},
   *                    {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
   *                    {@link Type#LONG}.
   * @param description Description of the constant
   * @param value       Value of the constant. Should be of the type specified in
   *                    'type' or string representation of the value of that type.
   * @return Constant instance
   */
  public static Constant create(String name, Type type, String description, Object value) {
    Constant c = new Constant();
    c.setName(name);
    c.setType(type);
    c.setDescription(description);
    c.setValue(value);
    return c;
  }

  private String name;
  private String description;
  private Object value;
  private Type type = Type.STRING;

  /**
   * @return Constant name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name Constant name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return Description of the constant
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description Description of the constant
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return Value of the constant, should be of the type specified in 'type' or
   *         string representation of the value of that type
   */
  public Object getValue() {
    return value;
  }

  /**
   * @param value Value of the constant, should be of the type specified in 'type'
   *              or string representation of the value of that type
   */
  public void setValue(Object value) {
    this.value = value;
  }

  /**
   * @return Constant value type, see {@link Type}, should be a primitive type
   *         e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
   *         {@link Type#BIGDECIMAL}, {@link Type#LONG}
   */
  public Type getType() {
    return type;
  }

  /**
   * @param type Constant value type, see {@link Type}, should be a primitive type
   *             e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
   *             {@link Type#BIGDECIMAL}, {@link Type#LONG}
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
   * @return String representation of the constant, see
   *         {@link EncodingUtil#pojoToString(Object)}
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
