# API descriptor file

JXAPI generates API wrapper using interface specifications described in _Exchange descriptor_ file.
This file declares REST and Websocket API endpoints, with associated request and response or message data, in 'groups' belonging to a root 'exchange'.

This section describes this descriptor file.

# 'Demo' exchange descriptor example

```json
{
	"name": "DemoExchange",
	"description": "Demo Exchange. This exchange uses connects to mock HTTP server and websocket server. It is used to test and validate a full Java wrapper generated using JXAPI.",
	"docUrl": "https://docs.myexchange.com/api",
	"basePackage": "com.scz.jxapi.exchanges.demo.gen",
		"properties": [
		{"name": "host", "description": "Mock HTTP server host", "defaultValue": "localhost"},
		{"name": "httpPort", "type":"INT",  "description": "Mock HTTP/Websocket server port"},
		{"name": "websocketPort", "type":"INT",  "description": "Mock websocket server port"},
		{"name": "websocketHeartBeatInterval", "type":"INT",  "description": "Mock websocket server expected heartBeat interval", "defaultValue": -1}
	],
	"constants": [
		{"name": "pingMessage", "type":"STRING", "description": "Ping message", "value": "Ping!"},
		{"name": "pongMessage", "type":"STRING", "description": "Pong message", "value": "Pong!"},
		{"name": "responseCodeOk", "type":"INT",  "description": "Possible value in <i>responseCode</i> field of rest request response: Successful response", "value": 200}
	],
	"httpUrl": "https://api.myexchange.com",
	"websocketUrl": "wss://api.myexchange.com/ws",
	"httpRequestInterceptorFactory": "com.scz.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory",
	"websocketHookFactory": "com.scz.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory",
	"apis":[
		{ 
			"name": "MarketData",
			"description": "Demo exchange market data API",
			"constants": [
				{"name": "allTickers", "type":"STRING", "description": "Constant for subscribing to all tickers", "value": "ticker@all"}
			],
			"httpUrl": "/marketData",
			"restEndpoints": [
				{
					"name": "exchangeInfo",
					"httpMethod": "GET",
					"description": "Fetch market information of symbols that can be traded",
					"docUrl": "https://docs.myexchange.com/api/rest/marketData/exchangeInfo",
					"url": "/exchangeInfo",
					"request":{
						"properties": [
							{"name":"symbols", "type": "STRING_LIST", "description":"The list of symbol to fetch market information for. Leave empty to fetch all markets", "sampleValue":"[\"BTC\", \"ETH\"]"}
						]
					},
					"response":{ 
						"properties": [
							{"name":"responseCode", "type": "INT", "description":"Request response code", "sampleValue":"0"},
							{"name":"payload", "type": "OBJECT_LIST", "description":"List of market information for each requested symbol", "properties":[
									{"name":"symbol", "type": "STRING", "description":"Market symbol", "sampleValue":"BTC_USDT"},
									{"name":"minOrderSize", "type": "BIGDECIMAL", "description":"Minimum order amount", "sampleValue":"0.0001"},
									{"name":"orderTickSize", "type": "BIGDECIMAL", "description":"Price precision. Prce of an order should be a multiple of this value", "sampleValue":0.05}
								]
							}
						]
					}
				}
			],
			"websocketEndpoints": [
				{
					"name": "tickerStream",
					"docUrl": "https://docs.myexchange.com/api/ws/marketData/tickerStream",
					"topic": "${symbol}@ticker",
					"description": "Subscribe to ticker stream",
					"request": {
						"properties": [
							{"name": "symbol", "type":"STRING", "description":"Symbol to subscribe to ticker stream of", "sampleValue":"BTC_USDT"}
						]
					},
					"messageTopicMatcherFields": [
						{"name": "topic",  "value": "ticker"},
						{"name": "symbol",  "value": "${symbol}"}
					],
					"message": { 
						"properties": [
							{"name":"topic", "msgField":"t", "type": "STRING", "description":"Topic", "sampleValue":"ticker"},
							{"name":"symbol", "msgField":"s", "type": "STRING", "description":"Symbol name", "sampleValue":"BTC_USDT"},
							{"name":"last", "msgField":"p", "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":"16000.00"},
							{"name":"high", "msgField":"h",  "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":10.0},
							{"name":"low", "msgField":"l",  "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":10.0},
							{"name":"volume", "msgField":"v",  "type": "BIGDECIMAL", "description":"Total traded amount in base asset during the last 24h from now", "sampleValue":10.0},
							{"name":"time", "msgField":"d",  "type": "TIMESTAMP", "description":"Current time", "sampleValue":"now()"}
						]
					}
				}
			]
		}
	]
}

```

## Exchange
The JSON descriptor data is a JSON object referred to as 'exchange'. An exchange is the root of API wrapper, mapped to [ExchangeDescriptor](../../src/main/java/com/scz/jxapi/exchange/descriptor/ExchangeDescriptor.java). It is composed of the following properties.
 * `name`: The name of this wrapper or API.
 * `description`: A description of this exchange.
 * `docUrl`: Link to exchange website API documentation home page
 * `basePackage`: The base Java package name for the generated code. It is recommended this package name contains `.gen.` as a convention for packages containing generated code that should not be edited manually.
 * `properties`: A list of properties required to configure the exchange. A property object is mapped to [ConfigProperty](../../src/main/java/com/scz/jxapi/exchange/descriptor/ConfigProperty.java)
 * `constants`: A list of constant values used in the exchange. A constant object is mapped to [Constant](../../src/main/java/com/scz/jxapi/exchange/descriptor/Constant.java)
 * `httpUrl`: The base URL for HTTP API endpoints.
 * `websocketUrl`: The base URL for WebSocket API endpoints.
 * `httpRequestInterceptorFactory`: The factory class for creating HTTP request interceptors, see [HttpRequestInterceptorFactory](../../src/main/java/com/scz/jxapi/netutils/rest/HttpRequestInterceptorFactory.java). If for exchange requires specific headers to be  set for instance for authentication, a specific interceptor has to be provided through this property, see [HTTP request interceptor dev guide](./HttpRequestInterceptorDevGuide.md).
 * `httpRequestExecutorFactory`:  The factory for HTTP request executor, see [HttpRequestExecutorFactory](../../src/main/java/com/scz/jxapi/netutils/rest/HttpRequestExecutorFactory.java). Default implementation is usually sufficient.
 * `websocketHookFactory`: The factory class for creating WebSocket hooks, see [WebsocketFactory](../../src/main/java/com/scz/jxapi/netutils/websocket/WebsocketHookFactory.java). Using a custom factory for hooks that implements exchange protocol specific handshake, heartbeats is generally required, see [Websocket hook dev guide](./WebsocketHookDevGuide.md)..
 * `websocketFactory`: The factory class for creating base websocket, see [WebsocketFactory](../../src/main/java/com/scz/jxapi/netutils/websocket/WebsocketFactory.java). The default implementation is usually sufficient.
 * `apis`: A list of API groups, each containing HTTP and WebSocket endpoints, see [below](#api-groups).

## API groups

 The exchange REST/Websocket endpoints are sorted in groups, this is useful to regroup API endpoints by functional affinity and not list every endpoint in a single interface. The API group object is mapped to [ExchangeApiDescriptor](../../src/main/java/com/scz/jxapi/exchange/descriptor/ExchangeApiDescriptor.java). It is composed of the following properties:
 * `name`: API group name
 * `description`: A description of the API group.
 * `constants`: A list of constant values specific to this API group.
 * `httpUrl`: The base URL for HTTP API endpoints. It can be a relative path, in which case the actual base url is result of concantenation of base `httpUrl` in parent (exchange) property and value of this property if it is set.
 * `websocketUrl`: The base URL for WebSocket API endpoints. It can be a relative path, in which case the actual base url is result of concantenation of base `websocketUrl` in parent (exchange) property and value of this property if it is set.
 * `httpRequestInterceptorFactory`: The factory class for creating HTTP request interceptors. If not defined, the value of `httpRequestInterceptorFactory` in parent (exchange) property is used.
 * `websocketHookFactory`: The factory class for creating WebSocket hooks. If not defined, the value of `websocketHookFactory` in parent (exchange) property is used.
 * `rateLimits`: List of [rate limits](#api-request-rate-limit) request rate limitation to enforce in addition to rules defined in parent exchange.
 * `restEndpoints`: A list of REST API endpoints in this group, see [below](#rest-api-endpoints).
 * `websocketEndpoints`: A list of WebSocket API endpoints in this group, see [below](#websocket-api-endpoints).

## REST API endpoints
A REST endpoint is an API interface which consists in sending a HTTP request with expected URL, verb... and request parameters which can be specifed as URL query parameters or as request body encoded in JSON format.
Such REST endpoint JSON object in descriptor file is mapped to [RestEndpointDescriptor](../../src/main/java/com/scz/jxapi/exchange/descriptor/RestEndpointDescriptor.java).

GET, DELETE HTTP methods do not expect a body in request, so parameters will be encoded to URL query params by default.
For other methods, request parameters are serialized as JSON in request body. It is possible to override default behavior by setting `queryParams` property: When set to _true_, the HTTP request will carry request data serialized as URL query params.

It is also possible to customize query parameters set in request using `urlParameters` property, which value is a template of URL suffix (after base URL + '?') that can contain placeholders like `${request}` for the whole request or `${myField}` to substitute with first value found for property named _myField_ in request properties.

The structure of request and response data are specified using the same JSON object as value of `request` and `response` properties, see [Flexible data structure](#flexible-data-structure-definition)

Each REST endpoint can be associated specific [rate limits](#api-request-rate-limit) that add up to API group specific and exchange specific ones. When using 'weighted' rate limit algorithm, the weight for one call to this REST API is specified in `requestWeight` properties.

Each REST API endpoint is described by the following properties:
 * `name`: The name of the endpoint.
 * `httpMethod`: The HTTP method used (e.g., GET, POST).
 * `description`: A description of the endpoint.
 * `docUrl`: Link to the endpoint's documentation.
 * `url`: The URL path for the endpoint. Can be a relative path, in which case actual URL used is concatenation of of base `httpUrl` in parent (api group) property and value of this property if it is set.
 * `request`: The request data type definition as [Flexible data structure](#flexible-data-structure-definition)
 * `response`: The response object, containing properties for the response data.
 * `urlParameters`: The request url parameters template. Can contain place holders like `${myArg}`
 * `urlParametersListSeparator`: The separator used between items of a list in serialized request url parameters
 * `isQueryParams`: Boolean property indicating wether request parameters must be set as URL query parameters or as JSON body of the request. Set to _true_ by default for GET, DELETE HTTP requests.
 * `rateLimits`: List of [rate limits](#api-request-rate-limit) request rate limitation to enforce in addition to rules defined in parent API group and exchange.
 * `rateLimitWeight`: When using 'weighted' rate limit algorithm, the weight for one call to this REST API.


## WebSocket API endpoints
A WebSocket endpoint in the exchange descriptor file defines how to subscribe to a specific topic and how to match incoming messages against this topic. The topic can include placeholders that are replaced with actual values from the request. Additionally, a list of field names and expected values can be defined to match a message against the topic.
For each API group that exposes websocket endpoints, a physical websocket is opened upon first subscription to a topic.
If more than one websocket endpoint is exposed in the API group, they are expected to share the same physical connection, which is known as 'multiplexing'.
Multiplexing means exchange protocol allows subscribing to distinct message streams by sending subscription or unsuscription messages on socket output stream, which causes server to disseminate or stop disseminating messages for the associated topic.

Notice exchange protocol may define no such multiplexing feature: In this case, upon opening connection and eventually issue a 'login' message the server will start disseminate message for the single websocket endpoint of the API group.  

Each WebSocket API endpoint is described by the following properties:
 * `name`: The name of the endpoint.
 * `docUrl`: Link to the endpoint's documentation.
 * `topic`: The topic to subscribe to. Should uniquely identify a websocket stream among every endpoints of this API group.
 * `description`: A description of the endpoint.
 * `request`: The request object, containing properties for the request parameters, which will be used to build the subscription message to a topic.
 * `messageTopicMatcherFields`: Fields used to match the message topic.
 * `message`: The message object, containing properties for the message data.
 
Serializing a request to a subscription message, building the unsubscription message to cancel an existing subscription, sending or listening to 'heartbeat' messages is customized using a [WebsocketHook](../../src/main/java/com/scz/jxapi/netutils/websocket/WebsocketHook.java).
See [Websocket Hook dev guide](./WebsocketHookDevGuide.md)

### Topic Placeholders

The `topic` property in a WebSocket endpoint can contain placeholders in the format `${placeholderName}`. These placeholders are replaced with the corresponding values from the request properties when subscribing to the topic.

Example:
```json
{
	"name": "tickerStream",
	"topic": "${symbol}@ticker",
	"request": {
		"properties": [
			{"name": "symbol", "type": "STRING", "description": "Symbol to subscribe to ticker stream of", "sampleValue": "BTC_USDT"}
		]
	}
}
```
In this example, the placeholder `${symbol}` in the topic will be replaced with the value of the `symbol` property from the request.

### Message Topic Matcher Fields

The `messageTopicMatcherFields` property is a list of fields used to match the message topic. Each field specifies a name and a value. The value can also contain placeholders that are replaced with actual values from the request.

Example:
```json
{
	"messageTopicMatcherFields": [
		{"name": "topic", "value": "ticker"},
		{"name": "symbol", "value": "${symbol}"}
	]
}
```
In this example, the incoming message `topic` field must have the value `ticker`, and the `symbol` field must match the value of the `symbol` property from the request.

### Message Matching

The `message` property defines the structure of the message and the fields that are expected in the message see [Flexible data structure](#flexible-data-structure-definition). Each field specifies a name, a type, and a description.

Example:
```json
{
	"message": {
		"properties": [
			{"name": "topic", "msgField": "t", "type": "STRING", "description": "Topic", "sampleValue": "ticker"},
			{"name": "symbol", "msgField": "s", "type": "STRING", "description": "Symbol name", "sampleValue": "BTC_USDT"},
			{"name": "last", "msgField": "p", "type": "BIGDECIMAL", "description": "Last traded price", "sampleValue": "16000.00"}
		]
	}
}
```
In this example, the message is expected to contain fields `t` (topic), `s` (symbol), and `p` (last traded price). The values of these fields are used to match the message against the topic and request properties.

By defining the topic with placeholders and specifying the message topic matcher fields, the WebSocket endpoint can accurately subscribe to the desired topic and match incoming messages based on the specified criteria.

## Flexible data structure definition
Both REST endpoints request and response, Websocket endpoints subscription request and response message are defined in exchange descriptor file using the same object structure, mapped to [Field](../../src/main/java/com/scz/jxapi/exchange/descriptor/Field.java) class. This class is used to define the structure of request and response data for both REST and WebSocket endpoints. It allows for a flexible type definition that can represent primitive types, lists, maps, or composite objects.

### Primitive Types
A field can be a primitive type such as `STRING`, `INT`, `BIGDECIMAL`, or `TIMESTAMP`. These types are directly mapped to their respective Java types.

### Lists
A field can be defined as a list by specifying the type as `LIST` and providing the `elementType`. This allows for the representation of arrays or collections of a specific type.

### Maps
A field can be defined as a String key map by specifying the type as `MAP` and providing type of value as type prefix, for instance `INT_MAP` is a map with string keys and `INT` values. This allows for the representation of key-value pairs where both keys and values can be of specified types.

### Objects
A field can be a object by specifying the type as `OBJECT` and providing a list of `properties`. Each property is itself a `Field` object, allowing for nested structures and complex data representations.
Remark: `OBJECT` is the default type for a `Field`.
Example:
```json
{
	"name": "exampleField",
	"type": "OBJECT",
	"properties": [
		{"name": "id", "type": "STRING", "description": "Unique identifier"},
		{"name": "values", "type": "LIST", "elementType": "INT", "description": "List of integer values"},
		{"name": "attributes", "type": "MAP", "keyType": "STRING", "valueType": "STRING", "description": "Map of attributes"}
	]
}
```

### Composite Types
The type definition of a field can be a composite of '`OBJECT` or primitive `STRING`, `INT`... and `LIST` or `MAP`:
for instance:

#### OBJECT_LIST_MAP
A field of type `OBJECT_LIST_MAP` represents a map where the keys are strings and the values are lists of objects. Each object in the list can have its own properties.

Field declaration example:
```json
{
	"name": "exampleObjectListMap",
	"type": "OBJECT_LIST_MAP",
	"description": "Map with string keys and lists of objects as values",
	"properties": [
		{"name": "name", "type":"STRING", "description": "Person name"},
		{"name": "age", "type":"INT", "description": "Person age"}
	]
}
```

Example of associated JSON data structure:
```json
{
	"A": [
		{"name": "Alex", "age": 25},
		{"name": "Aline", "age": 19}
	]
	"B": [
		{"name": "Bob", "age": 12},
		{"name": "Billy", "age": 35}
	]
}
```

#### LONG_LIST_LIST
A field of type `LONG_LIST_LIST` represents a list of lists, where each inner list contains long integer values.

Field declaration example:
```json
{
	"name": "exampleLongListList",
	"type": "LONG_LIST_LIST",
	"description": "List of lists containing long values"
}
```

Example of associated JSON data structure:
```json
[
	[123456789012345, 987654321098765],
	[111111111111111, 222222222222222]
]
```


This flexibility allows the `Field` class to accurately describe the structure of complex data used in API requests and responses/messages.

### Object name
The `objectName` property of `Field` can be used when field type is object, to assign the generated pojo a given name. When not specified, the generated POJO class name for an object field will be named ExchangeNameApiGroupNameEndpointNameRequest ('Request' suffix for a request, 'Response' for a REST endpoint response, 'Message' for a websocket endpoint message).
In addition to provide simpler POJO names, this allows reusing the same object when different APIs in same API group scope share the same object structure. This allows wrapper clients to apply common processing for these objects and the properties of a Field with a given `objectName` value needs to be defined only once in exchange descriptor.

### Sample value
`sampleValue` property of `Field` can be set with a sample value to use in demo snippets.

## API request rate limit

REST/HTTP APIs usually come whith request rate limits to prevent abusive use of APIs. Such limitations are expressed in number of requests over a given timeframe. Alternatively, a request could have a determined 'weight' that could vary between APIs: For example, a 'light' request would cost 1 and a 'heavy' request 100 from a quota of 1000 per minute.
The generated wrappers simplifies such request limit rate enforcement by defining rules either at 'exchange' level (example: limitation of 100 requests per minute across all APIs of all groups), or at 'api group' level (example: limitation of 50 requests per second across all APIs of this group), or at REST endpoint level for rules specific for that endpoint.

Rate limits are defined either at exchange, api group or REST endpoint level as a `rateLimits` property that carries as value a list of [RateLimitRule](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits/RateLimitRule.java) objects defined by following properties:
 * `id`: Unique identifier
 * `timeframe`: Time frame in ms for which request count or cumulated weight should not exceed limit.
 * `maxRequestCount`: The maximum number of requests that can be attempted within rolling time frame. A negative value means it should not be taken into account and this rate limit is expressed in cumulated weight.
 * `maxTotalWeight`: The maximum cumulated weight of calls within time frame limitation.


When a request is submitted to an endpoint, each rate limit rule is checked, taking into account the request (incrementing current request count) if it does not exceed any rule, or providing the remaining delay before it can be executed if it should not be submitted immediately.
Accordingly the request is either submitted immediately or delayed for the minimum time to wait for and then submitted again. See 
This when `THROTTLE` policy is applied, which is the default. Beware that client applications breaking rate limits may experience increasing delay in request execution and accumulation of requests in queue may cause a memory leak. For this reason it is advised to set a maximum wait delay on request throttling using `Exchange#setMaxRequestThrottleDelay(long)`method.
See [RequestThrottler](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits/RequestThrottler.java).

Different policies may be specified using `Exchange#setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode)`, see [RequestThrottlingMode](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits//RequestThrottlingMode.java):
 * `BLOCK`: Instead of throttling request, immediately anwser that request with a response carrying a [RateLimitReachedException](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits/RateLimitReachedException.java) exception stating how much time should be awaited for before retrying this request, and HTTP response code 429 (_TOO_MANY_REQUESTS_). This is similar to `THROTTLE` mode with `maxRequestThrottleDelay` set to 0.
 * `NONE`: Ignores rate limits check and always submit requests immediately. 
 







