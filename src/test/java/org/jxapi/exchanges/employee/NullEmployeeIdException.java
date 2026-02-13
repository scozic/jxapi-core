package org.jxapi.exchanges.employee;

import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;

public class NullEmployeeIdException extends Exception {

  private static final long serialVersionUID = -5279807627899792627L;
  
  private final Employee employee;
  
  public NullEmployeeIdException(Employee employee) {
    super("Employee ID cannot be null:" + employee);
    this.employee = employee;
  }

  public Employee getEmployee() {
    return employee;
  }

}
