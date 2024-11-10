package com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static com.scz.jxapi.util.JsonUtil.readNextBigDecimal;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload
 */
public class DemoExchangeMarketDataExchangeInfoResponsePayloadDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  
  @Override
  public DemoExchangeMarketDataExchangeInfoResponsePayload deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataExchangeInfoResponsePayload msg = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "minOrderSize":
        msg.setMinOrderSize(readNextBigDecimal(parser));
      break;
      case "orderTickSize":
        msg.setOrderTickSize(readNextBigDecimal(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
