# JXAPI

Generate a Java REST and/or Websocket API wrapper efficiently using code generation tools.

## Introduction
Most web services are published as REST/Websocket APIs. Those APIs disseminate data as structured JSON objects.
JXAPI is for generating java code to call those REST APIs using a simple function with a Java POJO as request and response data.

The generator will take as input a JSON file describing APIs, and generate request/response POJOs, Java interfaces to REST APIs and Websockets and their implementation, and also demo snippets.
You will also need to write a few lines of code for API specific implementation aspects like:
 * Authentication challenge (like computing authorization header added requests using API key/secret and request parameters)
 * Request parameter formatting (use as query parameters or request body parameters)

That can be achieved easily by specifying implementation REST+Websocket endpoint factories using hooks, check wrapper development guide below.

The JSON API descriptor file structure is simple, and can be AI generated from online API documentation.
 
## Java API Wrapper using JXAPI development guide.

You need a dev environment with GIT, Maven, and a JDK >= 8 installed.
I will use here an  [Bybit](https://www.bybit.com/en/) exchange API, which [V5 API documentation is available here](https://bybit-exchange.github.io/docs/v5/guide)

Let's build a java wrapper to this API by creating a java project first.

### Project bootstrap
Go to your 'workspace' directory where your JXAPI projects will be created and clone this repo using `git clone` and run maven install in it to install it as latest artefact in your repo.

```
cd jxapi-core
mvn clean install
cd..
```


Init Git `jxapi-bybit` project from workspace folder:

```
git init jxapi-bybit
```
And cd in newly created folder:

```
cd jxapi-bybit
```
Initialize [maven project](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

```
mvn archetype:generate -DgroupId=com.scz.exchanges.bybit -DartifactId=jxapi-bybit -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```
#### Initial changes to Maven pom.xml
Maven pom.xml file needs to be customized.

1. Change java.version file to 1.8


```xml
  <properties>
...
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
```

2. Add dependency to jxapi-core project:

```
  <dependencies>
... 
    <dependency>
      <groupId>com.scz.jxapi</groupId>
      <artifactId>jxapi-core</artifactId>
      <version>0.7.5</version>
    </dependency>
  </dependencies>
```

3. Add `exec-maven-plugin` which will allow performing code generation using Maven command `mvn exec:java`

```
  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
...
        <plugin>
		  <groupId>org.codehaus.mojo</groupId>
		  <artifactId>exec-maven-plugin</artifactId>
		  <configuration>
			  <mainClass>com.scz.jxapi.generator.exchange.ExchangeGeneratorMain</mainClass>
		  </configuration>
		</plugin>
      </plugins>
    </pluginManagement>
  </build>
```


You can now run `mvn clean install` to install initial maven project. Do not forget to update `.gitignore` file to ignore java classes and files in `/target`

### Manage specific REST/Websocket API authentication
A few lines of Java code have to be written manually to manage API specific authentication challenge.

#### REST requests factory

If the API descriptor file it contains REST endpoints, an implementation of [RestEndpointFactory](src/main/java/com/scz/jxapi/netutils/rest/RestEndpointFactory.java)
 interface is required. 
Let's do this in a class named `com.scz.jxapi.exchanges.bybit.net.BybitRestEndpointFactory`:

```java
public class BybitRestEndpointFactory implements RestEndpointFactory {
	
	public static final String API_KEY_PROPERTY = "apiKey";
	public static final String API_SECRET_PROPERTY = "apiSecret";
	private Properties properties;
	
	private final HttpRequestExecutor executor = new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {		
		return new DefaultRestEndpoint<>(new BybitHttpRequestBuilder(properties.getProperty(API_KEY_PROPERTY), 
																	 properties.getProperty(API_SECRET_PROPERTY)), 
										 executor, 
										 messageDeserializer);
	}

}
```
 * API specific configuration properties will be passed in a `java.util.Properties` object, passed from `setProperties` object
 * We need a generic [HttpRequestExecutor](src/main/java/com/scz/jxapi/netutils/rest/HttpRequestExecutor.java)
 implementation to execute REST requests. The default [JavaNetHttpRequestExecutor](src/main/java/com/scz/jxapi/netutils/rest/javanet/JavaNetHttpRequestExecutor.java)
 is usually suitable.
 * Actual REST requests customization are performed in `com.scz.jxapi.exchanges.bybit.net.BybitHttpRequestBuilder`. In this class we intercept outgoing REST request calls by overriding `com.scz.jxapi.netutils.rest.DefaultHttpRequestBuilder.build()` method:
 
```java
	@Override
	public HttpRequest build(RestRequest<?> restRequest) {
		HttpRequest httpRequest = super.build(restRequest);
		if (apiKey == null) {
			throw new IllegalStateException("Missing apiKey");
		}
		String timestampStr = "" + System.currentTimeMillis();
		httpRequest.setHeader("X-BAPI-API-KEY", apiKey);
       httpRequest.setHeader("X-BAPI-SIGN", genSignature(restRequest, httpRequest, timestampStr));
		httpRequest.setHeader("X-BAPI-SIGN-TYPE", "2");
		httpRequest.setHeader("X-BAPI-TIMESTAMP", timestampStr);
		httpRequest.setHeader("X-BAPI-RECV-WINDOW", RECV_WINDOW);
		httpRequest.setHeader("Content-Type", "application/json");
		return httpRequest;
	}
```

This method transcodes generic [RestRequest](src/main/java/com/scz/jxapi/netutils/rest/RestRequest.java)
 to raw [HttpRequest](src/main/java/com/scz/jxapi/netutils/rest/HttpRequest.java)
 to be executed. The result is added specific headers here but body, request URL with query string parameters and body can be also tuned.

#### Websocket management
If the API descriptor file it contains Websocket endpoints, an implementation of [WebsocketEndpointFactory](src/main/java/com/scz/jxapi/netutils/websocket/WebsocketEndpointFactory.java)
 interface is required.
For Bybit 'Spot' related WS endpoints, let's do this in a class named `com.scz.jxapi.exchanges.bybit.net.BybitSpotWebsocketEndpointFactory`:

```java
public class BybitSpotWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {

	public static final String BASE_URL = "wss://stream.bybit.com/v5/public/spot";
	
	@Override
	public void setApi(HasProperties api) {
		this.websocketManager = new BybitWebsocketManager(BASE_URL);
	}
}
```

 * API specific configuration properties will be passed in a [HasProperties](src/main/java/com/scz/jxapi/util/HasProperties.java) object, passed from `setProperties()`. This objects not only wraps properties with API specific configuration, but can be cast to Api implementation that exposes other REST/Websocket endpoints this factory will be used to create websocket endpoints for.
This may be useful for instance if websocket handshake involves a REST call to retrieve authentication token.
 * Specific handshake, heartbeat management, subscription/unsubscription to topics, is performed in a [WebsocketManager](src/main/java/com/scz/jxapi/netutils/websocket/WebsocketManager.java)
 implementation, here is the specific one for Bybit API `BybitWebsocketManager`
 * This class extends [SpringWebsocketManager](src/main/java/com/scz/jxapi/netutils/websocket/spring/SpringWebsocketManager.java) (SpringWebsocketManager)[src/main/java/com/scz/jxapi/netutils/websocket/spring/SpringWebsocketManager.java] which manages most the actual websocket creation.
 * Overrides `doConnect()` method to perform Bybit specific API handshake. This involves sending a message on websocket and waiting for response as websocket should be connected and ready to receive subscriptions when `doConnect()` method returns. 
 * Overrides `createHeartBeatMessage()` to send Bybit specific hearbeat message
 * Overrides `getSubscribeRequestMessage(String topic)` and `getUnSubscribeRequestMessage(String topic)` to send Bybit specific subscribe/unsubscribe topic messages.

### Write JSON file for API descriptor
Now the tedious work is done, let's create some API wrappers.

Create a file named BybitCEXDescriptor.json file. This file in `src/main/resources` with a name ending with `Descriptor.json` makes it eligible for API wrapper generation using `mvn exec:java` command.

Such API description file looks as follows:

```json
{
	"name": "Bybit",
	"description": "Bybit V5 unified API",
	"basePackage": "com.scz.jxapi.exchanges.bybit.gen",
	"rateLimits": [{"id":"BybitV5GetPostSharedRateLimit", "timeFrame": 1000,  "maxRequestCount": 120}],
	"apis":[
		{ 
			"name": "V5",
			"description": "Bybit V5 unified API",
			"restEndpointFactory": "com.scz.jxapi.exchanges.bybit.net.BybitRestEndpointFactory",
			"restEndpoints": [
				// REST endpoints definition
			],
			"websocketEndpointFactory": "com.scz.jxapi.exchanges.bybit.net.BybitPrivateWebsocketEndpointFactory",
			"websocketEndpoints": [
				// Websocket endpoints definitions
			]
		}
	]
}	
```

 * Such file contains one [ExchangeDescriptor](com.scz.jxapi.generator.exchange.ExchangeDescriptor) object (top level), with `name` and `description` fields. `basePackage` property tells the generator which base package to generate classes in. This should be a dedicated package with no other code than generated one. One suggestion is to include `gen` in package name to make it clear package contains generated code.
 * `rateLimits` property contains an array of objects describing API specific limitation for RESt request rate. In the example above, no more than 120 requests per second are allowed. Generated code REST endpoints implementation will delay requests that exceed this limitation to avoid breaching API limits. This may be dangerous because if client using API example above sends 120 requests in 100ms, the next request will wait 900ms before being sent. However, this may be not be as dangerous as breaching API limits, because it may cause client ban. Notice `rateLimits` property can be set at [ExchangeDescriptor](com.scz.jxapi.generator.exchange.ExchangeDescriptor), [ExchangeApiDescriptor](com.scz.jxapi.generator.exchange.ExchangeApiDescriptor) or [RestEndpointDescriptor](com.scz.jxapi.generator.exchange.RestEndpointDescriptor) level. Resource quota of a rate limit is shared among REST endpoints in its hierarchy.
 * `apis` property of [ExchangeDescriptor](com.scz.jxapi.generator.exchange.ExchangeDescriptor) object contains an array of [ExchangeApiDescriptor](com.scz.jxapi.generator.exchange.ExchangeApiDescriptor) objects. Each of these objects describe a set of REST or Websocket API endpoints with `name` and `description` properties.
 * When any REST endpoint is defined in a [ExchangeApiDescriptor](com.scz.jxapi.generator.exchange.ExchangeApiDescriptor) object, `restEndpointFactory` property of that object must be defined with the name of an existing [RestEndpointFactory](src/main/java/com/scz/jxapi/netutils/rest/RestEndpointFactory.java) implementation class.
 * `restEndpoints` property of [ExchangeApiDescriptor](com.scz.jxapi.generator.exchange.ExchangeApiDescriptor) object contains a JSON array of available REST API endpoints.
 * When any websocket endpoint is defined in a [ExchangeApiDescriptor](com.scz.jxapi.generator.exchange.ExchangeApiDescriptor) object, `websocketEndpointFactory` property of that object must be defined with the name of an existing [WebsocketEndpointFactory](src/main/java/com/scz/jxapi/netutils/websocket/WebsocketEndpointFactory.java) implementation class. 
 * `websocketEndpoints` property of [ExchangeApiDescriptor](src/main/java/com/scz/jxapi/generator/exchange/ExchangeApiDescriptor.java) object contains a JSON array of available Websocket API endpoints.


#### REST API Endpoints

Let's have a look at an example of REST endpoint definition:

```json
                {
					"name": "placeOrder",
					"httpMethod": "POST",
					"description": "This endpoint supports to create the order for spot, spot margin, USDT perpetual, USDC perpetual, USDC futures, inverse futures and options.<br/>See <a href=\"https://bybit-exchange.github.io/docs/v5/order/create-order\">API</a>",
					"url": "https://api.bybit.com/v5/order/create",
					"rateLimits": [{"id":"BybitV5PlaceOrderRateLimit", "timeFrame": 1000,  "maxRequestCount": 10}],
					"parameters": [
						{"name":"category", "type": "STRING", "description":"Product type. Unified account: spot, linear, inverse, option. Normal account: spot, linear, inverse", "sampleValue":"spot"},
						{"name":"symbol", "type": "STRING", "description":"Symbol name", "sampleValue":"1INCHUSDT"},
						{"name":"isLeverage", "type": "INT", "description":"Whether to borrow. Valid for Unified spot only. 0(default): false then spot trading, 1: true then margin trading", "sampleValue":0},
						{"name":"side", "type": "STRING", "description":"Buy, Sell", "sampleValue":"Buy"},
						{"name":"orderType", "type": "STRING", "description":"Market, Limit", "sampleValue":"Limit"},
						// Other parameters
					],
                    "responseInterfaces": ["com.scz.jxapi.exchanges.bybit.common.BybitResponse"],
                    "response": [
                        {"name":"retCode", "type": "INT", "description":"Success/Error code", "sampleValue": 0},
                        {"name":"retMsg", "type": "STRING", "description":"Success/Error msg. OK, success, SUCCESS indicate a successful response", "sampleValue": 0},
                        {"name":"time", "type": "TIMESTAMP", "description":"Current timestamp (ms)", "sampleValue": 0},
                        {"name":"result", "type": "OBJECT", "description":"Business data result list of tickers.", "parameters":[ 		
								{"name":"orderId", "type":"STRING", "description":"Order ID", "sampleValue":"1321003749386327552"},
								{"name":"orderLinkId", "type":"STRING", "description":"User customised order ID", "sampleValue":"spot-test-postonly"}
                            ] 
                        } 
                    ]
                }
```

 * The `name` property is the generated function name.
 * `httpMethod` property is HTTP verb to use for calls to that API
 * `url` property contains full URL for this API endpoint without request or query parameters
 * `rateLimit` is optional specific rate limit for that API endpoint
 * `parameters` is the list of parameters to pass for that API call. When `POST` is used as HTTP method for that API, the parameters are expected to be passed as property of a JSON object submitted as HTTP request body. If another HTTP method (`GET`, `DELETE`) is used, the parameters are expected to be passed as query parameters ( _param1=value1&param2=value2..._ ).
 * `responseInterface` is optional String array of names of interfaces implemented by generated POJO for response parameters. That interface should be written manually. This is useful for instance when response data to any API of a given exchange uses common fields like return code and error description.
 * `response` contains the list of parameters received in response. Such parameters may be nested structure like JSON object or object array, see [EndpointParameterType](src/main/java/com/scz/jxapi/generator/exchange/EndpointParameterType.java) for the full list of possible parameter types.
  * When a response parameter is of `OBJECT` or `OBJECT_ARRAY` type it should have `parameters` property exposing parameters of sub-structure. Such parameter may also define `objectName` property with full class name of generated POJO for corresponding structure. This is useful when same given structure is used across different APIs, for instance order data returned as object in 'get single order by ID' API and 'get all orders'. When a parameters stands for such object with objectName already defined in another API, the `parameters` property can be omitted, this simplifies the JSON as object structured needs not be repeated twice in JSON descriptor file.

#### Websocket API Endpoints
TODO!

### Tip to write JSON descriptor file: AI may help you :)
The API JSON description file described above has a structure simple enough for an AI like OpenAI to write most of it.
Give an  example of API documentation page and resulting JSON description and ask it to do the same using other API endpoint documentation pages.


### Resulting generated code
When finally done writing JSON descriptor file (actually it is recommanded to generate code and run demo after writing each enpoint), run Maven `mvn exec:java` command to run generator. If JSON descriptor file is placed in `src/main/resources/` folder and has file name ending with `Descriptor.json` the generator will generate for that file:
 * Exchange interface, with getter function to retrieve any exchange API.
 * Exchange interface implementation, with constructor expecting a single `java.util.Properties` argument containing API configuration parameters like API KEY/Secret
 * Exchange API interface for each exchange API defined in root exchange, and implementation of that interface with
  * One function for each REST API. Calls are performed asynchronously and return an instance of [FutureRestResponse](src/main/java/com/scz/jxapi/netutils/rest/FutureRestResponse.java) which is a `java.util.concurrent.Future` object.
   * REST API method take a request object as parameter and a response object as returned type which are generated Java POJOs carrying properties corresponding to parameters defined in JSON file.
  * One _subscribe_ and one _unsubscribe_ method to subscribe/unsubscribe to Websocket endpoint topics. Subscription and stream message parameters and are carried in generated POJOs.

TODO

## Supported exchanges
TODO! Currently under development :)

