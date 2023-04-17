package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse
 */
public class KucoinGetTickerResponseDeserializer extends AbstractJsonMessageDeserializer<KucoinGetTickerResponse> {
  private final KucoinGetTickerResponseDataDeserializer kucoinGetTickerResponseDataDeserializer = new KucoinGetTickerResponseDataDeserializer();
  
  @Override
  public KucoinGetTickerResponse deserialize(JsonParser parser) throws IOException {
    KucoinGetTickerResponse msg = new KucoinGetTickerResponse();
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
        msg.setData(kucoinGetTickerResponseDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
