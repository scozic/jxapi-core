package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseDataItems;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseDataItems instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseDataItems
 */
public class KucoinGetOrderListResponseDataItemsDeserializer extends AbstractJsonMessageDeserializer<KucoinGetOrderListResponseDataItems> {
  
  @Override
  public KucoinGetOrderListResponseDataItems deserialize(JsonParser parser) throws IOException {
    KucoinGetOrderListResponseDataItems msg = new KucoinGetOrderListResponseDataItems();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "id":
        msg.setId(parser.nextTextValue());
      break;
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "type":
        msg.setType(parser.nextTextValue());
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
      case "stp":
        msg.setStp(parser.nextTextValue());
      break;
      case "stop":
        msg.setStop(parser.nextTextValue());
      break;
      case "stopTriggered":
        msg.setStopTriggered(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "stopPrice":
        msg.setStopPrice(readNextBigDecimal(parser));
      break;
      case "timeInForce":
        msg.setTimeInForce(parser.nextTextValue());
      break;
      case "postOnly":
        msg.setPostOnly(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "hidden":
        msg.setHidden(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "iceberg":
        msg.setIceberg(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "leverage":
        msg.setLeverage(readNextBigDecimal(parser));
      break;
      case "forceHold":
        msg.setForceHold(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "closeOrder":
        msg.setCloseOrder(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "visibleSize":
        msg.setVisibleSize(readNextBigDecimal(parser));
      break;
      case "clientOid":
        msg.setClientOid(parser.nextTextValue());
      break;
      case "remark":
        msg.setRemark(parser.nextTextValue());
      break;
      case "tags":
        msg.setTags(parser.nextTextValue());
      break;
      case "isActive":
        msg.setIsActive(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "cancelExist":
        msg.setCancelExist(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "createdAt":
        msg.setCreatedAt(readNextLong(parser));
      break;
      case "updatedAt":
        msg.setUpdatedAt(readNextLong(parser));
      break;
      case "endAt":
        msg.setEndAt(readNextLong(parser));
      break;
      case "orderTime":
        msg.setOrderTime(readNextLong(parser));
      break;
      case "settleCurrency":
        msg.setSettleCurrency(parser.nextTextValue());
      break;
      case "status":
        msg.setStatus(parser.nextTextValue());
      break;
      case "tradeType":
        msg.setTradeType(parser.nextTextValue());
      break;
      case "filledValue":
        msg.setFilledValue(readNextBigDecimal(parser));
      break;
      case "filledSize":
        msg.setFilledSize(readNextBigDecimal(parser));
      break;
      case "reduceOnly":
        msg.setReduceOnly(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
