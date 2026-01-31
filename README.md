
# JXAPI

A powerful code generation tool for creating Java REST and WebSocket API client SDKs from JSON or YAML descriptor files.

<!-- BEGIN TABLE OF CONTENTS -->
<!-- END TABLE OF CONTENTS -->

## Overview

JXAPI generates comprehensive Java client libraries for REST and WebSocket APIs from simple JSON or YAML descriptor files. Originally designed for exchange APIs, it works with any REST or WebSocket API.

**Key capabilities:**
- Generate Java POJOs for all data structures
- Create complete REST API client wrappers 
- Build WebSocket API clients with advanced features
- Support for complex API requirements (rate limiting, authentication, multiplexing)
- Generate standalone demo code and documentation

You can generate only POJOs if needed - see the [POJO only generation](./doc/manual/PojoOnlyGeneration.md) guide.

## Why Choose JXAPI?

While [OpenAPI](https://www.openapis.org/) is widely adopted and may be preferable for APIs with existing OpenAPI specifications, JXAPI excels in specific scenarios:

- **APIs without OpenAPI specifications** - Many existing APIs lack proper OpenAPI documentation
- **WebSocket API support** - Essential for real-time data APIs like financial exchanges
- **Java-optimized generation** - Specifically tailored for the Java ecosystem

See the [OpenAPI comparison](./doc/manual/ComparisonWithOpenAPI.md) for detailed differences.

### Key features

**🚀 Advanced Serialization**
- Custom Jackson serializers/deserializers for optimal performance
- Support for mixed data types (numeric/boolean as strings or native types)
- Flexible field mapping between POJOs and JSON
- Reusable POJO definitions across endpoints

**🔒 Production-Ready Features**
- **Rate Limiting**: Built-in enforcement prevents API bans
- **Authentication**: Custom HTTP request interceptors for complex auth flows
- **Pagination**: Automatic handling of paginated responses
- **Observability**: Built-in monitoring for metrics

**🌐 WebSocket Excellence**
- Multiplexed stream support on shared connections
- Automatic heartbeat and connection management
- Custom protocol handshake support

**📁 Developer Experience**
- **Modular Definitions**: Split large APIs into maintainable files
- **Logical Organization**: Group endpoints into functional categories  
- **Constants & Configuration**: Generated classes for API constants and config properties
- **Demo Snippets**: Ready-to-run examples for every endpoint
- **Auto Documentation**: Generated README with complete API reference

See detailed documentation: [Constants](./doc/manual/ExchangeDescriptorFileDoc.md#constants) | [Configuration](./doc/manual/ExchangeDescriptorFileDoc.md#configuration-properties) | [WebSocket Endpoints](./doc/manual/ExchangeDescriptorFileDoc.md#websocket-api-endpoints) | [Rate Limiting](./doc/manual/ExchangeDescriptorFileDoc.md#api-request-rate-limit)

## Getting Started

JXAPI transforms JSON/YAML API descriptors into full-featured Java client libraries. The process is straightforward:

1. **Describe your API** in JSON or YAML (similar to OpenAPI format)
2. **Generate the wrapper** with Maven integration
3. **Add minimal custom code** for API-specific features:
   - Authentication logic (API key/secret handling)
   - Request formatting (query vs. body parameters)
   - WebSocket protocol handshakes

**Benefits:**
- Eliminates manual POJO creation and HTTP client boilerplate
- Handles complex features like rate limiting and WebSocket multiplexing
- Generates comprehensive demos and documentation
- Ideal for APIs with many endpoints or real-time requirements

**AI-Friendly:** Descriptor files can be efficiently generated from API documentation using AI assistants.

### 1. Project Setup

Create a new Java/Maven project and configure the JXAPI dependency and generation plugin in your `pom.xml`. See the complete [Wrapper Module Setup](doc/manual/WrapperModuleSetup.md) guide for detailed instructions.

### 2. Implement Custom Hooks (Optional)

Write minimal Java code to handle API-specific protocol requirements:

**For REST APIs:** Implement `HttpRequestInterceptorFactory` to handle:
- Authentication headers and signatures
- Request preprocessing
- Custom error handling

**For WebSocket APIs:** Implement `WebsocketHookFactory` to handle:
- Protocol handshakes
- Connection authentication
- Custom message formatting

See development guides: [HTTP Request Interceptors](./doc/manual/HttpRequestInterceptorDevGuide.md) | [WebSocket Hooks](./doc/manual/WebsocketHookDevGuide.md)

### 3. Create API Descriptor Files

Create `.yaml` or `.json` descriptor files in `src/main/jxapi/exchange/` to define your API endpoints, data structures, and configuration.

**💡 Pro Tip:** Use AI assistants to convert API documentation into descriptor files efficiently!

**Development Workflow:**
1. Write descriptors incrementally (one endpoint at a time)
2. Run `mvn jxapi:generate-exchange` goal to generate code
3. Test with generated demo snippets
4. Repeat until complete

This generates:
- Wrapper code in `target/generated-sources/jxapi/`
- Demo snippets in `target/generated-test-sources/jxapi/`
- Documentation in `_MyExchangeREADME.md_`

See the [Exchange Descriptor Documentation](./doc/manual/ExchangeDescriptorFileDoc.md) for complete syntax reference.

### Generated Code Structure

**Core Components:**
- **Exchange Interface** - Main entry point with API group accessors
- **Exchange Implementation** - Configured with `Properties` (API keys, secrets, etc.)
- **API Group Interfaces** - Logical groupings of related endpoints
- **Async REST Methods** - Return `FutureRestResponse` (extends `Future`)
   * REST API methods take a request object as a parameter and a response object as a returned type which are generated Java POJOs carrying properties corresponding to parameters defined in the JSON file.
   * One _subscribe_ and one _unsubscribe_ method to subscribe/unsubscribe to Websocket endpoint topics. Subscription and stream message parameters are carried in generated POJOs.
 * Generated POJOs for each endpoint request, response, or message. Such POJOs implement multiple features like exposing builder classes, see [Generated Java POJOs](./doc/manual/GeneratedJavaPojos.md).
 * Demo snippets in `src/test/java` to test call to REST endpoints and websocket endpoint subscription, see [Demos](#demo-snippets).
 * A sample README.md file (name is prefixed with exchange name) that documents the wrapper and exposed API groups and nested endpoints.

### 4. Test with Demo Snippets

Each endpoint gets a runnable demo class in `target/generated-test-sources/jxapi` with sample requests and logging.

**REST Demos:** Send requests with sample data and log responses
**WebSocket Demos:** Subscribe for 30 seconds (configurable) and log messages

**Configuration:**
1. Copy `demo-<yourExchange>.properties.dist` to `demo-<yourExchange>.properties`
2. Add API credentials and customize request parameters
3. Add the properties file to `.gitignore`

The configuration includes:
- API credentials (keys, secrets)
- Common settings (timeouts, etc.) - see [CommonConfigProperties](./src/main/java/org/jxapi/exchange/CommonConfigProperties.java)
- Demo-specific settings (`jxapi.demo.*` prefix)
- Per-endpoint sample parameters

### Generated Documentation

A complete `<yourExchange>_README.md` is generated containing:
- Wrapper overview and configuration guide
- API constants and configuration properties
- Detailed endpoint documentation for each API group
- Usage examples and integration instructions

Use this as your project's main README or reference it from your existing documentation.

### 5. Integration

Once demos run successfully, add your wrapper as a dependency to your main project and instantiate it following the demo patterns.

See the complete [Using the Wrapper](./doc/manual/UsingTheWrapper.md) guide for integration examples and best practices.

## Supported Exchanges
[jxapi-kucoin](https://github.com/scozic/jxapi-kucoin): KuCoin exchange API wrapper generated with JXAPI.
