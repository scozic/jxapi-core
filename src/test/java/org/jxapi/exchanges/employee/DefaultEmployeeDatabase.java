package org.jxapi.exchanges.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxapi.exchanges.employee.gen.EmployeeConstants;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;

public class DefaultEmployeeDatabase implements EmployeesDatabase {

  private final Map<Integer, Employee> employees = new HashMap<>();

  @Override
  public List<Employee> getAllEmployees() {
    return List.copyOf(employees.values());
  }

  @Override
  public Employee getEmployee(Integer id) {
    return employees.get(id);
  }

  @Override
  public void addEmployee(Employee employee) throws NullEmployeeIdException, EmployeeIdConflictException {
    Integer id = employee.getId();
    if (id == null) {
      throw new NullEmployeeIdException(employee);
    }
    if (employees.containsKey(id)) {
      throw new EmployeeIdConflictException(id);
    }
    
    employees.put(id, employee);
  }

  @Override
  public Employee updateEmployee(Employee employee) throws NullEmployeeIdException {
    Integer id = employee.getId();
    if (id == null) {
      throw new NullEmployeeIdException(employee);
    }
    
    return employees.put(id, employee);
  }

  @Override
  public Employee deleteEmployee(Integer id) {
    return employees.remove(id);
  }

  @Override
  public List<Employee> getAllEmployees(int page, int pageSize) {
    if (page < 1) {
      throw new IllegalArgumentException("Page number must be greater than 0");
    }
    if (pageSize < 1) {
      throw new IllegalArgumentException("Page size must be greater than 0");
    }
    if (pageSize > EmployeeConstants.MAX_PAGE_SIZE) {
      throw new IllegalArgumentException("Page size must not exceed " + EmployeeConstants.MAX_PAGE_SIZE);
    }
    return employees.values().stream().skip((page - 1) * pageSize).limit(pageSize).toList();
  }

}
