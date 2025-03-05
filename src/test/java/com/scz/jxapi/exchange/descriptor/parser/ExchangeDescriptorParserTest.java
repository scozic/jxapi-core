package com.scz.jxapi.exchange.descriptor.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketMessageTopicMatcherFieldDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import com.scz.jxapi.netutils.rest.HttpMethod;

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
		Assert.assertEquals("MyTestExchange", exchangeDescriptor.getName());
		Assert.assertEquals("A sample Exchange descriptor file", exchangeDescriptor.getDescription());
		Assert.assertEquals("com.foo.bar.gen", exchangeDescriptor.getBasePackage());
		List<ExchangeApiDescriptor> apis = exchangeDescriptor.getApis();
		Assert.assertEquals(1, apis.size());
		checkMarketDataApi(apis.get(0));
	}
	
	private void checkMarketDataApi(ExchangeApiDescriptor marketDataApi) {
		Assert.assertEquals("MarketData", marketDataApi.getName());
		Assert.assertEquals("The market data API of MyTestExchange", marketDataApi.getDescription());
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
		Assert.assertEquals("Fetch current tickers", tickersEndPoint.getDescription());
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
		Assert.assertEquals("Request response code", responseCode.getDescription());
		Assert.assertEquals(CanonicalType.INT, responseCode.getType().getCanonicalType());
		Assert.assertEquals("0", responseCode.getSampleValue());
		
		Field payload = tickersResponse.get(1);
		Assert.assertEquals("payload", payload.getName());
		Assert.assertEquals("Tickers for each symbol", payload.getDescription());
		Assert.assertEquals(CanonicalType.MAP, payload.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
		List<Field> payloadParameters = payload.getProperties();
		Assert.assertEquals(1, payloadParameters.size());
		
		Field last = payloadParameters.get(0);
		Assert.assertEquals("last", last.getName());
		Assert.assertEquals("Last traded price", last.getDescription());
		Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
		Assert.assertEquals(Double.valueOf("10.0"), last.getSampleValue());
		
	}

	private void checkExchangeInfoRestEndpoint(RestEndpointDescriptor exchangeInfoEndPoint) {
		Assert.assertEquals("exchangeInfo", exchangeInfoEndPoint.getName());
		Assert.assertEquals("Fetch market information of symbols that can be traded", exchangeInfoEndPoint.getDescription());
		Assert.assertEquals(HttpMethod.GET, exchangeInfoEndPoint.getHttpMethod());
		Assert.assertEquals("https://com.sample.mycex/exchangeInfo", exchangeInfoEndPoint.getUrl());
		List<Field> exchangeInfoParameters = exchangeInfoEndPoint.getRequest().getProperties();
		
		Assert.assertEquals(1, exchangeInfoParameters.size());
		Field symbolsParameter = exchangeInfoParameters.get(0);
		Assert.assertEquals("symbols", symbolsParameter.getName());
		Assert.assertEquals("The list of symbol to fetch market information for. Leave empty to fetch all markets", symbolsParameter.getDescription());
		Assert.assertEquals(CanonicalType.LIST, symbolsParameter.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.STRING, symbolsParameter.getType().getSubType().getCanonicalType());
		Assert.assertEquals("[\"BTC\", \"ETH\"]", symbolsParameter.getSampleValue());
		
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
		Assert.assertEquals("List of market information for each requested symbol", payload.getDescription());
		Assert.assertEquals(CanonicalType.LIST, payload.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.OBJECT, payload.getType().getSubType().getCanonicalType());
		List<Field> payloadParameters = payload.getProperties();
		Assert.assertEquals(3, payloadParameters.size());
		
		Field symbol = payloadParameters.get(0);
		Assert.assertEquals("symbol", symbol.getName());
		Assert.assertEquals("Market symbol (STRING type implicit)", symbol.getDescription());
		Assert.assertNull(symbol.getType());
		Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
		
		Field minOrderSize = payloadParameters.get(1);
		Assert.assertEquals("minOrderSize", minOrderSize.getName());
		Assert.assertEquals("Minimum order amount", minOrderSize.getDescription());
		Assert.assertEquals(CanonicalType.BIGDECIMAL, minOrderSize.getType().getCanonicalType());
		Assert.assertEquals("0.0001", minOrderSize.getSampleValue());
		
		Field levels = payloadParameters.get(2);
		Assert.assertEquals("levels", levels.getName());
		Assert.assertEquals("Amount precision levels", levels.getDescription());
		Assert.assertEquals(CanonicalType.LIST, levels.getType().getCanonicalType());
		Assert.assertEquals(CanonicalType.INT, levels.getType().getSubType().getCanonicalType());
		Assert.assertEquals(List.of(1, 10, 500).toString(), levels.getSampleValue().toString());
	}
	

	private void checkTickerStreamWebsocketEndpoint(WebsocketEndpointDescriptor tickerStreamEndpoint) {
		Assert.assertEquals("tickerStream", tickerStreamEndpoint.getName());
		Assert.assertEquals("Subscribe to ticker stream", tickerStreamEndpoint.getDescription());
		Assert.assertEquals("${symbol}@ticker", tickerStreamEndpoint.getTopic());
		Assert.assertEquals("|", tickerStreamEndpoint.getTopicParametersListSeparator());
		
		List<Field> parameters = tickerStreamEndpoint.getRequest().getProperties();
		Field symbols = parameters.get(0);
		Assert.assertEquals("symbol", symbols.getName());
		Assert.assertEquals("Symbol to subscribe to ticker stream of", symbols.getDescription());
		Assert.assertEquals(CanonicalType.STRING, symbols.getType().getCanonicalType());
		Assert.assertEquals("BTC_USDT", symbols.getSampleValue());
		
		List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields = tickerStreamEndpoint.getMessageTopicMatcherFields();
		Assert.assertEquals(2, messageTopicMatcherFields.size());
		Assert.assertEquals("topic", messageTopicMatcherFields.get(0).getName());
		Assert.assertEquals("ticker", messageTopicMatcherFields.get(0).getValue());
		Assert.assertEquals("symbol", messageTopicMatcherFields.get(1).getName());
		Assert.assertEquals("${symbol}", messageTopicMatcherFields.get(1).getValue());
		
		List<Field> response = tickerStreamEndpoint.getMessage().getProperties();
		Assert.assertEquals(3, response.size());
		
		Field topic = response.get(0);
		Assert.assertEquals("topic", topic.getName());
		Assert.assertEquals("Topic", topic.getDescription());
		Assert.assertEquals(CanonicalType.STRING, topic.getType().getCanonicalType());
		Assert.assertEquals("ticker", topic.getSampleValue());
		
		Field symbol = response.get(1);
		Assert.assertEquals("symbol", symbol.getName());
		Assert.assertEquals("Symbol name", symbol.getDescription());
		Assert.assertEquals(CanonicalType.STRING, symbol.getType().getCanonicalType());
		Assert.assertEquals("BTC_USDT", symbol.getSampleValue());
		
		Field last = response.get(2);
		Assert.assertEquals("last", last.getName());
		Assert.assertEquals("Last traded price", last.getDescription());
		Assert.assertEquals(CanonicalType.BIGDECIMAL, last.getType().getCanonicalType());
		Assert.assertEquals("16000.00", last.getSampleValue());
	}
	
	private void checkEmployeeExchange(ExchangeDescriptor exchangeDescriptor) {
		Assert.assertEquals("Employee", exchangeDescriptor.getName());
		Assert.assertEquals(
				"Employee exchange is a demo exchange REST APIs to get, add, delete and  update employees and "
				+ "a websocket endpoint to get notified of updates from an employee database.<br> A server can be started "
				+ "using <code>com.scz.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br> " 
				+ "The URL of the server must be set using the baseUrl property.<br>"
				+ " Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and "
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
		Assert.assertEquals("Value to replace in HTTP or Websocket base URL with value of <i>baseHttpUrl</i> or <i>baseWebsocketUrl</i> properties", 
							constant.getDescription());
		Assert.assertEquals("BASEURL", constant.getValue());
		
		Assert.assertEquals("BASEURL", exchangeDescriptor.getHttpUrl());
		Assert.assertEquals("com.scz.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory", 
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
		Assert.assertEquals("BASEURL", api.getWebsocketUrl());
		Assert.assertEquals("com.scz.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory", 
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
		Assert.assertEquals("Type of event. Can be 'ADD', 'UPDATE' or 'DELETE'", eventType.getDescription());
		Assert.assertNull(eventType.getType());
		Assert.assertEquals(1, eventType.getSampleValue());
		Field employee = messageProperties.get(1);
		Assert.assertEquals("employee", employee.getName());
		Assert.assertEquals("Employee that was updated", employee.getDescription());
		Assert.assertEquals("John", employee.getSampleValue());
		Assert.assertEquals("Employee", employee.getObjectName());
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
		Assert.assertEquals("Employee profile. Can be 'regular' or 'admin'", profileField.getDescription());
		Assert.assertEquals("REGULAR", profileField.getSampleValue());
	}
	
}
