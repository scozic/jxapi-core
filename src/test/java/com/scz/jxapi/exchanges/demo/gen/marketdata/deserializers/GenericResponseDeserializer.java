package com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static com.scz.jxapi.util.JsonUtil.readNextInteger;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse
 */
public class GenericResponseDeserializer extends AbstractJsonMessageDeserializer<GenericResponse> {
  
  @Override
  public GenericResponse deserialize(JsonParser parser) throws IOException {
    GenericResponse msg = new GenericResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "responseCode":
        msg.setResponseCode(readNextInteger(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
