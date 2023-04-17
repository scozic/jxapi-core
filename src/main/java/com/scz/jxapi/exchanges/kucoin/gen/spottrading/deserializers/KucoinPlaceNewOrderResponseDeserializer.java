package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse
 */
public class KucoinPlaceNewOrderResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinPlaceNewOrderResponse> {
  
  @Override
  public KucoinPlaceNewOrderResponse deserialize(JsonParser parser) throws IOException {
    KucoinPlaceNewOrderResponse msg = new KucoinPlaceNewOrderResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "orderId":
        msg.setOrderId(parser.nextTextValue());
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
