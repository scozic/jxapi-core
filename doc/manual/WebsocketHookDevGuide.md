# Websocket hook development guide

<!-- BEGIN TABLE OF CONTENTS -->
  - [Websocket hook development guide](#websocket-hook-development-guide)
    - [WebsocketHook Methods](#websockethook-methods)
      - [`init(WebsocketClient websocketManager)`](#initwebsocketclient-websocketmanager)
      - [`beforeConnect() throws WebsocketException`](#beforeconnect-throws-websocketexception)
      - [`afterConnect() throws WebsocketException`](#afterconnect-throws-websocketexception)
      - [`beforeDisconnect() throws WebsocketException`](#beforedisconnect-throws-websocketexception)
      - [`afterDisconnect() throws WebsocketException`](#afterdisconnect-throws-websocketexception)
      - [`getSubscribeRequestMessage(String topic)`](#getsubscriberequestmessagestring-topic)
      - [`getUnSubscribeRequestMessage(String topic)`](#getunsubscriberequestmessagestring-topic)
      - [`getHeartBeatMessage()`](#getheartbeatmessage)

<!-- END TABLE OF CONTENTS -->

Using JXAPI, websocket endpoints are managed with a single websocket for an API group, all endpoints belonging to this API group will subscribe and receive messages using multiplexing on that socket. Websocket multiplexing means messages of different streams are disseminated on same physical websocket. Clients subscribes to a given stream or 'topic' by sending a subscription message. Incoming messages must be matched against existing subscriptions, see [websocket topic matching](./ExchangeDescriptorFileDoc.md#message-matching).
Notice there may be no multiplexing in which case API group should expose a single endpoint receiving all websocket messages.

In addition websocket API specifications may require sending specific messages after opening socket, for instance for authentication.
Also, a 'heart beat' feature may be specified to make sure the socket link is alive, by sending regular 'ping' messages to the server or answering incoming ones.

JXAPI manages heartbeat sending, automatic disconnecting after websocket error or no heartbeat response detected using [DefaultWebsocketClient](../../src/main/java/org/jxapi/netutils/websocket/DefaultWebsocketClient.java.java) ... 
But most likely some API websocket protocol specificities have to be implemented. This is made as simple as possible by implementing [WebsocketHook](../../src/main/java/org/jxapi/netutils/websocket/WebsocketHook.java) interface.

A sample implementation can be found in [DemoExchangeWebsocketHook](../../src/test/java/org/jxapi/exchanges/demo/net/DemoExchangeWebsocketHook.java)

`WebsocketHook` instances are created using a [WebsocketHookFactory](../../src/main/java/org/jxapi/netutils/websocket/WebsocketHookFactory.java). This factory class must have a public constructor to be instantiated by reflection. Its full class name must be provided in exchange descriptor `websocketHookFactory` property of each defined Websocket client in `network\websocketClients` (see [network](./ExchangeDescriptorFileDoc.md#network) ) section of exchange descriptor.

## WebsocketHook Methods

### `init(WebsocketClient websocketManager)`

Called after the websocket manager has been initialized, to bind this hook to the manager. This is where configuration that remains unchanged can be performed, such as subscribing 'technical' message listeners or customizing the manager's configuration like heartbeat timeout, no message timeout, or delay before reconnection.

### `beforeConnect() throws WebsocketException`

Called just before connecting the websocket. This method can be used to append a token to the base URL or refresh the token using a REST API method before connecting. If an error occurs during the connection process, a `WebsocketException` is thrown, and the connection process will fail.

### `afterConnect() throws WebsocketException`

Called just after connecting the websocket. This method is the right place to send a specific message for authentication right after the connection. If an error occurs during the post-connection process, a `WebsocketException` is thrown, and the connection will be closed.

### `beforeDisconnect() throws WebsocketException`

Called just before disconnecting the websocket. This is where any cleanup or final message sending should be performed before disconnecting. If an error occurs during the disconnection process, a `WebsocketException` is thrown.

### `afterDisconnect() throws WebsocketException`

Called just after disconnecting the websocket. This is where any cleanup or final message sending should be performed after disconnecting. If an error occurs during the post-disconnection process, a `WebsocketException` is thrown.

### `getSubscribeRequestMessage(String topic)`

Gets the message to send to subscribe to a topic. This method must be overridden when the API protocol supports multiplexing and requires sending a specific message to subscribe to a topic. Returns the message to send to subscribe to the topic, or `null` if no message is required.

### `getUnSubscribeRequestMessage(String topic)`

Gets the message to send to unsubscribe from a topic. This method must be overridden when the API protocol supports multiplexing and requires sending a specific message to unsubscribe from a topic. Returns the message to send to unsubscribe from the topic, or `null` if no message is required.

### `getHeartBeatMessage()`

Gets the message to send to keep the connection alive. This method must be overridden when the API protocol requires sending regular 'heartbeat' messages to keep the connection alive. Returns the message to send to keep the connection alive, or `null` if no heartbeat is required.








