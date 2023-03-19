package com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData
 */
public class KucoinPrivateOrderChangeV2MessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinPrivateOrderChangeV2MessageData> {
  
  @Override
  public KucoinPrivateOrderChangeV2MessageData deserialize(JsonParser parser) throws IOException {
    KucoinPrivateOrderChangeV2MessageData msg = new KucoinPrivateOrderChangeV2MessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "orderType":
        msg.setOrderType(parser.nextTextValue());
      break;
      case "side":
        msg.setSide(parser.nextTextValue());
      break;
      case "orderId":
        msg.setOrderId(parser.nextTextValue());
      break;
      case "liquidity":
        msg.setLiquidity(parser.nextTextValue());
      break;
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "orderTime":
        msg.setOrderTime(parser.nextLongValue(0L));
      break;
      case "size":
        msg.setSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "filledSize":
        msg.setFilledSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "price":
        msg.setPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "matchPrice":
        msg.setMatchPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "matchSize":
        msg.setMatchSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "tradeId":
        msg.setTradeId(parser.nextTextValue());
      break;
      case "clientOid":
        msg.setClientOid(parser.nextTextValue());
      break;
      case "remainSize":
        msg.setRemainSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "status":
        msg.setStatus(parser.nextTextValue());
      break;
      case "canceledSize":
        msg.setCanceledSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "canceledFunds":
        msg.setCanceledFunds(toBigDecimal(parser.nextTextValue()));
      break;
      case "originSize":
        msg.setOriginSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "originFunds":
        msg.setOriginFunds(toBigDecimal(parser.nextTextValue()));
      break;
      case "ts":
        msg.setTs(parser.nextLongValue(0L));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
