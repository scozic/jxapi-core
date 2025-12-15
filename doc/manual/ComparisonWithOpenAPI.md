# Comparison with OpenAPI

Both this project and [OpenAPI](https://www.openapis.org/) suggest an human readable API specifications. OpenAPI being widely adopted, you may prefer using it especially if you are in charge of the server side specifications of REST APIs.

JXAPI aims to write API wrappers in Java for any REST/Websocket API in a more flexible simpler way:
 * Flexible:
   * Supports [extensible data types](./ExchangeDescriptorFileDoc.md#flexible-data-structure-definition) in JSON DTOs
   * Better support for websocket API protocol management, with [multiplexing](./ExchangeDescriptorFileDoc.md#websocket-api-endpoints) and [specific hooks](./WebsocketHookDevGuide.md) to manage handshake and heartbeat specificites.
 * Simplicity:
   * Simple definition of a plugin in your wrapper module project's pom.xml to trigger code generation within Maven build cycle.
   * Write efficiently HTTP request interceptors to tune requests.
 * Helpful POJO generation and JSON serialization features:
   * You can specify interface class names API DTO Pojos implement
   * The `msgField` property to specify a 'serialized' name for the field, so a field in POJ
   * You can specify `objectName` of a generated API DTO Pojo and reuse it across APIs, when different enpoints reuse same data structure. It can be also a the name of a class managed externally.
   * Supports numerical values encoded either as JSON String or numeric.
   * Generated JSON serializers and deserializers allow for efficient serialization/deserialization

It should be possible to convert OpenAPI API specification file to JXAPI format. This may be a future feature of this project.

