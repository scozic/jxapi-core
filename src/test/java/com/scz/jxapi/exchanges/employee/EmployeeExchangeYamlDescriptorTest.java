package com.scz.jxapi.exchanges.employee;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.netutils.rest.HttpMethod;

/**
 * Test loading of exchange descriptor from YAML file 'EmployeeExchange.yaml'.
 */
public class EmployeeExchangeYamlDescriptorTest {
	
	/*
name: "Employee"
description: > 
Employee exchange is a demo exchange REST APIs to get, add, delete and 
update employees and a websocket endpoint to get notified of updates from an employee database.<br>
A server can be started using TODO class to serve these APIs.<br>
The URL of the server must be set using the baseUrl property.<br>
Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.
docUrl: "https://www.example.com/docs/employee"
properties:
- name: "baseHttpUrl"
description: "Base URL for REST endpoints the Employee Exchange API"
- name: "baseWebsocketUrl"
description: "Base URL of the Employee Exchange API"   
constants:
- name: "baseUrlPattern"
description: "Value to replace in HTTP requests url with base URL with value of baseUrl"
value: "BASEURL"
- name: "profileRegular"
description: "Regular employee profile"
value: "regular"
- name: "profileAdmin"
description: "Admin employee profile"
value: "Admin"
basePackage: "com.scz.jxapi.exchanges.employee.gen"
httpUrl: "BASEURL"
httpRequestInterceptorFactory: "com.scz.jxapi.exchanges.employee.net.EmployeeHttpRequestInterceptorFactory"
apis:
- name: V1
description: "Version 1 of the Employee API"
httpUrl: "v1"
constants:
- name: "profileRegular"
  description: "Regular employee profile"
  value: "regular"
- name: "profileAdmin"
  description: "Admin employee profile"
  value: "Admin"
- name: "updateEmployeeTypeAdd"
  description: "Value of eventType field in WS message for new employee added event"
  value: "ADD"
- name: "updateEmployeeTypeUpate"
  description: "Value of eventType field in WS message for update of an existing employee event"
  value: "UPDATE"
- name: "updateEmployeeTypeDelete"
  description: "Value of eventType field in WS message for update of an existing employee event"
  value: "DELETE"      
restEndpoints:
- name: "getEmployee"
  description: "Get employee details by ID"
  httpMethod: "GET"
  docUrl: "https://www.example.com/docs/employee/get"
  url: "/employee"
  urlParameters: "/${id}"
  request:
   name: "id"
   type: "INT"
   description: "Employee ID"
   sampleValue: 1
  response:
   objectName: "Employee"
   type: "OBJECT_LIST"
   properties:
    - name: "id"
      type: "INT"
      description: "Employee ID"
      sampleValue: 1
    - name: "firstName"
      type: "STRING"
      description: "Employee First Name"
      sampleValue: "John"
    - name: "lastName"
      type: "STRING"
      description: "Employee last lame"
      sampleValue: "Doe"
    - name: "profile"
      type: "STRING"
      description: "Employee profile. Can be 'regular' or 'admin'"
      sampleValue: "regular"
- name: "getAllEmployees"
  description: "Get all employees"
  httpMethod: "GET"
  docUrl: "https://www.example.com/docs/employee/getAll"
  url: "/employees"
  response:
   objectName: "Employee"
   type: "OBJECT_LIST"          
- name: "addEmployee"
  description: "Add a new employee"
  httpMethod: "POST"
  docUrl: "https://www.example.com/docs/employee/add"
  url: "/employee"
  request:
   description: "Employee to add"
   objectName: "Employee"
- name: "updateEmployee"
  description: "Update an existing employee"
  httpMethod: "PUT"
  docUrl: "https://www.example.com/docs/employee/add"
  url: "/employee"
  request:
   description: "Employee to add"
   objectName: "Employee"           
- name: "deleteEmployee"
  description: "Delete an employee"
  httpMethod: "DELETE"
  docUrl: "https://www.example.com/docs/employee/delete"
  url: "/employee"
  urlParameters: "/${id}"
  request:
   name: "id"
   type: "INT"
   description: "Employee ID"
   sampleValue: 1
websocketUrl: "BASEURL/v1"
websocketHookFactory: "com.scz.jxapi.exchanges.employee.net.EmployeeWebsocketHookFactory"  
websocketEndpoints:
- name: "employeeUpdates"
  description: "Employee updates websocket"
  docUrl: "https://www.example.com/docs/employee/updates"
  message:
   description: "Employee update message"
   properties:
    - name: "eventType"
      type: "STRING"
      description: "Type of event. Can be 'ADD', 'UPDATE' or 'DELETE'"
      sampleValue: 1
    - name: "employee"
      objectName: "Employee"
      description: "Employee that was updated"
      sampleValue: "John"
   

	 */

	@Test
	public void testLoadEmployeeExchangeYamlDescriptor() throws IOException {
		Path descriptorPath = Paths.get(".", "src", "test", "resources", "EmployeeExchange.yaml");
		ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(descriptorPath);
		checkEmployeeExchange(exchangeDescriptor);
	}
	
	@Test
	public void testLoadEmployeeExchangeFromYamlDescriptorSplitInMultipleFiles() throws IOException {
		Path descriptorPath = Paths.get(".", "src", "test", "resources", "employeeExchange");
		List<ExchangeDescriptor> exchangeDescriptors = ExchangeDescriptorParser.collectAndMergeExchangeDescriptors(descriptorPath);
		Assert.assertEquals(1, exchangeDescriptors.size());
		checkEmployeeExchange(exchangeDescriptors.get(0));
	}
	
	private void checkEmployeeExchange(ExchangeDescriptor exchangeDescriptor) {
		Assert.assertEquals("Employee", exchangeDescriptor.getName());
		Assert.assertEquals(
				"Employee exchange is a demo exchange REST APIs to get, add, delete and  update employees and "
				+ "a websocket endpoint to get notified of updates from an employee database.<br> A server can be started "
				+ "using TODO class to serve these APIs.<br> The URL of the server must be set using the baseUrl property."
				+ "<br> Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and "
				+ "its properties defined only once.\n",
				exchangeDescriptor.getDescription());
		
		Assert.assertEquals("https://www.example.com/docs/employee", exchangeDescriptor.getDocUrl());
		Assert.assertEquals("com.scz.jxapi.exchanges.employee.gen", exchangeDescriptor.getBasePackage());
		List<DefaultConfigProperty> properties = exchangeDescriptor.getProperties();
		Assert.assertEquals(2, properties.size());
		ConfigProperty property = properties.get(0);
		Assert.assertEquals("baseHttpUrl", property.getName());
		Assert.assertEquals("Base URL for REST endpoints the Employee Exchange API", property.getDescription());
		property = properties.get(1);
		Assert.assertEquals("baseWebsocketUrl", property.getName());
		Assert.assertEquals("Base URL for websocket endpoints of the Employee Exchange API", property.getDescription());
		
		List<Constant> constants = exchangeDescriptor.getConstants();
		Assert.assertEquals(1, constants.size());
		Constant constant = constants.get(0);
		Assert.assertEquals("baseUrlPattern", constant.getName());
		Assert.assertEquals("Value to replace in HTTP requests url with base URL with value of baseUrl", constant.getDescription());
		Assert.assertEquals("BASEURL", constant.getValue());
		
		Assert.assertEquals("BASEURL", exchangeDescriptor.getHttpUrl());
		Assert.assertEquals("com.scz.jxapi.exchanges.employee.net.EmployeeHttpRequestInterceptorFactory", exchangeDescriptor.getHttpRequestInterceptorFactory());
		Assert.assertEquals(1, exchangeDescriptor.getApis().size());
		checkEmployeeExchangeV1ApiGroup(exchangeDescriptor.getApis().get(0));
	}
	
	private void checkEmployeeExchangeV1ApiGroup(ExchangeApiDescriptor api) {
		Assert.assertEquals("V1", api.getName());
		Assert.assertEquals("Version 1 of the Employee API", api.getDescription());
		Assert.assertEquals("v1", api.getHttpUrl());
		checkEmployeeExchangeV1ApiGroupConstants(api.getConstants());
		List<RestEndpointDescriptor> restEndpoints = api.getRestEndpoints();
		Assert.assertEquals(5, restEndpoints.size());
		checkEmployeeExchangeV1ApiGroupGetEmployeeRestEndpoint(restEndpoints.get(0));
		checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpoint(restEndpoints.get(1));
		checkEmployeeExchangeV1ApiGroupAddEmployeesRestEndpoint(restEndpoints.get(2));
		checkEmployeeExchangeV1ApiGroupUpdateEmployeesRestEndpoint(restEndpoints.get(3));
		checkEmployeeExchangeV1ApiGroupDeleteEmployeesRestEndpoint(restEndpoints.get(4));
	}

	private void checkEmployeeExchangeV1ApiGroupConstants(List<Constant> constants) {
		Assert.assertEquals(5, constants.size());
		
		Constant constant = constants.get(0);
		Assert.assertEquals("profileRegular", constant.getName());
		Assert.assertEquals("Regular employee profile", constant.getDescription());
		Assert.assertEquals("REGULAR", constant.getValue());
		
		constant = constants.get(1);
		Assert.assertEquals("profileAdmin", constant.getName());
		Assert.assertEquals("Admin employee profile", constant.getDescription());
		Assert.assertEquals("ADMIN", constant.getValue());
		
		constant = constants.get(2);
		Assert.assertEquals("updateEmployeeTypeAdd", constant.getName());
		Assert.assertEquals("Value of eventType field in WS message for new employee added event", constant.getDescription());
		Assert.assertEquals("ADD", constant.getValue());
		
		constant = constants.get(3);
		Assert.assertEquals("updateEmployeeTypeUpate", constant.getName());
		Assert.assertEquals("Value of eventType field in WS message for update of an existing employee event", constant.getDescription());
		Assert.assertEquals("UPDATE", constant.getValue());
		
		constant = constants.get(4);
		Assert.assertEquals("updateEmployeeTypeDelete", constant.getName());
		Assert.assertEquals("Value of eventType field in WS message for update of an existing employee event", constant.getDescription());
	}
	
	private void checkEmployeeExchangeV1ApiGroupGetEmployeeRestEndpoint(RestEndpointDescriptor restEndpointGetEmployee) {
		Assert.assertEquals("getEmployee", restEndpointGetEmployee.getName());
		Assert.assertEquals("Get employee details by ID", restEndpointGetEmployee.getDescription());
		Assert.assertEquals(HttpMethod.GET, restEndpointGetEmployee.getHttpMethod());
		Assert.assertEquals("https://www.example.com/docs/employee/get", restEndpointGetEmployee.getDocUrl());
		Assert.assertEquals("/employee", restEndpointGetEmployee.getUrl());
		Assert.assertEquals("/${id}", restEndpointGetEmployee.getUrlParameters());
		Field getEmployeeIdByIdRequest = restEndpointGetEmployee.getRequest();
		Assert.assertEquals("id", getEmployeeIdByIdRequest.getName());
		Assert.assertEquals(Type.INT, getEmployeeIdByIdRequest.getType());
		Assert.assertEquals("Employee ID", getEmployeeIdByIdRequest.getDescription());
		Assert.assertEquals(1, getEmployeeIdByIdRequest.getSampleValue());
		Field employeeField = restEndpointGetEmployee.getResponse();
		checkEmployeeObjectField(employeeField);
	}
	
	private void checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpoint(RestEndpointDescriptor restEndpointGetEmployee) {
		Assert.assertEquals("getAllEmployees", restEndpointGetEmployee.getName());
		Assert.assertEquals("Get all employees", restEndpointGetEmployee.getDescription());
		Assert.assertEquals(HttpMethod.GET, restEndpointGetEmployee.getHttpMethod());
		Assert.assertEquals("https://www.example.com/docs/employee/getAll", restEndpointGetEmployee.getDocUrl());
		Assert.assertEquals("/employees", restEndpointGetEmployee.getUrl());
		Field employeeField = restEndpointGetEmployee.getResponse();
		Assert.assertEquals("Employee", employeeField.getObjectName());
		Assert.assertEquals(Type.fromTypeName("OBJECT_LIST"), employeeField.getType());
	}
	
	private void checkEmployeeExchangeV1ApiGroupAddEmployeesRestEndpoint(RestEndpointDescriptor restEndpointGetEmployee) {
		Assert.assertEquals("addEmployee", restEndpointGetEmployee.getName());
		Assert.assertEquals("Add a new employee", restEndpointGetEmployee.getDescription());
		Assert.assertEquals(HttpMethod.POST, restEndpointGetEmployee.getHttpMethod());
		Assert.assertEquals("https://www.example.com/docs/employee/add", restEndpointGetEmployee.getDocUrl());
		Assert.assertEquals("/employee", restEndpointGetEmployee.getUrl());
		Field employeeField = restEndpointGetEmployee.getRequest();
		Assert.assertEquals("Employee", employeeField.getObjectName());
	}
	
	private void checkEmployeeExchangeV1ApiGroupUpdateEmployeesRestEndpoint(RestEndpointDescriptor restEndpointDescriptor) {
		Assert.assertEquals("updateEmployee", restEndpointDescriptor.getName());
        Assert.assertEquals("Update an existing employee", restEndpointDescriptor.getDescription());
        Assert.assertEquals(HttpMethod.PUT, restEndpointDescriptor.getHttpMethod());
        Assert.assertEquals("https://www.example.com/docs/employee/add", restEndpointDescriptor.getDocUrl());
        Assert.assertEquals("/employee", restEndpointDescriptor.getUrl());
        Field updateEmployeeRequest = restEndpointDescriptor.getRequest();
        Assert.assertEquals("Employee", updateEmployeeRequest.getObjectName());
	}

	private void checkEmployeeExchangeV1ApiGroupDeleteEmployeesRestEndpoint(RestEndpointDescriptor restEndpointDescriptor) {
		Assert.assertEquals("deleteEmployee", restEndpointDescriptor.getName());
		Assert.assertEquals("Delete an employee", restEndpointDescriptor.getDescription());
		Assert.assertEquals(HttpMethod.DELETE, restEndpointDescriptor.getHttpMethod());
		Assert.assertEquals("https://www.example.com/docs/employee/delete", restEndpointDescriptor.getDocUrl());
		Assert.assertEquals("/employee", restEndpointDescriptor.getUrl());
		Assert.assertEquals("/${id}", restEndpointDescriptor.getUrlParameters());
		Field deleteEmployeeIdRequest = restEndpointDescriptor.getRequest();
		Assert.assertEquals("id", deleteEmployeeIdRequest.getName());
		Assert.assertEquals(Type.INT, deleteEmployeeIdRequest.getType());
		Assert.assertEquals("Employee ID", deleteEmployeeIdRequest.getDescription());
		Assert.assertEquals(1, deleteEmployeeIdRequest.getSampleValue());
	}
	
	private void checkEmployeeObjectField(Field employeeField) {
		Assert.assertEquals("Employee", employeeField.getObjectName());
		Assert.assertEquals(Type.OBJECT, employeeField.getType());
		List<Field> employeeProperties = employeeField.getProperties();
		Assert.assertEquals(4, employeeProperties.size());
		Field idField = employeeProperties.get(0);
		Assert.assertEquals("id", idField.getName());
		Assert.assertEquals(Type.INT, idField.getType());
		Assert.assertEquals("Employee ID", idField.getDescription());
		Assert.assertEquals(1, idField.getSampleValue());
		
		Field firstNameField = employeeProperties.get(1);
		Assert.assertEquals("firstName", firstNameField.getName());
		Assert.assertEquals(Type.STRING, firstNameField.getType());
		Assert.assertEquals("Employee First Name", firstNameField.getDescription());
		Assert.assertEquals("John", firstNameField.getSampleValue());
		
		Field lastNameField = employeeProperties.get(2);
		Assert.assertEquals("lastName", lastNameField.getName());
		Assert.assertEquals(Type.STRING, lastNameField.getType());
		Assert.assertEquals("Employee last lame", lastNameField.getDescription());
		
		Field profileField = employeeProperties.get(3);
		Assert.assertEquals("profile", profileField.getName());
		Assert.assertEquals(Type.STRING, profileField.getType());
		Assert.assertEquals("Employee profile. Can be 'regular' or 'admin'", profileField.getDescription());
		Assert.assertEquals("REGULAR", profileField.getSampleValue());
	}
	
	

}
