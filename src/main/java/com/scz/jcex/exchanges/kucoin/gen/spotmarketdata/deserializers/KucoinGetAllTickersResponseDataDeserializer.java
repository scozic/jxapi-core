package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData
 */
public class KucoinGetAllTickersResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetAllTickersResponseData> {
  private final KucoinGetAllTickersResponseDataTickerDeserializer kucoinGetAllTickersResponseDataTickerDeserializer = new KucoinGetAllTickersResponseDataTickerDeserializer();
  private final StructListFieldDeserializer<KucoinGetAllTickersResponseDataTicker> kucoinGetAllTickersResponseDataTickerListDeserializer = new StructListFieldDeserializer<KucoinGetAllTickersResponseDataTicker>(kucoinGetAllTickersResponseDataTickerDeserializer);
  
  @Override
  public KucoinGetAllTickersResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetAllTickersResponseData msg = new KucoinGetAllTickersResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "time":
        msg.setTime(parser.nextLongValue(0L));
      break;
      case "ticker":
        parser.nextToken();
        msg.setTicker(kucoinGetAllTickersResponseDataTickerListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
