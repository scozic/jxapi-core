package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse
 */
public class KucoinGetSymbolsListResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinGetSymbolsListResponse> {
  private final KucoinGetSymbolsListResponseDataDeserializer kucoinGetSymbolsListResponseDataDeserializer = new KucoinGetSymbolsListResponseDataDeserializer();
  private final StructListFieldDeserializer<KucoinGetSymbolsListResponseData> kucoinGetSymbolsListResponseDataListDeserializer = new StructListFieldDeserializer<KucoinGetSymbolsListResponseData>(kucoinGetSymbolsListResponseDataDeserializer);
  
  @Override
  public KucoinGetSymbolsListResponse deserialize(JsonParser parser) throws IOException {
    KucoinGetSymbolsListResponse msg = new KucoinGetSymbolsListResponse();
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
        msg.setData(kucoinGetSymbolsListResponseDataListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
