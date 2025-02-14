package com.scz.jxapi.exchanges.employee;

public class EmployeeIdConflictException extends Exception {

	private final Integer id;
	
	public EmployeeIdConflictException(Integer id) {
		super("Employee with ID " + id + " already exists");
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
