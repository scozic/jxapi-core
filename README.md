# JXAPI

Generate a Java REST and/or Websocket API wrapper efficiently using code generation tools.

## Introduction
Many web services are consumed as HTTP REST/Websocket APIs. Those APIs disseminate data as structured JSON objects.
JXAPI is for generating java code to call those REST and Websocket APIs using a simple function with a Java POJO as request and response/message data.

The generator will take as input a JSON file describing APIs, and generate request/response POJOs, Java interfaces to REST APIs and Websockets and their implementation, and also demo snippets and a documentation skeleton.
You will also need to write a few lines of code for API specific implementation aspects like:
 * Authentication challenge (like computing authorization header added requests using API key/secret and request parameters)
 * Request parameter formatting (use as query parameters or request body parameters)

That can be achieved easily by specifying implementation REST+Websocket endpoint factories using hooks, check wrapper development guide below.

The JSON API descriptor file structure is simple, and can be AI generated from online API documentation.

Follow the guide below to write such API wrapper.

## Project bootstrap

See [Wrapper module Setup](doc/manual/WrapperModuleSetup.md) : Basically, you just initialize a Java/Maven project, and add to Maven _pom.xml_ file a `mvn:exec` plugin to run generation of Java source files.

## Manage specific REST/Websocket API authentication
A few lines of Java code have to be written manually to manage API specific authentication challenge. See dedicated guides:
 * If your wrapper exposes REST endpoints: You may need to write a specific `HttpRequestInterceptor` to customize incoming request headers, as explained in [HttpRequestInterceptorDevGuide](./doc/manual/HttpRequestInterceptorDevGuide.md). The full class name of `HttpRequestInterceptorFactory` that creates instances of your `HttpRequestInterceptor` must be set in exchange descriptor file `httpRequestInterceptorFactory` at exchange or API group level.
 * If your wrapper exposes REST endpoints, you should set in `httpRequestInterceptorFactory` property of exchange descriptor file at exchange or API group level with full name of class implementing `HttpRequestInterceptorFactory` that creates specific hooks for websocket protocol of you API, see [HttpRequestInterceptorDevGuide](./doc/manual/HttpRequestInterceptorDevGuide.md).
 * If your wrapper exposes websocket endpoints, you should set in `websocketHookFactory` property of exchange descriptor file at exchange or API group level with full name of class implementing `WebsocketHookFactory` that creates specific hooks for websocket protocol of you API, see [WebsocketHookDevGuide](./doc/manual/WebsocketHookDevGuide.md).


## Write JSON file for API descriptor
Now the tedious work is done, let's create some API wrappers.
You must create a file with name ending with `Descriptor.json` in `src/main/resources` folder of your module.
Follow [Exchange descriptor file documentation](./doc/manual/ExchangeDescriptorFileDoc.md) guide to convert API documentation into such files.

AI assistants can be efficient to write such files &#128521;. 

Running `mvn exec:java` command triggers generation of wrapper code in `src/main/java` folder, demos in `src/test/java` and a sample _MyExchangeREADME.md_ file at root of module project.

I recommand writing the API descriptor file incrementally and running generator after adding an endpoint to run the demo snippet (see [Demos](#demos)) to ensure it works as expected.

## Resulting generated code
The resulting generated code includes:
 * Exchange interface, with getter function to retrieve any exchange API.
 * Exchange interface implementation, with constructor expecting a single `java.util.Properties` argument containing API configuration parameters like API KEY/Secret
 * Exchange API interface for each exchange API defined in root exchange, and implementation of that interface with
  * One function for each REST API. Calls are performed asynchronously and return an instance of [FutureRestResponse](src/main/java/com/scz/jxapi/netutils/rest/FutureRestResponse.java) which is a `java.util.concurrent.Future` object.
   * REST API method take a request object as parameter and a response object as returned type which are generated Java POJOs carrying properties corresponding to parameters defined in JSON file.
  * One _subscribe_ and one _unsubscribe_ method to subscribe/unsubscribe to Websocket endpoint topics. Subscription and stream message parameters and are carried in generated POJOs.
 * Demo snippets in `src/test/java` to test call to REST endpoints and websocket endpoint subscription, see (see [Demos](#demos)).
 * A sample README.md file (name is prefixed with exchange name) that documents the wrapper and exposed api groups and nested endpoints.

### Demos

### Sample manual

## Using the wrapper  

## Supported exchanges
TODO! Currently under development :)

