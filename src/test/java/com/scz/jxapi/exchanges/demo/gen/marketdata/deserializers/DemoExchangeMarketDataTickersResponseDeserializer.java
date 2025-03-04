package com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import javax.annotation.processing.Generated;
import static com.scz.jxapi.util.JsonUtil.readNextInteger;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse instances
 * @see com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerGenerator")
public class DemoExchangeMarketDataTickersResponseDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataTickersResponse> {
  private final MapJsonFieldDeserializer<DemoExchangeMarketDataTickersResponsePayload> payloadDeserializer = new MapJsonFieldDeserializer<>(new DemoExchangeMarketDataTickersResponsePayloadDeserializer());
  
  @Override
  public DemoExchangeMarketDataTickersResponse deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataTickersResponse msg = new DemoExchangeMarketDataTickersResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "responseCode":
        msg.setResponseCode(readNextInteger(parser));
      break;
      case "payload":
        parser.nextToken();
        msg.setPayload(payloadDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
