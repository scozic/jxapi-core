package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData
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
        msg.setTime(readNextLong(parser));
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
