package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponse;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponse
 */
public class KucoinCancelAnOrderResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinCancelAnOrderResponse> {
  private final KucoinCancelAnOrderResponseDataDeserializer kucoinCancelAnOrderResponseDataDeserializer = new KucoinCancelAnOrderResponseDataDeserializer();
  
  @Override
  public KucoinCancelAnOrderResponse deserialize(JsonParser parser) throws IOException {
    KucoinCancelAnOrderResponse msg = new KucoinCancelAnOrderResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "code":
        msg.setCode(parser.nextTextValue());
      break;
      case "msg":
        msg.setMsg(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinCancelAnOrderResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
