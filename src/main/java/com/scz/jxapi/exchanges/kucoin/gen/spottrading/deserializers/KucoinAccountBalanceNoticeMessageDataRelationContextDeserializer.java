package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageDataRelationContext;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageDataRelationContext instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageDataRelationContext
 */
public class KucoinAccountBalanceNoticeMessageDataRelationContextDeserializer extends AbstractJsonMessageDeserializer<KucoinAccountBalanceNoticeMessageDataRelationContext> {
  
  @Override
  public KucoinAccountBalanceNoticeMessageDataRelationContext deserialize(JsonParser parser) throws IOException {
    KucoinAccountBalanceNoticeMessageDataRelationContext msg = new KucoinAccountBalanceNoticeMessageDataRelationContext();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "tradeId":
        msg.setTradeId(parser.nextTextValue());
      break;
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
