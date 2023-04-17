package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers
 */
public class KucoinApplyConnectTokenPublicResponseDataInstanceServersDeserializer extends AbstractJsonMessageDeserializer<KucoinApplyConnectTokenPublicResponseDataInstanceServers> {
  
  @Override
  public KucoinApplyConnectTokenPublicResponseDataInstanceServers deserialize(JsonParser parser) throws IOException {
    KucoinApplyConnectTokenPublicResponseDataInstanceServers msg = new KucoinApplyConnectTokenPublicResponseDataInstanceServers();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "endpoint":
        msg.setEndpoint(parser.nextTextValue());
      break;
      case "protocol":
        msg.setProtocol(parser.nextTextValue());
      break;
      case "encrypt":
        msg.setEncrypt(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "pingInterval":
        msg.setPingInterval(readNextLong(parser));
      break;
      case "pingTimeout":
        msg.setPingTimeout(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
