package org.jxapi.exchanges.employee;

import java.util.List;

import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;

public interface EmployeesDatabase {

  List<Employee> getAllEmployees();
  
  List<Employee> getAllEmployees(int page, int pageSize);
  
  Employee getEmployee(Integer id);
  
  void addEmployee(Employee employee) throws NullEmployeeIdException, EmployeeIdConflictException;
  
  Employee updateEmployee(Employee employee) throws NullEmployeeIdException;
  
  Employee deleteEmployee(Integer id);

}
