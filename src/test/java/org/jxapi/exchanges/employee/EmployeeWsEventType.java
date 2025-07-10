package org.jxapi.exchanges.employee;

import org.jxapi.exchanges.employee.gen.EmployeeConstants;

public enum EmployeeWsEventType {

  EMPLOYEE_ADDED(EmployeeConstants.UPDATE_EMPLOYEE_TYPE_ADD), 
  EMPLOYEE_UPDATED(EmployeeConstants.UPDATE_EMPLOYEE_TYPE_UPATE), 
  EMPLOYEE_DELETED(EmployeeConstants.UPDATE_EMPLOYEE_TYPE_DELETE);
  
  public final String code;
  
  private EmployeeWsEventType(String eventType) {
    this.code = eventType;
  }
}
