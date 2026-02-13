# DemoExchange API Java wrapper

Demo Exchange. This exchange uses connects to mock HTTP server and websocket server. It is used to test and validate a full Java wrapper generated using JXAPI.
<!-- BEGIN TABLE OF CONTENTS -->
  - [DemoExchange API Java wrapper](#demoexchange-api-java-wrapper)
      - [Quick start](#quick-start)
      - [Properties](#properties)
      - [Constants](#constants)
    - [API Groups](#api-groups)
      - [MarketData](#marketdata)
        - [REST endpoints](#rest-endpoints)
        - [Websocket endpoints](#websocket-endpoints)
    - [Demo snippets](#demo-snippets)
      - [Endpoint demo snippets](#endpoint-demo-snippets)
        - [MarketData REST endpoints demo snippets:](#marketdata-rest-endpoints-demo-snippets)
        - [MarketData Websocket endpoints demo snippets](#marketdata-websocket-endpoints-demo-snippets)

<!-- END TABLE OF CONTENTS -->
See <a href="https://docs.myexchange.com/api">reference documentation</a>
### Quick start

This wrapper offers Java interfaces for using DemoExchange API
Add maven dependency to your project, then you can use the wrapper by instantiating <a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/DemoExchangeExchange.html">DemoExchangeExchange</a> in your code:
``` java
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;

public void call() {
  Properties properties = new Properties();
  // Set relevant configuration properties (see documentation) in 'props'
  DemoExchangeExchange   exchange = new DemoExchangeExchangeImpl(properties);
  // Access API groups and their endpoints through 'exchange' methods.
}
```
You may have a look at <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataExchangeInfoDemo.java">DemoExchangeMarketDataExchangeInfoDemo</a> class for full usage example

### Properties

<table>
  <caption>Configuration properties</caption>
  <tr>
    <th>Name</th>
    <th>Type</th>
    <th>description</th>
    <th>Default value</th>
  </tr>
  <tr>
    <td>baseHttpUrl</td>
    <td>STRING</td>
    <td>Mock HTTP server base API URL</td>
    <td>http://localhost:8080</td>
  </tr>
  <tr>
    <td>baseWebsocketUrl</td>
    <td>STRING</td>
    <td>Mock websocket server base API URL</td>
    <td>ws://localhost:8090</td>
  </tr>
  <tr>
    <td>ws</td>
    <td>group</td>
    <td colspan="2">Mock websocket server related properties</td>
  </tr>
  <tr>
    <td>ws.websocketHeartBeatInterval</td>
    <td>INT</td>
    <td>Mock websocket server expected heartBeat interval</td>
    <td>-1</td>
  </tr>
</table>
Some demo configuration properties are available to tune common request parameters used in demo snippets, as <a href="./src/test/java/org/jxapi/exchanges/demo/gen/DemoExchangeDemoProperties.java">DemoExchangeDemoProperties</a> class.
 These properties are used to configure default values for request parameters used in demo snippets.

In order to run demo snippets, you can uncomment and set properties values in __demo-DemoExchange.properties__ properties file you create from .dist template in src/test/resources folder.


### Constants

Some useful constants are defined in <a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/DemoExchangeConstants.html">DemoExchangeConstants</a>

## API Groups
APIs are available using the following interfaces accessible from <a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/DemoExchangeExchange.html">DemoExchangeExchange</a> interface

### MarketData
Methods exposed in <a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html">DemoExchangeMarketDataApi</a> accessible from <a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/DemoExchangeExchange#getDemoExchangeMarketDataApi().html">DemoExchangeExchange#getDemoExchangeMarketDataApi()</a>

Demo exchange market data API

#### REST endpoints

<table>
  <caption>DemoExchange MarketData REST endpoints</caption>
  <tr>
    <th>Endpoint</th>
    <th>Description</th>
    <th>API Reference</th>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#exchangeInfo(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest)">exchangeInfo</a></td>
    <td>Fetch market information of symbols that can be traded</td>
    <td><a href="https://docs.myexchange.com/api/rest/marketData/exchangeInfo">link</a></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#tickers()">tickers</a></td>
    <td>Fetch current tickers for all markets</td>
    <td></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#postRestRequestDataTypeInt(java.lang.Integer)">postRestRequestDataTypeInt</a></td>
    <td>A sample REST endpoint using INT response data type</td>
    <td></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#getRestRequestDataTypePrimitiveWithMsgField(java.lang.Integer)">getRestRequestDataTypePrimitiveWithMsgField</a></td>
    <td>A sample REST endpoint using GET (hence url query params) primitive request type, with 'msgField' property defined. That msgField value should be used as query param argument name</td>
    <td></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#postRestRequestDataTypeIntList(java.util.List)">postRestRequestDataTypeIntList</a></td>
    <td>A sample REST endpoint using INT_LIST request data type</td>
    <td></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#postRestRequestDataTypeObjectListMap(java.util.Map)">postRestRequestDataTypeObjectListMap</a></td>
    <td>A sample REST endpoint using OBJECT_LIST_MAP request data type</td>
    <td></td>
  </tr>
</table>

#### Websocket endpoints

<table>
  <caption>DemoExchange MarketData websocket endpoints</caption>
  <tr>
    <th>Subscription method</th>
    <th>Description</th>
    <th>API Reference</th>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/demo/gen/marketdata/DemoExchangeMarketDataApi.html#subscribeTickerStream(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest)">subscribeTickerStream</a></td>
    <td>Subscribe to ticker stream</td>
    <td><a href="https://docs.myexchange.com/api/ws/marketData/tickerStream">link</a></td>
  </tr>
</table>

## Demo snippets

This wrapper contains demo snippets for the most important endpoints. These snippets are generated in _src/test/java/_ source folder.

Some demo configuration properties are available to tune common request parameters used in snippets, as <a href="./src/test/java/org/jxapi/exchanges/demo/gen/DemoExchangeDemoProperties.java">DemoExchangeDemoProperties</a> class.
 These properties are used to configure default values for request parameters used in demo snippets.

In order to run demo snippets, you can set properties values in __demo-DemoExchange.properties__ properties file in src/test/resources folder.


### Endpoint demo snippets


#### MarketData REST endpoints demo snippets:

 - __exchangeInfo__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataExchangeInfoDemo.java">DemoExchangeMarketDataExchangeInfoDemo</a>
 - __tickers__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataTickersDemo.java">DemoExchangeMarketDataTickersDemo</a>
 - __postRestRequestDataTypeInt__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataPostRestRequestDataTypeIntDemo.java">DemoExchangeMarketDataPostRestRequestDataTypeIntDemo</a>
 - __getRestRequestDataTypePrimitiveWithMsgField__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataGetRestRequestDataTypePrimitiveWithMsgFieldDemo.java">DemoExchangeMarketDataGetRestRequestDataTypePrimitiveWithMsgFieldDemo</a>
 - __postRestRequestDataTypeIntList__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataPostRestRequestDataTypeIntListDemo.java">DemoExchangeMarketDataPostRestRequestDataTypeIntListDemo</a>
 - __postRestRequestDataTypeObjectListMap__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataPostRestRequestDataTypeObjectListMapDemo.java">DemoExchangeMarketDataPostRestRequestDataTypeObjectListMapDemo</a>

#### MarketData Websocket endpoints demo snippets

 - __tickerStream__: <a href="./src/test/java/org/jxapi/exchanges/demo/gen/marketdata/demo/DemoExchangeMarketDataTickerStreamDemo.java">DemoExchangeMarketDataTickerStreamDemo</a>
