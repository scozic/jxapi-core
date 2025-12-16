package org.jxapi.exchanges.employee;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jxapi.exchanges.employee.gen.EmployeeConstants;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeDeserializer;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestUtil;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.javanet.HttpServerUtil;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServer;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import org.jxapi.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Employee exchange server. It can be used to test employee exchange clients.
 */
public class EmployeeExchangeServer {
  
  private static final Logger log = LoggerFactory.getLogger(EmployeeExchangeServer.class);
  
  /**
   * Creates {@link EmployeeV1GetAllEmployeesRequest} from URL.
   * 
   * @param getAllEmployeesUrl URL to parse
   * @return {@link EmployeeV1GetAllEmployeesRequest} object. If no query
   *         parameters in provided URL, (which can be <code>null</code> or
   *         empty), a default request with page=1 and size = default size (10)
   */
  public static EmployeeV1GetAllEmployeesRequest creatGetAllEmployeesRequestFromUrl(String getAllEmployeesUrl) {
    Map<String, String> queryParams = HttpRequestUtil.parseUrlQueryParams(getAllEmployeesUrl);
    return EmployeeV1GetAllEmployeesRequest.builder()
        .page(queryParams.containsKey("page") ? Integer.valueOf(queryParams.get("page")) : 1) 
        .size(queryParams.containsKey("size") ? Integer.valueOf(queryParams.get("size")) : EmployeeConstants.DEFAULT_PAGE_SIZE)
        .build();
  }
  
  /**
   * System property to set HTTP server port.
   */
  public static final String HTTP_SERVER_PORT_SYSPROP = "http.port";
  
  /**
   * System property to set Websocket server port.
   */
  public static final String WS_SERVER_PORT_SYSPROP = "ws.port";
  
  /**
   * Default port for HTTP (REST Endpoints) server.
     */
  public static final int DEFAULT_HTTP_PORT = 8080;
  
  /**
   * Default port for Websocket server.
   */
  public static final int DEFAULT_WS_PORT = 8081;
  
  private final HttpHandler httpHandler;
  
  private final int httpPort;
  private final int webSocketPort;
  private HttpServer httpServer;
  private AtomicBoolean started = new AtomicBoolean(false);
  private MockWebsocketServer wsServer;
  private final EmployeesDatabase employeesDatabase = new DefaultEmployeeDatabase();
  private final String baseHttpUrl;
  private final String baseWsUrl;

  public EmployeeExchangeServer(int httpPort, int webSocketPort) {
    httpHandler = new HttpHandler() {
      @Override
      public void service(Request request, Response response) throws Exception {
        serveRequest(request, response);
      }
    };
    this.httpPort = httpPort;
    this.webSocketPort = webSocketPort;
    this.baseHttpUrl = "http://localhost:" + httpPort + "/";
    this.baseWsUrl = "ws://localhost:" + webSocketPort + "/employee/v1/ws";
  }
  
  /**
   * Starts server.
   * @throws IOException if server could not be started
   */
  public void start() throws IOException {
    if (started.getAndSet(true)) {
      return;
    }
    httpServer = HttpServer.createSimpleServer(null, httpPort);
    httpServer.getServerConfiguration().addHttpHandler(httpHandler);
    log.info("Starting server on port:{}", httpPort);
    httpServer.start();
    
    wsServer = new MockWebsocketServer(webSocketPort, "employee/v1");
    log.info("Starting websocket server on port:{}", webSocketPort);
    wsServer.start();
  }
  
  /**
   * Stops server.
   */
  public void stop() {
    if (!started.getAndSet(false)) {
      return;
    }
        log.debug("Stopping server on port:{}", httpPort);
        httpServer.shutdownNow();
        wsServer.stop();
  }
  
  /**
   * @return <code>true</code> if server is started
   */
  public boolean isStarted() {
    return started.get();
  }
  
  /**
   * @return HTTP base URL of local server, e.g.
   *         <code>http://localhost:&lt;port&gt;</code>
   */
  public String getHttpBaseUrl() {
    return this.baseHttpUrl;
  }
  
  /**
   * @return Websocket base URL of local server, e.g.
   *         <code>ws://localhost:&lt;port&gt;/employee/ws</code>
   */
  public String getWebSocketBaseUrl() {
        return this.baseWsUrl;
  }
  
  public MockWebsocketServerEvent popWsEvent() throws TimeoutException {
    return wsServer.waitUntilCount(1).pop();
  }
  
  private void serveRequest(Request request, Response response) {
    try {
      HttpRequest httpRequest = HttpServerUtil.convertRequest(request);
      log.debug("Serving request:{}", httpRequest);
      HttpResponse httpResponse = process(httpRequest);
      HttpServerUtil.fillResponse(httpResponse, response);
    } catch (Exception ex) {
      log.error("Error serving request:{}", request, ex);
    }
  }

  private HttpResponse process(HttpRequest httpRequest) {
    try {
      if (httpRequest.getUrl().contains("employees")) {
        if (HttpMethod.GET.equals(httpRequest.getHttpMethod())) {
                  return getAllEmployees(creatGetAllEmployeesRequestFromUrl(httpRequest.getUrl()));
        }
      } else if (httpRequest.getUrl().contains("employee")) {
        if (HttpMethod.GET.equals(httpRequest.getHttpMethod())) {
          Integer id = Integer.valueOf(StringUtils.substringAfterLast(httpRequest.getUrl(), "/"));
                  return getEmployee(id);
        } else if (HttpMethod.POST.equals(httpRequest.getHttpMethod())) {
          return addEmployee(httpRequest.getBody());
        } else if (HttpMethod.PUT.equals(httpRequest.getHttpMethod())) {
          return updateEmployee(httpRequest.getBody());
        } else if (HttpMethod.DELETE.equals(httpRequest.getHttpMethod())) {
          return deleteEmployee(Integer.valueOf(StringUtils.substringAfterLast(httpRequest.getUrl(), "/")));
        }
      }
    } catch (Exception e) {
            log.error("Error processing request:" + httpRequest, e);
            return createHttpResponse(500);
        }
    
    log.info("Unknown request:{}", httpRequest);
    return createHttpResponse(404);
  }
    
  private HttpResponse getEmployee(Integer id) {
    log.info("Getting employee with id:{}", id);
    HttpResponse response = createHttpResponse(200);
    response.setBody(JsonUtil.pojoToJsonString(employeesDatabase.getEmployee(id)));
    return response;
  }

  private HttpResponse getAllEmployees(EmployeeV1GetAllEmployeesRequest employeeV1GetAllEmployeesRequest) {
    log.info("Getting all employees:{}", employeeV1GetAllEmployeesRequest);
    List<Employee> employees = null;
    try {
      employees = employeesDatabase.getAllEmployees(employeeV1GetAllEmployeesRequest.getPage(),
          employeeV1GetAllEmployeesRequest.getSize());
    } catch (IllegalArgumentException e) {
      log.error("Error getting all employees with request:" + employeeV1GetAllEmployeesRequest, e);
      return createHttpResponse(400);
    }
    int totalPageCount = (int) Math.ceil((double) employeesDatabase.getAllEmployees().size() / employeeV1GetAllEmployeesRequest.getSize());
    HttpResponse response = createHttpResponse(200);
    response.setBody(JsonUtil.pojoToJsonString(EmployeeV1GetAllEmployeesResponse.builder()
        .page(employeeV1GetAllEmployeesRequest.getPage())
        .totalPages(totalPageCount)
        .employees(employees)
        .build()));
    return response;
  }
  
  private HttpResponse createHttpResponse(int status) {
    HttpResponse response = new HttpResponse();
        response.setResponseCode(status);
        response.setTime(new Date());
        return response;
  }
  
  private HttpResponse addEmployee(String requestBody) throws IOException {
    log.info("Adding employee:{}", requestBody);
    Employee employee = null;
    try {
      employee = new EmployeeDeserializer().deserialize(requestBody);
    } catch (Exception e) {
      log.error("Error deserializing employee:" + requestBody, e);
      return createHttpResponse(400);
    }
    
    try {
      employeesDatabase.addEmployee(employee);
    } catch (NullEmployeeIdException e) {
            log.error("Error adding employee:" + employee, e);
            return createHttpResponse(400);
    } catch (EmployeeIdConflictException e) {
            log.error("Error adding employee:" + employee, e);
            return createHttpResponse(409);
        }
    dispatchWsEvent(EmployeeWsEventType.EMPLOYEE_ADDED, employee);
    return createHttpResponse(200);
  }
  
  private HttpResponse updateEmployee(String requestBody) throws IOException {
    log.info("Updating employee:{}", requestBody);
    Employee employee = null;
    try {
      employee = new EmployeeDeserializer().deserialize(requestBody);
    } catch (Exception e) {
      log.error("Error deserializing employee:" + requestBody, e);
      return createHttpResponse(400);
    }
    
    try {
      employeesDatabase.updateEmployee(employee);
    } catch (NullEmployeeIdException e) {
            log.error("Error adding employee:" + employee, e);
            return createHttpResponse(400);
    }
    dispatchWsEvent(EmployeeWsEventType.EMPLOYEE_UPDATED, employee);
    return createHttpResponse(200);
  }
  
  private HttpResponse deleteEmployee(Integer id) throws IOException {
    log.info("Deleting employee with id:{}", id);
    Employee e = employeesDatabase.deleteEmployee(id);
    if (e == null) {
            return createHttpResponse(404);
    }
    dispatchWsEvent(EmployeeWsEventType.EMPLOYEE_DELETED, e);
    return createHttpResponse(200);
  }
  
  private void dispatchWsEvent(EmployeeWsEventType eventType, Employee employee) throws IOException {
    EmployeeV1EmployeeUpdatesMessage message = new EmployeeV1EmployeeUpdatesMessage();
    message.setEventType(eventType.code);
    message.setEmployee(employee);
    wsServer.sendMessageToClients(JsonUtil.pojoToJsonString(message));
  }
  
  /**
   * Main method to start server.
   * 
   * @param args command line arguments
   * @throws IOException      if server could not be started
   */
  public static void main(String[] args) throws IOException {
    try {
      int httpPort = Integer.getInteger(HTTP_SERVER_PORT_SYSPROP, DEFAULT_HTTP_PORT);
      int wsPort = Integer.getInteger(WS_SERVER_PORT_SYSPROP, DEFAULT_WS_PORT);
      EmployeeExchangeServer server = new EmployeeExchangeServer(httpPort, wsPort);
      server.start();
      Runtime.getRuntime().addShutdownHook(new Thread() {
              @Override
              public void run() {
                try {
                  server.stop();
                } catch (Exception ex) {
                  log.error("Error stopping server", ex);
                }
              }
      });
    } catch (Throwable t) {
      log.error("Error raised from main()", t);
      System.exit(-1);
    }
  }

}
