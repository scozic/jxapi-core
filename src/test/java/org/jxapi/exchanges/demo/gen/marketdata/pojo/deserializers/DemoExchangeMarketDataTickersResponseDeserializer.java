package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class DemoExchangeMarketDataTickersResponseDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataTickersResponse> {
  private MapJsonFieldDeserializer<DemoExchangeMarketDataTickersResponsePayload> payloadDeserializer;
  
  @Override
  public DemoExchangeMarketDataTickersResponse deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataTickersResponse msg = new DemoExchangeMarketDataTickersResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "responseCode":
        msg.setResponseCode(readNextInteger(parser));
      break;
      case "payload":
        parser.nextToken();
        if(payloadDeserializer == null) {
          payloadDeserializer = new MapJsonFieldDeserializer<>(new DemoExchangeMarketDataTickersResponsePayloadDeserializer());
        }
        msg.setPayload(payloadDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
