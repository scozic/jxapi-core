package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.ConfigPropertyDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.ConfigPropertyDescriptorSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Represents a configuration property or a group of properties of an exchange like authentication credentials 
 * (API Key,secret) the wrapper client should provide to instantiate a wrapper.<br>
 * Exchange descriptor may contain a list of such properties as value of
 * 'properties' property of exchange.<p>
 * The name of a property should be spelled 'camelCase' like a Java variable
 * name.
 * <p>
 * The value of a property can be a 'primitive' type e.g. {@link org.jxapi.pojo.descriptor.Type#STRING},
 * {@link org.jxapi.pojo.descriptor.Type#INT}, {@link org.jxapi.pojo.descriptor.Type#BOOLEAN}, 
 * {@link org.jxapi.pojo.descriptor.Type#BIGDECIMAL}, {@link org.jxapi.pojo.descriptor.Type#LONG}. 
 * It can't be a list, map, or object.<p>
 * The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. 
 * That class wlll list constants for property names and default values, and default 'getter' methods for 
 * retrieving their values from properties
 * <p>
 * The properties can be grouped together. For example, authentication
 * credentials can be grouped into a 'group' property called 'auth' with
 * sub-properties for API key, secret, etc listed. Those properties can be
 * referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain
 * other groups, so the structure is hierarchical.
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = ConfigPropertyDescriptorSerializer.class)
@JsonDeserialize(using = ConfigPropertyDescriptorDeserializer.class)
public class ConfigPropertyDescriptor implements Pojo<ConfigPropertyDescriptor> {
  
  private static final long serialVersionUID = 4732400927168532522L;
  
  /**
   * @return A new builder to build {@link ConfigPropertyDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String description;
  private String type;
  private Object defaultValue;
  private List<ConfigPropertyDescriptor> properties;
  
  /**
   * @return The name of the property
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The name of the property
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The description of the property
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description The description of the property
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return Property value type, see {@link org.jxapi.pojo.descriptor.Type}, should be a primitive type e.g. 'STRING', 'INT', 'LONG', 'BIGDECIMAL', 'BOOLEAN'.
   * When property is a group of sub-properties, this property is ignored.
   * 
   */
  public String getType() {
    return type;
  }
  
  /**
   * @param type Property value type, see {@link org.jxapi.pojo.descriptor.Type}, should be a primitive type e.g. 'STRING', 'INT', 'LONG', 'BIGDECIMAL', 'BOOLEAN'.
   * When property is a group of sub-properties, this property is ignored.
   * 
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return Default value of the property. Should be of the type specified in 'type' or string representation of the value of that type. Can be <code>null</code> if no default value.
   */
  public Object getDefaultValue() {
    return defaultValue;
  }
  
  /**
   * @param defaultValue Default value of the property. Should be of the type specified in 'type' or string representation of the value of that type. Can be <code>null</code> if no default value.
   */
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  /**
   * @return The list of sub-properties if this property is a group of properties.
   */
  public List<ConfigPropertyDescriptor> getProperties() {
    return properties;
  }
  
  /**
   * @param properties The list of sub-properties if this property is a group of properties.
   */
  public void setProperties(List<ConfigPropertyDescriptor> properties) {
    this.properties = properties;
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
    ConfigPropertyDescriptor o = (ConfigPropertyDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.description, o.description)
        && Objects.equals(this.type, o.type)
        && Objects.equals(this.defaultValue, o.defaultValue)
        && Objects.equals(this.properties, o.properties);
  }
  
  @Override
  public int compareTo(ConfigPropertyDescriptor other) {
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
    res = CompareUtil.compareObjects(this.defaultValue, other.defaultValue);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.properties, other.properties, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, description, type, defaultValue, properties);
  }
  
  @Override
  public ConfigPropertyDescriptor deepClone() {
    ConfigPropertyDescriptor clone = new ConfigPropertyDescriptor();
    clone.name = this.name;
    clone.description = this.description;
    clone.type = this.type;
    clone.defaultValue = this.defaultValue;
    clone.properties = CollectionUtil.deepCloneList(this.properties, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link ConfigPropertyDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String description;
    private String type;
    private Object defaultValue;
    private List<ConfigPropertyDescriptor> properties;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The name of the property
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>description</code> field in the builder
     * @param description The description of the property
     * @return Builder instance
     * @see #setDescription(String)
     */
    public Builder description(String description)  {
      this.description = description;
      return this;
    }
    
    /**
     * Will set the value of <code>type</code> field in the builder
     * @param type Property value type, see {@link org.jxapi.pojo.descriptor.Type}, should be a primitive type e.g. 'STRING', 'INT', 'LONG', 'BIGDECIMAL', 'BOOLEAN'.
     * When property is a group of sub-properties, this property is ignored.
     * 
     * @return Builder instance
     * @see #setType(String)
     */
    public Builder type(String type)  {
      this.type = type;
      return this;
    }
    
    /**
     * Will set the value of <code>defaultValue</code> field in the builder
     * @param defaultValue Default value of the property. Should be of the type specified in 'type' or string representation of the value of that type. Can be <code>null</code> if no default value.
     * @return Builder instance
     * @see #setDefaultValue(Object)
     */
    public Builder defaultValue(Object defaultValue)  {
      this.defaultValue = defaultValue;
      return this;
    }
    
    /**
     * Will set the value of <code>properties</code> field in the builder
     * @param properties The list of sub-properties if this property is a group of properties.
     * @return Builder instance
     * @see #setProperties(List<ConfigPropertyDescriptor>)
     */
    public Builder properties(List<ConfigPropertyDescriptor> properties)  {
      this.properties = properties;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>properties</code> list.
     * @param item Item to add to current <code>properties</code> list
     * @return Builder instance
     * @see ConfigPropertyDescriptor#setProperties(List)
     */
    public Builder addToProperties(ConfigPropertyDescriptor item) {
      if (this.properties == null) {
        this.properties = CollectionUtil.createList();
      }
      this.properties.add(item);
      return this;
    }
    
    /**
     * @return a new instance of ConfigPropertyDescriptor using the values set in this builder
     */
    public ConfigPropertyDescriptor build() {
      ConfigPropertyDescriptor res = new ConfigPropertyDescriptor();
      res.name = this.name;
      res.description = this.description;
      res.type = this.type;
      res.defaultValue = this.defaultValue;
      res.properties = CollectionUtil.deepCloneList(this.properties, DeepCloneable::deepClone);
      return res;
    }
  }
}
