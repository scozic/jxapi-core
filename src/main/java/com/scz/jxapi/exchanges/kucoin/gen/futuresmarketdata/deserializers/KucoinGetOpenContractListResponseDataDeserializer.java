package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextInteger;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponseData
 */
public class KucoinGetOpenContractListResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetOpenContractListResponseData> {
  
  @Override
  public KucoinGetOpenContractListResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetOpenContractListResponseData msg = new KucoinGetOpenContractListResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "rootSymbol":
        msg.setRootSymbol(parser.nextTextValue());
      break;
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "firstOpenDate":
        msg.setFirstOpenDate(readNextLong(parser));
      break;
      case "expireDate":
        msg.setExpireDate(readNextLong(parser));
      break;
      case "settleDate":
        msg.setSettleDate(readNextLong(parser));
      break;
      case "baseCurrency":
        msg.setBaseCurrency(parser.nextTextValue());
      break;
      case "quoteCurrency":
        msg.setQuoteCurrency(parser.nextTextValue());
      break;
      case "settleCurrency":
        msg.setSettleCurrency(parser.nextTextValue());
      break;
      case "maxOrderQty":
        msg.setMaxOrderQty(readNextInteger(parser));
      break;
      case "maxPrice":
        msg.setMaxPrice(readNextBigDecimal(parser));
      break;
      case "lotSize":
        msg.setLotSize(readNextInteger(parser));
      break;
      case "tickSize":
        msg.setTickSize(readNextBigDecimal(parser));
      break;
      case "indexPriceTickSize":
        msg.setIndexPriceTickSize(readNextBigDecimal(parser));
      break;
      case "multiplier":
        msg.setMultiplier(readNextBigDecimal(parser));
      break;
      case "initialMargin":
        msg.setInitialMargin(readNextBigDecimal(parser));
      break;
      case "maintainMargin":
        msg.setMaintainMargin(readNextBigDecimal(parser));
      break;
      case "maxRiskLimit":
        msg.setMaxRiskLimit(readNextInteger(parser));
      break;
      case "minRiskLimit":
        msg.setMinRiskLimit(readNextInteger(parser));
      break;
      case "riskStep":
        msg.setRiskStep(readNextInteger(parser));
      break;
      case "makerFeeRate":
        msg.setMakerFeeRate(readNextBigDecimal(parser));
      break;
      case "takerFeeRate":
        msg.setTakerFeeRate(readNextBigDecimal(parser));
      break;
      case "makerFixFee":
        msg.setMakerFixFee(readNextBigDecimal(parser));
      break;
      case "takerFixFee":
        msg.setTakerFixFee(readNextBigDecimal(parser));
      break;
      case "settlementFee":
        msg.setSettlementFee(readNextBigDecimal(parser));
      break;
      case "isDeleverage":
        msg.setIsDeleverage(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "isQuanto":
        msg.setIsQuanto(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "isInverse":
        msg.setIsInverse(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "markMethod":
        msg.setMarkMethod(parser.nextTextValue());
      break;
      case "fairMethod":
        msg.setFairMethod(parser.nextTextValue());
      break;
      case "fundingBaseSymbol":
        msg.setFundingBaseSymbol(parser.nextTextValue());
      break;
      case "fundingQuoteSymbol":
        msg.setFundingQuoteSymbol(parser.nextTextValue());
      break;
      case "fundingRateSymbol":
        msg.setFundingRateSymbol(parser.nextTextValue());
      break;
      case "indexSymbol":
        msg.setIndexSymbol(parser.nextTextValue());
      break;
      case "settlementSymbol":
        msg.setSettlementSymbol(parser.nextTextValue());
      break;
      case "status":
        msg.setStatus(parser.nextTextValue());
      break;
      case "fundingFeeRate":
        msg.setFundingFeeRate(readNextBigDecimal(parser));
      break;
      case "predictedFundingFeeRate":
        msg.setPredictedFundingFeeRate(readNextBigDecimal(parser));
      break;
      case "openInterest":
        msg.setOpenInterest(parser.nextTextValue());
      break;
      case "turnoverOf24h":
        msg.setTurnoverOf24h(readNextBigDecimal(parser));
      break;
      case "volumeOf24h":
        msg.setVolumeOf24h(readNextBigDecimal(parser));
      break;
      case "markPrice":
        msg.setMarkPrice(readNextBigDecimal(parser));
      break;
      case "indexPrice":
        msg.setIndexPrice(readNextBigDecimal(parser));
      break;
      case "lastTradePrice":
        msg.setLastTradePrice(readNextBigDecimal(parser));
      break;
      case "nextFundingRateTime":
        msg.setNextFundingRateTime(readNextLong(parser));
      break;
      case "maxLeverage":
        msg.setMaxLeverage(readNextBigDecimal(parser));
      break;
      case "sourceExchanges":
        parser.nextToken();
        msg.setSourceExchanges(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      case "premiumsSymbol1M":
        msg.setPremiumsSymbol1M(parser.nextTextValue());
      break;
      case "premiumsSymbol8H":
        msg.setPremiumsSymbol8H(parser.nextTextValue());
      break;
      case "fundingBaseSymbol1M":
        msg.setFundingBaseSymbol1M(parser.nextTextValue());
      break;
      case "fundingQuoteSymbol1M":
        msg.setFundingQuoteSymbol1M(parser.nextTextValue());
      break;
      case "lowPrice":
        msg.setLowPrice(readNextBigDecimal(parser));
      break;
      case "highPrice":
        msg.setHighPrice(readNextBigDecimal(parser));
      break;
      case "priceChgPct":
        msg.setPriceChgPct(readNextBigDecimal(parser));
      break;
      case "priceChg":
        msg.setPriceChg(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
