# Comparison with OpenAPI

Both this project and [OpenAPI](https://www.openapis.org/) suggest an human readable API specifications. OpenAPI being widely adopted, you may prefer using it especially if you are in charge of the server side specifications of REST APIs.

JXAPI aims to write API wrappers in Java for any REST/Websocket API in a more flexible simpler way:
 * Flexible:
   * Supports [extensible data types](./ExchangeDescriptorFileDoc.md#flexible-data-structure-definition) in JSON DTOs
   * Better support for websocket API protocol management, with [multiplexing](./ExchangeDescriptorFileDoc.md#websocket-api-endpoints) and [specific hooks](./WebsocketHookDevGuide.md) to manage handshake and heartbeat specificites.
 * Simplicity:
   * Simple definition of a plugin in your wrapper module project's pom.xml to trigger code generation within Maven build cycle.
   * You can specify `objectName` of a generated API DTO Pojo and reuse it across APIs, when different enpoints reuse same data structure
   * You can specify interface class names API DTO Pojos implement
   * Write efficiently HTTP request interceptors to tune requests.

It should be possible to convert OpenAPI API specification file to JXAPI format. This may be a future feature of this project.

