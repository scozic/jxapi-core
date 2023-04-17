package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse
 */
public class BinanceSpotKeepAliveListenKeyResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceSpotKeepAliveListenKeyResponse> {
  
  @Override
  public BinanceSpotKeepAliveListenKeyResponse deserialize(JsonParser parser) throws IOException {
    BinanceSpotKeepAliveListenKeyResponse msg = new BinanceSpotKeepAliveListenKeyResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
