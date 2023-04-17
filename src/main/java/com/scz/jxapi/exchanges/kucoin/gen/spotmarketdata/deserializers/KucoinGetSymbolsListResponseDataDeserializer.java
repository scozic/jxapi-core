package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData
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
        msg.setBaseMinSize(readNextBigDecimal(parser));
      break;
      case "quoteMinSize":
        msg.setQuoteMinSize(readNextBigDecimal(parser));
      break;
      case "baseMaxSize":
        msg.setBaseMaxSize(readNextBigDecimal(parser));
      break;
      case "quoteMaxSize":
        msg.setQuoteMaxSize(readNextBigDecimal(parser));
      break;
      case "baseIncrement":
        msg.setBaseIncrement(readNextBigDecimal(parser));
      break;
      case "quoteIncrement":
        msg.setQuoteIncrement(readNextBigDecimal(parser));
      break;
      case "priceIncrement":
        msg.setPriceIncrement(readNextBigDecimal(parser));
      break;
      case "priceLLimitRate":
        msg.setPriceLLimitRate(readNextBigDecimal(parser));
      break;
      case "minFunds":
        msg.setMinFunds(readNextBigDecimal(parser));
      break;
      case "enableTrading":
        msg.setEnableTrading(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "isMarginEnabled":
        msg.setIsMarginEnabled(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
