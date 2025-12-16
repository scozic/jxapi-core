package org.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeV1EmployeeUpdatesMessageDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.serializers.EmployeeV1EmployeeUpdatesMessageSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Employee update message
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = EmployeeV1EmployeeUpdatesMessageSerializer.class)
@JsonDeserialize(using = EmployeeV1EmployeeUpdatesMessageDeserializer.class)
public class EmployeeV1EmployeeUpdatesMessage implements Pojo<EmployeeV1EmployeeUpdatesMessage> {
  
  private static final long serialVersionUID = -1771730097053933571L;
  
  /**
   * @return A new builder to build {@link EmployeeV1EmployeeUpdatesMessage} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String eventType;
  private Employee employee;
  
  /**
   * @return Type of event, e.g. one of {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#ADD}, {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#UPDATE} or {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#DELETE} (see {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType} constants).
   */
  public String getEventType() {
    return eventType;
  }
  
  /**
   * @param eventType Type of event, e.g. one of {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#ADD}, {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#UPDATE} or {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#DELETE} (see {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType} constants).
   */
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  /**
   * @return Employee that was updated
   */
  public Employee getEmployee() {
    return employee;
  }
  
  /**
   * @param employee Employee that was updated
   */
  public void setEmployee(Employee employee) {
    this.employee = employee;
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
    EmployeeV1EmployeeUpdatesMessage o = (EmployeeV1EmployeeUpdatesMessage) other;
    return Objects.equals(this.eventType, o.eventType)
        && Objects.equals(this.employee, o.employee);
  }
  
  @Override
  public int compareTo(EmployeeV1EmployeeUpdatesMessage other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.eventType, other.eventType);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.employee, other.employee);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(eventType, employee);
  }
  
  @Override
  public EmployeeV1EmployeeUpdatesMessage deepClone() {
    EmployeeV1EmployeeUpdatesMessage clone = new EmployeeV1EmployeeUpdatesMessage();
    clone.eventType = this.eventType;
    clone.employee = this.employee != null ? this.employee.deepClone() : null;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link EmployeeV1EmployeeUpdatesMessage}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String eventType;
    private Employee employee;
    
    /**
     * Will set the value of <code>eventType</code> field in the builder
     * @param eventType Type of event, e.g. one of {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#ADD}, {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#UPDATE} or {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType#DELETE} (see {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.UpdateEmployeeType} constants).
     * @return Builder instance
     * @see #setEventType(String)
     */
    public Builder eventType(String eventType)  {
      this.eventType = eventType;
      return this;
    }
    
    /**
     * Will set the value of <code>employee</code> field in the builder
     * @param employee Employee that was updated
     * @return Builder instance
     * @see #setEmployee(Employee)
     */
    public Builder employee(Employee employee)  {
      this.employee = employee;
      return this;
    }
    
    /**
     * @return a new instance of EmployeeV1EmployeeUpdatesMessage using the values set in this builder
     */
    public EmployeeV1EmployeeUpdatesMessage build() {
      EmployeeV1EmployeeUpdatesMessage res = new EmployeeV1EmployeeUpdatesMessage();
      res.eventType = this.eventType;
      res.employee = this.employee != null ? this.employee.deepClone() : null;
      return res;
    }
  }
}
