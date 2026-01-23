package org.jxapi.exchanges.employee;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchanges.employee.gen.EmployeeConstants;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import org.jxapi.exchanges.employee.gen.EmployeeProperties;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.rest.javanet.HttpServerUtil;
import org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import org.jxapi.netutils.rest.pagination.PaginationUtil;
import org.jxapi.netutils.websocket.mock.MockWebsocketListener;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEventType;
import org.slf4j.LoggerFactory;

/**
 * Employee exchange server integration test.<br>
 * Will spawn a server on a random port and test employee exchange APIs against it.
 * @see EmployeeExchangeServer
 * @see EmployeeExchange
 */
public class EmployeeExchangeTest {
  
  private static final EmployeeV1GetAllEmployeesRequest DEFAULT_GET_ALL_EMPLOYEES_REQUEST = EmployeeExchangeServer.creatGetAllEmployeesRequestFromUrl(null);
  
  private int httpPort;
  private int webSocketPort;
  private EmployeeExchange exchange;
  private EmployeeV1Api api;
  private EmployeeExchangeServer server;
  private MockWebsocketListener<EmployeeV1EmployeeUpdatesMessage> wsListener;
  
  @Before
  public void setUp() throws IOException, TimeoutException {
    this.httpPort = HttpServerUtil.findAvailablePort();
    this.webSocketPort = HttpServerUtil.findAvailablePort(this.httpPort + 1);
    this.server = new EmployeeExchangeServer(httpPort, webSocketPort);
    this.server.start();
    Properties config = new Properties();
    config.setProperty(EmployeeProperties.Server.BASE_HTTP_URL.getName(), server.getHttpBaseUrl());
    config.setProperty(EmployeeProperties.Server.BASE_WEBSOCKET_URL.getName(), server.getWebSocketBaseUrl());
    this.exchange = new EmployeeExchangeImpl("testEmployeeClient", config);
    this.api = exchange.getV1Api();
    this.wsListener = new MockWebsocketListener<>();
    api.subscribeEmployeeUpdates(wsListener);
    checkWsClientConnect();
  }
  
  @After
  public void tearDown() {
    exchange.dispose();
    this.server.stop();
  }
  
  @Test
  public void testGetAllEmployees_Empty() throws InterruptedException, ExecutionException {
        RestResponse<EmployeeV1GetAllEmployeesResponse> getAllResponse = api.getAllEmployees(DEFAULT_GET_ALL_EMPLOYEES_REQUEST).get();
        Exception ex = getAllResponse.getException();
        if (ex != null) {
          LoggerFactory.getLogger(getClass()).error("Error while getting all employees", ex);
        }
        Assert.assertTrue(getAllResponse.isOk());
        List<Employee> employees = getAllResponse.getResponse().getEmployees();
        Assert.assertNotNull(employees);
        Assert.assertTrue(employees.isEmpty());
  }
  
  @Test
  public void testGetAllEmployees_MultiplePages() throws InterruptedException, ExecutionException, TimeoutException {
    // Add three employees, each with a different ID
    Employee emp1 = createEmployee1();
    RestResponse<String> addResponse = api.addEmployee(emp1).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);

    Employee emp2 = createEmployee2();
    addResponse = api.addEmployee(emp2).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp2, EmployeeWsEventType.EMPLOYEE_ADDED);
    
    Employee emp3 = createEmployee3();
    addResponse = api.addEmployee(emp3).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp3, EmployeeWsEventType.EMPLOYEE_ADDED); 
            // First request should return empty list
    
    // Request first page.
    EmployeeV1GetAllEmployeesRequest request1 = EmployeeV1GetAllEmployeesRequest.builder()
        .page(1)
        .size(2) // Set page size to 2
        .build();
    
    RestResponse<List<EmployeeV1GetAllEmployeesResponse>> getAllResponses = PaginationUtil.fetchAllPages(api.getAllEmployees(request1)).get();
    Assert.assertTrue(getAllResponses.isOk());
    
    EmployeeV1GetAllEmployeesResponse response1 = getAllResponses.getResponse().get(0);
    Assert.assertNotNull(response1);
    Assert.assertEquals(Integer.valueOf(1), response1.getPage());
    Assert.assertEquals(Integer.valueOf(2), response1.getTotalPages());
    List<Employee> employees = response1.getEmployees();
    Assert.assertNotNull(employees);
    Assert.assertEquals(2, employees.size());
    Assert.assertEquals(emp1, employees.get(0));
    Assert.assertEquals(emp2, employees.get(1));
     
    EmployeeV1GetAllEmployeesResponse response2 = getAllResponses.getResponse().get(1);
    Assert.assertNotNull(response2);
    Assert.assertEquals(Integer.valueOf(2), response2.getPage());
    Assert.assertEquals(Integer.valueOf(2), response2.getTotalPages());
    employees = response2.getEmployees();
    Assert.assertNotNull(employees);
    Assert.assertEquals(1, employees.size());
    Assert.assertEquals(emp3, employees.get(0));
  }
  
  @Test
  public void testAddEmployee_OneEmployeeOK() throws InterruptedException, ExecutionException, TimeoutException {
    Employee emp1 = createEmployee1();
    RestResponse<String> addResponse = api.addEmployee(emp1).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);

    List<Employee> employees = getAllEmployees();
    Assert.assertNotNull(employees);
    Assert.assertEquals(1, employees.size());
    Assert.assertEquals(emp1, employees.get(0));

    RestResponse<Employee> getResponse = api.getEmployee(1).get();
    Assert.assertTrue(getResponse.isOk());
    Employee emp1Retrieved = getResponse.getResponse();
    Assert.assertEquals(emp1, emp1Retrieved);
  }
  
  @Test
  public void testAddEmployee_NullEmployeeID() throws InterruptedException, ExecutionException {
    Employee emp1 = createEmployee1();
    emp1.setId(null);
    RestResponse<String> addResponse = api.addEmployee(emp1).get();
    Assert.assertFalse(addResponse.isOk());
    Assert.assertEquals(400, addResponse.getHttpResponse().getResponseCode());
  }
  
  @Test
  public void testAddEmployee_ThreeEmployeesOK() throws InterruptedException, ExecutionException, TimeoutException {
    Employee emp1 = createEmployee1();
    RestResponse<String> addResponse = api.addEmployee(emp1).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);

    Employee emp2 = createEmployee2();
    addResponse = api.addEmployee(emp2).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp2, EmployeeWsEventType.EMPLOYEE_ADDED);
    
    Employee emp3 = createEmployee3();
    addResponse = api.addEmployee(emp3).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp3, EmployeeWsEventType.EMPLOYEE_ADDED);

    List<Employee> employees = getAllEmployees();
    Assert.assertNotNull(employees);
    Assert.assertEquals(3, employees.size());
    Assert.assertEquals(emp1, employees.get(0));
    Assert.assertEquals(emp2, employees.get(1));
    Assert.assertEquals(emp3, employees.get(2));
  }
  
  @Test
  public void testAddEmployee_DuplicateEmployeeID() throws InterruptedException, ExecutionException, TimeoutException {
    Employee emp1 = createEmployee1();
    RestResponse<String> addResponse = api.addEmployee(emp1).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);
    Employee emp2 = createEmployee2();
    emp2.setId(emp1.getId());
    addResponse = api.addEmployee(emp2).get();
    Assert.assertFalse(addResponse.isOk());
    Assert.assertEquals(409, addResponse.getHttpResponse().getResponseCode());
  }
  
  @Test
  public void testUpdateEmployee_OK() throws InterruptedException, ExecutionException, TimeoutException {
    Employee emp1 = createEmployee1();
    RestResponse<String> updateResponse = api.updateEmployee(emp1).get();
    Assert.assertTrue(updateResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_UPDATED);

    emp1.setFirstName("Jane");
    updateResponse = api.updateEmployee(emp1).get();
    Assert.assertTrue(updateResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_UPDATED);

    List<Employee> employees = getAllEmployees();
    Assert.assertNotNull(employees);
    Assert.assertEquals(1, employees.size());
    Assert.assertEquals(emp1, employees.get(0));
  }
  
  @Test
  public void testUpdateEmployee_NullEmployeeID() throws InterruptedException, ExecutionException {
    Employee emp1 = createEmployee1();
    emp1.setId(null);
    RestResponse<String> updateResponse = api.updateEmployee(emp1).get();
    Assert.assertFalse(updateResponse.isOk());
    Assert.assertEquals(400, updateResponse.getHttpResponse().getResponseCode());
  }
  
  @Test
  public void testDeleteEmployee_OK() throws InterruptedException, ExecutionException, TimeoutException {
    Employee emp1 = createEmployee1();
    RestResponse<String> addResponse = api.addEmployee(emp1).get();
    Assert.assertTrue(addResponse.isOk());
    checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);
    
    RestResponse<String> deleteResponse = api.deleteEmployee(emp1.getId()).get();
        Assert.assertTrue(deleteResponse.isOk());
        List<Employee> employees = getAllEmployees();
        Assert.assertNotNull(employees);
        Assert.assertTrue(employees.isEmpty());
        
  }
  
  @Test
  public void testDeleteEmployee_NotFound() throws InterruptedException, ExecutionException {
    RestResponse<String> deleteResponse = api.deleteEmployee(1).get();
        Assert.assertFalse(deleteResponse.isOk());
        Assert.assertEquals(404, deleteResponse.getHttpResponse().getResponseCode());
  }
  
  @Test
  public void testGetEmployee_InvalidUrlParam() throws InterruptedException, ExecutionException {
    HttpRequestExecutor executor = new JavaNetHttpRequestExecutor("EmployeeExchangeTest");
    try {
      HttpRequest request = new HttpRequest();
      request.setUrl(server.getHttpBaseUrl() + "/v1/employee/invalid");
      request.setHttpMethod(HttpMethod.GET);
      HttpResponse response = executor.execute(request).get();
      Assert.assertEquals(500, response.getResponseCode());
    } finally {
            executor.dispose();
        }  
  }
  
  @Test
  public void testAddEmployee_InvalidBody() throws InterruptedException, ExecutionException {
    doTestAddOrUpdateEmployeeInvalidBody(HttpMethod.POST);
  }
  
  @Test
  public void testUpdateEmployee_InvalidBody() throws InterruptedException, ExecutionException {
    doTestAddOrUpdateEmployeeInvalidBody(HttpMethod.PUT);
  }
  
  private void doTestAddOrUpdateEmployeeInvalidBody(HttpMethod method) throws InterruptedException, ExecutionException {
    HttpRequestExecutor executor = new JavaNetHttpRequestExecutor("EmployeeExchangeTest");
    try {
      HttpRequest request = new HttpRequest();
      request.setUrl(server.getHttpBaseUrl() + "/v1/employee");
      request.setHttpMethod(method);
      request.setBody("Invalid body");
      HttpResponse response = executor.execute(request).get();
      Assert.assertEquals(400, response.getResponseCode());
    } finally {
            executor.dispose();
        }  
  }

  private List<Employee> getAllEmployees() throws InterruptedException, ExecutionException {
    RestResponse<EmployeeV1GetAllEmployeesResponse> getAllResponse = api.getAllEmployees(DEFAULT_GET_ALL_EMPLOYEES_REQUEST).get();
    Assert.assertTrue(getAllResponse.isOk());
    return getAllResponse.getResponse().getEmployees();
  }
  
  private Employee createEmployee1() {
    return Employee.builder()
        .id(1)
        .firstName("John")
        .lastName("Doe")
        .profile(EmployeeConstants.Profile.ADMIN)
        .build();
  }
  
  private Employee createEmployee2() {
    return Employee.builder()
      .id(2)
      .firstName("Lucy")
      .lastName("Smith")
      .profile(EmployeeConstants.Profile.REGULAR)
      .build();
  }
  
  private Employee createEmployee3() {
    return Employee.builder()
      .id(3)
      .firstName("Bob")
      .lastName("Johnson")
      .profile(EmployeeConstants.Profile.REGULAR)
      .build();
  }
  
  private void checkMessage(Employee employee, EmployeeWsEventType eventType) throws TimeoutException {
    EmployeeV1EmployeeUpdatesMessage message = wsListener.waitUntilCount(1, 1000).pop();
    Assert.assertNotNull(message);
    Assert.assertEquals(eventType.code, message.getEventType());
    Assert.assertEquals(employee, message.getEmployee());
  }
  
  private void checkWsClientConnect() throws TimeoutException {
    Assert.assertEquals(MockWebsocketServerEventType.CLIENT_CONNECT, server.popWsEvent().getType());
    MockWebsocketServerEvent event = server.popWsEvent();
    Assert.assertEquals(MockWebsocketServerEventType.MESSAGE_RECEIVED, event.getType());
    Assert.assertEquals("Hello!", event.getMessage());
  }
  
}
