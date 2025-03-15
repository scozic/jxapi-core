# API descriptor file

JXAPI generates API wrapper using interface specifications described in _Exchange descriptor_ file.
This file declares REST and Websocket API endpoints, with associated request and response or message data, in 'groups' belonging to a root 'exchange'.

This section describes such descriptor file.

# 'Demo' exchange descriptor example

The following is content of a complete descriptor file [employeeExchange.yaml](../../src/test/resources/employeeExchange.yaml).
Descriptor file can also be specified in JSON format, see [employeeExchange.json](../../src/test/resources/employeeExchange.json).

```yaml
name: "Employee"
description: > 
 Employee exchange is a demo exchange REST APIs to get, add, delete and 
 update employees and a websocket endpoint to get notified of updates from an employee database.<br>
 A server can be started using <code>org.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br>
 The URL of the server must be set using the baseUrl property.<br>
 Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.
docUrl: "https://www.example.com/docs/employee"
properties:
 - name: "baseHttpUrl"
   description: "Base URL for REST endpoints the Employee Exchange API"
 - name: "baseWebsocketUrl"
   description: "Base URL for websocket endpoints of the Employee Exchange API"   
constants:
 - name: "baseUrlPattern"
   description: "Value to replace in HTTP or Websocket base URL with value of <i>baseHttpUrl</i> or <i>baseWebsocketUrl</i> properties"
   value: "BASEURL"
basePackage: "org.jxapi.exchanges.employee.gen"
httpUrl: "BASEURL"
httpRequestInterceptorFactory: org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory
apis:
 - name: V1
   description: "Version 1 of the Employee API"
   httpUrl: "v1"
   constants:
    - name: "profileRegular"
      description: "Regular employee profile"
      value: "REGULAR"
    - name: "profileAdmin"
      description: "Admin employee profile"
      value: "ADMIN"
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
       type: INT
       description: "Employee ID"
       sampleValue: 1
      response:
       objectName: "Employee"
       type: OBJECT
       properties:
        - name: "id"
          type: INT
          description: "Employee ID"
          sampleValue: 1
        - name: "firstName"
          type: STRING
          description: "Employee First Name"
          sampleValue: "John"
        - name: "lastName"
          type: STRING
          description: "Employee last lame"
          sampleValue: "Doe"
        - name: "profile"
          type: STRING
          description: "Employee profile. Can be 'regular' or 'admin'"
          sampleValue: "REGULAR"
    - name: "getAllEmployees"
      description: "Get all employees"
      httpMethod: "GET"
      docUrl: "https://www.example.com/docs/employee/getAll"
      url: "/employees"
      response:
       objectName: "Employee"
       type: OBJECT_LIST          
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
       description: "Employee to update"
       objectName: "Employee"       
    - name: "deleteEmployee"
      description: "Delete an employee"
      httpMethod: "DELETE"
      docUrl: "https://www.example.com/docs/employee/delete"
      url: "/employee"
      urlParameters: "/${id}"
      request:
       name: "id"
       type: INT
       description: "Employee ID"
       sampleValue: 1
   websocketUrl: "BASEURL"
   websocketHookFactory: org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory  
   websocketEndpoints:
    - name: "employeeUpdates"
      description: "Employee updates websocket"
      docUrl: "https://www.example.com/docs/employee/updates"
      message:
       description: "Employee update message"
       properties:
        - name: "eventType"
          type: STRING
          description: "Type of event. Can be 'ADD', 'UPDATE' or 'DELETE'"
          sampleValue: 1
        - name: "employee"
          objectName: "Employee"
          description: "Employee that was updated"
          sampleValue: "John"

```

Such exchange descriptor can also be written across multiple files with same exchange name: When running generator plugin in wrapper project module, it will scan `src/main/resources/jxapi` folder and aggregate all exchanges (multiple exchange can be specified) found in every .yaml and .json file.

For complex API specifications with many endpoints it is preferable to do so. Have a look at [employeeExchange](../../src/test/resources/employeeExchange/) folder to see the same exchange as above defined in multiple files.

## Exchange
The root of JSON descriptor data is a JSON object referred to as 'exchange'. An exchange is the root of API wrapper, mapped to [ExchangeDescriptor](../../src/main/java/com/scz/jxapi/exchange/descriptor/ExchangeDescriptor.java). It is composed of the following properties:
 * `name`: The name of this wrapper or API ( here `Employee`)
 * `description`: A description of this exchange.
 * `docUrl`: Link to exchange website API documentation home page
 * `basePackage`: The base Java package name for the generated code. It is recommended this package name contains `.gen.` as a convention for packages containing generated code that should not be edited manually.
 * `properties`: A list of properties required to configure the exchange. A property object is mapped to [ConfigProperty](../../src/main/java/com/scz/jxapi/exchange/descriptor/ConfigProperty.java)
 * `constants`: A list of constant values used in the exchange. A constant object is mapped to [Constant](../../src/main/java/com/scz/jxapi/exchange/descriptor/Constant.java)
 * `httpUrl`: The base URL for HTTP API endpoints. In sample above, it is set to a placeholder `BASEURL` that is meant to be replaced by base server URL set in `baseHttpUrl` config property. Optional property, when not set, either the REST endpoints `url` property define a full URL value, or the API group `httpUrl` is set.
 * `websocketUrl`: The base URL for WebSocket API endpoints. Optional when 
 * `httpRequestInterceptorFactory`: The factory class for creating HTTP request interceptors, see [HttpRequestInterceptorFactory](../../src/main/java/com/scz/jxapi/netutils/rest/HttpRequestInterceptorFactory.java). If for exchange requires specific headers to be  set for instance for authentication, a specific interceptor has to be provided through this property, see [HTTP request interceptor dev guide](./HttpRequestInterceptorDevGuide.md).
 * `httpRequestExecutorFactory`:  The factory for HTTP request executor, see [HttpRequestExecutorFactory](../../src/main/java/com/scz/jxapi/netutils/rest/HttpRequestExecutorFactory.java). Default implementation used when this property is not set is usually sufficient.
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
```yaml
name: "tickerStream"
topic: "${symbol}@ticker"
request:
  properties:
    - name: "symbol"
      type: "STRING"
      description: "Symbol to subscribe to ticker stream of"
      sampleValue: "BTC_USDT"
```

In this example, the placeholder `${symbol}` in the topic will be replaced with the value of the `symbol` property from the request.

### Message Topic Matcher Fields

The `messageTopicMatcherFields` property is a list of fields used to match the message topic. Each field specifies a name and a value. The value can also contain placeholders that are replaced with actual values from the request.

Example:
```yaml
messageTopicMatcherFields:
  - name: "topic"
    value: "ticker"
  - name: "symbol"
    value: "${symbol}"
```
In this example, the incoming message `topic` field must have the value `ticker`, and the `symbol` field must match the value of the `symbol` property from the request.

### Message Matching

The `message` property defines the structure of the message and the fields that are expected in the message see [Flexible data structure](#flexible-data-structure-definition). Each field specifies a name, a type, and a description.

Example:
```yaml
message:
  properties:
    - name: "topic"
      msgField: "t"
      type: "STRING"
      description: "Topic"
      sampleValue: "ticker"
    - name: "symbol"
      msgField: "s"
      type: "STRING"
      description: "Symbol name"
      sampleValue: "BTC_USDT"
    - name: "last"
      msgField: "p"
      type: "BIGDECIMAL"
      description: "Last traded price"
      sampleValue: "16000.00"
```
In this example, the message is expected to contain fields `t` (topic), `s` (symbol), and `p` (last traded price). The values of these fields are used to match the message against the topic and request properties.

By defining the topic with placeholders and specifying the message topic matcher fields, the WebSocket endpoint can accurately subscribe to the desired topic and match incoming messages based on the specified criteria.

## Flexible data structure definition
Both REST endpoints request and response, Websocket endpoints subscription request and response message are defined in exchange descriptor file using the same object structure, mapped to [Field](../../src/main/java/com/scz/jxapi/exchange/descriptor/Field.java) class. This class is used to define the structure of request and response data for both REST and WebSocket endpoints. It allows for a flexible type definition that can represent primitive types, lists, maps, or composite objects.

### Primitive Types
A field can be a primitive type such as `STRING`, `INT`, `BIGDECIMAL`, or `TIMESTAMP`. These types are directly mapped to their respective Java types.
Remark: When neither of 'object' type specific `properties` or `objectName` is specified, the default field type is `STRING` unless `type` property is specified.

### Lists
A field can be defined as a list by specifying the type as `<SUBTYPE>_LIST` and providing the list element type as type prefix, for instance `BIGDECIMAL_MAP`. This allows for the representation of arrays or collections of a specific type.

### Maps
A field can be defined as a String key map by specifying the type as `<SUBTYPE>_MAP` and providing type of value as type prefix, for instance `INT_MAP` is a map with string keys and `INT` values. This allows for the representation of key-value pairs where both keys and values can be of specified types.

### Objects
A field can be a object by specifying the type as `OBJECT` and providing a list of `properties`. Each property is itself a `Field` object, allowing for nested structures and complex data representations.
Remark: `OBJECT` is the default type for a `Field` when either `objectName` or `properties` property is defined.
Example:
```json
name: "exampleField"
# Remark 'type' needs not be specified because 'properties' is
# which implies type is 'OBJECT'
properties:
  - name: "id"
    type: "STRING"
    description: "Unique identifier"
  - name: "values"
    type: "LIST"
    elementType: "INT"
    description: "List of integer values"
  - name: "attributes"
    type: "MAP"
    keyType: "STRING"
    valueType: "STRING"
    description: "Map of attributes"
```

### Composite Types
The type definition of a field can be a composite of '`OBJECT` or primitive `STRING`, `INT`... and `LIST` or `MAP`:
for instance:

#### OBJECT_LIST_MAP
A field of type `OBJECT_LIST_MAP` represents a map where the keys are strings and the values are lists of objects. Each object in the list can have its own properties.

Field declaration example:

```yaml
name: "exampleObjectListMap"
type: "OBJECT_LIST_MAP"
description: "Map with string keys and lists of objects as values"
properties:
  - name: "name"
    type: "STRING"
    description: "Person name"
  - name: "age"
    type: "INT"
    description: "Person age"
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
```yaml
name: "exampleLongListList"
type: "LONG_LIST_LIST"
description: "List of lists containing long values"
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

REST/HTTP APIs often have request rate limits to prevent abuse. These limits can be defined in two ways:
1. **Number of Requests**: The maximum number of requests allowed within a specific timeframe.
2. **Request Weight**: Each request has a weight, and the total weight of requests must not exceed a certain limit within a timeframe.

For example:
- A 'light' request might have a weight of 1.
- A 'heavy' request might have a weight of 100.
- The total quota might be 1000 per minute.

### Rate Limit Levels
Rate limits can be enforced at different levels:
- **Exchange Level**: Applies to all APIs of the exchange (e.g., 100 requests per minute).
- **API Group Level**: Applies to all APIs within a specific group (e.g., 50 requests per second).
- **REST Endpoint Level**: Specific to an endpoint (e.g., 10 requests per second).

### Example

```yaml
name: "myExchange"
rateLimits:
  - id: "globalLimit"
    timeframe: 60000
    maxRequestCount: 100
apis:
  - name: "V1"
    rateLimits:
      - id: "apiGroupLimit"
        timeframe: 10000
        maxRequestCount: 50
    restEndpoints:
      - name: "myRestApi"
        rateLimits:
          - id: "myRestApiRule"
            timeframe: 1000
            maxRequestCount: 10

```

In example above, request submitted to `myRestApi` REST API of `V1` API group of `myExchange` exchange must enforce 3 rules:
 * `globalLimit` defined at exchange level: No more that 100 requests can be submitted per minute among all REST APIs of all API groups.
 * `apiGroupLimit` defined at API group level: No more than 50 request must be output among all requests sent to any REST API of `V1` group.
 * `myRestApiRule` defined at REST endpoint level: No more that 10 requests per second should be sumitted to `myRestApi` REST API.

Rate limits are defined either at exchange, api group or REST endpoint level as a `rateLimits` property that carries as value a list of [RateLimitRule](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits/RateLimitRule.java) objects defined by following properties:
 * `id`: Unique identifier
 * `timeframe`: Time frame in ms for which request count or cumulated weight should not exceed limit.
 * `maxRequestCount`: The maximum number of requests that can be attempted within rolling time frame. A negative value means it should not be taken into account and this rate limit is expressed in cumulated weight.
 * `maxTotalWeight`: The maximum cumulated weight of calls within time frame limitation.


When a request is submitted to an endpoint, each rate limit rule is checked, taking into account the request (incrementing current request count if it does not exceed any rule), or providing the remaining delay before it can be executed if it should not be submitted immediately.
Accordingly the request is either submitted immediately or delayed for the minimum time to wait for and then submitted again. See 
This when `THROTTLE` policy is applied, which is the default. Beware that client applications breaking rate limits may experience increasing delay in request execution and accumulation of requests in queue may cause a memory leak. For this reason it is advised to set a maximum wait delay on request throttling using `Exchange#setMaxRequestThrottleDelay(long)`method.
See [RequestThrottler](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits/RequestThrottler.java).

Different policies may be specified using `Exchange#setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode)`, see [RequestThrottlingMode](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits//RequestThrottlingMode.java):
 * `BLOCK`: Instead of throttling request, immediately anwser that request with a response carrying a [RateLimitReachedException](../../src/main/java/com/scz/jxapi/netutils/rest/ratelimits/RateLimitReachedException.java) exception stating how much time should be awaited for before retrying this request, and HTTP response code 429 (_TOO_MANY_REQUESTS_). This is similar to `THROTTLE` mode with `maxRequestThrottleDelay` set to 0.
 * `NONE`: Ignores rate limits check and always submit requests immediately. 
 







