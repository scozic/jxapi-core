package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse
 */
public class KucoinApplyConnectTokenPublicResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinApplyConnectTokenPublicResponse> {
  private final KucoinApplyConnectTokenPublicResponseDataDeserializer kucoinApplyConnectTokenPublicResponseDataDeserializer = new KucoinApplyConnectTokenPublicResponseDataDeserializer();
  
  @Override
  public KucoinApplyConnectTokenPublicResponse deserialize(JsonParser parser) throws IOException {
    KucoinApplyConnectTokenPublicResponse msg = new KucoinApplyConnectTokenPublicResponse();
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
        msg.setData(kucoinApplyConnectTokenPublicResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
