package com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import javax.annotation.processing.Generated;
import static com.scz.jxapi.util.JsonUtil.readNextBigDecimal;
import static com.scz.jxapi.util.JsonUtil.readNextLong;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage instances
 * @see com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerGenerator")
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
