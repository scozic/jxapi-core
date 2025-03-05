package com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import javax.annotation.processing.Generated;
import static com.scz.jxapi.util.JsonUtil.readNextInteger;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse instances
 * @see com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerGenerator")
public class DemoExchangeMarketDataExchangeInfoResponseDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataExchangeInfoResponse> {
  private final ListJsonFieldDeserializer<DemoExchangeMarketDataExchangeInfoResponsePayload> payloadDeserializer = new ListJsonFieldDeserializer<>(new DemoExchangeMarketDataExchangeInfoResponsePayloadDeserializer());
  
  @Override
  public DemoExchangeMarketDataExchangeInfoResponse deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataExchangeInfoResponse msg = new DemoExchangeMarketDataExchangeInfoResponse();
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
