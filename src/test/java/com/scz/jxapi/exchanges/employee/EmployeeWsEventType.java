package com.scz.jxapi.exchanges.employee;

import com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1Constants;

public enum EmployeeWsEventType {

	EMPLOYEE_ADDED(EmployeeV1Constants.UPDATE_EMPLOYEE_TYPE_ADD), 
	EMPLOYEE_UPDATED(EmployeeV1Constants.UPDATE_EMPLOYEE_TYPE_UPATE), 
	EMPLOYEE_DELETED(EmployeeV1Constants.UPDATE_EMPLOYEE_TYPE_DELETE);
	
	public final String code;
	
	private EmployeeWsEventType(String eventType) {
		this.code = eventType;
	}
}
