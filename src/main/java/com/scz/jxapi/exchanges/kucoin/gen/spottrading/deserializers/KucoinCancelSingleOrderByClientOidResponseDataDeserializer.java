package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponseData
 */
public class KucoinCancelSingleOrderByClientOidResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinCancelSingleOrderByClientOidResponseData> {
  
  @Override
  public KucoinCancelSingleOrderByClientOidResponseData deserialize(JsonParser parser) throws IOException {
    KucoinCancelSingleOrderByClientOidResponseData msg = new KucoinCancelSingleOrderByClientOidResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "cancelledOrderId":
        msg.setCancelledOrderId(parser.nextTextValue());
      break;
      case "clientOid":
        msg.setClientOid(parser.nextTextValue());
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
