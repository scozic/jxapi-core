package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse
 */
public class KucoinCancelAllOrdersResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinCancelAllOrdersResponse> {
  private final KucoinCancelAllOrdersResponseDataDeserializer kucoinCancelAllOrdersResponseDataDeserializer = new KucoinCancelAllOrdersResponseDataDeserializer();
  
  @Override
  public KucoinCancelAllOrdersResponse deserialize(JsonParser parser) throws IOException {
    KucoinCancelAllOrdersResponse msg = new KucoinCancelAllOrdersResponse();
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
        msg.setData(kucoinCancelAllOrdersResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
