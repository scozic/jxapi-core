package org.jxapi.exchange.descriptor;

import java.util.List;
import java.util.Objects;

import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;

/**
 * Represents a constant value used across APIs of an exchange or a group of such constants.<br>
 * Exchange descriptor may contain a list of such constants as value of
 * 'constants' property of exchange<br>
 * Such constants will be exposed as static final fields in a generated Java
 * class of the generated Java wrapper class for the exchange.<br>
 * A constant may not be a final constant but a 'group' of constants that functionally come together.
 * For example, when an exchange uses constants to represent bid or ask a side of, 
 * it makes sense to group them together in a single constant group.
 * In this case, final constants of the group will be exposed as static field of a nested public class
 * in the main constant class, named after group name. A constant represents a group when its 
 * <code>constants</code> property is set with a non empty list of nested constants<br>
 * The name of a constant should be a valid camel case Java identifier.<br>
 * The value of a constant must be a 'primitive' type e.g. {@link Type#STRING},
 * {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
 * {@link Type#LONG}. It can't be a list, map, or object. <code>value</code>
 * and <code>type</code> properties are relevant only when constant is not a group.<br>
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
  
  /**
   * Factory method to create a constant group instance
   * 
   * @param name            Constant group name
   * @param description     Description of the constant group
   * @param nestedConstants List of nested constants in the group, may be empty
   *                        but not <code>null</code>
   * @return Constant instance representing a group of constants
   */
  public static Constant createGroup(String name, String description, List<Constant> nestedConstants) {
    Constant g = new Constant();
    g.setName(name);
    g.setDescription(description);
    g.setConstants(nestedConstants);
    return g;
  }

  private String name;
  private String description;
  private Object value;
  private Type type = Type.STRING;
  private List<Constant> constants;

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
   * Returns a list of nested constants, if this constant is a group of constants.
   * @return List of nested constants, may be empty or <code>null</code> if this constant is not a group
   */
  public List<Constant> getConstants() {
    return constants;
  }
  
  /**
   * Returns <code>true</code> if this constant is a group of constants, 
   * i.e. {@link #getConstants()} returns a non-empty list of nested constants.
   * 
   * @return <code>true</code> if this constant is a group of constants, otherwise
   *         <code>false</code>
   */
  public boolean isGroup() {
    return !CollectionUtil.isEmpty(constants);
  }

  /**
   * Sets the list of nested constants, if this constant is a group of constants.
   * 
   * @param constants List of nested constants, if this constant is a group of constants.
   */
  public void setConstants(List<Constant> constants) {
    this.constants = constants;
  }

  /**
   * @return String representation of the constant, see
   *         {@link EncodingUtil#pojoToString(Object)}
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Constant)) {
      return false;
    }
    Constant other = (Constant) obj;
    return Objects.equals(this.name, other.name) 
        && Objects.equals(this.type, other.type)
        && Objects.equals(this.value, other.value)
        && Objects.equals(this.description, other.description) 
        && Objects.equals(this.constants, other.constants);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, type, value, description, constants);
  }
}
