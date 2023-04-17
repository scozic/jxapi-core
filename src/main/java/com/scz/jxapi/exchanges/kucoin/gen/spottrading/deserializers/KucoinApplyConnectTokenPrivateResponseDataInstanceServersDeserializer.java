package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers
 */
public class KucoinApplyConnectTokenPrivateResponseDataInstanceServersDeserializer extends AbstractJsonMessageDeserializer<KucoinApplyConnectTokenPrivateResponseDataInstanceServers> {
  
  @Override
  public KucoinApplyConnectTokenPrivateResponseDataInstanceServers deserialize(JsonParser parser) throws IOException {
    KucoinApplyConnectTokenPrivateResponseDataInstanceServers msg = new KucoinApplyConnectTokenPrivateResponseDataInstanceServers();
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
