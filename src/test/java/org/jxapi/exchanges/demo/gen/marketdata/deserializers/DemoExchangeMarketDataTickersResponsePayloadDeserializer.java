package org.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextBigDecimal;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload
 */
@Generated("org.jxapi.generator.java.pojo.JsonMessageDeserializerGenerator")
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
