package com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponseData
 */
public class KucoinListAccountsResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinListAccountsResponseData> {
  
  @Override
  public KucoinListAccountsResponseData deserialize(JsonParser parser) throws IOException {
    KucoinListAccountsResponseData msg = new KucoinListAccountsResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "id":
        msg.setId(parser.nextTextValue());
      break;
      case "currency":
        msg.setCurrency(parser.nextTextValue());
      break;
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "balance":
        msg.setBalance(toBigDecimal(parser.nextTextValue()));
      break;
      case "available":
        msg.setAvailable(toBigDecimal(parser.nextTextValue()));
      break;
      case "holds":
        msg.setHolds(toBigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
