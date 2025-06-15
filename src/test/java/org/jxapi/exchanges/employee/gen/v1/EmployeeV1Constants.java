package org.jxapi.exchanges.employee.gen.v1;

import javax.annotation.processing.Generated;

/**
 * Constants used in Employee exchange API wrapper {@link org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api} API group
 */
@Generated("org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator")
public class EmployeeV1Constants {
  
  private EmployeeV1Constants(){}
  
  /**
   * Default page size for paginated requests
   */
  public static final Integer DEFAULT_PAGE_SIZE = Integer.valueOf("10");
  
  /**
   * Maximum page size for paginated requests
   */
  public static final Integer MAX_PAGE_SIZE = Integer.valueOf("10000");
  
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
