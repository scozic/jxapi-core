# REST API request interception development guide

## Request interception overview

Most APIs REST interfaces require specific headers to be set in requests, for instance to carry authentication data.
JXAPI Java wrapper generator allows to write custom interceptors to alter HTTP requests before they are submitted.

Apart from customizing the request, interceptors also have the responsibility to serialize / deserialize request and response data, see [Body serialization and deserialization](#body-serialization-and-deserialization) section below for more details.

This is achieved by implementing [HttpRequestInterceptor](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptor.java) interface: `intercept(HttpRequest)` method allows to modify any part of an outgoing request: headers, URL, body... before it is submitted.

In exchange descriptor JSON file you should declare property `httpRequestInterceptorFactory` property of structure describing an HTTP client among ones defined in `network\httpClients` part of exchange descriptor (see [network](./ExchangeDescriptorFileDoc.md#network) ), with value containing name of a class implementing [HttpRequestInterceptorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptorFactory.java) class.
Such class must have a defaut public constructor. The `createInterceptor(Exchange exchange)` method implementation must return a `HttpRequestInterceptor` instance. 

## Request interception

JXAPI also allows to intercept HTTP responses by implementing the [HttpResponseInterceptor](../../src/main/java/org/jxapi/netutils/rest/HttpResponseInterceptor.java) interface and declaring the corresponding factory in exchange descriptor file with `httpResponseInterceptorFactory` property of HTTP client structure. Such factory class must expose a public default constructor, implement the [HttpResponseInterceptorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpResponseInterceptorFactory.java) interface, and its `createInterceptor(Exchange exchange)` method must return a `HttpResponseInterceptor` instance.

## Body serialization and deserialization

Interceptors have the responsibility serialize / deserialize HTTP request POJOs to body and deserialize response body to POJOs.
When endpoint request definition specifies a body for the request, the *request* property of an HttpRequest is a POJO, that can be serialized to JSON using the `requestSerializer` property of the interceptor. This property is an instance of a class extending [StdSerializer](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/latest/com/fasterxml/jackson/databind/ser/std/StdSerializer.html) that is generated for each request POJO. The `intercept(HttpRequest)` method implementation should serialize the request POJO to JSON and set it as the body of the request, unless specific needs require a different behavior, for instance altering the JSON structure or using another serialization format.

When endpoint response definition specifies a body for the response, the *response* property of an HttpResponse is a String containing the JSON response body, and the interceptor must deserialize it to a POJO using the `responseDeserializer` property of the HTTP request associated to the response, which is an instance of a class extending [StdDeserializer](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/latest/com/fasterxml/jackson/databind/deser/std/StdDeserializer.html) that is generated for each response POJO.

Wether request serialization or response deserialization should be peformed by the interceptor depends on the HTTP method of the endpoint, as follows:
- For GET endpoints, only response deserialization is performed by the interceptor, as GET requests do not have a body.
- For POST, PUT, PATCH, DELETE endpoints, both request serialization and response deserialization are performed by the interceptor, as those endpoints can have a body in both request and response.
Also, it depends on definition of the endpoint in the exchange descriptor file, as only endpoints with a body defined for request or response will require serialization or deserialization to be performed by the interceptor.
The generated `HttpRequest` instance carries the `requestSerializer` property only if the endpoint has a body defined for request and HTTP method applicable to it, and the `HttpResponse` instance carries the `responseDeserializer` property only if the endpoint has a body defined for response.

Unless you have specific needs, you can extend the [DefaultHttpRequestInterceptor](../../src/main/java/org/jxapi/netutils/rest/DefaultHttpRequestInterceptor.java) class that provides a default implementation of `intercept(HttpRequest)` method to perform request serialization as described above using the generated JSON serializer for theh request, and override it to add custom behavior like adding headers or altering the URL.

For response deserialization, you should also extend the [DefaultHttpResponseInterceptor](../../src/main/java/org/jxapi/netutils/rest/DefaultHttpResponseInterceptor.java) class that provides a default implementation of `intercept(HttpResponse)` method to perform response deserialization as described above using the generated JSON deserializers, and override it to add custom behavior like custom error handling.

## Examples

### Request interception example
Here is an example of a simple `HttpRequestInterceptor` implementation that adds a custom header to each request and keepsn default body serialization behavior by extending `DefaultHttpRequestInterceptor`:

```java
public class CustomHeaderInterceptor extends DefaultHttpRequestInterceptor {
    @Override
    public void intercept(HttpRequest request) {
        super.intercept(request); // Call the default implementation to handle body serialization
        request.addHeader("X-Custom-Header", "CustomValue");
    }
}
```

And the corresponding factory class:

```java
public class CustomHeaderInterceptorFactory implements HttpRequestInterceptorFactory {
    @Override
    public HttpRequestInterceptor createInterceptor(Exchange exchange) {
        return new CustomHeaderInterceptor();
    }
}
```


### Response interception example
Here is an example of a simple `HttpResponseInterceptor` implementation that logs response status and keeps default body deserialization behavior by extending `DefaultHttpResponseInterceptor`:
```java
public class LoggingResponseInterceptor extends DefaultHttpResponseInterceptor {
    @Override
    public void intercept(HttpResponse response) {
        super.intercept(response); // Call the default implementation to handle body deserialization
        System.out.println("Received response with status: " + response.getStatusCode());
    }
```
And the corresponding factory class:
```java
public class LoggingResponseInterceptorFactory implements HttpResponseInterceptorFactory {
    @Override
    public HttpResponseInterceptor createInterceptor(Exchange exchange) {
        return new LoggingResponseInterceptor();
    }
}
```


In your exchange descriptor JSON file, you would then specify the `httpRequestInterceptorFactory` property like this:

```json
{
    "httpRequestInterceptorFactory": "org.jxapi.netutils.rest.CustomHeaderInterceptorFactory"
}
```

This setup ensures that every outgoing HTTP request will include the custom header defined in the interceptor.

