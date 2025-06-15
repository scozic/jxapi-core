package org.jxapi.exchange.descriptor.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.CanonicalType;
import org.jxapi.exchange.descriptor.ConfigProperty;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.DefaultConfigProperty;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketMessageTopicMatcherFieldDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.util.CollectionUtil;

/**
 * Unit test for {@link ExchangeDescriptorParser}
 */
public class ExchangeDescriptorParserTest {
  
  private Path srcFolder;
  private final Path employeeDescriptorYamlPath = Paths.get(".", "src", "test", "resources", "employeeExchange.yaml");
  private final Path employeeDescriptorJsonPath = Paths.get(".", "src", "test", "resources", "employeeExchange.json");
  private final Path testExchangeDescriptorPath = Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json");
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }
  
  @Test
  public void testParseTestExchangeJsonDescriptor() throws Exception {
    ExchangeDescriptor ex = ExchangeDescriptorParser.fromJson(testExchangeDescriptorPath);
    checkTestExchange(ex);
  }
  
  @Test
  public void testLoadEmployeeExchangeYamlDescriptor() throws IOException {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(employeeDescriptorYamlPath);
    checkEmployeeExchange(exchangeDescriptor);
  }
  
  @Test
  public void testLoadEmployeeExchangeJsonDescriptor() throws IOException {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(employeeDescriptorJsonPath);
    checkEmployeeExchange(exchangeDescriptor);
  }
  
  @Test
  public void testLoadEmployeeExchangeFromYamlDescriptorSplitInMultipleFiles() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    Path descriptorPath = Paths.get(".", "src", "test", "resources", "employeeExchange");
    List<ExchangeDescriptor> exchangeDescriptors = ExchangeDescriptorParser.collectAndMergeExchangeDescriptors(descriptorPath);
    Assert.assertEquals(1, exchangeDescriptors.size());
    checkEmployeeExchange(exchangeDescriptors.get(0));
  }
  
  @Test
  public void testCollectAndMergeExchangeDescriptors_WithResourcesPathContaining2ExchangeDescriptorsAsJsonAndYaml() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    Files.copy(employeeDescriptorYamlPath, srcFolder.resolve(employeeDescriptorYamlPath.getFileName()));
    Files.copy(testExchangeDescriptorPath, srcFolder.resolve(testExchangeDescriptorPath.getFileName()));
    List<ExchangeDescriptor> exchangeDescriptors = ExchangeDescriptorParser.collectAndMergeExchangeDescriptors(srcFolder);
    Assert.assertEquals(2, exchangeDescriptors.size());
    checkEmployeeExchange(exchangeDescriptors.get(0));
    checkTestExchange(exchangeDescriptors.get(1));
  }
  
  private void checkTestExchange(ExchangeDescriptor exchangeDescriptor) {
    Assert.assertEquals("MyTestExchange", exchangeDescriptor.getId());
    Assert.assertEquals("A sample Exchange descriptor file. Should be provided config properties: ${config.apiKey}, ${config.apiSecret}. Author: ${constants.author}", 
                        exchangeDescriptor.getDescription());
    Assert.assertEquals("com.foo.bar.gen", exchangeDescriptor.getBasePackage());
    List<ExchangeApiDescriptor> apis = exchangeDescriptor.getApis();
    Assert.assertEquals(1, apis.size());
    checkMarketDataApi(apis.get(0));
  }
  
  private void checkMarketDataApi(ExchangeApiDescriptor marketDataApi) {
    Assert.assertEquals("MarketData", marketDataApi.getName());
    Assert.assertEquals("The market data API of MyTestExchange. Author: ${constants.author}", marketDataApi.getDescription());
    Assert.assertEquals("com.foo.bar.BarHttpRequestInterceptorFactory", marketDataApi.getHttpRequestInterceptorFactory());
    List<RestEndpointDescriptor> restEndpoints = marketDataApi.getRestEndpoints();
    Assert.assertEquals(2, restEndpoints.size());
    checkExchangeInfoRestEndpoint(restEndpoints.get(0));
    checkTickersRestEndpooint(restEndpoints.get(1));
    Assert.assertEquals("wss://com.foo.exchange/ws", marketDataApi.getWebsocketUrl());
    Assert.assertEquals("com.foo.bar.BarWebsocketFactory", marketDataApi.getWebsocketFactory());
    Assert.assertEquals("com.foo.bar.BarWebsocketHookFactory", marketDataApi.getWebsocketHookFactory());
    List<WebsocketEndpointDescriptor> websocketEndpoints = marketDataApi.getWebsocketEndpoints();
    Assert.assertEquals(1, websocketEndpoints.size());
    checkTickerStreamWebsocketEndpoint(websocketEndpoints.get(0));
  }

  private void checkTickersRestEndpooint(RestEndpointDescriptor tickersEndPoint) {
    Assert.assertEquals("tickers", tickersEndPoint.getName());
    Assert.assertEquals("Fetch current tickers. Author: ${constants.author}", tickersEndPoint.getDescription());
    Assert.assertEquals(HttpMethod.GET, tickersEndPoint.getHttpMethod());
    Assert.assertEquals("https://com.sample.mycex/tickers", tickersEndPoint.getUrl());
    List<Field> exchangeInfoParameters = tickersEndPoint.getRequest().getProperties();
    Assert.assertEquals(0, exchangeInfoParameters.size());
    
    checkTickersResponse(tickersEndPoint.getResponse().getProperties());
    
  }

  private void checkTickersResponse(List<Field> tickersResponse) {
    Assert.assertEquals(2, tickersResponse.size());
    Field responseCode = tickersResponse.get(0);
    Assert.assertEquals("responseCode", responseCode.getName());
    Assert.assertEquals("Request response code. Author: ${constants.author}", responseCode.getDescription());
    Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
    Assert.assertEquals("0", responseCode.getSampleValue());
    
    Field payload = tickersResponse.get(1);
    Assert.assertEquals("payload", payload.getName());
    Assert.assertEquals("Tickers for each symbol. Author: ${constants.author}", payload.getDescription());
    Assert.assertEquals(CanonicalType.MAP, payload.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
    List<Field> payloadParameters = payload.getProperties();
    Assert.assertEquals(1, payloadParameters.size());
    
    Field last = payloadParameters.get(0);
    Assert.assertEquals("last", last.getName());
    Assert.assertEquals("Last traded price. Author: ${constants.author}", last.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
    Assert.assertEquals(Double.valueOf("10.0"), last.getSampleValue());
    
  }

  private void checkExchangeInfoRestEndpoint(RestEndpointDescriptor exchangeInfoEndPoint) {
    Assert.assertEquals("exchangeInfo", exchangeInfoEndPoint.getName());
    Assert.assertEquals("Fetch market information of symbols that can be traded. Author: ${constants.author}", exchangeInfoEndPoint.getDescription());
    Assert.assertEquals(HttpMethod.GET, exchangeInfoEndPoint.getHttpMethod());
    Assert.assertEquals("https://com.sample.mycex/exchangeInfo", exchangeInfoEndPoint.getUrl());
    List<Field> exchangeInfoParameters = exchangeInfoEndPoint.getRequest().getProperties();
    
    Assert.assertEquals(2, exchangeInfoParameters.size());
    Field symbolsParameter = exchangeInfoParameters.get(0);
    Assert.assertEquals("symbols", symbolsParameter.getName());
    Assert.assertEquals("The list of symbol to fetch market information for. Leave empty to fetch all markets. Author: ${constants.author}", symbolsParameter.getDescription());
    Assert.assertEquals(CanonicalType.LIST, symbolsParameter.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.STRING, symbolsParameter.getType().getSubType().getCanonicalType());
    Assert.assertEquals("[\"${demoSymbol}\"]", symbolsParameter.getSampleValue());
    
    checkExchangeInfoResponse(exchangeInfoEndPoint.getResponse().getProperties());
  }
  
  private void checkExchangeInfoResponse(List<Field> exchangeInfoResponse) {
    Assert.assertEquals(2, exchangeInfoResponse.size());
    Field responseCode = exchangeInfoResponse.get(0);
    Assert.assertEquals("responseCode", responseCode.getName());
    Assert.assertEquals("Request response code", responseCode.getDescription());
    Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
    Assert.assertEquals("0", responseCode.getSampleValue());
    
    Field payload = exchangeInfoResponse.get(1);
    Assert.assertEquals("payload", payload.getName());
    Assert.assertEquals("List of market information for each requested symbol. Author: ${constants.author}", payload.getDescription());
    Assert.assertEquals(CanonicalType.LIST, payload.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
    List<Field> payloadParameters = payload.getProperties();
    Assert.assertEquals(3, payloadParameters.size());
    
    Field symbol = payloadParameters.get(0);
    Assert.assertEquals("symbol", symbol.getName());
    Assert.assertEquals("Market symbol (STRING type implicit). Author: ${constants.author}", symbol.getDescription());
    Assert.assertNull(symbol.getType());
    Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
    
    Field minOrderSize = payloadParameters.get(1);
    Assert.assertEquals("minOrderSize", minOrderSize.getName());
    Assert.assertEquals("Minimum order amount. Author: ${constants.author}", minOrderSize.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, minOrderSize.getType().getCanonicalType());
    Assert.assertEquals("0.0001", minOrderSize.getSampleValue());
    
    Field levels = payloadParameters.get(2);
    Assert.assertEquals("levels", levels.getName());
    Assert.assertEquals("Amount precision levels. Author: ${constants.author}", levels.getDescription());
    Assert.assertEquals(CanonicalType.LIST, levels.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.INT, levels.getType().getSubType().getCanonicalType());
    Assert.assertEquals(List.of(1, 10, 500).toString(), levels.getSampleValue().toString());
  }
  

  private void checkTickerStreamWebsocketEndpoint(WebsocketEndpointDescriptor tickerStreamEndpoint) {
    Assert.assertEquals("tickerStream", tickerStreamEndpoint.getName());
    Assert.assertEquals("Subscribe to ticker stream. Author: ${constants.author}", tickerStreamEndpoint.getDescription());
    Assert.assertEquals("${symbol}@ticker", tickerStreamEndpoint.getTopic());
    Assert.assertEquals("|", tickerStreamEndpoint.getTopicParametersListSeparator());
    
    List<Field> parameters = tickerStreamEndpoint.getRequest().getProperties();
    Field symbols = parameters.get(0);
    Assert.assertEquals("symbol", symbols.getName());
    Assert.assertEquals("Symbol to subscribe to ticker stream of. Use ${constants.allTickers} to subscribe to every ticker. Author: ${constants.author}", symbols.getDescription());
    Assert.assertEquals(CanonicalType.STRING, symbols.getType().getCanonicalType());
    Assert.assertEquals("${constants.allTickers}", symbols.getSampleValue());
    
    List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields = tickerStreamEndpoint.getMessageTopicMatcherFields();
    Assert.assertEquals(2, messageTopicMatcherFields.size());
    Assert.assertEquals("topic", messageTopicMatcherFields.get(0).getName());
    Assert.assertEquals("ticker", messageTopicMatcherFields.get(0).getValue());
    Assert.assertEquals("symbol", messageTopicMatcherFields.get(1).getName());
    Assert.assertEquals("${symbol}", messageTopicMatcherFields.get(1).getValue());
    
    List<Field> response = tickerStreamEndpoint.getMessage().getProperties();
    Assert.assertEquals(6, response.size());
    
    Field topic = response.get(0);
    Assert.assertEquals("topic", topic.getName());
    Assert.assertEquals("Topic. Author: ${constants.author}", topic.getDescription());
    Assert.assertEquals(CanonicalType.STRING, topic.getType().getCanonicalType());
    Assert.assertEquals("ticker", topic.getSampleValue());
    
    Field symbol = response.get(1);
    Assert.assertEquals("symbol", symbol.getName());
    Assert.assertEquals("Symbol name. Author: ${constants.author}", symbol.getDescription());
    Assert.assertEquals(CanonicalType.STRING, symbol.getType().getCanonicalType());
    Assert.assertEquals("${constants.demoSymbol}", symbol.getSampleValue());
    
    Field timestamp = response.get(2);
    Assert.assertEquals("timestamp", timestamp.getName());
    Assert.assertEquals("Timestamp of the message. Author: ${constants.author}", timestamp.getDescription());
    Assert.assertEquals(CanonicalType.LONG, timestamp.getType().getCanonicalType());
    Assert.assertEquals("1633036800000", timestamp.getSampleValue());
    
    Field priceChange = response.get(3);
    Assert.assertEquals("priceChange", priceChange.getName());
    Assert.assertEquals("Price change in the last 24 hours. Author: ${constants.author}", priceChange.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, priceChange.getType().getCanonicalType());
    Assert.assertEquals("100.00", priceChange.getSampleValue());
    
    Field priceChangePercent = response.get(4);
    Assert.assertEquals("priceChangePercent", priceChangePercent.getName());
    Assert.assertEquals("Price change percent in the last 24 hours. Author: ${constants.author}", priceChangePercent.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, priceChangePercent.getType().getCanonicalType());
    Assert.assertEquals("0.5", priceChangePercent.getSampleValue());
    
    Field last = response.get(5);
    Assert.assertEquals("last", last.getName());
    Assert.assertEquals("Last traded price. Author: ${constants.author}", last.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
    Assert.assertEquals("16000.00", last.getSampleValue());
  }
  
  private void checkEmployeeExchange(ExchangeDescriptor exchangeDescriptor) {
    Assert.assertEquals("Employee", exchangeDescriptor.getId());
    Assert.assertEquals(
        "Employee exchange is a demo exchange REST APIs to get, add, delete and "
        + "update employees and a websocket endpoint to get notified of updates from an employee database.<br>"
        + " A server can be started using <code>org.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br>"
        + " The URL of the HTTP server and Websocket server must be set using the ${config.baseHttpUrl} and ${config.baseWebsocketUrl} properties.<br>"
        + " Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.\n",
        exchangeDescriptor.getDescription());
    
    Assert.assertEquals("https://www.example.com/docs/employee", exchangeDescriptor.getDocUrl());
    Assert.assertEquals("org.jxapi.exchanges.employee.gen", exchangeDescriptor.getBasePackage());
    List<DefaultConfigProperty> properties = exchangeDescriptor.getProperties();
    Assert.assertEquals(3, properties.size());
    ConfigProperty property = properties.get(0);
    Assert.assertEquals("baseHttpUrl", property.getName());
    Assert.assertEquals("Base URL for REST endpoints the Employee Exchange API", property.getDescription());
    property = properties.get(1);
    Assert.assertEquals("baseWebsocketUrl", property.getName());
    Assert.assertEquals("Base URL for websocket endpoints of the Employee Exchange API", property.getDescription());
    property = properties.get(2);
    Assert.assertEquals("demoEmployeeId", property.getName());
    Assert.assertEquals("Used in demo snippets to set as value of Employee 'id' property", property.getDescription());
    Assert.assertEquals(1, property.getDefaultValue());
    
    Assert.assertEquals(0, CollectionUtil.emptyIfNull(exchangeDescriptor.getConstants()).size());
    
    Assert.assertEquals("${config.baseHttpUrl}", exchangeDescriptor.getHttpUrl());
    Assert.assertEquals("org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory", 
              exchangeDescriptor.getHttpRequestInterceptorFactory());
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
    Assert.assertEquals("${config.baseWebsocketUrl}", api.getWebsocketUrl());
    Assert.assertEquals("org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory", 
              api.getWebsocketHookFactory());
    List<WebsocketEndpointDescriptor> websocketEndpoints = api.getWebsocketEndpoints();
    Assert.assertEquals(1, websocketEndpoints.size());
    checkEmployeeUpdatesWebsocketEndpoint(websocketEndpoints.get(0));
  }

  private void checkEmployeeUpdatesWebsocketEndpoint(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
    Assert.assertEquals("employeeUpdates", websocketEndpointDescriptor.getName());
    Assert.assertEquals("Employee updates websocket", websocketEndpointDescriptor.getDescription());
    Assert.assertEquals("https://www.example.com/docs/employee/updates", websocketEndpointDescriptor.getDocUrl());
    Assert.assertNull(websocketEndpointDescriptor.getTopic());
    Assert.assertNull(websocketEndpointDescriptor.getTopicParametersListSeparator());
    Assert.assertNull(websocketEndpointDescriptor.getRequest());
    Assert.assertNull(websocketEndpointDescriptor.getMessageTopicMatcherFields());
    Field message = websocketEndpointDescriptor.getMessage();
    Assert.assertEquals("Employee update message", message.getDescription());
    List<Field> messageProperties = message.getProperties();
    Assert.assertEquals(2, messageProperties.size());
    Field eventType = messageProperties.get(0);
    Assert.assertEquals("eventType", eventType.getName());
    Assert.assertEquals("Type of event, e.g. one of ${constants.updateEmployeeTypeAdd}, ${constants.updateEmployeeTypeUpdate} or ${constants.updateEmployeeTypeDelete}", eventType.getDescription());
    Assert.assertNull(eventType.getType());
    Assert.assertEquals("${constants.updateEmployeeTypeUpdate}", eventType.getSampleValue());
    Field employee = messageProperties.get(1);
    Assert.assertEquals("employee", employee.getName());
    Assert.assertEquals("Employee that was updated", employee.getDescription());
    Assert.assertEquals("John", employee.getSampleValue());
    Assert.assertEquals("Employee", employee.getObjectName());
  }

  private void checkEmployeeExchangeV1ApiGroupConstants(List<Constant> constants) {
    Assert.assertEquals(7, constants.size());
    
    Constant constant = constants.get(0);
    Assert.assertEquals("defaultPageSize", constant.getName());
    Assert.assertEquals("Default page size for paginated requests", constant.getDescription());
    
    constant = constants.get(1);
    Assert.assertEquals("maxPageSize", constant.getName());
    Assert.assertEquals("Maximum page size for paginated requests", constant.getDescription());
    
    constant = constants.get(2);
    Assert.assertEquals("profileRegular", constant.getName());
    Assert.assertEquals("Regular employee profile", constant.getDescription());
    Assert.assertEquals("REGULAR", constant.getValue());
    
    constant = constants.get(3);
    Assert.assertEquals("profileAdmin", constant.getName());
    Assert.assertEquals("Admin employee profile", constant.getDescription());
    Assert.assertEquals("ADMIN", constant.getValue());
    
    constant = constants.get(4);
    Assert.assertEquals("updateEmployeeTypeAdd", constant.getName());
    Assert.assertEquals("Value of eventType field in WS message for new employee added event", constant.getDescription());
    Assert.assertEquals("ADD", constant.getValue());
    
    constant = constants.get(5);
    Assert.assertEquals("updateEmployeeTypeUpate", constant.getName());
    Assert.assertEquals("Value of eventType field in WS message for update of an existing employee event", constant.getDescription());
    Assert.assertEquals("UPDATE", constant.getValue());
    
    constant = constants.get(6);
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
    Assert.assertEquals("${config.demoEmployeeId}", getEmployeeIdByIdRequest.getSampleValue());
    Field employeeField = restEndpointGetEmployee.getResponse();
    checkEmployeeObjectField(employeeField);
  }
  
  private void checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpoint(RestEndpointDescriptor restEndpointGetEmployee) {
    Assert.assertEquals("getAllEmployees", restEndpointGetEmployee.getName());
    Assert.assertEquals("Get all employees", restEndpointGetEmployee.getDescription());
    Assert.assertEquals(HttpMethod.GET, restEndpointGetEmployee.getHttpMethod());
    Assert.assertEquals("https://www.example.com/docs/employee/getAll", restEndpointGetEmployee.getDocUrl());
    Assert.assertEquals("/employees", restEndpointGetEmployee.getUrl());
    
    checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpointRequest(restEndpointGetEmployee.getRequest());
    checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpointResponse(restEndpointGetEmployee.getResponse());

  }
  
  private void checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpointRequest(Field restEndpointGetAllEmployeesRequest) {
    Assert.assertNotNull(restEndpointGetAllEmployeesRequest);
    List<String> requestImplementedInterfaces = restEndpointGetAllEmployeesRequest.getImplementedInterfaces();
    Assert.assertEquals(1, requestImplementedInterfaces.size());
    Assert.assertEquals("org.jxapi.exchanges.employee.EmployeePaginatedRequest", requestImplementedInterfaces.get(0));
    List<Field> requestProperties = restEndpointGetAllEmployeesRequest.getProperties();
    Assert.assertEquals(2, requestProperties.size());
    Field pageField = requestProperties.get(0);
    Assert.assertEquals("page", pageField.getName());
    Assert.assertEquals(Type.INT, pageField.getType());
    Assert.assertEquals("Page number to return, defaults to 1.", pageField.getDescription());
    Assert.assertEquals(1, pageField.getSampleValue());
    Field sizeField = requestProperties.get(1);
    Assert.assertEquals("size", sizeField.getName());
    Assert.assertEquals(Type.INT, sizeField.getType());
    Assert.assertEquals("Number of employees to return per page.<br> Defaults to ${constants.defaultPageSize}.<br> Maximum is ${constants.maxPageSize}.\n", 
                        sizeField.getDescription());
  }
  
  private void checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpointResponse(Field restEndpointGetAllEmployeesReesponse) {
    List<String> responseImplementedInterfaces = restEndpointGetAllEmployeesReesponse.getImplementedInterfaces();
    Assert.assertEquals(1, responseImplementedInterfaces.size());
    Assert.assertEquals("org.jxapi.exchanges.employee.EmployeePaginatedResponse", responseImplementedInterfaces.get(0));
    List<Field> employeeResponseProperties = restEndpointGetAllEmployeesReesponse.getProperties();
    Assert.assertEquals(3, employeeResponseProperties.size());
    Field pageField = employeeResponseProperties.get(0);
    Assert.assertEquals("page", pageField.getName());
    Assert.assertEquals(Type.INT, pageField.getType());
    Assert.assertEquals("Page index, starting from 1", pageField.getDescription());
    Assert.assertEquals(1, pageField.getSampleValue());
    Field totalPagesField = employeeResponseProperties.get(1);
    Assert.assertEquals("totalPages", totalPagesField.getName());
    Assert.assertEquals(Type.INT, totalPagesField.getType());
    Assert.assertEquals("Total number of pages available", totalPagesField.getDescription());
    Assert.assertEquals(10, totalPagesField.getSampleValue());
    Field employeesField = employeeResponseProperties.get(2);
    Assert.assertEquals("employees", employeesField.getName());
    Assert.assertEquals("Employee", employeesField.getObjectName());
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
    Assert.assertEquals("${config.demoEmployeeId}", deleteEmployeeIdRequest.getSampleValue());
  }
  
  private void checkEmployeeObjectField(Field employeeField) {
    Assert.assertEquals("Employee", employeeField.getObjectName());
    List<Field> employeeProperties = employeeField.getProperties();
    Assert.assertEquals(4, employeeProperties.size());
    Field idField = employeeProperties.get(0);
    Assert.assertEquals("id", idField.getName());
    Assert.assertEquals(Type.INT, idField.getType());
    Assert.assertEquals("Employee ID", idField.getDescription());
    Assert.assertEquals("${config.demoEmployeeId}", idField.getSampleValue());
    
    Field firstNameField = employeeProperties.get(1);
    Assert.assertEquals("firstName", firstNameField.getName());
    Assert.assertNull(firstNameField.getType());
    Assert.assertEquals("Employee first name", firstNameField.getDescription());
    Assert.assertEquals("John", firstNameField.getSampleValue());
    
    Field lastNameField = employeeProperties.get(2);
    Assert.assertEquals("lastName", lastNameField.getName());
    Assert.assertNull(lastNameField.getType());
    Assert.assertEquals("Employee last name", lastNameField.getDescription());
    
    Field profileField = employeeProperties.get(3);
    Assert.assertEquals("profile", profileField.getName());
    Assert.assertNull(profileField.getType());
    Assert.assertEquals("Employee profile. Can be ${constants.profileRegular} or ${constants.profileAdmin}", profileField.getDescription());
    Assert.assertEquals("${constants.profileRegular}", profileField.getSampleValue());
  }
  
}
