package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderResponse
 */
public class KucoinGetDetailsOfASingleOrderResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinGetDetailsOfASingleOrderResponse> {
  private final KucoinGetDetailsOfASingleOrderResponseDataDeserializer kucoinGetDetailsOfASingleOrderResponseDataDeserializer = new KucoinGetDetailsOfASingleOrderResponseDataDeserializer();
  
  @Override
  public KucoinGetDetailsOfASingleOrderResponse deserialize(JsonParser parser) throws IOException {
    KucoinGetDetailsOfASingleOrderResponse msg = new KucoinGetDetailsOfASingleOrderResponse();
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
        msg.setData(kucoinGetDetailsOfASingleOrderResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
