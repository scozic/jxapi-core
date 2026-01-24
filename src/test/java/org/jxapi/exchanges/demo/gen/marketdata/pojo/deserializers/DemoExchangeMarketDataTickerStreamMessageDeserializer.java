package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextBigDecimal;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class DemoExchangeMarketDataTickerStreamMessageDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataTickerStreamMessage> {
  
  @Override
  public DemoExchangeMarketDataTickerStreamMessage deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataTickerStreamMessage msg = new DemoExchangeMarketDataTickerStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "t":
        msg.setTopic(readNextString(parser));
      break;
      case "s":
        msg.setSymbol(readNextString(parser));
      break;
      case "p":
        msg.setLast(readNextBigDecimal(parser));
      break;
      case "h":
        msg.setHigh(readNextBigDecimal(parser));
      break;
      case "l":
        msg.setLow(readNextBigDecimal(parser));
      break;
      case "v":
        msg.setVolume(readNextBigDecimal(parser));
      break;
      case "d":
        msg.setTime(readNextLong(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
