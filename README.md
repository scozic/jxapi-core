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
Using JXAPI to create Java wrapper saves the time to write Pojos for endpoint request/response/messages DTOs, HTTP request management, request rate limits and fix issues that come with manually writing the code. The websocket endpoint management allows efficient implementation of multiplexing (subscribing to distinct stream on a single physical websocket), heartbeat, handsharke management. It is particulary efficient for using complex APIs with many endpoints.

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

I recommand writing the API descriptor file incrementally and running generator after adding an endpoint to run the demo snippet (see [Demos](#demo-snippets)) to ensure it works as expected.

## Resulting generated code
The resulting generated code includes:
 * Exchange interface, with getter function to retrieve any exchange API.
 * Exchange interface implementation, with constructor expecting a single `java.util.Properties` argument containing API configuration parameters like API KEY/Secret
 * Exchange API interface for each exchange API defined in root exchange, and implementation of that interface with
  * One function for each REST API. Calls are performed asynchronously and return an instance of [FutureRestResponse](src/main/java/com/scz/jxapi/netutils/rest/FutureRestResponse.java) which is a `java.util.concurrent.Future` object.
   * REST API method take a request object as parameter and a response object as returned type which are generated Java POJOs carrying properties corresponding to parameters defined in JSON file.
  * One _subscribe_ and one _unsubscribe_ method to subscribe/unsubscribe to Websocket endpoint topics. Subscription and stream message parameters and are carried in generated POJOs.
 * Demo snippets in `src/test/java` to test call to REST endpoints and websocket endpoint subscription, see (see [Demos](#demo-snippets)).
 * A sample README.md file (name is prefixed with exchange name) that documents the wrapper and exposed api groups and nested endpoints.

### Demo snippets.

Generated source files include demo snippets to test each REST and Websocket endpoint, generated as classes with `public static void main(String[] args)` method in `src/test/java` source folder of project module.

When run, REST endpoint snippet will issue a request built using fields sample values from descriptor file, then wait for response and log it at INFO level.

Websocket endpoint snippet will subscribe to corresponding endpoint with a subscription request built using fields sample values from descriptor file, and wait for some delay (configurable, default is 30s) before unsubscribing and exiting. Incoming messages will be logged at INFO level.

Generated wrapper configuration properties are loaded from file `src/test/resources/demo-<yourExchange>.properties`. The generator will create a sample value of this file as `src/test/resources/demo-<yourExchange>.properties.dist` you should create a copy of, removing `.dist` suffix. This `demo-<yourExchange>.properties` file should be added to _.gitignore_ file in case it contains sensitive information like API key / secret.
Notice this file carries not only wrapper specific properties but also common properties like `jxapi.httpRequestTimeout` (maximum timeout to wait for REST endpoint call response), see [CommonConfigProperties](./src/main/java/com/scz/jxapi/exchange/CommonConfigProperties.java), and demo snippet specific properties like `jxapi.demo.ws.subscriptionDuration` that controls he duration in ms of the subscription in websocket endpoint demo snippets, see [DemoProperties](./src/main/java/com/scz/jxapi/util/DemoProperties.java).

### Sample README.md

A `<yourExchange>_README.md` file is also generated with wrapper, with documentation describing 
 * The wrapper from its description in descriptor
 * Constants
 * Configuration properties 
 * Description of each API groups from description in descriptor file
 * Description of each REST and Websocket endpoint of each API group.
 
This file can be used as _README.md_ file of the project, or referenced from it.

## Using the wrapper

When demo snippets successfully run, your wrapper module is ready to be used. Add it as a dependency of your main project that needs to communicate with API, and instantiate it just like in demo snippets.


## Supported exchanges
TODO! Currently under development :)

