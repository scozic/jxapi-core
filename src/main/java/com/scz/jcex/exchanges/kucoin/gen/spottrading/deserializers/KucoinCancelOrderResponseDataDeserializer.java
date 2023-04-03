package com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponseData
 */
public class KucoinCancelOrderResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinCancelOrderResponseData> {
  
  @Override
  public KucoinCancelOrderResponseData deserialize(JsonParser parser) throws IOException {
    KucoinCancelOrderResponseData msg = new KucoinCancelOrderResponseData();
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
