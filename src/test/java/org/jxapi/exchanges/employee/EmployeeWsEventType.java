package org.jxapi.exchanges.employee;

import org.jxapi.exchanges.employee.gen.EmployeeConstants;

/**
 * Enumeration of WebSocket event types for Employee events.
 */
public enum EmployeeWsEventType {

  EMPLOYEE_ADDED(EmployeeConstants.UpdateEmployeeType.ADD), 
  EMPLOYEE_UPDATED(EmployeeConstants.UpdateEmployeeType.UPDATE), 
  EMPLOYEE_DELETED(EmployeeConstants.UpdateEmployeeType.DELETE);
  
  /**
   * The code representing the event type in message. Should be one of {@link EmployeeConstants.UpdateEmployeeType} enumerated values.
   */
  public final String code;
  
  private EmployeeWsEventType(String eventType) {
    this.code = eventType;
  }
}
