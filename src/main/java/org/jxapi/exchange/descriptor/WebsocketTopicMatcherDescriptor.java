package org.jxapi.exchange.descriptor;

import java.util.List;

import org.jxapi.util.EncodingUtil;

/**
 * When subscribing to a websocket endpoint, a topic is extracted from the
 * message stream and matched against the topic of the endpoint.
 * The way the message is matched against the topic is described using this
 * descriptor.<br>
 * Depending on the properties set, the matching can be:
 * <ul>
 * <li>A logical AND of several field matchers</li>
 * <li>A logical OR of several field matchers</li>
 * <li>A single field matcher matching a field value</li>
 * <li>A single field matcher matching a field value using a regular expression</li>
 * </ul>
 * <p>
 * This means {@link #getAnd()} and {@link #getOr()} are mutually exclusive, and when neither is set,
 * then only fieldName with either {@link #getFieldValue()} or {@link #getFieldRegexp()} must be set.
 * see {@link WebsocketEndpointDescriptor}
 */
public class WebsocketTopicMatcherDescriptor {
  
  private String fieldName;
  
  private Object fieldValue;
  
  private String fieldRegexp;

  private List<WebsocketTopicMatcherDescriptor> or;

  /**
   * @return When this descriptor is a logical OR, the list of operand field matchers that must match the topic of the endpoint.
   */
  public List<WebsocketTopicMatcherDescriptor> getOr() {
    return or;
  }

  /**
   * @param operands Set the list of operand field matchers that can match the
   *                 topic of the endpoint when this descriptor is a logical OR.
   */
  public void setOr(List<WebsocketTopicMatcherDescriptor> operands) {
    this.or = operands;
  }
  
  private List<WebsocketTopicMatcherDescriptor> and;


  /**
   * @return When this descriptor is a logical AND, the list of operand field
   *         matchers that must match the topic of the endpoint.
   */
  public List<WebsocketTopicMatcherDescriptor> getAnd() {
    return and;
  }
  
  /**
   * @param operands Set the list of operand field matchers that must match the
   *                 topic of the endpoint when this descriptor is a logical AND.
   */
  public void setAnd(List<WebsocketTopicMatcherDescriptor> operands) {
    this.and = operands;
  }
  
  /**
   * @return The name of the field to match value in message against value or regexp.
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   * Set the name of the field to match value in message against value or regexp.
   * 
   * @param fieldName The field name
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }
  
  /**
   * @return The value to match the field value against.
   */
  public Object getFieldValue() {
    return fieldValue;
  }

  /**
   * Set the regular expression to match the field value against.
   * 
   * @param fieldValue The field value regular expression
   */
  public void setFieldValue(Object fieldValue) {
    this.fieldValue = fieldValue;
  }
  
  /**
   * @return The regular expression to match the field value against.
   */
  public String getFieldRegexp() {
    return fieldRegexp;
  }

  /**
   * Set the regular expression to match the field value against.
   * 
   * @param fieldRegexp The field value regular expression
   */
  public void setFieldRegexp(String fieldRegexp) {
    this.fieldRegexp = fieldRegexp;
  }

  /**
   * @return a string representation of the object. see
   *         {@link EncodingUtil#pojoToString(Object)}
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
