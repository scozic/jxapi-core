# Using the wrapper

The generated wrapper Java module can be exported as Maven artifact so client application can use it by adding it as a dependency.

# Creating a wrapper instance

Create `Exchange` implementation specific to target API instance like follows:

```java
EmployeeExchange exchange = new EmployeeExchangeImpl("myEmployeeClient1", configProperties);
```

Where config properties are a `Properties` object with specific configuration properties set.
The full list of available configuration properties (if there are) can be retrieved in generated `<ExchangeID>Properties` interface.
Common properties can also be tuned.

Then, endpoints can be called from 'group' interfaces accessible from 'Exchange' instance like:
```java
exchange.getEmployeeV1Api()
```

## Rest endpoints

For each REST API is a method in endpoint API group dedicated interface accessible from `Exchange` instance. For instance:

```java
FutureRestResponse<Employee> response = exchange.getEmployeeV1Api().getEmployee(123);
```

The `FutureRestResponse` object returned is a `CompletableFuture` that wraps asynchronous execution of request. Calling thread can wait for result of using `get()` or using a callback with `thenApply()`. 
The resolved `RestResponse` object wraps the result of REST API call, and `isOk()` should be checked before reading the response.

### REST request pagination

A common design in REST APIs is to provide 'pagination' interface to load large data in chunks. Such APIs use a page index in request to specify which 'page' of data to load, and response provide next page (or last page) information.
The `RestRequestPagination` class has been designed to load all pages from a specified index from a generic such API. See [RestRequestPagination](./RestRequestPagination.md) to learn more.

## Websocket endpoints

Subscribe to a websocket endpoint using subscription method in endpoint API group dedicated interface accessible from `Exchange` instance. For instance:

```java
String subId = exchange.getEmployeeV1Api().subscribeEmployeeUpdates(this::handleMessage);
```

Unsubscription must be performed using `unsubscribe...` method.
Websockets should be monitored using [observability API](#observability).

## POJOs

REST endpoint requests, responses, Websocket endpoint subscribe requests and incoming messages are wrapped either in primitive type, list, map of a subtype or object carrying expected properties wrapped in a POJO.

Pojos generated in wrapper module have multiple features, see [Generated Java Pojos](./GeneratedJavaPojos.md).

## Observability

You may subscribe observers to monitor wrapper instance using `Exchange.subscribeObserver(ExchangeApiObserver)`. Events are fired for HTTP requests submission/completion, or websocket disconnect/reconnect, stream subscription, message reception events.

This can be used for instance to update real time application metrics for higher level monitoring.

When API exposes websocket endpoints, websocket service handles error and retries lost connections, but should be monitored for unusual failure rate.

## Disposability

Do not forget to call `Exchange.dispose()` method when done using the wrapper free resources used by wrapper like websocket connections and thread pools.
