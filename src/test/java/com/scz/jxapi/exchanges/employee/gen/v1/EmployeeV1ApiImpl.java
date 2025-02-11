package com.scz.jxapi.exchanges.employee.gen.v1;

import java.util.List;
import java.util.Properties;

import com.scz.jxapi.exchange.AbstractExchangeApi;
import com.scz.jxapi.exchanges.employee.gen.EmployeeExchange;
import com.scz.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import com.scz.jxapi.exchanges.employee.gen.v1.deserializers.EmployeeDeserializer;
import com.scz.jxapi.exchanges.employee.gen.v1.deserializers.EmployeeV1EmployeeUpdatesMessageDeserializer;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import com.scz.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link EmployeeV1Api}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class EmployeeV1ApiImpl extends AbstractExchangeApi implements EmployeeV1Api {
  
  private static final Logger log = LoggerFactory.getLogger(EmployeeV1ApiImpl.class);
  
  
  
  /**
   * Base URL for <i>Employee</i> exchange <i>V1</i> API REST endpoints
   */
  public static final String HTTP_URL = EmployeeExchangeImpl.HTTP_URL + "v1";
  
  /**
   * Base URL for <i>Employee</i> exchange <i>V1</i> API Websocket endpoints
   */
  public static final String WEBSOCKET_URL = "BASEURL/v1";
  
  // REST endpoint URLs
  
  /**
   * URL for <i>getEmployee</i> REST endpoint.
   * @see EmployeeV1Api#getEmployee(Integer)
   */
  public static final String GET_EMPLOYEE_URL = HTTP_URL + "/employee";
  
  /**
   * URL for <i>getAllEmployees</i> REST endpoint.
   * @see EmployeeV1Api#getAllEmployees()
   */
  public static final String GET_ALL_EMPLOYEES_URL = HTTP_URL + "/employees";
  
  /**
   * URL for <i>addEmployee</i> REST endpoint.
   * @see EmployeeV1Api#addEmployee(Employee)
   */
  public static final String ADD_EMPLOYEE_URL = HTTP_URL + "/employee";
  
  /**
   * URL for <i>updateEmployee</i> REST endpoint.
   * @see EmployeeV1Api#updateEmployee(Employee)
   */
  public static final String UPDATE_EMPLOYEE_URL = HTTP_URL + "/employee";
  
  /**
   * URL for <i>deleteEmployee</i> REST endpoint.
   * @see EmployeeV1Api#deleteEmployee(Integer)
   */
  public static final String DELETE_EMPLOYEE_URL = HTTP_URL + "/employee";
  
  // Websocket endpoints
  private final WebsocketEndpoint<EmployeeV1EmployeeUpdatesMessage> employeeUpdatesWs;
  
  // Message deserializers
  private final MessageDeserializer<Employee> getEmployeeResponseDeserializer = new EmployeeDeserializer();
  private final MessageDeserializer<List<Employee>> getAllEmployeesResponseDeserializer = new ListJsonFieldDeserializer<>(new EmployeeDeserializer());
  private final MessageDeserializer<String> addEmployeeResponseDeserializer = RawStringMessageDeserializer.getInstance();
  private final MessageDeserializer<String> updateEmployeeResponseDeserializer = RawStringMessageDeserializer.getInstance();
  private final MessageDeserializer<String> deleteEmployeeResponseDeserializer = RawStringMessageDeserializer.getInstance();
  
  // Constructor
  public EmployeeV1ApiImpl(String exchangeName, Properties properties) {
    super(ID, exchangeName, EmployeeExchange.ID, properties);
    createHttpRequestExecutor(null, -1L);
    createHttpRequestInterceptor("com.scz.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory");
    createWebsocketManager(WEBSOCKET_URL, null, "com.scz.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory");
    this.employeeUpdatesWs = createWebsocketEndpoint(EMPLOYEE_UPDATES_WS_API, new EmployeeV1EmployeeUpdatesMessageDeserializer());
  }
  
  // REST endpoint method call implementations
  @Override
  public FutureRestResponse<String> addEmployee(Employee request) {
    log.debug("POST addEmployee > {}", request);
    return submit(HttpRequest.create(ADD_EMPLOYEE_REST_API, ADD_EMPLOYEE_URL, HttpMethod.POST, request, null, 0, serializeRequestBody(request)), addEmployeeResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<Employee> getEmployee(Integer request) {
    String urlParameters = EncodingUtil.substituteArguments("/${id}", "id", request);
    log.debug("GET getEmployee > {}", request);
    return submit(HttpRequest.create(GET_EMPLOYEE_REST_API, GET_EMPLOYEE_URL + urlParameters, HttpMethod.GET, request, null, 0, null), getEmployeeResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<String> deleteEmployee(Integer request) {
    String urlParameters = EncodingUtil.substituteArguments("/${id}", "id", request);
    log.debug("DELETE deleteEmployee > {}", request);
    return submit(HttpRequest.create(DELETE_EMPLOYEE_REST_API, DELETE_EMPLOYEE_URL + urlParameters, HttpMethod.DELETE, request, null, 0, null), deleteEmployeeResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<List<Employee>> getAllEmployees() {
    log.debug("GET getAllEmployees >");
    return submit(HttpRequest.create(GET_ALL_EMPLOYEES_REST_API, GET_ALL_EMPLOYEES_URL, HttpMethod.GET, null, null, 0, null), getAllEmployeesResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<String> updateEmployee(Employee request) {
    log.debug("PUT updateEmployee > {}", request);
    return submit(HttpRequest.create(UPDATE_EMPLOYEE_REST_API, UPDATE_EMPLOYEE_URL, HttpMethod.PUT, request, null, 0, serializeRequestBody(request)), updateEmployeeResponseDeserializer);
  }
  
  
  // Websocket endpoint subscribe/unsubscribe methods implementations
  @Override
  public String subscribeEmployeeUpdates(WebsocketListener<EmployeeV1EmployeeUpdatesMessage> listener) {
    String topic = "";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.create());
    String subId = employeeUpdatesWs.subscribe(subscribeRequest, listener);
    log.debug("subscribeEmployeeUpdates > {} returned subscriptionId:{}", subscribeRequest, subId);
    return subId;
  }
  
  @Override
  public boolean unsubscribeEmployeeUpdates(String subscriptionId) {
    log.debug("unsubscribeEmployeeUpdates: subscriptionId:{}", subscriptionId);
    return employeeUpdatesWs.unsubscribe(subscriptionId);
  }
  
}
