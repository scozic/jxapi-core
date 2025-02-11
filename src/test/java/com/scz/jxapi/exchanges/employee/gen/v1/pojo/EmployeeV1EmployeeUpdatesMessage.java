package com.scz.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.employee.gen.v1.serializers.EmployeeV1EmployeeUpdatesMessageSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Employee V1 API employeeUpdates websocket endpoint request<br>
 * Employee updates websocket
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = EmployeeV1EmployeeUpdatesMessageSerializer.class)
public class EmployeeV1EmployeeUpdatesMessage {
  private Employee employee;
  private String eventType;
  
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
  
  /**
   * @return Type of event. Can be 'ADD', 'UPDATE' or 'DELETE'
   */
  public String getEventType() {
    return eventType;
  }
  
  /**
   * @param eventType Type of event. Can be 'ADD', 'UPDATE' or 'DELETE'
   */
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    EmployeeV1EmployeeUpdatesMessage o = (EmployeeV1EmployeeUpdatesMessage) other;
    return Objects.equals(employee, o.employee)
            && Objects.equals(eventType, o.eventType);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(employee, eventType);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
