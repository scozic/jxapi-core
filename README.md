
# JXAPI

Code generation tools to generate a Java REST and/or Websocket API wrapper (SDK) efficiently.

<!-- BEGIN TABLE OF CONTENTS -->
<!-- END TABLE OF CONTENTS -->

## Introduction
Many web services are consumed as HTTP REST/Websocket APIs. Those APIs disseminate data as structured JSON objects.
JXAPI is for generating Java code to call those REST and Websocket APIs using a simple function with a Java POJO as request and response/message data.

The generator will take as input a JSON or YAML file describing APIs, and generate request/response POJOs, Java interfaces to REST APIs and Websockets and their implementation, and also demo snippets and a documentation skeleton.
You will also need to write a few lines of code for API-specific implementation aspects like:
 * Authentication challenge (like computing authorization header added requests using API key/secret and request parameters)
 * Request parameter formatting (use as query parameters or request body parameters)
 * Websocket API protocol specific handshake.

The descriptor files are quite similar to [OpenAPI](https://www.openapis.org/) specifications. Read [here](./doc/manual/ComparisonWithOpenAPI.md) for a comparison with that project.

That can be achieved easily by specifying implementation REST+Websocket endpoint factories using hooks, check the wrapper development guide below.

The JSON API descriptor file structure is simple and can be AI-generated from online API documentation.
Using JXAPI to create a Java wrapper saves the time to write POJOs for endpoint request/response/messages DTOs, HTTP request management, request rate limits, and avoid issues that come with manually writing the code. The websocket endpoint management allows efficient implementation of multiplexing (subscribing to distinct streams on a single physical websocket), heartbeat, and handshake management. It is particularly efficient for using complex APIs with many endpoints.

Follow the guide below to write such an API wrapper.

## Project Bootstrap

See [Wrapper module Setup](doc/manual/WrapperModuleSetup.md): Basically, you just initialize a Java/Maven project, and add to the Maven `pom.xml` file a dependency to this project plus a plugin to run the generation of Java source files within the Maven build cycle.

## Manage REST/Websocket API protocol specificites using custom hooks
A few lines of Java code have to be written manually to manage API protocol specificities like authentication challenges.
See dedicated guides:
 * If your wrapper exposes REST endpoints: You may set `httpRequestInterceptorFactory` property with full class name of [HttpRequestInterceptorFactory](./src/main/java/org/jxapi/netutils/rest/HttpRequestInterceptorFactory.java) that creates specific hooks for HTTP requests at the exchange or API group level. Such hooks are used to modify a request before it is sent, for instance adding it specific headers, as explained in [HttpRequestInterceptorDevGuide](./doc/manual/HttpRequestInterceptorDevGuide.md)
 * If your wrapper exposes websocket endpoints, you should set in `websocketHookFactory` property of the exchange descriptor file at the exchange or API group level with the full name of the class implementing [WebsocketHookFactory](./src/main/java/org/jxapi/netutils/websocket/WebsocketHookFactory.java) that creates specific hooks for the websocket protocol of your API, see [WebsocketHookDevGuide](./doc/manual/WebsocketHookDevGuide.md).

## Write Descriptor File for API Descriptor
Now the tedious work is done, let's create some API wrappers.
You must create either a `.yaml` or `.json` exchange descriptor file in the `src/main/jxapi/exchange` folder of your module.
Follow [Exchange descriptor file documentation](./doc/manual/ExchangeDescriptorFileDoc.md) guide to convert API documentation into such files.

AI assistants can be efficient to write such files &#128521;.

Running the `mvn exec:java` command triggers the generation of wrapper code in the `src/main/java` folder, demos in `src/test/java`, and a sample `_MyExchangeREADME.md_` file at the root of the module project.
II recommend writing the API descriptor file incrementally and running the generator after adding an endpoint to run the demo snippet (see [Demos](#demo-snippets)) to ensure it works as expected.

## Resulting Generated Code
The resulting generated code includes:
 * Exchange interface, with a getter function to retrieve any exchange API.
 * Exchange interface implementation, with a constructor expecting a single `java.util.Properties` argument containing API configuration parameters like API KEY/Secret.
 * Exchange API interface for each exchange API defined in the root exchange, and implementation of that interface with:
   * One function for each REST API. Calls are performed asynchronously and return an instance of `FutureRestResponse` which is a `java.util.concurrent.Future` object.
   * REST API methods take a request object as a parameter and a response object as a returned type which are generated Java POJOs carrying properties corresponding to parameters defined in the JSON file.
   * One _subscribe_ and one _unsubscribe_ method to subscribe/unsubscribe to Websocket endpoint topics. Subscription and stream message parameters are carried in generated POJOs.
 * Generated POJOs for each endpoint request, response, or message. Such POJOs implement multiple features like exposing builder classes, see [Generated Java POJOs](./doc/manual/GeneratedJavaPojos.md).
 * Demo snippets in `src/test/java` to test call to REST endpoints and websocket endpoint subscription, see [Demos](#demo-snippets).
 * A sample README.md file (name is prefixed with exchange name) that documents the wrapper and exposed API groups and nested endpoints.

### Demo Snippets

Generated source files include demo snippets to test each REST and Websocket endpoint, generated as classes with `public static void main(String[] args)` method in the `src/test/java` source folder of the project module.

When run, REST endpoint snippets will issue a request built using fields sample values from the descriptor file, then wait for a response and log it at INFO level.

Websocket endpoint snippets will subscribe to the corresponding endpoint with a subscription request built using fields sample values from the descriptor file, and wait for some delay (configurable, default is 30s) before unsubscribing and exiting. Incoming messages will be logged at INFO level.

Generated wrapper configuration properties are loaded from the file `src/test/resources/demo-<yourExchange>.properties`. The generator will create a sample value of this file as `src/test/resources/demo-<yourExchange>.properties.dist` you should create a copy of, removing the `.dist` suffix. This `demo-<yourExchange>.properties` file should be added to the `.gitignore` file in case it contains sensitive information like API key/secret.
Notice this file carries not only wrapper-specific properties but also common properties like `jxapi.httpRequestTimeout` (maximum timeout to wait for REST endpoint call response), see [CommonConfigProperties](./src/main/java/com/scz/jxapi/exchange/CommonConfigProperties.java), and demo snippet-specific properties like `jxapi.demo.ws.subscriptionDuration` that controls the duration in ms of the subscription in websocket endpoint demo snippets. Also, for each parameter of each endpoint request created in demo snippets, a 'demo' specific configuration property is created and used in generated snippet code to tune the request sent. You may uncomment and customize any of the _demo.*_ prefixed properties.

### Sample README.md

A `<yourExchange>_README.md` file is also generated with the wrapper, with documentation describing:
 * The wrapper from its description in the descriptor.
 * Constants.
 * Configuration properties.
 * Description of each API group from the description in the descriptor file.
 * Description of each REST and Websocket endpoint of each API group.

This file can be used as the _README.md_ file of the project, or referenced from it.

## Using the Wrapper

When demo snippets successfully run, your wrapper module is ready to be used. Add it as a dependency of your main project that needs to communicate with the API, and instantiate it just like in demo snippets. See [Using the wrapper](./doc/manual/UsingTheWrapper.md) guide.

## Supported Exchanges
TODO! Currently under development :)
