package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseData;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseData
 */
public class KucoinApplyConnectTokenPublicResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinApplyConnectTokenPublicResponseData> {
  private final KucoinApplyConnectTokenPublicResponseDataInstanceServersDeserializer kucoinApplyConnectTokenPublicResponseDataInstanceServersDeserializer = new KucoinApplyConnectTokenPublicResponseDataInstanceServersDeserializer();
  private final StructListFieldDeserializer<KucoinApplyConnectTokenPublicResponseDataInstanceServers> kucoinApplyConnectTokenPublicResponseDataInstanceServersListDeserializer = new StructListFieldDeserializer<KucoinApplyConnectTokenPublicResponseDataInstanceServers>(kucoinApplyConnectTokenPublicResponseDataInstanceServersDeserializer);
  
  @Override
  public KucoinApplyConnectTokenPublicResponseData deserialize(JsonParser parser) throws IOException {
    KucoinApplyConnectTokenPublicResponseData msg = new KucoinApplyConnectTokenPublicResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "token":
        msg.setToken(parser.nextTextValue());
      break;
      case "instanceServers":
        parser.nextToken();
        msg.setInstanceServers(kucoinApplyConnectTokenPublicResponseDataInstanceServersListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
