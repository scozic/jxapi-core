package org.jxapi.exchanges.employee.gen.v1;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeV1EmployeeUpdatesMessageDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeV1GetAllEmployeesResponseDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.serializers.EmployeeSerializer;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequestUrlParamsSerializer;
import org.jxapi.netutils.rest.RestEndpoint;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.EncodingUtil;

/**
 * Actual implementation of {@link EmployeeV1Api}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator")
public class EmployeeV1ApiImpl extends AbstractExchangeApi implements EmployeeV1Api {
  
  // REST endpoints URL parameter serializers
  private static final HttpRequestUrlParamsSerializer<Integer> GET_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER = (request, url) -> new StringBuilder(128).append(url)
    .append(EncodingUtil.createUrlPathParameters(request)).toString();
  private static final HttpRequestUrlParamsSerializer<EmployeeV1GetAllEmployeesRequest> GET_ALL_EMPLOYEES_REST_API_URL_PARAMS_SERIALIZER = (request, url) -> new StringBuilder(128).append(url)
    .append(EncodingUtil.createUrlQueryParameters("page", request.getPage(), "size", request.getSize())).toString();
  private static final HttpRequestUrlParamsSerializer<Employee> ADD_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER = HttpRequestUrlParamsSerializer.noParams();
  private static final HttpRequestUrlParamsSerializer<Employee> UPDATE_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER = HttpRequestUrlParamsSerializer.noParams();
  private static final HttpRequestUrlParamsSerializer<Integer> DELETE_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER = (request, url) -> new StringBuilder(128).append(url)
    .append(EncodingUtil.createUrlPathParameters(request)).toString();
  
  // Request serializers
  private static final MessageSerializer<Employee> ADD_EMPLOYEE_REST_API_REQUEST_SERIALIZER = new EmployeeSerializer();
  private static final MessageSerializer<Employee> UPDATE_EMPLOYEE_REST_API_REQUEST_SERIALIZER = new EmployeeSerializer();
  
  // Message deserializers
  private static final MessageDeserializer<Employee> GET_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER = new EmployeeDeserializer();
  private static final MessageDeserializer<EmployeeV1GetAllEmployeesResponse> GET_ALL_EMPLOYEES_REST_API_RESPONSE_DESERIALIZER = new EmployeeV1GetAllEmployeesResponseDeserializer();
  private static final MessageDeserializer<String> ADD_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER = MessageDeserializer.NO_OP;
  private static final MessageDeserializer<String> UPDATE_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER = MessageDeserializer.NO_OP;
  private static final MessageDeserializer<String> DELETE_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER = MessageDeserializer.NO_OP;
  private static final MessageDeserializer<EmployeeV1EmployeeUpdatesMessage> EMPLOYEE_UPDATES_WS_API_MESSAGE_DESERIALIZER = new EmployeeV1EmployeeUpdatesMessageDeserializer();
  
  // REST endpoints
  private final RestEndpoint<Integer, Employee> getEmployeeRestEndpoint;
  private final RestEndpoint<EmployeeV1GetAllEmployeesRequest, EmployeeV1GetAllEmployeesResponse> getAllEmployeesRestEndpoint;
  private final RestEndpoint<Employee, String> addEmployeeRestEndpoint;
  private final RestEndpoint<Employee, String> updateEmployeeRestEndpoint;
  private final RestEndpoint<Integer, String> deleteEmployeeRestEndpoint;
  
  // Websocket endpoints
  private final WebsocketEndpoint<EmployeeV1EmployeeUpdatesMessage> employeeUpdatesWsEndpoint;
  
  // Constructor
  /**
   * Constructor
   * 
   * @param exchange the exchange instance
   * @param exchangeObserver the exchange API observer to dispatch endpoint events to
   * 
   */
  public EmployeeV1ApiImpl(EmployeeExchange exchange, ExchangeObserver exchangeObserver) {
    super(ID,
          exchange,
          exchangeObserver,
          "v1");
    this.getEmployeeRestEndpoint = createRestEndpoint(GET_EMPLOYEE_REST_API, EmployeeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.getEmployeeRestEndpoint.setHttpMethod(HttpMethod.GET);
    this.getEmployeeRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/employee"));
    this.getEmployeeRestEndpoint.setUrlParamsSerializer(GET_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER);
    this.getEmployeeRestEndpoint.setDeserializer(GET_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER);
    
    this.getAllEmployeesRestEndpoint = createPaginatedRestEndpoint(GET_ALL_EMPLOYEES_REST_API, EmployeeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.getAllEmployeesRestEndpoint.setHttpMethod(HttpMethod.GET);
    this.getAllEmployeesRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/employees"));
    this.getAllEmployeesRestEndpoint.setUrlParamsSerializer(GET_ALL_EMPLOYEES_REST_API_URL_PARAMS_SERIALIZER);
    this.getAllEmployeesRestEndpoint.setDeserializer(GET_ALL_EMPLOYEES_REST_API_RESPONSE_DESERIALIZER);
    
    this.addEmployeeRestEndpoint = createRestEndpoint(ADD_EMPLOYEE_REST_API, EmployeeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.addEmployeeRestEndpoint.setHttpMethod(HttpMethod.POST);
    this.addEmployeeRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/employee"));
    this.addEmployeeRestEndpoint.setUrlParamsSerializer(ADD_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER);
    this.addEmployeeRestEndpoint.setSerializer(ADD_EMPLOYEE_REST_API_REQUEST_SERIALIZER);
    this.addEmployeeRestEndpoint.setDeserializer(ADD_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER);
    
    this.updateEmployeeRestEndpoint = createRestEndpoint(UPDATE_EMPLOYEE_REST_API, EmployeeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.updateEmployeeRestEndpoint.setHttpMethod(HttpMethod.PUT);
    this.updateEmployeeRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/employee"));
    this.updateEmployeeRestEndpoint.setUrlParamsSerializer(UPDATE_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER);
    this.updateEmployeeRestEndpoint.setSerializer(UPDATE_EMPLOYEE_REST_API_REQUEST_SERIALIZER);
    this.updateEmployeeRestEndpoint.setDeserializer(UPDATE_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER);
    
    this.deleteEmployeeRestEndpoint = createRestEndpoint(DELETE_EMPLOYEE_REST_API, EmployeeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.deleteEmployeeRestEndpoint.setHttpMethod(HttpMethod.DELETE);
    this.deleteEmployeeRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/employee"));
    this.deleteEmployeeRestEndpoint.setUrlParamsSerializer(DELETE_EMPLOYEE_REST_API_URL_PARAMS_SERIALIZER);
    this.deleteEmployeeRestEndpoint.setDeserializer(DELETE_EMPLOYEE_REST_API_RESPONSE_DESERIALIZER);
    
    this.employeeUpdatesWsEndpoint = createWebsocketEndpoint(EMPLOYEE_UPDATES_WS_API, EmployeeExchange.WS_DEFAULT_WEBSOCKET_CLIENT, EMPLOYEE_UPDATES_WS_API_MESSAGE_DESERIALIZER);
  }
  
  // REST endpoint method call implementations
  @Override
  public FutureRestResponse<Employee> getEmployee(Integer request) {
    return getEmployeeRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<EmployeeV1GetAllEmployeesResponse> getAllEmployees(EmployeeV1GetAllEmployeesRequest request) {
    return getAllEmployeesRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<String> addEmployee(Employee request) {
    return addEmployeeRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<String> updateEmployee(Employee request) {
    return updateEmployeeRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<String> deleteEmployee(Integer request) {
    return deleteEmployeeRestEndpoint.submit(request);
  }
  
  
  // Websocket endpoint subscribe/unsubscribe methods implementations
  @Override
  public String subscribeEmployeeUpdates(WebsocketListener<EmployeeV1EmployeeUpdatesMessage> listener) {
    String topic = "employeeUpdates";
    WebsocketMessageTopicMatcherFactory topicMatcherFactory = WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY;
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, topicMatcherFactory);
    return employeeUpdatesWsEndpoint.subscribe(subscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeEmployeeUpdates(String subscriptionId) {
    return employeeUpdatesWsEndpoint.unsubscribe(subscriptionId);
  }
  
}
