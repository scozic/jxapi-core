package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse
 */
public class KucoinGetOpenContractListResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinGetOpenContractListResponse> {
  private final KucoinGetOpenContractListResponseDataDeserializer kucoinGetOpenContractListResponseDataDeserializer = new KucoinGetOpenContractListResponseDataDeserializer();
  private final StructListFieldDeserializer<KucoinGetOpenContractListResponseData> kucoinGetOpenContractListResponseDataListDeserializer = new StructListFieldDeserializer<KucoinGetOpenContractListResponseData>(kucoinGetOpenContractListResponseDataDeserializer);
  
  @Override
  public KucoinGetOpenContractListResponse deserialize(JsonParser parser) throws IOException {
    KucoinGetOpenContractListResponse msg = new KucoinGetOpenContractListResponse();
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
        msg.setData(kucoinGetOpenContractListResponseDataListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
