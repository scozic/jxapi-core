# API descriptor file

JXAPI generates API wrapper using interface specifications described in _Exchange descriptor_ file.
This file declares REST and Websocket API endpoints, with associated request and response or message data, in 'groups' belonging to a root 'exchange'.

This section describes such descriptor file.

<!-- BEGIN TABLE OF CONTENTS -->
  - [API descriptor file](#api-descriptor-file)
  - ['Employee' exchange descriptor example](#employee-exchange-descriptor-example)
    - [Exchange](#exchange)
    - [Configuration properties and constants.](#configuration-properties-and-constants)
      - [Constants](#constants)
      - [Configuration properties](#configuration-properties)
        - [Demo configuration properties](#demo-configuration-properties)
      - [Network](#network)
        - [HTTP client](#http-client)
    - [API groups](#api-groups)
    - [REST API endpoints](#rest-api-endpoints)
    - [WebSocket API endpoints](#websocket-api-endpoints)
      - [Topic Placeholders](#topic-placeholders)
      - [Message topic matcher](#message-topic-matcher)
        - [Message matching using a single field](#message-matching-using-a-single-field)
        - [Matching messages using multiple fields](#matching-messages-using-multiple-fields)
        - [TopicMatcher Placeholders](#topicmatcher-placeholders)
      - [Message Matching](#message-matching)
    - [Flexible data structure definition](#flexible-data-structure-definition)
      - [Primitive Types](#primitive-types)
      - [Lists](#lists)
      - [Maps](#maps)
      - [Objects](#objects)
      - [Composite Types](#composite-types)
        - [OBJECT_LIST_MAP](#objectlistmap)
        - [LONG_LIST_LIST](#longlistlist)
      - [Object name](#object-name)
      - [Sample value](#sample-value)
      - [Default value](#default-value)
      - ['in' (QUERY or PATH)](#in-query-or-path)
    - [API request rate limit](#api-request-rate-limit)
      - [Rate Limit Levels](#rate-limit-levels)
      - [Example](#example)
    - [Placeholders](#placeholders)
    - [Splitting large exchange definitions into multiple files](#splitting-large-exchange-definitions-into-multiple-files)

<!-- END TABLE OF CONTENTS -->

# 'Employee' exchange descriptor example

The following is content of a complete descriptor file [employeeExchange.yaml](../../src/test/resources/employeeExchange.yaml).
Descriptor file can also be specified in JSON format, see [employeeExchange.json](../../src/test/resources/employeeExchange.json).

```yaml
id: Employee
version: 1.0.0
description: > 
  Employee exchange is a demo exchange REST APIs to get, add, delete and
  update employees and a websocket endpoint to get notified of updates from an employee database.<br>
  A server can be started using <code>org.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br>
  The URL of the HTTP server and Websocket server must be set using the ${config.baseHttpUrl} and ${config.baseWebsocketUrl} properties.<br>
  Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.
docUrl: https://www.example.com/docs/employee
properties:
 - name: server
   description: Server related properties
   properties:
    - name: "baseHttpUrl"
      description: "Base URL for REST endpoints the Employee Exchange API"
    - name: "baseWebsocketUrl"
      description: "Base URL for websocket endpoints of the Employee Exchange API"
basePackage: org.jxapi.exchanges.employee.gen
httpUrl: ${config.server.baseHttpUrl}
constants:
  - name: defaultPageSize
    type: INT
    description: Default page size for paginated requests
    value: 10
  - name: maxPageSize
    type: INT
    description: Maximum page size for paginated requests
    value: 10000
  - name: "profile"
    description: "Employee profile types"
    constants:
      - name: regular
        description: "Regular employee profile"
        value: REGULAR
      - name: admin
        description: "Admin employee profile"
        value: ADMIN
  - name: "updateEmployeeType"
    description: "Value of eventType field in WS message"
    constants:        
      - name: add
        description: Value of eventType field in WS message for new employee added event
        value: ADD
      - name: update
        description: Value of eventType field in WS message for update of an existing employee event
        value: UPDATE
      - name: delete
        description: Value of eventType field in WS message for update of an existing employee event
        value: DELETE
network:
 httpClients:
  - name: httpDefault
    httpRequestInterceptorFactory: org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory
 websocketClients:
  - name: wsDefault
    websocketUrl: ${config.server.baseWebsocketUrl}
    websocketHookFactory: org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory        
apis:
 - name: v1
   description: "Version 1 of the Employee API"
   httpUrl: v1
   defaultHttpClient: httpDefault
   defaultWebsocketClient: wsDefault
   restEndpoints:
    - name: getEmployee
      description: Get employee details by ID
      httpMethod: GET
      docUrl: https://www.example.com/docs/employee/get
      url: /employee
      request:
        name: id
        type: INT
        description: Employee ID
        sampleValue: 1
        in: PATH
      response:
       objectName: Employee
       description: Employee details response for requested employee ID
       objectDescription: Employee details
       type: OBJECT
       properties:
        - name: id
          type: INT
          description: Employee ID
          sampleValue: 1
        - name: firstName
          description: "Employee first name"
          sampleValue: John
        - name: lastName
          description: Employee last name
          sampleValue: Doe
        - name: profile
          description: Employee profile. See ${constants.profile}
          sampleValue: ${constants.profile.regular}
    - name: getAllEmployees
      description: Get all employees
      httpMethod: GET
      docUrl: https://www.example.com/docs/employee/getAll
      url: /employees
      paginated: true
      request:
        implementedInterfaces: 
        - org.jxapi.exchanges.employee.EmployeePaginatedRequest
        description: Page request parameters for 'getAllEmployees' rest endpoint paginated requests.
        properties:
        - name: page
          type: INT
          description: Page number to return, defaults to 1.
          sampleValue: 1
        - name: size
          type: INT
          description: >
            Number of employees to return per page.<br>
            Defaults to ${constants.defaultPageSize}.<br>
            Maximum is ${constants.maxPageSize}.
          sampleValue: 10
      response:
        implementedInterfaces:
        - org.jxapi.exchanges.employee.EmployeePaginatedResponse
        properties:
        - name: page
          type: INT
          description: Page index, starting from 1
          sampleValue: 1
        - name: totalPages
          type: INT
          description: Total number of pages available
          sampleValue: 10
        - name: employees
          objectName: Employee			
          type: OBJECT_LIST       
    - name: addEmployee
      description: Add a new employee
      httpMethod: POST
      docUrl: https://www.example.com/docs/employee/add
      url: /employee
      request:
       description: Employee to add
       objectName: Employee
    - name: updateEmployee
      description: Update an existing employee
      httpMethod: PUT
      docUrl: https://www.example.com/docs/employee/add
      url: /employee
      request:
       description: Employee to update
       objectName: Employee
    - name: deleteEmployee
      description: Delete an employee
      httpMethod: DELETE
      docUrl: https://www.example.com/docs/employee/delete
      url: /employee
      request:
       name: id
       type: INT
       description: Employee ID
       sampleValue: 1
       in: PATH
   websocketEndpoints:
    - name: employeeUpdates
      description: Employee updates websocket
      docUrl: https://www.example.com/docs/employee/updates
      message:
       description: Employee update message
       properties:
        - name: eventType
          description: Type of event, e.g. one of ${constants.updateEmployeeType.add}, ${constants.updateEmployeeType.update} or ${constants.updateEmployeeType.delete} (see ${constants.updateEmployeeType} constants).
          sampleValue: ${constants.updateEmployeeType.update}
        - name: employee
          objectName: Employee
          description: Employee that was updated
          sampleValue: John
```

Such exchange descriptor can also be written across multiple files with same exchange name: When running generator plugin in wrapper project module, it will scan `src/main/resources/jxapi` folder and aggregate all exchanges (multiple exchange can be specified) found in every .yaml and .json file.

For complex API specifications with many endpoints it is preferable to do so. Have a look at [employeeExchange](../../src/test/resources/employeeExchange/) folder to see the same exchange as above defined in multiple files.

## Exchange
The root of JSON descriptor data is a JSON object referred to as 'exchange'. An exchange is the root of API wrapper, mapped to [ExchangeDescriptor](../../src/main/java/org/jxapi/exchange/descriptor/ExchangeDescriptor.java). It is composed of the following properties:
 * `name`: The name of this wrapper or API ( here `Employee`)
 * `description`: A description of this exchange. May contain [placeholders](#placeholders) referencing exchange contants or properties.
 * `docUrl`: Link to exchange website API documentation home page
 * `basePackage`: The base Java package name for the generated code. It is recommended this package name contains `.gen.` as a convention for packages containing generated code that should not be edited manually.
 * `properties`: A list of properties required to configure the exchange. A property object is mapped to [ConfigProperty](../../src/main/java/org/jxapi/exchange/descriptor/ConfigProperty.java). Wrappers are instantiated with `Properties` object expected to carry defined configuration properties like API key / secret.
 * `constants`: A list of constant values used in the exchange. A constant object is mapped to [Constant](../../src/main/java/org/jxapi/exchange/descriptor/Constant.java) object. Defining constants for instance for possible values of a request or response field improves clarity and avoids duplication. Constants values or description may contain  [placeholders](#placeholders) referencing other previously defined exchange contants.
 * `httpUrl`: The base URL for HTTP API endpoints. The value may contain  [placeholders](#placeholders) referencing exchange contants or configuration properties. In sample above, it is set to a placeholder `config.baseHttpUrl` that is meant to be replaced by base server URL set in `baseHttpUrl` config property. This property may not be set when there are every REST endpoint is defined with either `url` property set to a relative URL or belonging to API group with `httpUrl` set to an absolute URL.
 * `httpRequestInterceptorFactory`: The factory class for creating HTTP request interceptors, see [HttpRequestInterceptorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptorFactory.java). If for exchange requires specific headers to be  set for instance for authentication, a specific interceptor has to be provided through this property, see [HTTP request interceptor dev guide](./HttpRequestInterceptorDevGuide.md).
 * `httpRequestExecutorFactory`:  The factory for HTTP request executor, see [HttpRequestExecutorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpRequestExecutorFactory.java). Default implementation used when this property is not set is usually sufficient.
 * `websocketHookFactory`: The factory class for creating WebSocket hooks, see [WebsocketFactory](../../src/main/java/org/jxapi/netutils/websocket/WebsocketHookFactory.java). Using a custom factory for hooks that implements exchange protocol specific handshake, heartbeats is generally required, see [Websocket hook dev guide](./WebsocketHookDevGuide.md)..
 * `websocketFactory`: The factory class for creating base websocket, see [WebsocketFactory](../../src/main/java/org/jxapi/netutils/websocket/WebsocketFactory.java). The default implementation is usually sufficient.
 * `network`: This section describes the different HTTP and Websocket clients that will carry endpoint requests. Each REST endpoint must be defined an HTTP client and each Websocket endpoint must be defined a websocket endpoint present declared in [network](#network).
 * `apis`: A list of API groups, each containing HTTP and WebSocket endpoints, see [below](#api-groups).

## Configuration properties and constants.

### Constants
In addition to endpoints, API specifications may use constant values, for instance as enumarated possible values for a field.
These constants may be used across several endpoints.
In order to simplify, improve clarity and avoid code duplication using the wrapper, such constants may be defined in `constants` property of `exchange` structure.

A constant definition may contain nested constants in its `constants` properties. Such constants containing nested constants are 'groups' defining a hierarchy. Groups are used to group constants that come together like enumarations of field possible values. References to a nested constant should be like `constants.myGroup.mySubGroup.myNestedConstant`

The resulting generated code will list constants as `public static final` members of a generated `<ExchangeID>Constants` class in main generated wrapper file.

Constants can be referenced in description, sample values of fields, http/websocket base URL property definitions of the descriptor file.
The generated code will substitute placeholders like `${constants.myGroup.myConstant}` with javadoc link, markdown link for javadoc/markdown documentation, or reference to the constant for a sample value.

Remark: The type must be either a primitive type, e.g. `STRING` (default), `INT`, `LONG`, `BOOLEAN`, `BIGDECIMAL`, or a `MAP` or `LIST` with primitive type value like `INT_LIST_MAP`.

### Configuration properties

The descriptor root `exchange` structure exposes a `properties` property that can be defined a list of configuration properties (like API key/secret for authentification), that wrapper client can use to configure the wrapper.

The wrapper client will instantiate it by creating an `<ExchangeID>ExchangeImpl` instance that takes a `Properties` instance in constructor. This class will expose configuration properties as `public static final ConfigProperty` members and static getter methods making it easy to retrieve the value of a property from such properties instance.

For instance API Key/Secret properties can be listed, to be used by wrapper `HttpRequestInterceptor` implementation to sign outgoing authenticated requests.

Remark: The type of a configuration property must be a primitive type, e.g. `STRING` (default), `INT`, `LONG`, `BOOLEAN`, `BIGDECIMAL`. 

#### Demo configuration properties

The generated wrapper contains [demo snippets](./UsingTheWrapper.md#demo-snippets) to test each defined REST or Websocket endpoint.
In addition to specific configuration properties defined in descriptor and common ones, some properties specific to demo snippets are also generated in `Demo<exchangeId>Properties` class in _src/test/java_ source folder.
There is one property for each parameter of each. List/Map/Object parameters can be configured using a raw JSON value.

Every available property is listed in generated `demo-<exchangeId>.properties.dist` file.
This file should be duplicated as `demo-<exchangeId>.properties` file (not to be put under version control if it contains sensitive/secret informations), that is used be default by demo snippets.

### Network

The `network` section declares HTTP and Websocket clients used by endpoints.

#### HTTP client

An HTTP client processes REST endpoint requests. It is composed of an interceptor that will pre-process incoming HTTP requests and executor that will send it on network and receive response asynchronously. If there are any REST endpoints, at least one HTTP client must be defined.

 * `name`: Identifier of the client that can be referenced either in API groups `defaultHttpClient` or REST endpoint `httpClient` properties.
 * `httpRequestInterceptorFactory`: The factory class for creating HTTP request interceptors, see [HttpRequestInterceptorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptorFactory.java). If for exchange requires specific headers to be  set for instance for authentication, a specific interceptor has to be provided through this property, see [HTTP request interceptor dev guide](./HttpRequestInterceptorDevGuide.md).
 * `httpRequestExecutorFactory`:  The factory for HTTP request executor, see [HttpRequestExecutorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpRequestExecutorFactory.java). Default implementation used when this property is not set is usually sufficient: This default implementation uses a shared [HttpClient](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html) with its dedicated multiple thread pool executor.


 #### Websocket client

 * `name`: Identifier of the client that can be referenced either in API groups `defaultWebsocketClient` or REST endpoint `websocketClient` properties.
 * `websocketHookFactory`: The factory class for creating WebSocket hooks. Must be defined with a custom websocket hook factory, see [Websocket hook factory dev guide](./WebsocketHookDevGuide.md).
 * `websocketFactory`: The factory class for creating base websocket, see [WebsocketFactory](../../src/main/java/org/jxapi/netutils/websocket/WebsocketFactory.java). The default implementation used when this property is not set is usually sufficient.
 * `websocketUrl`: The URL to open websocket connection to. The value may contain  [placeholders](#placeholders) referencing exchange contants or configuration properties.

## API groups

 The exchange REST/Websocket endpoints are sorted in groups, this is useful to regroup API endpoints by functional affinity and not list every endpoint in a single interface. The API group object is mapped to [ExchangeApiDescriptor](../../src/main/java/org/jxapi/exchange/descriptor/ExchangeApiDescriptor.java). It is composed of the following properties:
 * `name`: API group name
 * `description`: A description of the API group. May contain [placeholders](#placeholders) referencing exchange or API group contants, or configuration properties. 
 * `httpUrl`: The base URL for HTTP API endpoints. It can be a relative path, in which case the actual base url is result of concantenation of base `httpUrl` in parent (exchange) property and value of this property if it is set. The value may contain  [placeholders](#placeholders) referencing exchange contants or configuration properties.
 * `rateLimits`: List of [rate limits](#api-request-rate-limit) request rate limitation to enforce in addition to rules defined in parent exchange.
 * `restEndpoints`: A list of REST API endpoints in this group, see [below](#rest-api-endpoints).
 * `websocketEndpoints`: A list of WebSocket API endpoints in this group, see [below](#websocket-api-endpoints).

## REST API endpoints
A REST endpoint is an API interface which consists in sending a HTTP request with expected URL, verb... and request parameters which can be specifed as URL query parameters or as request body encoded in JSON format.
Such REST endpoint JSON object in descriptor file is mapped to [RestEndpointDescriptor](../../src/main/java/org/jxapi/exchange/descriptor/RestEndpointDescriptor.java).

GET, DELETE HTTP methods do not expect a body in request, so parameters will be encoded to URL query params by default. They may also be serialized as path par ameters by tuning field [in](#in-query-or-path) property to `PATH`.
For other methods, request parameters are serialized as JSON in request body.

The structure of request and response data are specified using the same JSON object as value of `request` and `response` properties, see [Flexible data structure](#flexible-data-structure-definition)

Each REST endpoint can be associated specific [rate limits](#api-request-rate-limit) that add up to API group specific and exchange specific ones. When using 'weighted' rate limit algorithm, the weight for one call to this REST API is specified in `requestWeight` properties.

Each REST API endpoint is described by the following properties:
 * `name`: The name of the endpoint.
 * `httpMethod`: The HTTP method used (e.g., GET, POST).
 * `description`: A description of the endpoint.  May contain [placeholders](#placeholders) referencing exchange or API group contants, or configuration properties. 
 * `docUrl`: Link to the endpoint's documentation.
 * `url`: The URL path for the endpoint. Can be a relative path, in which case actual URL used is concatenation of of base `httpUrl` in parent (api group) property and value of this property if it is set.
 * `request`: The request data type definition as [Flexible data structure](#flexible-data-structure-definition)
 * `response`: The response object, containing properties for the response data.
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
 * `topic`: The topic to subscribe to. Should uniquely identify a websocket stream among every endpoints of this API group. May contain placeholders as specified in [Topic placeholders](#topic-placeholders).
 * `description`: A description of the endpoint. May contain [placeholders](#placeholders) referencing exchange or API group contants, or configuration properties.  
 * `request`: The request object, containing properties for the request parameters, which will be used to build the subscription message to a topic.
 * `topicMatcher`: Describes the logic and fields used to match an incoming message on websocket as being relevant for subscription to this stream, see [Message topic matcher](#message-topic-matcher)
 * `message`: The message object, containing properties for the message data.
 
Serializing a request to a subscription message, building the unsubscription message to cancel an existing subscription, sending or listening to 'heartbeat' messages is customized using a [WebsocketHook](../../src/main/java/org/jxapi/netutils/websocket/WebsocketHook.java).
See [Websocket Hook dev guide](./WebsocketHookDevGuide.md)

### Topic Placeholders

The `topic` property in a WebSocket endpoint can contain placeholders in the format `${placeholderName}`. These placeholders are replaced with the corresponding values from the request properties when subscribing to the topic.

Example:
```yaml
name: "tickerStream"
topic: "${request.symbol}@ticker"
request:
  properties:
    - name: "symbol"
      type: "STRING"
      description: "Symbol to subscribe to ticker stream of"
      sampleValue: "BTC_USDT"
```

In this example, the placeholder `${request.symbol}` in the topic will be replaced with the value of the `symbol` property from the request.
Available placeholders are:

- Request placeholders: `${request}` placeholder will be replaced with whole request object passed to subscription method. `${request.symbolInfo.symbol}` will be replaced with value of _symbol_ property of object carried in _symbolInfo_ property of request object.
- Constant placeholders: `{$constants.group1.myConstant}` will be replaced with value of _myConstant_ constant from _group1_ constant group, see [constants](#constants). 
- Configuration properties placeholders: `${config.myProperty}` will be replaced with configured value for _myProperty_ , see [configuration properties](#configuration-properties)

### Message topic matcher

The `topicMatcher` property describes how an incoming message on websocket is matched as relevant for a subscription to this endpoint. This is achieved by matching one or more fields of message against a pattern.

Examples:

#### Message matching using a single field
```yaml
topicMatcher:
  - fieldName: "topic"
    fieldValue: "${topic}"
```
In this example, the incoming message `topic` field must have the value computed in 'topic', hence `{symbol}@ticker`, where `symbol` must match the value of the `symbol` property from the request.

Alternatively, field value can be matched against a regular expression:
```yaml
topicMatcher:
  - fieldName: "topic"
    fieldRegexp: "ticker.*"
```

#### Matching messages using multiple fields

```yaml
topicMatcher:
  - and:
    - fieldName: "subject"
      fieldValue: "ticker"
    - fieldName: "symbol"
      fieldValue: "${topic}"      
```
In this example the incoming message `subject` field must carry the value `ticker` and `symbol` field must carry the value of request object `symbol` property.
Notice you could also use `or` logical matcher if a message must match value of either one of multiple fields

```yaml
topicMatcher:
  - or:
    - fieldName: "field1"
      fieldValue: "field1Value"
    - fieldName: "field2"
      fieldValue: "field2Value"
    - fieldName: "field3"
      fieldValue: "field2Valu3"  
```

In the above example, a message will match when either `field1` carries `field1Value` or `field2` carries `field2Value` or `field3` carries `field3Value`.

Finally, matchers can be composed to create any logical pattern to match a message against fields. Example:

```yaml
topicMatcher:
  - and:
    - or:
      - fieldName: "field1"
        fieldValue: "field1Value"
      - fieldName: "field2"
        fieldValue: "field2Value"
    - fieldName: "field3"
      fieldRegexp: "field3Value"
```

#### TopicMatcher Placeholders
Like `topic` websocket endpoint property value, the value either plain value (`fieldValue`) or regular expression (`fieldRegexp`) property may contain placeholders for constants, configuration properties, subscription request or sub property of it, plus additionnal _topic_ (`${topic}`) placeholder is available,
such placeholder will be replaced by computed value for `topic`.



### Message Matching

The `message` property defines the structure of the message and the fields that are expected in the message see [Flexible data structure](#flexible-data-structure-definition). Each field specifies a name, a type, and a description.

Example:
```yaml
message:
  properties:
    - name: "subject"
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
In this example, the message is expected to contain fields `t` (subject), `s` (symbol), and `p` (last traded price). The values of these fields are used to match the message against the topic and request properties.

By defining the topic with placeholders and specifying the message topic matcher fields, the WebSocket endpoint can accurately subscribe to the desired topic and match incoming messages based on the specified criteria.

## Flexible data structure definition
Both REST endpoints request and response, Websocket endpoints subscription request and response message are defined in exchange descriptor file using the same object structure, mapped to [Field](../../src/main/java/org/jxapi/exchange/descriptor/Field.java) class. This class is used to define the structure of request and response data for both REST and WebSocket endpoints. It allows for a flexible type definition that can represent primitive types, lists, maps, or composite objects.

### Primitive Types
A field can be a primitive type such as `STRING`, `INT`, `BIGDECIMAL`, or `LONG`. These types are directly mapped to their respective Java types.
Remark: When neither of 'object' type specific `properties` or `objectName` is specified, the default field type is `STRING` unless `type` property is specified. If either `properties` or `objectName` property are present, the default type is `OBJECT`.

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
 <!-- Remark 'type' needs not be specified because 'properties' is which implies type is 'OBJECT' --> 
properties:
  - name: "id"
    type: "STRING"
    description: "Unique identifier"
    sampleValue: bob1234
  - name: "values"
    type: "INT_LIST"
    description: "List of integer values"
    defaultValue: [1, 3, 5]
  - name: "attributes"
    type: "MAP"
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
`sampleValue` property of `Field` can be set with a sample value to use in demo snippets. The generated code of the demo snippet will create for instance a REST or websocket subscription request will be set with `sampleValue` property value of the request field. Such value may contain [placeholders](#placeholders) referencing constants, configuration properties or demo configuration properties. Such configuration properties can be set to customize request sent.

### Default value
`defaultValue` property of `Field` can be set with an initial value that will be used unless set explicitely. Such value may contain only 'constants' [placeholders](#placeholders). 

### 'in' (QUERY or PATH)
`in` property of field is designed for fields that describe a REST endpoint request (or one of it sub-structure fields) that should provide arguments in URL.
Such arguments extracted from request properties can be passed either as URL path parameters (ex: _https/api.example/com/user/{id}_) or URL query parameters (ex: _https://api.example.com/user?id={id})_ ). 
When `in` property is not set or set to `QUERY`, the corresponding request property is passed as URL query parameters. When explicitely set to `PATH`, the coresponding request property is passed as path parameters.
Parameters are provided in order _pathParam1Value/pathParam2Value...?queryParam1=queryParam1Value.._ e.g. path parameters in order they are found in descriptor, then URL query parameters in order they are found in descriptor.

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

Rate limits are defined either at exchange, api group or REST endpoint level as a `rateLimits` property that carries as value a list of [RateLimitRule](../../src/main/java/org/jxapi/netutils/rest/ratelimits/RateLimitRule.java) objects defined by following properties:
 * `id`: Unique identifier
 * `timeframe`: Time frame in ms for which request count or cumulated weight should not exceed limit.
 * `maxRequestCount`: The maximum number of requests that can be attempted within rolling time frame. A negative value means it should not be taken into account and this rate limit is expressed in cumulated weight.
 * `maxTotalWeight`: The maximum cumulated weight of calls within time frame limitation.


When a request is submitted to an endpoint, each rate limit rule is checked, taking into account the request (incrementing current request count if it does not exceed any rule), or providing the remaining delay before it can be executed if it should not be submitted immediately.
Accordingly the request is either submitted immediately or delayed for the minimum time to wait for and then submitted again. See 
This when `THROTTLE` policy is applied, which is the default. Beware that client applications breaking rate limits may experience increasing delay in request execution and accumulation of requests in queue may cause a memory leak. For this reason it is advised to set a maximum wait delay on request throttling using `Exchange#setMaxRequestThrottleDelay(long)`method.
See [RequestThrottler](../../src/main/java/org/jxapi/netutils/rest/ratelimits/RequestThrottler.java).

Different policies may be specified using `Exchange#setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode)`, see [RequestThrottlingMode](../../src/main/java/org/jxapi/netutils/rest/ratelimits/RequestThrottlingMode.java):
 * `BLOCK`: Instead of throttling request, immediately anwser that request with a response carrying a [RateLimitReachedException](../../src/main/java/org/jxapi/netutils/rest/ratelimits/RateLimitReachedException.java) exception stating how much time should be awaited for before retrying this request, and HTTP response code 429 (_TOO_MANY_REQUESTS_). This is similar to `THROTTLE` mode with `maxRequestThrottleDelay` set to 0.
 * `NONE`: Ignores rate limits check and always submit requests immediately. 
 
## Placeholders

 Placeholders referencing constants or configuration properties can be used in values of descriptor properties using `${constants.myConstant}` or `${config.myProperty}` syntax. The value in brackets will reference a constant (defined in API group or exchange) or a configuration property.
 
  - When used in _description_ properties, they will be substituted with javadoc link to constant or property.
  - When used in exchange or api groups _httpUrl_ or _websocketUrl_ property, the generated URL value will substitute the given placeholder with constant or property value.
  - When used in constants values, a placeholder may only reference another constant. The generated constant value will substitute the given placeholder with constant value.
  - When used in constants _sampleValue_ properties, the generated value will substitute the given placeholder constant or either exchange or demo configuration property value. Such value is generated for instance in demo snippets to build default request.

  Defining constants and using placeholders to reference them improves clarity of generated wrapper and reduces duplication.

## Splitting large exchange definitions into multiple files

  For complex API specifications with many endpoints it is preferable to split exchange definition into multiple files. When running generator plugin in wrapper project module, it will scan `src/main/resources/jxapi` folder and aggregate all exchanges (multiple exchange can be specified) found in every .yaml and .json file.
  
  Have a look at [employeeExchange](../../src/test/resources/employeeExchange/) folder to see the same exchange as above defined in multiple files.

  This has following advantages:
  - Each endpoint can be defined in a separate file, making it easier to maintain and update.
  - AI assistants can be used more effectively to generate or update endpoint definitions based on documentation pages.
  - Reduces the risk of merge conflicts when multiple developers are working on the same exchange definition
  - Encourages modular design and organization of API specifications.
  - Can be used to segregate Java specific properties (like `implementedInterfaces`) from API specific definition itself.

  When splitting exchange definition into multiple files, ensure that:
  - Each file contains a valid JSON or YAML structure.
  - The root structure of each file is either an `exchange` object or an `apis` list to be merged into parent exchange definition.
  - File names should be descriptive of their content, e.g., `getTickerEndpoint.yaml` for a file defining a ticker endpoint.

  When generator encounters multiple files defining the same exchange (same `id` property in `exchange` structure), it will merge them into a single exchange definition by aggregating API groups and endpoints and properties defined across files.
  General rules for merging are:
  - Properties with scalar values (like `name`, `description`) defined in multiple files will take single value from the first encountered definition. If conflicting values are found, an error is raised.
  - List properties (like `apis`, `restEndpoints`, `websocketEndpoints`, `rateLimits`, `properties`, `constants`) are merged by concatenating lists from all definitions.
  - Nested objects (like `network`, `exchange` properties) are merged recursively following the same rules.







