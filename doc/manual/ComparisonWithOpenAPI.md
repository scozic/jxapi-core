# Comparison with OpenAPI

Both this project and [OpenAPI](https://www.openapis.org/) provide human-readable API specifications. Since OpenAPI is widely adopted, you may prefer using it, especially if you are responsible for the server-side specifications of REST APIs.

JXAPI has been designed to create wrappers for existing APIs that do not provide OpenAPI specifications, or that expose websocket endpoints, which is common for real-time data APIs like financial exchanges. Additionally, JXAPI is specifically tailored for Java code generation, providing optimized Java client libraries with features that leverage the Java ecosystem more effectively than general-purpose code generators.

In these situations, JXAPI offers several advantages:

* **Flexible data type serialization**: JSON serialization/deserialization supports key features such as:
  * Custom Jackson serializer and deserializer classes are generated, offering faster deserialization
  * Deserialization supports numeric or boolean data provided either as native types or as JSON strings
  * You can specify different field names in DTO POJOs than in serialized JSON, which is useful for APIs that use shorter field names to save bandwidth
  * Other JXAPI POJO features: Specifying `objectName` for POJOs reused across endpoints

* **Custom HTTP request interceptors**: These can be used to implement authentication challenges and other request processing logic

* **WebSocket multiplexed stream support**: Handle multiple concurrent WebSocket streams efficiently

* **HTTP request rate limiting**: Public APIs always have rate limits, and exceeding them can result in serious consequences such as IP banning. JXAPI wrappers enforce these limits by rejecting or throttling requests that would breach the limits

* **Built-in pagination support**: Many APIs use pagination for large datasets. JXAPI wrappers can handle pagination automatically, fetching subsequent pages without requiring clients to implement specific pagination logic

* **Modular API definitions**: Split large API specifications into multiple documents. For APIs with many endpoints, this is easier to maintain than a single large JSON or YAML file. Writing one file per endpoint based on the corresponding documentation page can be done more efficiently with AI assistants

* **Endpoint organization**: Group endpoints into logical categories, creating cleaner code with separate sub-interfaces for each functional area

* **Constant definitions**: Define constants in endpoint descriptions or default values. The generated code provides a class with static final fields for these constants, organized into logical groups (for instance, all possible values for an enumerated request field), making the code more maintainable

* **Configuration property definitions**: Declare configuration properties like API keys and secrets in the API specification. A dedicated class is generated for easier configuration management. Properties can also be organized into logical groups

**Observability**: Monitor client API usage using observability API.

**Demo snippets**: Generated wrapper comes with standalone demo snippets to try sending a request and logging its response  for each REST endpoint, or subscription to websocket endpoint. 



