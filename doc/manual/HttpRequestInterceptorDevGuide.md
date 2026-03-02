# HTTP Request and Response Interceptor Development Guide

## Overview

When working with REST APIs, you often need to customize HTTP requests and responses. Common use cases include:

- Adding authentication headers
- Logging requests and responses
- Modifying request URLs or bodies
- Custom error handling
- Rate limiting

JXAPI provides **interceptors** to handle these scenarios. Interceptors allow you to:
1. **Modify outgoing HTTP requests** before they are sent
2. **Process incoming HTTP responses** after they are received
3. **Handle serialization/deserialization** of request and response data

## How Interceptors Work

### Request Interceptors
Request interceptors implement the [HttpRequestInterceptor](../../src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptor.java) interface. The `intercept(HttpRequest)` method lets you modify any part of an outgoing request:
- Headers (authentication, content-type, etc.)
- URL parameters
- Request body
- HTTP method

### Response Interceptors  
Response interceptors implement the [HttpResponseInterceptor](../../src/main/java/org/jxapi/netutils/rest/HttpResponseInterceptor.java) interface. The `intercept(HttpResponse)` method lets you:
- Process response headers and status codes
- Handle errors
- Log response data
- Transform response bodies

### Factory Pattern
Interceptors are created using factory classes:
- **HttpRequestInterceptorFactory** creates request interceptors
- **HttpResponseInterceptorFactory** creates response interceptors

These factories must have a public default constructor and implement the `createInterceptor(Exchange exchange)` method.

## Configuration

To use interceptors, configure them in your exchange descriptor JSON file:

```json
{
  "network": {
    "httpClients": [
      {
        "name": "myClient",
        "httpRequestInterceptorFactory": "com.example.MyRequestInterceptorFactory",
        "httpResponseInterceptorFactory": "com.example.MyResponseInterceptorFactory"
      }
    ]
  }
}
```

See the [network configuration documentation](./ExchangeDescriptorFileDoc.md#network) for more details.

## Data Serialization and Deserialization

Interceptors are responsible for converting between Java objects (POJOs) and JSON data. This process varies depending on the HTTP method and endpoint configuration.

### Request Serialization

When an endpoint expects a request body:
- The `HttpRequest.request` property contains a Java object (POJO)
- Use the `HttpRequest.requestSerializer` to convert this object to JSON
- Set the resulting JSON as the request body

**Example flow:**
```
Java Object → JSON Serializer → JSON String → HTTP Request Body
```

### Response Deserialization

When an endpoint returns a response body:
- The `HttpResponse.response` property contains a JSON string
- Use the `HttpRequest.responseDeserializer` to convert this JSON to a Java object
- The result becomes your typed response object

**Example flow:**
```
HTTP Response Body → JSON String → JSON Deserializer → Java Object
```

### When Serialization Occurs

The need for serialization depends on the HTTP method and endpoint definition:

| HTTP Method | Request Serialization | Response Deserialization |
|-------------|----------------------|--------------------------|
| GET         | ❌ No body            | ✅ If response has body   |
| POST        | ✅ If request has body | ✅ If response has body   |
| PUT         | ✅ If request has body | ✅ If response has body   |
| PATCH       | ✅ If request has body | ✅ If response has body   |
| DELETE      | ✅ If request has body | ✅ If response has body   |

### Default Implementations

For most use cases, you can extend the provided default classes:

- **[DefaultHttpRequestInterceptor](../../src/main/java/org/jxapi/netutils/rest/DefaultHttpRequestInterceptor.java)**: Handles request serialization automatically
- **[DefaultHttpResponseInterceptor](../../src/main/java/org/jxapi/netutils/rest/DefaultHttpResponseInterceptor.java)**: Handles response deserialization automatically

These classes apply default JSON serialization/deserialization of request POJOs using Jackson's [StdSerializer](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/latest/com/fasterxml/jackson/databind/ser/std/StdSerializer.html) and [StdDeserializer](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/latest/com/fasterxml/jackson/databind/deser/std/StdDeserializer.html) that are generated for each POJO.
These default request and response interceptors are used when no custom interceptor factories are defined in the exchange descriptor file.

When extending `DefaultHttpRequestInterceptor` or `DefaultHttpResponseInterceptor`, always call `super.intercept()` to ensure proper serialization/deserialization:

```java
@Override
public void intercept(HttpRequest request) {
    super.intercept(request); // Important: call this first
    // Your custom logic here
}

## Implementation Examples

### Example 1: Adding Custom Headers

This example shows how to add authentication headers to every request:

**Step 1: Create the Request Interceptor**
```java
public class AuthHeaderInterceptor extends DefaultHttpRequestInterceptor {
    
    private final String apiKey;
    
    public AuthHeaderInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }
    
    @Override
    public void intercept(HttpRequest request) {
        // Handle default serialization first
        super.intercept(request);
        
        // Add custom headers
        request.addHeader("Authorization", "Bearer " + apiKey);
        request.addHeader("Content-Type", "application/json");
    }
}
```

**Step 2: Create the Factory**
```java
public class AuthHeaderInterceptorFactory implements HttpRequestInterceptorFactory {
    
    @Override
    public HttpRequestInterceptor createInterceptor(Exchange exchange) {
        // Get API key from exchange configuration or environment
        String apiKey = getApiKeyFromConfig(exchange);
        return new AuthHeaderInterceptor(apiKey);
    }
    
    private String getApiKeyFromConfig(Exchange exchange) {
        // Implementation depends on your configuration system
        return System.getenv("API_KEY");
    }
}
```

### Example 2: Logging and Error Handling

This example demonstrates response logging with custom error handling:

**Step 1: Create the Response Interceptor**
```java
public class LoggingResponseInterceptor extends DefaultHttpResponseInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingResponseInterceptor.class);
    
    @Override
    public void intercept(HttpResponse response) {
        // Log response details
        logger.info("Received response - Status: {}, Content-Length: {}", 
                   response.getStatusCode(), 
                   response.getHeaders().get("Content-Length"));
        
        // Handle errors before deserialization
        if (response.getStatusCode() >= 400) {
            handleErrorResponse(response);
            return;
        }
        
        // Proceed with default deserialization
        super.intercept(response);
    }
    
    private void handleErrorResponse(HttpResponse response) {
        String errorBody = response.getResponse();
        logger.error("API Error {}: {}", response.getStatusCode(), errorBody);
    }
}
```

**Step 2: Create the Factory**
```java
public class LoggingResponseInterceptorFactory implements HttpResponseInterceptorFactory {
    
    @Override
    public HttpResponseInterceptor createInterceptor(Exchange exchange) {
        return new LoggingResponseInterceptor();
    }
}
```

### Example 3: Complete Setup

Here's how your exchange descriptor JSON should look:

```json
{
  "network": {
    "httpClients": [
      {
        "name": "myExchangeClient",
        "baseUrl": "https://api.myexchange.com",
        "httpRequestInterceptorFactory": "com.example.AuthHeaderInterceptorFactory",
        "httpResponseInterceptorFactory": "com.example.LoggingResponseInterceptorFactory",
        "timeout": 30000,
        "retries": 3
      }
    ]
  }
}
```

## Common Use Cases

- **Authentication**: Add API keys, JWT tokens, or OAuth headers
- **Custom Serialization**: Serialize request bodies in a specific format or handle complex data structures
- **Rate Limiting**: When sophisticated rate limiting is needed (like weight of a request depending on its content), you can use interceptors to analyze request content override the default weight defined in the descriptor file, and implement custom rate limiting strategies.

