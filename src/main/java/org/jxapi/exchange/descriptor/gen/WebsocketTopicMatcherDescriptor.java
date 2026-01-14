package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.WebsocketTopicMatcherDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.WebsocketTopicMatcherDescriptorSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * The description of how to match incoming messages to this topic.
 * This can be either the value or pattern of a specific field, or a logical combination of field matchers using AND/OR operations.
 * Remarks:
 * <ul>
 * <li>One of <code>fieldName</code> or <code>and</code> or <code>or</code> must be provided.</li>
 * <li>If <code>fieldName</code> is provided, either <code>fieldValue</code> or <code>fieldRegexp</code> must be provided.</li>
 * </ul>
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = WebsocketTopicMatcherDescriptorSerializer.class)
@JsonDeserialize(using = WebsocketTopicMatcherDescriptorDeserializer.class)
public class WebsocketTopicMatcherDescriptor implements Pojo<WebsocketTopicMatcherDescriptor> {
  
  private static final long serialVersionUID = 1247228024754855825L;
  
  /**
   * @return A new builder to build {@link WebsocketTopicMatcherDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String fieldName;
  private Object fieldValue;
  private String fieldRegexp;
  private List<WebsocketTopicMatcherDescriptor> and;
  private List<WebsocketTopicMatcherDescriptor> or;
  
  /**
   * @return The name of the field to match
   */
  public String getFieldName() {
    return fieldName;
  }
  
  /**
   * @param fieldName The name of the field to match
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  
  /**
   * @return The value the field with 'fieldName' should have to match.
   */
  public Object getFieldValue() {
    return fieldValue;
  }
  
  /**
   * @param fieldValue The value the field with 'fieldName' should have to match.
   */
  public void setFieldValue(Object fieldValue) {
    this.fieldValue = fieldValue;
  }
  
  /**
   * @return The pattern (Java regular expression) field with 'fieldName' should match.
   */
  public String getFieldRegexp() {
    return fieldRegexp;
  }
  
  /**
   * @param fieldRegexp The pattern (Java regular expression) field with 'fieldName' should match.
   */
  public void setFieldRegexp(String fieldRegexp) {
    this.fieldRegexp = fieldRegexp;
  }
  
  /**
   * @return The list of sub-matchers that must all match in incoming messages to identify
   * those belonging to this topic. This is a logical AND operation.
   * 
   */
  public List<WebsocketTopicMatcherDescriptor> getAnd() {
    return and;
  }
  
  /**
   * @param and The list of sub-matchers that must all match in incoming messages to identify
   * those belonging to this topic. This is a logical AND operation.
   * 
   */
  public void setAnd(List<WebsocketTopicMatcherDescriptor> and) {
    this.and = and;
  }
  
  /**
   * @return The list of sub-matchers where at least one must match in incoming messages to identify
   * those belonging to this topic. This is a logical OR operation.
   * 
   */
  public List<WebsocketTopicMatcherDescriptor> getOr() {
    return or;
  }
  
  /**
   * @param or The list of sub-matchers where at least one must match in incoming messages to identify
   * those belonging to this topic. This is a logical OR operation.
   * 
   */
  public void setOr(List<WebsocketTopicMatcherDescriptor> or) {
    this.or = or;
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
    WebsocketTopicMatcherDescriptor o = (WebsocketTopicMatcherDescriptor) other;
    return Objects.equals(this.fieldName, o.fieldName)
        && Objects.equals(this.fieldValue, o.fieldValue)
        && Objects.equals(this.fieldRegexp, o.fieldRegexp)
        && Objects.equals(this.and, o.and)
        && Objects.equals(this.or, o.or);
  }
  
  @Override
  public int compareTo(WebsocketTopicMatcherDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.fieldName, other.fieldName);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareObjects(this.fieldValue, other.fieldValue);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.fieldRegexp, other.fieldRegexp);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.and, other.and, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.or, other.or, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(fieldName, fieldValue, fieldRegexp, and, or);
  }
  
  @Override
  public WebsocketTopicMatcherDescriptor deepClone() {
    WebsocketTopicMatcherDescriptor clone = new WebsocketTopicMatcherDescriptor();
    clone.fieldName = this.fieldName;
    clone.fieldValue = this.fieldValue;
    clone.fieldRegexp = this.fieldRegexp;
    clone.and = CollectionUtil.deepCloneList(this.and, DeepCloneable::deepClone);
    clone.or = CollectionUtil.deepCloneList(this.or, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link WebsocketTopicMatcherDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String fieldName;
    private Object fieldValue;
    private String fieldRegexp;
    private List<WebsocketTopicMatcherDescriptor> and;
    private List<WebsocketTopicMatcherDescriptor> or;
    
    /**
     * Will set the value of <code>fieldName</code> field in the builder
     * @param fieldName The name of the field to match
     * @return Builder instance
     * @see #setFieldName(String)
     */
    public Builder fieldName(String fieldName)  {
      this.fieldName = fieldName;
      return this;
    }
    
    /**
     * Will set the value of <code>fieldValue</code> field in the builder
     * @param fieldValue The value the field with 'fieldName' should have to match.
     * @return Builder instance
     * @see #setFieldValue(Object)
     */
    public Builder fieldValue(Object fieldValue)  {
      this.fieldValue = fieldValue;
      return this;
    }
    
    /**
     * Will set the value of <code>fieldRegexp</code> field in the builder
     * @param fieldRegexp The pattern (Java regular expression) field with 'fieldName' should match.
     * @return Builder instance
     * @see #setFieldRegexp(String)
     */
    public Builder fieldRegexp(String fieldRegexp)  {
      this.fieldRegexp = fieldRegexp;
      return this;
    }
    
    /**
     * Will set the value of <code>and</code> field in the builder
     * @param and The list of sub-matchers that must all match in incoming messages to identify
     * those belonging to this topic. This is a logical AND operation.
     * 
     * @return Builder instance
     * @see #setAnd(List<WebsocketTopicMatcherDescriptor>)
     */
    public Builder and(List<WebsocketTopicMatcherDescriptor> and)  {
      this.and = and;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>and</code> list.
     * @param item Item to add to current <code>and</code> list
     * @return Builder instance
     * @see WebsocketTopicMatcherDescriptor#setAnd(List)
     */
    public Builder addToAnd(WebsocketTopicMatcherDescriptor item) {
      if (this.and == null) {
        this.and = CollectionUtil.createList();
      }
      this.and.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>or</code> field in the builder
     * @param or The list of sub-matchers where at least one must match in incoming messages to identify
     * those belonging to this topic. This is a logical OR operation.
     * 
     * @return Builder instance
     * @see #setOr(List<WebsocketTopicMatcherDescriptor>)
     */
    public Builder or(List<WebsocketTopicMatcherDescriptor> or)  {
      this.or = or;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>or</code> list.
     * @param item Item to add to current <code>or</code> list
     * @return Builder instance
     * @see WebsocketTopicMatcherDescriptor#setOr(List)
     */
    public Builder addToOr(WebsocketTopicMatcherDescriptor item) {
      if (this.or == null) {
        this.or = CollectionUtil.createList();
      }
      this.or.add(item);
      return this;
    }
    
    /**
     * @return a new instance of WebsocketTopicMatcherDescriptor using the values set in this builder
     */
    public WebsocketTopicMatcherDescriptor build() {
      WebsocketTopicMatcherDescriptor res = new WebsocketTopicMatcherDescriptor();
      res.fieldName = this.fieldName;
      res.fieldValue = this.fieldValue;
      res.fieldRegexp = this.fieldRegexp;
      res.and = CollectionUtil.deepCloneList(this.and, DeepCloneable::deepClone);
      res.or = CollectionUtil.deepCloneList(this.or, DeepCloneable::deepClone);
      return res;
    }
  }
}
