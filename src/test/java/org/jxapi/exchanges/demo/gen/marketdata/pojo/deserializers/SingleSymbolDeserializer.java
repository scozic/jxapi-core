package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class SingleSymbolDeserializer extends AbstractJsonMessageDeserializer<SingleSymbol> {
  
  @Override
  public SingleSymbol deserialize(JsonParser parser) throws IOException {
    SingleSymbol msg = new SingleSymbol();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "s":
        msg.setSymbol(readNextString(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
