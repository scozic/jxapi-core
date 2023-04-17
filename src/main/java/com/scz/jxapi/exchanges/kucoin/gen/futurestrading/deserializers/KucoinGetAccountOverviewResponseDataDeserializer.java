package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponseData
 */
public class KucoinGetAccountOverviewResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetAccountOverviewResponseData> {
  
  @Override
  public KucoinGetAccountOverviewResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetAccountOverviewResponseData msg = new KucoinGetAccountOverviewResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "accountEquity":
        msg.setAccountEquity(readNextBigDecimal(parser));
      break;
      case "unrealisedPNL":
        msg.setUnrealisedPNL(readNextBigDecimal(parser));
      break;
      case "marginBalance":
        msg.setMarginBalance(readNextBigDecimal(parser));
      break;
      case "positionMargin":
        msg.setPositionMargin(readNextBigDecimal(parser));
      break;
      case "orderMargin":
        msg.setOrderMargin(readNextBigDecimal(parser));
      break;
      case "frozenFunds":
        msg.setFrozenFunds(readNextBigDecimal(parser));
      break;
      case "availableBalance":
        msg.setAvailableBalance(readNextBigDecimal(parser));
      break;
      case "currency":
        msg.setCurrency(parser.nextTextValue());
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
