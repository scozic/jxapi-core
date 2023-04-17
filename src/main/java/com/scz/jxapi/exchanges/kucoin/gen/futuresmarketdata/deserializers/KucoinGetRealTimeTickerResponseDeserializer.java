package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponse
 */
public class KucoinGetRealTimeTickerResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinGetRealTimeTickerResponse> {
  private final KucoinGetRealTimeTickerResponseDataDeserializer kucoinGetRealTimeTickerResponseDataDeserializer = new KucoinGetRealTimeTickerResponseDataDeserializer();
  
  @Override
  public KucoinGetRealTimeTickerResponse deserialize(JsonParser parser) throws IOException {
    KucoinGetRealTimeTickerResponse msg = new KucoinGetRealTimeTickerResponse();
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
        msg.setData(kucoinGetRealTimeTickerResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
