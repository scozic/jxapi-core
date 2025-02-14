package com.scz.jxapi.exchanges.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee;

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

}
