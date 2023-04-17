package com.scz.jcex.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jcex.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamMessage
 */
public class BinanceListStatusUserDataStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceListStatusUserDataStreamMessage> {
  
  @Override
  public BinanceListStatusUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceListStatusUserDataStreamMessage msg = new BinanceListStatusUserDataStreamMessage();
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
