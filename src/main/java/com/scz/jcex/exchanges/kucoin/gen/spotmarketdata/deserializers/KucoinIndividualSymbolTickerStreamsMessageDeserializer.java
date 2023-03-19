package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessage
 */
public class KucoinIndividualSymbolTickerStreamsMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinIndividualSymbolTickerStreamsMessage> {
  private final KucoinIndividualSymbolTickerStreamsMessageDataDeserializer kucoinIndividualSymbolTickerStreamsMessageDataDeserializer = new KucoinIndividualSymbolTickerStreamsMessageDataDeserializer();
  
  @Override
  public KucoinIndividualSymbolTickerStreamsMessage deserialize(JsonParser parser) throws IOException {
    KucoinIndividualSymbolTickerStreamsMessage msg = new KucoinIndividualSymbolTickerStreamsMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinIndividualSymbolTickerStreamsMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
