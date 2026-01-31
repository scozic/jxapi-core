package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class DemoExchangeMarketDataTickerStreamRequestDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataTickerStreamRequest> {
  
  @Override
  public DemoExchangeMarketDataTickerStreamRequest deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataTickerStreamRequest msg = new DemoExchangeMarketDataTickerStreamRequest();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "symbol":
        msg.setSymbol(readNextString(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
