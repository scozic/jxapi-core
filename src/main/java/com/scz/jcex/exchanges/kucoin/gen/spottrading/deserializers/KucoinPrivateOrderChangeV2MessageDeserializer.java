package com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message
 */
public class KucoinPrivateOrderChangeV2MessageDeserializer extends AbstractJsonMessageDeserializer<KucoinPrivateOrderChangeV2Message> {
  private final KucoinPrivateOrderChangeV2MessageDataDeserializer kucoinPrivateOrderChangeV2MessageDataDeserializer = new KucoinPrivateOrderChangeV2MessageDataDeserializer();
  
  @Override
  public KucoinPrivateOrderChangeV2Message deserialize(JsonParser parser) throws IOException {
    KucoinPrivateOrderChangeV2Message msg = new KucoinPrivateOrderChangeV2Message();
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
        msg.setData(kucoinPrivateOrderChangeV2MessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
