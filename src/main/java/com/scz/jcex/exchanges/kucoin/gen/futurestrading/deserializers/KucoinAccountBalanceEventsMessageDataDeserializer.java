package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessageData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jcex.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessageData
 */
public class KucoinAccountBalanceEventsMessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinAccountBalanceEventsMessageData> {
  
  @Override
  public KucoinAccountBalanceEventsMessageData deserialize(JsonParser parser) throws IOException {
    KucoinAccountBalanceEventsMessageData msg = new KucoinAccountBalanceEventsMessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "orderMargin":
        msg.setOrderMargin(readNextBigDecimal(parser));
      break;
      case "withdrawHold":
        msg.setWithdrawHold(readNextBigDecimal(parser));
      break;
      case "availableBalance":
        msg.setAvailableBalance(readNextBigDecimal(parser));
      break;
      case "holdBalance":
        msg.setHoldBalance(readNextBigDecimal(parser));
      break;
      case "currency":
        msg.setCurrency(parser.nextTextValue());
      break;
      case "timestamp":
        msg.setTimestamp(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
