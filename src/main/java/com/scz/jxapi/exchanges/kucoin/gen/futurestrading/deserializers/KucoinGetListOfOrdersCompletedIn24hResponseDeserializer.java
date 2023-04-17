package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hResponse;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hResponse
 */
public class KucoinGetListOfOrdersCompletedIn24hResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinGetListOfOrdersCompletedIn24hResponse> {
  private final KucoinGetListOfOrdersCompletedIn24hResponseDataDeserializer kucoinGetListOfOrdersCompletedIn24hResponseDataDeserializer = new KucoinGetListOfOrdersCompletedIn24hResponseDataDeserializer();
  private final StructListFieldDeserializer<KucoinGetListOfOrdersCompletedIn24hResponseData> kucoinGetListOfOrdersCompletedIn24hResponseDataListDeserializer = new StructListFieldDeserializer<KucoinGetListOfOrdersCompletedIn24hResponseData>(kucoinGetListOfOrdersCompletedIn24hResponseDataDeserializer);
  
  @Override
  public KucoinGetListOfOrdersCompletedIn24hResponse deserialize(JsonParser parser) throws IOException {
    KucoinGetListOfOrdersCompletedIn24hResponse msg = new KucoinGetListOfOrdersCompletedIn24hResponse();
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
        msg.setData(kucoinGetListOfOrdersCompletedIn24hResponseDataListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
