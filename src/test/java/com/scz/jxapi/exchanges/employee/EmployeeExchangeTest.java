package com.scz.jxapi.exchanges.employee;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.scz.jxapi.exchanges.employee.gen.EmployeeExchange;
import com.scz.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import com.scz.jxapi.exchanges.employee.gen.EmployeeProperties;
import com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1Constants;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpResponse;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.netutils.rest.javanet.HttpServerUtil;
import com.scz.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketListener;
import com.scz.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import com.scz.jxapi.netutils.websocket.mock.server.MockWebsocketServerEventType;

/**
 * Employee exchange server integration test.<br>
 * Will spawn a server on a random port and test employee exchange APIs against it.
 * @see EmployeeExchangeServer
 * @see EmployeeExchange
 */
public class EmployeeExchangeTest {
	
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
		config.setProperty(EmployeeProperties.BASE_HTTP_URL.getName(), server.getHttpBaseUrl());
		config.setProperty(EmployeeProperties.BASE_WEBSOCKET_URL.getName(), server.getWebSocketBaseUrl());
		this.exchange = new EmployeeExchangeImpl("testEmployeeClient", config);
		this.api = exchange.getEmployeeV1Api();
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
        RestResponse<List<Employee>> getAllResponse = api.getAllEmployees().get();
        Assert.assertTrue(getAllResponse.isOk());
        List<Employee> employees = getAllResponse.getResponse();
        Assert.assertNotNull(employees);
        Assert.assertTrue(employees.isEmpty());
	}
	
	@Test
	public void testAddEmployee_OneEmployeeOK() throws InterruptedException, ExecutionException, TimeoutException {
		Employee emp1 = createEmployee1();
		RestResponse<String> addResponse = api.addEmployee(emp1).get();
		Assert.assertTrue(addResponse.isOk());
		checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);
		
		RestResponse<List<Employee>> getAllResponse = api.getAllEmployees().get();
        Assert.assertTrue(getAllResponse.isOk());
        List<Employee> employees = getAllResponse.getResponse();
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
	public void testAddEmployee_TwoEmployeesOK() throws InterruptedException, ExecutionException, TimeoutException {
		Employee emp1 = createEmployee1();
		RestResponse<String> addResponse = api.addEmployee(emp1).get();
		Assert.assertTrue(addResponse.isOk());
		checkMessage(emp1, EmployeeWsEventType.EMPLOYEE_ADDED);
		
		Employee emp2 = createEmployee2();
		addResponse = api.addEmployee(emp2).get();
		Assert.assertTrue(addResponse.isOk());
		checkMessage(emp2, EmployeeWsEventType.EMPLOYEE_ADDED);
        
		RestResponse<List<Employee>> getAllResponse = api.getAllEmployees().get();
        Assert.assertTrue(getAllResponse.isOk());
        List<Employee> employees = getAllResponse.getResponse();
        Assert.assertNotNull(employees);
        Assert.assertEquals(2, employees.size());
        Assert.assertEquals(emp1, employees.get(0));
        Assert.assertEquals(emp2, employees.get(1));
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
        
		RestResponse<List<Employee>> getAllResponse = api.getAllEmployees().get();
        Assert.assertTrue(getAllResponse.isOk());
        List<Employee> employees = getAllResponse.getResponse();
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
        RestResponse<List<Employee>> getAllResponse = api.getAllEmployees().get();
        List<Employee> employees = getAllResponse.getResponse();
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
		HttpRequestExecutor executor = new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
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
		HttpRequestExecutor executor = new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
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


	private Employee createEmployee1() {
		Employee e = new Employee();
		e.setId(1);
		e.setFirstName("John");
		e.setLastName("Doe");
		e.setProfile(EmployeeV1Constants.PROFILE_ADMIN);
		return e;
	}
	
	private Employee createEmployee2() {
		Employee e = new Employee();
		e.setId(2);
		e.setFirstName("Lucy");
		e.setLastName("Smith");
		e.setProfile(EmployeeV1Constants.PROFILE_REGULAR);
		return e;
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
