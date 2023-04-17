package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessageData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessageData
 */
public class KucoinStopOrderLifecycleEventMessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinStopOrderLifecycleEventMessageData> {
  
  @Override
  public KucoinStopOrderLifecycleEventMessageData deserialize(JsonParser parser) throws IOException {
    KucoinStopOrderLifecycleEventMessageData msg = new KucoinStopOrderLifecycleEventMessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "orderId":
        msg.setOrderId(parser.nextTextValue());
      break;
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "status":
        msg.setStatus(parser.nextTextValue());
      break;
      case "orderType":
        msg.setOrderType(parser.nextTextValue());
      break;
      case "side":
        msg.setSide(parser.nextTextValue());
      break;
      case "orderPrice":
        msg.setOrderPrice(readNextBigDecimal(parser));
      break;
      case "size":
        msg.setSize(readNextBigDecimal(parser));
      break;
      case "stop":
        msg.setStop(parser.nextTextValue());
      break;
      case "stopPrice":
        msg.setStopPrice(readNextBigDecimal(parser));
      break;
      case "stopPriceType":
        msg.setStopPriceType(parser.nextTextValue());
      break;
      case "triggerSuccess":
        msg.setTriggerSuccess(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "error":
        msg.setError(parser.nextTextValue());
      break;
      case "ts":
        msg.setTs(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
