package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponse
 */
public class KucoinCancelSingleOrderByClientOidResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinCancelSingleOrderByClientOidResponse> {
  private final KucoinCancelSingleOrderByClientOidResponseDataDeserializer kucoinCancelSingleOrderByClientOidResponseDataDeserializer = new KucoinCancelSingleOrderByClientOidResponseDataDeserializer();
  
  @Override
  public KucoinCancelSingleOrderByClientOidResponse deserialize(JsonParser parser) throws IOException {
    KucoinCancelSingleOrderByClientOidResponse msg = new KucoinCancelSingleOrderByClientOidResponse();
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
        msg.setData(kucoinCancelSingleOrderByClientOidResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
