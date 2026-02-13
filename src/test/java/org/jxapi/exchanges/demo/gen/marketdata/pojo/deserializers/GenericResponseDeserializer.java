package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class GenericResponseDeserializer extends AbstractJsonMessageDeserializer<GenericResponse> {
  
  @Override
  public GenericResponse deserialize(JsonParser parser) throws IOException {
    GenericResponse msg = new GenericResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
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
