package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.ConstantDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.ConstantDescriptorSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

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
 * The value of a constant must be a 'primitive' type e.g. {@link org.jxapi.pojo.descriptor.Type#STRING},
 * {@link org.jxapi.pojo.descriptor.Type#INT}, {@link org.jxapi.pojo.descriptor.Type#BOOLEAN}, 
 * {@link org.jxapi.pojo.descriptor.Type#BIGDECIMAL}, {@link org.jxapi.pojo.descriptor.Type#LONG}. 
 * It can't be a list, map, or object. <code>value</code>
 * and <code>type</code> properties are relevant only when constant is not a group.<br>
 * The name of a constant should provide a more readable name for the value. The
 * description allows to provide semantic details.<br>
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = ConstantDescriptorSerializer.class)
@JsonDeserialize(using = ConstantDescriptorDeserializer.class)
public class ConstantDescriptor implements Pojo<ConstantDescriptor> {
  
  private static final long serialVersionUID = -4269134152873248313L;
  
  /**
   * @return A new builder to build {@link ConstantDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String description;
  private String type;
  private Object value;
  private List<ConstantDescriptor> constants;
  
  /**
   * @return The name of the constant
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The name of the constant
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The description of the constant
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description The description of the constant
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return The type of the constant value, see {@link org.jxapi.pojo.descriptor.Type}, should be a primitive type e.g. 'STRING', 'INT', 'LONG', 'BIGDECIMAL', 'BOOLEAN'.
   * This property is relevant only when the constant is not a group of constants.
   * 
   */
  public String getType() {
    return type;
  }
  
  /**
   * @param type The type of the constant value, see {@link org.jxapi.pojo.descriptor.Type}, should be a primitive type e.g. 'STRING', 'INT', 'LONG', 'BIGDECIMAL', 'BOOLEAN'.
   * This property is relevant only when the constant is not a group of constants.
   * 
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return The value of the constant. Should be of the type specified in 'type' or string representation of the value of that type. Can be <code>null</code> if constant is a group.
   * 
   */
  public Object getValue() {
    return value;
  }
  
  /**
   * @param value The value of the constant. Should be of the type specified in 'type' or string representation of the value of that type. Can be <code>null</code> if constant is a group.
   * 
   */
  public void setValue(Object value) {
    this.value = value;
  }
  
  /**
   * @return The list of nested constants if this constant is a group of constants.
   * 
   */
  public List<ConstantDescriptor> getConstants() {
    return constants;
  }
  
  /**
   * @param constants The list of nested constants if this constant is a group of constants.
   * 
   */
  public void setConstants(List<ConstantDescriptor> constants) {
    this.constants = constants;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    ConstantDescriptor o = (ConstantDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.description, o.description)
        && Objects.equals(this.type, o.type)
        && Objects.equals(this.value, o.value)
        && Objects.equals(this.constants, o.constants);
  }
  
  @Override
  public int compareTo(ConstantDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.name, other.name);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.description, other.description);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.type, other.type);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareObjects(this.value, other.value);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.constants, other.constants, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, description, type, value, constants);
  }
  
  @Override
  public ConstantDescriptor deepClone() {
    ConstantDescriptor clone = new ConstantDescriptor();
    clone.name = this.name;
    clone.description = this.description;
    clone.type = this.type;
    clone.value = this.value;
    clone.constants = CollectionUtil.deepCloneList(this.constants, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link ConstantDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String description;
    private String type;
    private Object value;
    private List<ConstantDescriptor> constants;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The name of the constant
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>description</code> field in the builder
     * @param description The description of the constant
     * @return Builder instance
     * @see #setDescription(String)
     */
    public Builder description(String description)  {
      this.description = description;
      return this;
    }
    
    /**
     * Will set the value of <code>type</code> field in the builder
     * @param type The type of the constant value, see {@link org.jxapi.pojo.descriptor.Type}, should be a primitive type e.g. 'STRING', 'INT', 'LONG', 'BIGDECIMAL', 'BOOLEAN'.
     * This property is relevant only when the constant is not a group of constants.
     * 
     * @return Builder instance
     * @see #setType(String)
     */
    public Builder type(String type)  {
      this.type = type;
      return this;
    }
    
    /**
     * Will set the value of <code>value</code> field in the builder
     * @param value The value of the constant. Should be of the type specified in 'type' or string representation of the value of that type. Can be <code>null</code> if constant is a group.
     * 
     * @return Builder instance
     * @see #setValue(Object)
     */
    public Builder value(Object value)  {
      this.value = value;
      return this;
    }
    
    /**
     * Will set the value of <code>constants</code> field in the builder
     * @param constants The list of nested constants if this constant is a group of constants.
     * 
     * @return Builder instance
     * @see #setConstants(List<ConstantDescriptor>)
     */
    public Builder constants(List<ConstantDescriptor> constants)  {
      this.constants = constants;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>constants</code> list.
     * @param item Item to add to current <code>constants</code> list
     * @return Builder instance
     * @see ConstantDescriptor#setConstants(List)
     */
    public Builder addToConstants(ConstantDescriptor item) {
      if (this.constants == null) {
        this.constants = CollectionUtil.createList();
      }
      this.constants.add(item);
      return this;
    }
    
    /**
     * @return a new instance of ConstantDescriptor using the values set in this builder
     */
    public ConstantDescriptor build() {
      ConstantDescriptor res = new ConstantDescriptor();
      res.name = this.name;
      res.description = this.description;
      res.type = this.type;
      res.value = this.value;
      res.constants = CollectionUtil.deepCloneList(this.constants, DeepCloneable::deepClone);
      return res;
    }
  }
}
