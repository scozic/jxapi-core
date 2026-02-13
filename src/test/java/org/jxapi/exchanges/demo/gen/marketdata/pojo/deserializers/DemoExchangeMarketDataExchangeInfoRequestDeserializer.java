package org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest instances
 * @see org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class DemoExchangeMarketDataExchangeInfoRequestDeserializer extends AbstractJsonMessageDeserializer<DemoExchangeMarketDataExchangeInfoRequest> {
  private final ListJsonFieldDeserializer<String> symbolsDeserializer = new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance());
  
  @Override
  public DemoExchangeMarketDataExchangeInfoRequest deserialize(JsonParser parser) throws IOException {
    DemoExchangeMarketDataExchangeInfoRequest msg = new DemoExchangeMarketDataExchangeInfoRequest();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "symbols":
        parser.nextToken();
        msg.setSymbols(symbolsDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
