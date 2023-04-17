package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessageData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessageData
 */
public class KucoinTradeOrdersMessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinTradeOrdersMessageData> {
  
  @Override
  public KucoinTradeOrdersMessageData deserialize(JsonParser parser) throws IOException {
    KucoinTradeOrdersMessageData msg = new KucoinTradeOrdersMessageData();
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
      case "matchSize":
        msg.setMatchSize(readNextBigDecimal(parser));
      break;
      case "matchPrice":
        msg.setMatchPrice(readNextBigDecimal(parser));
      break;
      case "orderType":
        msg.setOrderType(parser.nextTextValue());
      break;
      case "side":
        msg.setSide(parser.nextTextValue());
      break;
      case "price":
        msg.setPrice(readNextBigDecimal(parser));
      break;
      case "size":
        msg.setSize(readNextBigDecimal(parser));
      break;
      case "remainSize":
        msg.setRemainSize(readNextBigDecimal(parser));
      break;
      case "filledSize":
        msg.setFilledSize(readNextBigDecimal(parser));
      break;
      case "canceledSize":
        msg.setCanceledSize(readNextBigDecimal(parser));
      break;
      case "tradeId":
        msg.setTradeId(parser.nextTextValue());
      break;
      case "clientOId":
        msg.setClientOId(parser.nextTextValue());
      break;
      case "orderTime":
        msg.setOrderTime(readNextLong(parser));
      break;
      case "oldSize":
        msg.setOldSize(readNextBigDecimal(parser));
      break;
      case "liquidity":
        msg.setLiquidity(parser.nextTextValue());
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
