package org.jxapi.exchanges.employee;

public class EmployeeIdConflictException extends Exception {

  private static final long serialVersionUID = -4920815880843301622L;
  private final Integer id;
  
  public EmployeeIdConflictException(Integer id) {
    super("Employee with ID " + id + " already exists");
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

}
