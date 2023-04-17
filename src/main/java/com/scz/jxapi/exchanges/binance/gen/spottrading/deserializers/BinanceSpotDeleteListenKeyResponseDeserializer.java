package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse
 */
public class BinanceSpotDeleteListenKeyResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceSpotDeleteListenKeyResponse> {
  
  @Override
  public BinanceSpotDeleteListenKeyResponse deserialize(JsonParser parser) throws IOException {
    BinanceSpotDeleteListenKeyResponse msg = new BinanceSpotDeleteListenKeyResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
