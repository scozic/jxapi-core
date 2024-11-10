package com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static com.scz.jxapi.util.JsonUtil.readNextBigDecimal;
import static com.scz.jxapi.util.JsonUtil.readNextLong;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload
 */
public class DemoExchangeMarketDataTickersResponsePayloadDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataTickersResponsePayload> {
  
  @Override
  public DemoExchangeMarketDataTickersResponsePayload deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataTickersResponsePayload msg = new DemoExchangeMarketDataTickersResponsePayload();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "last":
        msg.setLast(readNextBigDecimal(parser));
      break;
      case "high":
        msg.setHigh(readNextBigDecimal(parser));
      break;
      case "low":
        msg.setLow(readNextBigDecimal(parser));
      break;
      case "volume":
        msg.setVolume(readNextBigDecimal(parser));
      break;
      case "time":
        msg.setTime(readNextLong(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
