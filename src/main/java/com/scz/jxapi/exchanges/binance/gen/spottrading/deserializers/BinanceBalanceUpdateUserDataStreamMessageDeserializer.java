package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage
 */
public class BinanceBalanceUpdateUserDataStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceBalanceUpdateUserDataStreamMessage> {
  
  @Override
  public BinanceBalanceUpdateUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceBalanceUpdateUserDataStreamMessage msg = new BinanceBalanceUpdateUserDataStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.setEventType(parser.nextTextValue());
      break;
      case "E":
        msg.setEventTime(readNextLong(parser));
      break;
      case "a":
        msg.setAsset(parser.nextTextValue());
      break;
      case "d":
        msg.setBalanceDelta(readNextBigDecimal(parser));
      break;
      case "T":
        msg.setClearTime(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
