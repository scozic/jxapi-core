package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponseData
 */
public class KucoinCancelAnOrderResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinCancelAnOrderResponseData> {
  
  @Override
  public KucoinCancelAnOrderResponseData deserialize(JsonParser parser) throws IOException {
    KucoinCancelAnOrderResponseData msg = new KucoinCancelAnOrderResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "cancelledOrderIds":
        parser.nextToken();
        msg.setCancelledOrderIds(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
