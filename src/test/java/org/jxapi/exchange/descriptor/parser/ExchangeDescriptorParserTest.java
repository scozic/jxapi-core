package org.jxapi.exchange.descriptor.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

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
    Assert.assertEquals("A sample Exchange descriptor file. Should be provided config properties: ${config.apiKey}, ${config.apiSecret}. Author: ${constants.author.firstName} ${constants.author.lastName}", 
                        exchangeDescriptor.getDescription());
    Assert.assertEquals("com.foo.bar.gen", exchangeDescriptor.getBasePackage());
    List<ExchangeApiDescriptor> apis = exchangeDescriptor.getApis();
    Assert.assertEquals(1, apis.size());
    checkMarketDataApi(apis.get(0));
  }
  
  private void checkMarketDataApi(ExchangeApiDescriptor marketDataApi) {
    Assert.assertEquals("MarketData", marketDataApi.getName());
    Assert.assertEquals("The market data API of MyTestExchange. Author: ${constants.authorFullName}", marketDataApi.getDescription());
    List<RestEndpointDescriptor> restEndpoints = marketDataApi.getRestEndpoints();
    Assert.assertEquals(2, restEndpoints.size());
    checkExchangeInfoRestEndpoint(restEndpoints.get(0));
    checkTickersRestEndpooint(restEndpoints.get(1));
    List<WebsocketEndpointDescriptor> websocketEndpoints = marketDataApi.getWebsocketEndpoints();
    Assert.assertEquals(1, websocketEndpoints.size());
    checkTickerStreamWebsocketEndpoint(websocketEndpoints.get(0));
  }

  private void checkTickersRestEndpooint(RestEndpointDescriptor tickersEndPoint) {
    Assert.assertEquals("tickers", tickersEndPoint.getName());
    Assert.assertEquals("Fetch current tickers. Author: ${constants.authorFullName}", tickersEndPoint.getDescription());
    Assert.assertEquals(HttpMethod.GET.toString(), tickersEndPoint.getHttpMethod());
    Assert.assertEquals("https://com.sample.mycex/tickers", tickersEndPoint.getUrl());
    List<Field> exchangeInfoParameters = tickersEndPoint.getRequest().getProperties();
    Assert.assertEquals(0, exchangeInfoParameters.size());
    
    checkTickersResponse(tickersEndPoint.getResponse().getProperties());
    
  }

  private void checkTickersResponse(List<Field> tickersResponse) {
    Assert.assertEquals(2, tickersResponse.size());
    Field responseCode = tickersResponse.get(0);
    Assert.assertEquals("responseCode", responseCode.getName());
    Assert.assertEquals("Request response code. Author: ${constants.authorFullName}", responseCode.getDescription());
    Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
    Assert.assertEquals("0", responseCode.getSampleValue());
    
    Field payload = tickersResponse.get(1);
    Assert.assertEquals("payload", payload.getName());
    Assert.assertEquals("Tickers for each symbol. Author: ${constants.authorFullName}", payload.getDescription());
    Assert.assertEquals(CanonicalType.MAP, payload.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
    List<Field> payloadParameters = payload.getProperties();
    Assert.assertEquals(1, payloadParameters.size());
    
    Field last = payloadParameters.get(0);
    Assert.assertEquals("last", last.getName());
    Assert.assertEquals("Last traded price. Author: ${constants.authorFullName}", last.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
    Assert.assertEquals(Double.valueOf("10.0"), last.getSampleValue());
    
  }

  private void checkExchangeInfoRestEndpoint(RestEndpointDescriptor exchangeInfoEndPoint) {
    Assert.assertEquals("exchangeInfo", exchangeInfoEndPoint.getName());
    Assert.assertEquals("Fetch market information of symbols that can be traded. Author: ${constants.authorFullName}", exchangeInfoEndPoint.getDescription());
    Assert.assertEquals(HttpMethod.GET.toString(), exchangeInfoEndPoint.getHttpMethod());
    Assert.assertEquals("https://com.sample.mycex/exchangeInfo", exchangeInfoEndPoint.getUrl());
    List<Field> exchangeInfoParameters = exchangeInfoEndPoint.getRequest().getProperties();
    
    Assert.assertEquals(4, exchangeInfoParameters.size());
    Field symbolsParameter = exchangeInfoParameters.get(0);
    Assert.assertEquals("symbols", symbolsParameter.getName());
    Assert.assertEquals("The list of symbol to fetch market information for. Leave empty to fetch all markets. Author: ${constants.authorFullName}", symbolsParameter.getDescription());
    Assert.assertEquals(CanonicalType.LIST, symbolsParameter.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.STRING, symbolsParameter.getType().getSubType().getCanonicalType());
    Assert.assertEquals("[\"${constants.demoSymbol}\"]", symbolsParameter.getSampleValue());
    
    Field apiKeyParameter = exchangeInfoParameters.get(1);
    Assert.assertEquals("apiKey", apiKeyParameter.getName());
    Assert.assertEquals("API key, see ${config.apiKey}", apiKeyParameter.getDescription());
    Assert.assertEquals(CanonicalType.STRING, apiKeyParameter.getType().getCanonicalType());
    Assert.assertEquals("${config.apiKey}", apiKeyParameter.getSampleValue());
    
    Field authorParameter = exchangeInfoParameters.get(2);
    Assert.assertEquals("author", authorParameter.getName());
    Assert.assertEquals("Author name, see ${constants.authorFullName}", authorParameter.getDescription());
    Assert.assertEquals("${constants.author.firstName} ${constants.author.lastName}", authorParameter.getSampleValue());
    
    Field pageParameter = exchangeInfoParameters.get(3);
    Assert.assertEquals("page", pageParameter.getName());
    Assert.assertEquals("Page number to return, defaults to 1. Author: ${constants.authorFullName}", pageParameter.getDescription());
    
    checkExchangeInfoResponse(exchangeInfoEndPoint.getResponse().getProperties());
  }
  
  private void checkExchangeInfoResponse(List<Field> exchangeInfoResponse) {
    Assert.assertEquals(4, exchangeInfoResponse.size());
    Field responseCode = exchangeInfoResponse.get(0);
    Assert.assertEquals("responseCode", responseCode.getName());
    Assert.assertEquals("Request response code", responseCode.getDescription());
    Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
    Assert.assertEquals("0", responseCode.getSampleValue());
    
    Field currentPage = exchangeInfoResponse.get(1);
    Assert.assertEquals("currentPage", currentPage.getName());
    Assert.assertEquals("Current page index.", currentPage.getDescription());
    Assert.assertEquals(CanonicalType.INT, currentPage.getType().getCanonicalType());
    Assert.assertEquals(1, currentPage.getSampleValue());
    
    Field totalPages = exchangeInfoResponse.get(2);
    Assert.assertEquals("totalPages", totalPages.getName());
    Assert.assertEquals("Total number of pages.", totalPages.getDescription());
    Assert.assertEquals(CanonicalType.INT, totalPages.getType().getCanonicalType());
    Assert.assertEquals(10, totalPages.getSampleValue());
    
    Field payload = exchangeInfoResponse.get(3);
    Assert.assertEquals("payload", payload.getName());
    Assert.assertEquals("List of market information for each requested symbol. Author: ${constants.authorFullName}", payload.getDescription());
    Assert.assertEquals(CanonicalType.LIST, payload.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
    List<Field> payloadParameters = payload.getProperties();
    Assert.assertEquals(3, payloadParameters.size());
    
    Field symbol = payloadParameters.get(0);
    Assert.assertEquals("symbol", symbol.getName());
    Assert.assertEquals("Market symbol (STRING type implicit). Author: ${constants.authorFullName}", symbol.getDescription());
    Assert.assertNull(symbol.getType());
    Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
    
    Field minOrderSize = payloadParameters.get(1);
    Assert.assertEquals("minOrderSize", minOrderSize.getName());
    Assert.assertEquals("Minimum order amount. Author: ${constants.authorFullName}", minOrderSize.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, minOrderSize.getType().getCanonicalType());
    Assert.assertEquals("0.0001", minOrderSize.getSampleValue());
    
    Field levels = payloadParameters.get(2);
    Assert.assertEquals("levels", levels.getName());
    Assert.assertEquals("Amount precision levels. Author: ${constants.authorFullName}", levels.getDescription());
    Assert.assertEquals(CanonicalType.LIST, levels.getType().getCanonicalType());
    Assert.assertEquals(CanonicalType.INT, levels.getType().getSubType().getCanonicalType());
    Assert.assertEquals(List.of(1, 10, 500).toString(), levels.getSampleValue().toString());
  }
  

  private void checkTickerStreamWebsocketEndpoint(WebsocketEndpointDescriptor tickerStreamEndpoint) {
    Assert.assertEquals("tickerStream", tickerStreamEndpoint.getName());
    Assert.assertEquals("Subscribe to ticker stream. Author: ${constants.authorFullName}", tickerStreamEndpoint.getDescription());
    Assert.assertEquals("${request.symbol}@ticker", tickerStreamEndpoint.getTopic());
    
    List<Field> parameters = tickerStreamEndpoint.getRequest().getProperties();
    Field symbols = parameters.get(0);
    Assert.assertEquals("symbol", symbols.getName());
    Assert.assertEquals("Symbol to subscribe to ticker stream of. Use ${constants.allTickers} to subscribe to every ticker. Author: ${constants.authorFullName}", symbols.getDescription());
    Assert.assertEquals(CanonicalType.STRING, symbols.getType().getCanonicalType());
    Assert.assertEquals("${constants.demoSymbol}", symbols.getSampleValue());
    List<Field> response = tickerStreamEndpoint.getMessage().getProperties();
    Assert.assertEquals(6, response.size());
    
    Field topic = response.get(0);
    Assert.assertEquals("topic", topic.getName());
    Assert.assertEquals("Topic. Author: ${constants.authorFullName}", topic.getDescription());
    Assert.assertEquals(CanonicalType.STRING, topic.getType().getCanonicalType());
    Assert.assertEquals("ticker", topic.getSampleValue());
    
    Field symbol = response.get(1);
    Assert.assertEquals("symbol", symbol.getName());
    Assert.assertEquals("Symbol name. Author: ${constants.authorFullName}", symbol.getDescription());
    Assert.assertEquals(CanonicalType.STRING, symbol.getType().getCanonicalType());
    Assert.assertEquals("${constants.demoSymbol}", symbol.getSampleValue());
    
    Field timestamp = response.get(2);
    Assert.assertEquals("timestamp", timestamp.getName());
    Assert.assertEquals("Timestamp of the message. Author: ${constants.authorFullName}", timestamp.getDescription());
    Assert.assertEquals(CanonicalType.LONG, timestamp.getType().getCanonicalType());
    Assert.assertEquals("1633036800000", timestamp.getSampleValue());
    
    Field priceChange = response.get(3);
    Assert.assertEquals("priceChange", priceChange.getName());
    Assert.assertEquals("Price change in the last 24 hours. Author: ${constants.authorFullName}", priceChange.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, priceChange.getType().getCanonicalType());
    Assert.assertEquals("100.00", priceChange.getSampleValue());
    
    Field priceChangePercent = response.get(4);
    Assert.assertEquals("priceChangePercent", priceChangePercent.getName());
    Assert.assertEquals("Price change percent in the last 24 hours. Author: ${constants.authorFullName}", priceChangePercent.getDescription());
    Assert.assertEquals(CanonicalType.BIGDECIMAL, priceChangePercent.getType().getCanonicalType());
    Assert.assertEquals("0.5", priceChangePercent.getSampleValue());
    
    Field last = response.get(5);
    Assert.assertEquals("last", last.getName());
    Assert.assertEquals("Last traded price. Author: ${constants.authorFullName}", last.getDescription());
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

    checkEmployeeExchangeProperties(exchangeDescriptor);
    
    Assert.assertEquals("${config.server.baseHttpUrl}", exchangeDescriptor.getHttpUrl());
    Assert.assertEquals(1, exchangeDescriptor.getApis().size());
    checkEmployeeExchangeConstants(exchangeDescriptor.getConstants());
    checkEmployeeExchangeV1ApiGroup(exchangeDescriptor.getApis().get(0));
  }
  
  private void checkEmployeeExchangeProperties(ExchangeDescriptor exchangeDescriptor) {
    List<ConfigPropertyDescriptor> properties = exchangeDescriptor.getProperties();
    Assert.assertEquals(1, properties.size());
    ConfigPropertyDescriptor serverGroupProp = properties.get(0);
    Assert.assertEquals("server", serverGroupProp.getName());
    Assert.assertEquals("Server related properties", serverGroupProp.getDescription());
    ConfigPropertyDescriptor baseHttpUrlProp = serverGroupProp.getProperties().get(0);
    Assert.assertEquals("baseHttpUrl", baseHttpUrlProp.getName());
    Assert.assertEquals("Base URL for REST endpoints the Employee Exchange API", baseHttpUrlProp.getDescription());
    ConfigPropertyDescriptor wsHttpUrlProp = serverGroupProp.getProperties().get(1);
    Assert.assertEquals("baseWebsocketUrl", wsHttpUrlProp.getName());
    Assert.assertEquals("Base URL for websocket endpoints of the Employee Exchange API", wsHttpUrlProp.getDescription());
  }
  
  private void checkEmployeeExchangeV1ApiGroup(ExchangeApiDescriptor api) {
    Assert.assertEquals("v1", api.getName());
    Assert.assertEquals("Version 1 of the Employee API", api.getDescription());
    Assert.assertEquals("v1", api.getHttpUrl());
    List<RestEndpointDescriptor> restEndpoints = api.getRestEndpoints();
    Assert.assertEquals(5, restEndpoints.size());
    checkEmployeeExchangeV1ApiGroupGetEmployeeRestEndpoint(restEndpoints.get(0));
    checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpoint(restEndpoints.get(1));
    checkEmployeeExchangeV1ApiGroupAddEmployeesRestEndpoint(restEndpoints.get(2));
    checkEmployeeExchangeV1ApiGroupUpdateEmployeesRestEndpoint(restEndpoints.get(3));
    checkEmployeeExchangeV1ApiGroupDeleteEmployeesRestEndpoint(restEndpoints.get(4));
    List<WebsocketEndpointDescriptor> websocketEndpoints = api.getWebsocketEndpoints();
    Assert.assertEquals(1, websocketEndpoints.size());
    checkEmployeeUpdatesWebsocketEndpoint(websocketEndpoints.get(0));
  }

  private void checkEmployeeUpdatesWebsocketEndpoint(WebsocketEndpointDescriptor websocketEndpointDescriptor) {
    Assert.assertEquals("employeeUpdates", websocketEndpointDescriptor.getName());
    Assert.assertEquals("Employee updates websocket", websocketEndpointDescriptor.getDescription());
    Assert.assertEquals("https://www.example.com/docs/employee/updates", websocketEndpointDescriptor.getDocUrl());
    Assert.assertNull(websocketEndpointDescriptor.getTopic());
    Assert.assertNull(websocketEndpointDescriptor.getRequest());
    Field message = websocketEndpointDescriptor.getMessage();
    Assert.assertEquals("Employee update message", message.getDescription());
    List<Field> messageProperties = message.getProperties();
    Assert.assertEquals(2, messageProperties.size());
    Field eventType = messageProperties.get(0);
    Assert.assertEquals("eventType", eventType.getName());
    Assert.assertEquals("Type of event, e.g. one of ${constants.updateEmployeeType.add}, ${constants.updateEmployeeType.update} or ${constants.updateEmployeeType.delete} (see ${constants.updateEmployeeType} constants).", eventType.getDescription());
    Assert.assertNull(eventType.getType());
    Assert.assertEquals("${constants.updateEmployeeType.update}", eventType.getSampleValue());
    Field employee = messageProperties.get(1);
    Assert.assertEquals("employee", employee.getName());
    Assert.assertEquals("Employee that was updated", employee.getDescription());
    Assert.assertEquals("John", employee.getSampleValue());
    Assert.assertEquals("Employee", employee.getObjectName());
  }

  private void checkEmployeeExchangeConstants(List<ConstantDescriptor> constants) {
    Assert.assertEquals(4, constants.size());
    
    ConstantDescriptor constant = constants.get(0);
    Assert.assertEquals("defaultPageSize", constant.getName());
    Assert.assertEquals("Default page size for paginated requests", constant.getDescription());
    
    constant = constants.get(1);
    Assert.assertEquals("maxPageSize", constant.getName());
    Assert.assertEquals("Maximum page size for paginated requests", constant.getDescription());
    
    constant = constants.get(2);
    Assert.assertEquals("profile", constant.getName());
    Assert.assertEquals("Employee profile types", constant.getDescription());
    checkEmployeeExchangeProfileConstants(constant.getConstants());
    
    
    constant = constants.get(3);
    Assert.assertEquals("updateEmployeeType", constant.getName());
    Assert.assertEquals("Value of eventType field in WS message", constant.getDescription());
    checkEmployeeExchangeUpdateEmployeeTypeConstants(constant.getConstants());
  }
  
  private void checkEmployeeExchangeUpdateEmployeeTypeConstants(List<ConstantDescriptor> constants) {
    ConstantDescriptor constant = constants.get(0);
    Assert.assertEquals("add", constant.getName());
    Assert.assertEquals("Value of eventType field in WS message for new employee added event", constant.getDescription());
    Assert.assertEquals("ADD", constant.getValue());
    
    constant = constants.get(1);
    Assert.assertEquals("update", constant.getName());
    Assert.assertEquals("Value of eventType field in WS message for update of an existing employee event", constant.getDescription());
    Assert.assertEquals("UPDATE", constant.getValue());
    
    constant = constants.get(2);
    Assert.assertEquals("delete", constant.getName());
    Assert.assertEquals("Value of eventType field in WS message for update of an existing employee event", constant.getDescription());
    
  }

  private void checkEmployeeExchangeProfileConstants(List<ConstantDescriptor> constants) {
    ConstantDescriptor constant = constants.get(0);
    Assert.assertEquals("regular", constant.getName());
    Assert.assertEquals("Regular employee profile", constant.getDescription());
    Assert.assertEquals("REGULAR", constant.getValue());
    
    constant = constants.get(1);
    Assert.assertEquals("admin", constant.getName());
    Assert.assertEquals("Admin employee profile", constant.getDescription());
    Assert.assertEquals("ADMIN", constant.getValue());
    
  }
  
  private void checkEmployeeExchangeV1ApiGroupGetEmployeeRestEndpoint(RestEndpointDescriptor restEndpointGetEmployee) {
    Assert.assertEquals("getEmployee", restEndpointGetEmployee.getName());
    Assert.assertEquals("Get employee details by ID", restEndpointGetEmployee.getDescription());
    Assert.assertEquals(HttpMethod.GET.name(), restEndpointGetEmployee.getHttpMethod());
    Assert.assertEquals("https://www.example.com/docs/employee/get", restEndpointGetEmployee.getDocUrl());
    Assert.assertEquals("/employee", restEndpointGetEmployee.getUrl());
    Field getEmployeeIdByIdRequest = restEndpointGetEmployee.getRequest();
    Assert.assertEquals("id", getEmployeeIdByIdRequest.getName());
    Assert.assertEquals(Type.INT, getEmployeeIdByIdRequest.getType());
    Assert.assertEquals("Employee ID", getEmployeeIdByIdRequest.getDescription());
    Assert.assertEquals(1, getEmployeeIdByIdRequest.getSampleValue());
    Field employeeField = restEndpointGetEmployee.getResponse();
    checkEmployeeObjectField(employeeField);
  }
  
  private void checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpoint(RestEndpointDescriptor restEndpointGetAllEmployees) {
    Assert.assertEquals("getAllEmployees", restEndpointGetAllEmployees.getName());
    Assert.assertEquals("Get all employees", restEndpointGetAllEmployees.getDescription());
    Assert.assertEquals(HttpMethod.GET.name(), restEndpointGetAllEmployees.getHttpMethod());
    Assert.assertEquals("https://www.example.com/docs/employee/getAll", restEndpointGetAllEmployees.getDocUrl());
    Assert.assertEquals("/employees", restEndpointGetAllEmployees.getUrl());
    Assert.assertTrue(restEndpointGetAllEmployees.isPaginated());
    
    checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpointRequest(restEndpointGetAllEmployees.getRequest());
    checkEmployeeExchangeV1ApiGroupGetAllEmployeesRestEndpointResponse(restEndpointGetAllEmployees.getResponse());

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
    Assert.assertEquals(HttpMethod.POST.name(), restEndpointGetEmployee.getHttpMethod());
    Assert.assertEquals("https://www.example.com/docs/employee/add", restEndpointGetEmployee.getDocUrl());
    Assert.assertEquals("/employee", restEndpointGetEmployee.getUrl());
    Field employeeField = restEndpointGetEmployee.getRequest();
    Assert.assertEquals("Employee", employeeField.getObjectName());
  }
  
  private void checkEmployeeExchangeV1ApiGroupUpdateEmployeesRestEndpoint(RestEndpointDescriptor restEndpointDescriptor) {
    Assert.assertEquals("updateEmployee", restEndpointDescriptor.getName());
        Assert.assertEquals("Update an existing employee", restEndpointDescriptor.getDescription());
        Assert.assertEquals(HttpMethod.PUT.name(), restEndpointDescriptor.getHttpMethod());
        Assert.assertEquals("https://www.example.com/docs/employee/add", restEndpointDescriptor.getDocUrl());
        Assert.assertEquals("/employee", restEndpointDescriptor.getUrl());
        Field updateEmployeeRequest = restEndpointDescriptor.getRequest();
        Assert.assertEquals("Employee", updateEmployeeRequest.getObjectName());
  }

  private void checkEmployeeExchangeV1ApiGroupDeleteEmployeesRestEndpoint(RestEndpointDescriptor restEndpointDescriptor) {
    Assert.assertEquals("deleteEmployee", restEndpointDescriptor.getName());
    Assert.assertEquals("Delete an employee", restEndpointDescriptor.getDescription());
    Assert.assertEquals(HttpMethod.DELETE.name(), restEndpointDescriptor.getHttpMethod());
    Assert.assertEquals("https://www.example.com/docs/employee/delete", restEndpointDescriptor.getDocUrl());
    Assert.assertEquals("/employee", restEndpointDescriptor.getUrl());
    Field deleteEmployeeIdRequest = restEndpointDescriptor.getRequest();
    Assert.assertEquals("id", deleteEmployeeIdRequest.getName());
    Assert.assertEquals(Type.INT, deleteEmployeeIdRequest.getType());
    Assert.assertEquals("Employee ID", deleteEmployeeIdRequest.getDescription());
    Assert.assertEquals(1, deleteEmployeeIdRequest.getSampleValue());
  }
  
  private void checkEmployeeObjectField(Field employeeField) {
    Assert.assertEquals("Employee", employeeField.getObjectName());
    List<Field> employeeProperties = employeeField.getProperties();
    Assert.assertEquals(4, employeeProperties.size());
    Field idField = employeeProperties.get(0);
    Assert.assertEquals("id", idField.getName());
    Assert.assertEquals(Type.INT, idField.getType());
    Assert.assertEquals("Employee ID", idField.getDescription());
    Assert.assertEquals(1, idField.getSampleValue());
    
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
    Assert.assertEquals("Employee profile. See ${constants.profile}", 
                        profileField.getDescription());
    Assert.assertEquals("${constants.profile.regular}", profileField.getSampleValue());
  }
  
}
