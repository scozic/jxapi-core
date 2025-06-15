package org.jxapi.exchanges.employee.gen.v1;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeProperties;
import org.jxapi.exchanges.employee.gen.v1.deserializers.EmployeeDeserializer;
import org.jxapi.exchanges.employee.gen.v1.deserializers.EmployeeV1EmployeeUpdatesMessageDeserializer;
import org.jxapi.exchanges.employee.gen.v1.deserializers.EmployeeV1GetAllEmployeesResponseDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PropertiesUtil;

/**
 * Actual implementation of {@link EmployeeV1Api}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator")
public class EmployeeV1ApiImpl extends AbstractExchangeApi implements EmployeeV1Api {
  
  // REST endpoint URLs
  
  /**
   * URL for <i>getEmployee</i> REST endpoint.
   * @see EmployeeV1Api#getEmployee(Integer)
   */
  protected final String getEmployeeHttpUrl;
  
  /**
   * URL for <i>getAllEmployees</i> REST endpoint.
   * @see EmployeeV1Api#getAllEmployees(EmployeeV1GetAllEmployeesRequest)
   */
  protected final String getAllEmployeesHttpUrl;
  
  /**
   * URL for <i>addEmployee</i> REST endpoint.
   * @see EmployeeV1Api#addEmployee(Employee)
   */
  protected final String addEmployeeHttpUrl;
  
  /**
   * URL for <i>updateEmployee</i> REST endpoint.
   * @see EmployeeV1Api#updateEmployee(Employee)
   */
  protected final String updateEmployeeHttpUrl;
  
  /**
   * URL for <i>deleteEmployee</i> REST endpoint.
   * @see EmployeeV1Api#deleteEmployee(Integer)
   */
  protected final String deleteEmployeeHttpUrl;
  
  // Websocket endpoints
  private final WebsocketEndpoint<EmployeeV1EmployeeUpdatesMessage> employeeUpdatesWs;
  
  // Message deserializers
  private final MessageDeserializer<Employee> getEmployeeResponseDeserializer = new EmployeeDeserializer();
  private final MessageDeserializer<EmployeeV1GetAllEmployeesResponse> getAllEmployeesResponseDeserializer = new EmployeeV1GetAllEmployeesResponseDeserializer();
  private final MessageDeserializer<String> addEmployeeResponseDeserializer = RawStringMessageDeserializer.getInstance();
  private final MessageDeserializer<String> updateEmployeeResponseDeserializer = RawStringMessageDeserializer.getInstance();
  private final MessageDeserializer<String> deleteEmployeeResponseDeserializer = RawStringMessageDeserializer.getInstance();
  
  // Constructor
  public EmployeeV1ApiImpl(EmployeeExchange exchange) {
    super(ID,
          exchange,
          null,
          "v1",
          EncodingUtil.substituteArguments("${config.baseWebsocketUrl}", "config.baseWebsocketUrl", PropertiesUtil.getString(exchange.getProperties(), EmployeeProperties.BASE_WEBSOCKET_URL)));
    createHttpRequestExecutor(null, -1L);
    createHttpRequestInterceptor("org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory");
    this.getEmployeeHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/employee");
    this.getAllEmployeesHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/employees");
    this.addEmployeeHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/employee");
    this.updateEmployeeHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/employee");
    this.deleteEmployeeHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/employee");
    createWebsocketManager(this.wsUrl, null, "org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory");
    this.employeeUpdatesWs = createWebsocketEndpoint(EMPLOYEE_UPDATES_WS_API, new EmployeeV1EmployeeUpdatesMessageDeserializer());
  }
  
  // REST endpoint method call implementations
  @Override
  public FutureRestResponse<Employee> getEmployee(Integer request) {
    String urlParameters = EncodingUtil.substituteArguments("/${id}", "id", request);
    return submit(HttpRequest.create(GET_EMPLOYEE_REST_API, getEmployeeHttpUrl + urlParameters, HttpMethod.GET, request, null, 0), getEmployeeResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<EmployeeV1GetAllEmployeesResponse> getAllEmployees(EmployeeV1GetAllEmployeesRequest request) {
    String urlParameters = EncodingUtil.createUrlQueryParameters("page", request.getPage(), "size", request.getSize());
    return submitPaginated(HttpRequest.create(GET_ALL_EMPLOYEES_REST_API, getAllEmployeesHttpUrl + urlParameters, HttpMethod.GET, request, null, 0), getAllEmployeesResponseDeserializer, this::getAllEmployees);
  }
  
  @Override
  public FutureRestResponse<String> addEmployee(Employee request) {
    return submit(HttpRequest.create(ADD_EMPLOYEE_REST_API, addEmployeeHttpUrl, HttpMethod.POST, request, null, 0), addEmployeeResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<String> updateEmployee(Employee request) {
    return submit(HttpRequest.create(UPDATE_EMPLOYEE_REST_API, updateEmployeeHttpUrl, HttpMethod.PUT, request, null, 0), updateEmployeeResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<String> deleteEmployee(Integer request) {
    String urlParameters = EncodingUtil.substituteArguments("/${id}", "id", request);
    return submit(HttpRequest.create(DELETE_EMPLOYEE_REST_API, deleteEmployeeHttpUrl + urlParameters, HttpMethod.DELETE, request, null, 0), deleteEmployeeResponseDeserializer);
  }
  
  
  // Websocket endpoint subscribe/unsubscribe methods implementations
  @Override
  public String subscribeEmployeeUpdates(WebsocketListener<EmployeeV1EmployeeUpdatesMessage> listener) {
    String topic = "";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.create());
    return employeeUpdatesWs.subscribe(subscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeEmployeeUpdates(String subscriptionId) {
    return employeeUpdatesWs.unsubscribe(subscriptionId);
  }
  
}
