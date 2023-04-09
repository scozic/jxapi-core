package com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems
 */
public class KucoinListOrdersResponseDataItemsDeserializer extends AbstractJsonMessageDeserializer<KucoinListOrdersResponseDataItems> {
  
  @Override
  public KucoinListOrdersResponseDataItems deserialize(JsonParser parser) throws IOException {
    KucoinListOrdersResponseDataItems msg = new KucoinListOrdersResponseDataItems();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "id":
        msg.setId(parser.nextTextValue());
      break;
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "opType":
        msg.setOpType(parser.nextTextValue());
      break;
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "side":
        msg.setSide(parser.nextTextValue());
      break;
      case "price":
        msg.setPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "size":
        msg.setSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "funds":
        msg.setFunds(toBigDecimal(parser.nextTextValue()));
      break;
      case "dealFunds":
        msg.setDealFunds(toBigDecimal(parser.nextTextValue()));
      break;
      case "dealSize":
        msg.setDealSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "fee":
        msg.setFee(toBigDecimal(parser.nextTextValue()));
      break;
      case "feeCurrency":
        msg.setFeeCurrency(parser.nextTextValue());
      break;
      case "stp":
        msg.setStp(parser.nextTextValue());
      break;
      case "stop":
        msg.setStop(parser.nextTextValue());
      break;
      case "stopTriggered":
        msg.setStopTriggered(parser.nextBooleanValue());
      break;
      case "stopPrice":
        msg.setStopPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "timeInForce":
        msg.setTimeInForce(parser.nextTextValue());
      break;
      case "postOnly":
        msg.setPostOnly(parser.nextBooleanValue());
      break;
      case "hidden":
        msg.setHidden(parser.nextBooleanValue());
      break;
      case "iceberg":
        msg.setIceberg(parser.nextBooleanValue());
      break;
      case "visibleSize":
        msg.setVisibleSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "cancelAfter":
        msg.setCancelAfter(toBigDecimal(parser.nextTextValue()));
      break;
      case "channel":
        msg.setChannel(parser.nextTextValue());
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
        msg.setIsActive(parser.nextBooleanValue());
      break;
      case "cancelExist":
        msg.setCancelExist(parser.nextBooleanValue());
      break;
      case "createdAt":
        msg.setCreatedAt(parser.nextLongValue(0L));
      break;
      case "tradeType":
        msg.setTradeType(parser.nextTextValue());
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
