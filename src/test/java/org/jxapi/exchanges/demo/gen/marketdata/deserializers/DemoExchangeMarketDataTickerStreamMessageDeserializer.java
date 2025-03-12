package org.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextBigDecimal;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerGenerator")
public class DemoExchangeMarketDataTickerStreamMessageDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataTickerStreamMessage> {
  
  @Override
  public DemoExchangeMarketDataTickerStreamMessage deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataTickerStreamMessage msg = new DemoExchangeMarketDataTickerStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "t":
        msg.setTopic(parser.nextTextValue());
      break;
      case "s":
        msg.setSymbol(parser.nextTextValue());
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
