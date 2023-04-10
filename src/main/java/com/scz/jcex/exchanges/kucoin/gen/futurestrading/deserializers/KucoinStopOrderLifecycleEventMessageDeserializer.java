package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage
 */
public class KucoinStopOrderLifecycleEventMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinStopOrderLifecycleEventMessage> {
  private final KucoinStopOrderLifecycleEventMessageDataDeserializer kucoinStopOrderLifecycleEventMessageDataDeserializer = new KucoinStopOrderLifecycleEventMessageDataDeserializer();
  
  @Override
  public KucoinStopOrderLifecycleEventMessage deserialize(JsonParser parser) throws IOException {
    KucoinStopOrderLifecycleEventMessage msg = new KucoinStopOrderLifecycleEventMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "userId":
        msg.setUserId(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinStopOrderLifecycleEventMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
