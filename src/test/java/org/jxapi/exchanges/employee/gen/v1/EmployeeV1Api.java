package org.jxapi.exchanges.employee.gen.v1;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.websocket.WebsocketListener;

/**
 * Employee v1 API<br>
 * Version 1 of the Employee API
 */
@Generated("org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceGenerator")
public interface EmployeeV1Api extends ExchangeApi {
  
  /**
   * Name of 'v1' API group.
   */
  String ID = "v1";
  /**
   * Name of <code>getEmployee</code> RestApi.
   */
   String GET_EMPLOYEE_REST_API = "getEmployee";
  
  /**
   * Name of <code>getAllEmployees</code> RestApi.
   */
   String GET_ALL_EMPLOYEES_REST_API = "getAllEmployees";
  
  /**
   * Name of <code>addEmployee</code> RestApi.
   */
   String ADD_EMPLOYEE_REST_API = "addEmployee";
  
  /**
   * Name of <code>updateEmployee</code> RestApi.
   */
   String UPDATE_EMPLOYEE_REST_API = "updateEmployee";
  
  /**
   * Name of <code>deleteEmployee</code> RestApi.
   */
   String DELETE_EMPLOYEE_REST_API = "deleteEmployee";
  /**
   * Name of <code>employeeUpdates</code> WsApi.
   */
   String EMPLOYEE_UPDATES_WS_API = "employeeUpdates";
  
  /**
   * Get employee details by ID
   * @param id Employee ID
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed. If successful, will provide response: Employee details response for requested employee ID
   * 
   * @see <a href="https://www.example.com/docs/employee/get">Reference documentation</a>
   */
  FutureRestResponse<Employee> getEmployee(Integer id);
  
  /**
   * Get all employees
   * @param request Page request parameters for 'getAllEmployees' rest endpoint paginated requests.
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.
   * @see <a href="https://www.example.com/docs/employee/getAll">Reference documentation</a>
   */
  FutureRestResponse<EmployeeV1GetAllEmployeesResponse> getAllEmployees(EmployeeV1GetAllEmployeesRequest request);
  
  /**
   * Add a new employee
   * @param request Employee to add
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.
   * @see <a href="https://www.example.com/docs/employee/add">Reference documentation</a>
   */
  FutureRestResponse<String> addEmployee(Employee request);
  
  /**
   * Update an existing employee
   * @param request Employee to update
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.
   * @see <a href="https://www.example.com/docs/employee/add">Reference documentation</a>
   */
  FutureRestResponse<String> updateEmployee(Employee request);
  
  /**
   * Delete an employee
   * @param id Employee ID
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.
   * @see <a href="https://www.example.com/docs/employee/delete">Reference documentation</a>
   */
  FutureRestResponse<String> deleteEmployee(Integer id);
  
  /**
   * Subscribe to employeeUpdates stream.<br>
   * Employee updates websocket
   * 
   * @param listener listener that will receive incoming messages
   * @return client subscriptionId to use for unsubscription using {@link #unsubscribeEmployeeUpdates(String)}
   * @see <a href="https://www.example.com/docs/employee/updates">Reference documentation</a>
   */
  String subscribeEmployeeUpdates(WebsocketListener<EmployeeV1EmployeeUpdatesMessage> listener);
  
  /**
   * Unsubscribe from employeeUpdates stream.
   * 
   * @param subscriptionId client subscription ID
   * @return <code>true</code> if given <code>subscriptionId</code> was found.
   * @see #subscribeEmployeeUpdates(WebsocketListener)
   */
  boolean unsubscribeEmployeeUpdates(String subscriptionId);
}
