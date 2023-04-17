package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseData;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseData
 */
public class KucoinApplyConnectTokenPrivateResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinApplyConnectTokenPrivateResponseData> {
  private final KucoinApplyConnectTokenPrivateResponseDataInstanceServersDeserializer kucoinApplyConnectTokenPrivateResponseDataInstanceServersDeserializer = new KucoinApplyConnectTokenPrivateResponseDataInstanceServersDeserializer();
  private final StructListFieldDeserializer<KucoinApplyConnectTokenPrivateResponseDataInstanceServers> kucoinApplyConnectTokenPrivateResponseDataInstanceServersListDeserializer = new StructListFieldDeserializer<KucoinApplyConnectTokenPrivateResponseDataInstanceServers>(kucoinApplyConnectTokenPrivateResponseDataInstanceServersDeserializer);
  
  @Override
  public KucoinApplyConnectTokenPrivateResponseData deserialize(JsonParser parser) throws IOException {
    KucoinApplyConnectTokenPrivateResponseData msg = new KucoinApplyConnectTokenPrivateResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "token":
        msg.setToken(parser.nextTextValue());
      break;
      case "instanceServers":
        parser.nextToken();
        msg.setInstanceServers(kucoinApplyConnectTokenPrivateResponseDataInstanceServersListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
