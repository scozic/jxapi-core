package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems
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
        msg.setPrice(readNextBigDecimal(parser));
      break;
      case "size":
        msg.setSize(readNextBigDecimal(parser));
      break;
      case "funds":
        msg.setFunds(readNextBigDecimal(parser));
      break;
      case "dealFunds":
        msg.setDealFunds(readNextBigDecimal(parser));
      break;
      case "dealSize":
        msg.setDealSize(readNextBigDecimal(parser));
      break;
      case "fee":
        msg.setFee(readNextBigDecimal(parser));
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
      case "visibleSize":
        msg.setVisibleSize(readNextBigDecimal(parser));
      break;
      case "cancelAfter":
        msg.setCancelAfter(readNextBigDecimal(parser));
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
        msg.setIsActive(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "cancelExist":
        msg.setCancelExist(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "createdAt":
        msg.setCreatedAt(readNextLong(parser));
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
