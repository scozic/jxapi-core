package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextBigDecimal;
import static org.jxapi.util.JsonUtil.readNextObject;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class DemoExchangeMarketDataExchangeInfoResponsePayloadDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  
  @Override
  public DemoExchangeMarketDataExchangeInfoResponsePayload deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataExchangeInfoResponsePayload msg = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "symbol":
        msg.setSymbol(readNextString(parser));
      break;
      case "minOrderSize":
        msg.setMinOrderSize(readNextBigDecimal(parser));
      break;
      case "orderTickSize":
        msg.setOrderTickSize(readNextBigDecimal(parser));
      break;
      case "blob":
        msg.setBlob(readNextObject(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
