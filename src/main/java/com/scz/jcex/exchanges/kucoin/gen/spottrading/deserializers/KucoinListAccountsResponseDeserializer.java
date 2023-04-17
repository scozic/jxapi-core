package com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse
 */
public class KucoinListAccountsResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinListAccountsResponse> {
  private final KucoinListAccountsResponseDataDeserializer kucoinListAccountsResponseDataDeserializer = new KucoinListAccountsResponseDataDeserializer();
  private final StructListFieldDeserializer<KucoinListAccountsResponseData> kucoinListAccountsResponseDataListDeserializer = new StructListFieldDeserializer<KucoinListAccountsResponseData>(kucoinListAccountsResponseDataDeserializer);
  
  @Override
  public KucoinListAccountsResponse deserialize(JsonParser parser) throws IOException {
    KucoinListAccountsResponse msg = new KucoinListAccountsResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "code":
        msg.setCode(parser.nextTextValue());
      break;
      case "msg":
        msg.setMsg(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinListAccountsResponseDataListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
