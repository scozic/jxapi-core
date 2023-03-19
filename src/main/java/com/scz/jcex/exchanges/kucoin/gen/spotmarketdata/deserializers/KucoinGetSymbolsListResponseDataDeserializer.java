package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData
 */
public class KucoinGetSymbolsListResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetSymbolsListResponseData> {
  
  @Override
  public KucoinGetSymbolsListResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetSymbolsListResponseData msg = new KucoinGetSymbolsListResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "name":
        msg.setName(parser.nextTextValue());
      break;
      case "baseCurrency":
        msg.setBaseCurrency(parser.nextTextValue());
      break;
      case "quoteCurrency":
        msg.setQuoteCurrency(parser.nextTextValue());
      break;
      case "feeCurrency":
        msg.setFeeCurrency(parser.nextTextValue());
      break;
      case "market":
        msg.setMarket(parser.nextTextValue());
      break;
      case "baseMinSize":
        msg.setBaseMinSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "quoteMinSize":
        msg.setQuoteMinSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "baseMaxSize":
        msg.setBaseMaxSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "quoteMaxSize":
        msg.setQuoteMaxSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "baseIncrement":
        msg.setBaseIncrement(toBigDecimal(parser.nextTextValue()));
      break;
      case "quoteIncrement":
        msg.setQuoteIncrement(toBigDecimal(parser.nextTextValue()));
      break;
      case "priceIncrement":
        msg.setPriceIncrement(toBigDecimal(parser.nextTextValue()));
      break;
      case "priceLLimitRate":
        msg.setPriceLLimitRate(toBigDecimal(parser.nextTextValue()));
      break;
      case "minFunds":
        msg.setMinFunds(toBigDecimal(parser.nextTextValue()));
      break;
      case "enableTrading":
        msg.setEnableTrading(parser.nextBooleanValue());
      break;
      case "isMarginEnabled":
        msg.setIsMarginEnabled(parser.nextBooleanValue());
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
