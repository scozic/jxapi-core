package com.scz.jxapi.exchanges.employee.gen.v1;

import javax.annotation.processing.Generated;

/**
 * Constants used in Employee exchange API wrapper {@link com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1Api} API group
 */
@Generated("com.scz.jxapi.generator.java.exchange.constants.ConstantsClassGenerator")
public class EmployeeV1Constants {
  
  private EmployeeV1Constants(){}
  
  /**
   * Regular employee profile
   */
  public static final String PROFILE_REGULAR = "REGULAR";
  
  /**
   * Admin employee profile
   */
  public static final String PROFILE_ADMIN = "ADMIN";
  
  /**
   * Value of eventType field in WS message for new employee added event
   */
  public static final String UPDATE_EMPLOYEE_TYPE_ADD = "ADD";
  
  /**
   * Value of eventType field in WS message for update of an existing employee event
   */
  public static final String UPDATE_EMPLOYEE_TYPE_UPATE = "UPDATE";
  
  /**
   * Value of eventType field in WS message for update of an existing employee event
   */
  public static final String UPDATE_EMPLOYEE_TYPE_DELETE = "DELETE";
}
