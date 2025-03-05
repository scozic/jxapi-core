package com.scz.jxapi.exchanges.employee;

import com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee;

public class NullEmployeeIdException extends Exception {

	private final Employee employee;
	
	public NullEmployeeIdException(Employee employee) {
		super("Employee ID cannot be null:" + employee);
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

}
