# REST API request interception development guide

Most APIs REST interfaces require specific headers to be set in requests, for instance to carry authentication data.
JXAPI Java wrapper generator allows to write custom interceptors to alter HTTP requests before they are submitted.

This is achieved by implementing [HttpRequestInterceptor](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptor.java) interface: `intercept(HttpRequest)` method allows to modify any part of an outgoing request: headers, URL, body... before it is submitted.

In exchange descriptor JSON file you should declare property `httpRequestInterceptorFactory` property of structure describing an HTTP client among ones defined in `network\httpClients` part of exchange descriptor (see [network](./ExchangeDescriptorFileDoc.md#network) ), with value containing name of a class implementing [HttpRequestInterceptorFactory](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptorFactory.java) class.
Such class must have a defaut public constructor. The `createInterceptor(Exchange exchange)` method implementation must return a `HttpRequestInterceptor` instance. 

Here is an example of a simple `HttpRequestInterceptor` implementation that adds a custom header to each request:

```java
public class CustomHeaderInterceptor implements HttpRequestInterceptor {
    @Override
    public void intercept(HttpRequest request) {
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

In your exchange descriptor JSON file, you would then specify the `httpRequestInterceptorFactory` property like this:

```json
{
    "httpRequestInterceptorFactory": "org.jxapi.netutils.rest.CustomHeaderInterceptorFactory"
}
```

This setup ensures that every outgoing HTTP request will include the custom header defined in the interceptor.

