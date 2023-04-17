package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage
 */
public class KucoinAllSymbolsTickerStreamMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinAllSymbolsTickerStreamMessage> {
  private final KucoinAllSymbolsTickerStreamMessageDataDeserializer kucoinAllSymbolsTickerStreamMessageDataDeserializer = new KucoinAllSymbolsTickerStreamMessageDataDeserializer();
  
  @Override
  public KucoinAllSymbolsTickerStreamMessage deserialize(JsonParser parser) throws IOException {
    KucoinAllSymbolsTickerStreamMessage msg = new KucoinAllSymbolsTickerStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(readNextLong(parser));
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinAllSymbolsTickerStreamMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
