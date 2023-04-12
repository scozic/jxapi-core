package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderResponseData
 */
public class KucoinPlaceAnOrderResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinPlaceAnOrderResponseData> {
  
  @Override
  public KucoinPlaceAnOrderResponseData deserialize(JsonParser parser) throws IOException {
    KucoinPlaceAnOrderResponseData msg = new KucoinPlaceAnOrderResponseData();
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
