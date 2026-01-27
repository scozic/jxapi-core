# Comparison with OpenAPI

The role of the generator is similar to [openapi-maven-plugin](https://openapi-generator.tech/docs/plugins/#maven) but with a different approach: JXAPI is focused on Java wrappers for not only REST but also Websocket APIs, with support for complex API features like request rate limits, websocket multiplexing, heartbeat management, etc. See below for more details.

Since [OpenAPI](https://www.openapis.org/) is widely adopted, you may prefer using it, especially if you are responsible for the server-side specifications of REST APIs, or you are implementing a client SDK for an API that already provides OpenAPI specifications.

JXAPI has been designed to create wrappers for existing APIs that do not provide OpenAPI specifications, or that expose websocket endpoints, which is common for real-time data APIs like financial exchanges. Additionally, JXAPI is specifically tailored for Java code generation, providing optimized Java client libraries with features that leverage the Java ecosystem more effectively.

In these situations, JXAPI offers several advantages:

* **Flexible data type serialization**: JSON serialization/deserialization supports key features such as:
  * Custom Jackson serializer and deserializer classes are generated, offering faster deserialization
  * Deserialization supports numeric or boolean data provided either as native types or as JSON strings
  * You can specify different field names in DTO POJOs than in serialized JSON, which is useful for APIs that use shorter field names to save bandwidth
  * Other JXAPI POJO features: Specifying `objectName` for POJOs reused across endpoints

* **Custom HTTP request interceptors**: These can be used to implement authentication challenges and other request processing logic

* **WebSocket multiplexed stream support**: Handle multiple concurrent WebSocket streams on shared socket efficiently. See [WebSocket API endpoints](./doc/manual/ExchangeDescriptorFileDoc.md#websocket-api-endpoints) for more details

* **HTTP request rate limiting**: Public APIs always have rate limits, and exceeding them can result in serious consequences such as IP banning. JXAPI wrappers enforce these limits by rejecting or throttling requests that would breach the limits. See [HTTP Request Rate Limiting](./doc/manual/ExchangeDescriptorFileDoc.md#api-request-rate-limit) for more details

* **Built-in pagination support**: Many APIs use pagination for large datasets. JXAPI wrappers can handle pagination automatically, fetching subsequent pages without requiring clients to implement specific pagination logic. See [REST Request Pagination](./doc/manual/RestRequestPagination.md) for more details

* **Modular API definitions**: Split large API specifications into multiple documents. For APIs with many endpoints, this is easier to maintain than a single large JSON or YAML file. Writing one file per endpoint based on the corresponding documentation page can be done more efficiently with AI assistants. See [Splitting large exchange definitions into multiple files](./doc/manual/ExchangeDescriptorFileDoc.md#splitting-large-exchange-definitions-into-multiple-files) for more details.

* **Endpoint organization**: Group endpoints into logical categories, creating cleaner code with separate sub-interfaces for each functional area

* **Constant definitions**: Define constants in endpoint descriptions or default values. The generated code provides a class with static final fields for these constants, organized into logical groups (for instance, all possible values for an enumerated request field), making the code more maintainable. See [Constants](./doc/manual/ExchangeDescriptorFileDoc.md#constants) for more details. See [Using the Wrapper](./doc/manual/UsingTheWrapper.md#constants) for how to use them.

* **Configuration property definitions**: Declare configuration properties like API keys and secrets in the API specification. A dedicated class is generated for easier configuration management. Properties can also be organized into logical groups. See [Configuration Properties](./doc/manual/ExchangeDescriptorFileDoc.md#configuration-properties) for more details. See [Using the Wrapper](./doc/manual/UsingTheWrapper.md#configuration-properties) for how to use them.

* **Observability**: Monitor client API usage using observability API. See [Observability](./doc/manual/UsingTheWrapper.md#observability) for more details.

* **Demo snippets**: Generated wrapper comes with standalone demo snippets to try sending a request and logging its response  for each REST endpoint, or subscription to websocket endpoint. This is useful to test the wrapper and understand how to use it. See [Demo Snippets](./doc/manual/UsingTheWrapper.md#demo-snippets) for more details.
