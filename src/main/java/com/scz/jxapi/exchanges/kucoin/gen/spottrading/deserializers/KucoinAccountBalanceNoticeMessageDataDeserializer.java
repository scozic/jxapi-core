package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageData
 */
public class KucoinAccountBalanceNoticeMessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinAccountBalanceNoticeMessageData> {
  private final KucoinAccountBalanceNoticeMessageDataRelationContextDeserializer kucoinAccountBalanceNoticeMessageDataRelationContextDeserializer = new KucoinAccountBalanceNoticeMessageDataRelationContextDeserializer();
  
  @Override
  public KucoinAccountBalanceNoticeMessageData deserialize(JsonParser parser) throws IOException {
    KucoinAccountBalanceNoticeMessageData msg = new KucoinAccountBalanceNoticeMessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "total":
        msg.setTotal(readNextBigDecimal(parser));
      break;
      case "available":
        msg.setAvailable(readNextBigDecimal(parser));
      break;
      case "availableChange":
        msg.setAvailableChange(readNextBigDecimal(parser));
      break;
      case "currency":
        msg.setCurrency(parser.nextTextValue());
      break;
      case "hold":
        msg.setHold(readNextBigDecimal(parser));
      break;
      case "holdChange":
        msg.setHoldChange(readNextBigDecimal(parser));
      break;
      case "relationEvent":
        msg.setRelationEvent(parser.nextTextValue());
      break;
      case "relationContext":
        parser.nextToken();
        msg.setRelationContext(kucoinAccountBalanceNoticeMessageDataRelationContextDeserializer.deserialize(parser));
      break;
      case "time":
        msg.setTime(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
