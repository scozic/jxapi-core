package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message
 */
public class KucoinRealTimeSymbolTickerV2MessageDeserializer extends AbstractJsonMessageDeserializer<KucoinRealTimeSymbolTickerV2Message> {
  private final KucoinRealTimeSymbolTickerV2MessageDataDeserializer kucoinRealTimeSymbolTickerV2MessageDataDeserializer = new KucoinRealTimeSymbolTickerV2MessageDataDeserializer();
  
  @Override
  public KucoinRealTimeSymbolTickerV2Message deserialize(JsonParser parser) throws IOException {
    KucoinRealTimeSymbolTickerV2Message msg = new KucoinRealTimeSymbolTickerV2Message();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinRealTimeSymbolTickerV2MessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
