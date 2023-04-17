package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponseData;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetOpenContractListResponseData
 */
public class KucoinGetOpenContractListResponseDataSerializer extends StdSerializer<KucoinGetOpenContractListResponseData> {
  public KucoinGetOpenContractListResponseDataSerializer() {
    super(KucoinGetOpenContractListResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetOpenContractListResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getRootSymbol() != null){
      gen.writeStringField("rootSymbol", String.valueOf(value.getRootSymbol()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getFirstOpenDate() != null){
      gen.writeNumberField("firstOpenDate", value.getFirstOpenDate());
    }
    if (value.getExpireDate() != null){
      gen.writeNumberField("expireDate", value.getExpireDate());
    }
    if (value.getSettleDate() != null){
      gen.writeNumberField("settleDate", value.getSettleDate());
    }
    if (value.getBaseCurrency() != null){
      gen.writeStringField("baseCurrency", String.valueOf(value.getBaseCurrency()));
    }
    if (value.getQuoteCurrency() != null){
      gen.writeStringField("quoteCurrency", String.valueOf(value.getQuoteCurrency()));
    }
    if (value.getSettleCurrency() != null){
      gen.writeStringField("settleCurrency", String.valueOf(value.getSettleCurrency()));
    }
    if (value.getMaxOrderQty() != null){
      gen.writeNumberField("maxOrderQty", value.getMaxOrderQty());
    }
    if (value.getMaxPrice() != null){
      gen.writeStringField("maxPrice", EncodingUtil.bigDecimalToString(value.getMaxPrice()));
    }
    if (value.getLotSize() != null){
      gen.writeNumberField("lotSize", value.getLotSize());
    }
    if (value.getTickSize() != null){
      gen.writeStringField("tickSize", EncodingUtil.bigDecimalToString(value.getTickSize()));
    }
    if (value.getIndexPriceTickSize() != null){
      gen.writeStringField("indexPriceTickSize", EncodingUtil.bigDecimalToString(value.getIndexPriceTickSize()));
    }
    if (value.getMultiplier() != null){
      gen.writeStringField("multiplier", EncodingUtil.bigDecimalToString(value.getMultiplier()));
    }
    if (value.getInitialMargin() != null){
      gen.writeStringField("initialMargin", EncodingUtil.bigDecimalToString(value.getInitialMargin()));
    }
    if (value.getMaintainMargin() != null){
      gen.writeStringField("maintainMargin", EncodingUtil.bigDecimalToString(value.getMaintainMargin()));
    }
    if (value.getMaxRiskLimit() != null){
      gen.writeNumberField("maxRiskLimit", value.getMaxRiskLimit());
    }
    if (value.getMinRiskLimit() != null){
      gen.writeNumberField("minRiskLimit", value.getMinRiskLimit());
    }
    if (value.getRiskStep() != null){
      gen.writeNumberField("riskStep", value.getRiskStep());
    }
    if (value.getMakerFeeRate() != null){
      gen.writeStringField("makerFeeRate", EncodingUtil.bigDecimalToString(value.getMakerFeeRate()));
    }
    if (value.getTakerFeeRate() != null){
      gen.writeStringField("takerFeeRate", EncodingUtil.bigDecimalToString(value.getTakerFeeRate()));
    }
    if (value.getMakerFixFee() != null){
      gen.writeStringField("makerFixFee", EncodingUtil.bigDecimalToString(value.getMakerFixFee()));
    }
    if (value.getTakerFixFee() != null){
      gen.writeStringField("takerFixFee", EncodingUtil.bigDecimalToString(value.getTakerFixFee()));
    }
    if (value.getSettlementFee() != null){
      gen.writeStringField("settlementFee", EncodingUtil.bigDecimalToString(value.getSettlementFee()));
    }
    if (value.isIsDeleverage() != null){
      gen.writeBooleanField("isDeleverage", value.isIsDeleverage());
    }
    if (value.isIsQuanto() != null){
      gen.writeBooleanField("isQuanto", value.isIsQuanto());
    }
    if (value.isIsInverse() != null){
      gen.writeBooleanField("isInverse", value.isIsInverse());
    }
    if (value.getMarkMethod() != null){
      gen.writeStringField("markMethod", String.valueOf(value.getMarkMethod()));
    }
    if (value.getFairMethod() != null){
      gen.writeStringField("fairMethod", String.valueOf(value.getFairMethod()));
    }
    if (value.getFundingBaseSymbol() != null){
      gen.writeStringField("fundingBaseSymbol", String.valueOf(value.getFundingBaseSymbol()));
    }
    if (value.getFundingQuoteSymbol() != null){
      gen.writeStringField("fundingQuoteSymbol", String.valueOf(value.getFundingQuoteSymbol()));
    }
    if (value.getFundingRateSymbol() != null){
      gen.writeStringField("fundingRateSymbol", String.valueOf(value.getFundingRateSymbol()));
    }
    if (value.getIndexSymbol() != null){
      gen.writeStringField("indexSymbol", String.valueOf(value.getIndexSymbol()));
    }
    if (value.getSettlementSymbol() != null){
      gen.writeStringField("settlementSymbol", String.valueOf(value.getSettlementSymbol()));
    }
    if (value.getStatus() != null){
      gen.writeStringField("status", String.valueOf(value.getStatus()));
    }
    if (value.getFundingFeeRate() != null){
      gen.writeStringField("fundingFeeRate", EncodingUtil.bigDecimalToString(value.getFundingFeeRate()));
    }
    if (value.getPredictedFundingFeeRate() != null){
      gen.writeStringField("predictedFundingFeeRate", EncodingUtil.bigDecimalToString(value.getPredictedFundingFeeRate()));
    }
    if (value.getOpenInterest() != null){
      gen.writeStringField("openInterest", String.valueOf(value.getOpenInterest()));
    }
    if (value.getTurnoverOf24h() != null){
      gen.writeStringField("turnoverOf24h", EncodingUtil.bigDecimalToString(value.getTurnoverOf24h()));
    }
    if (value.getVolumeOf24h() != null){
      gen.writeStringField("volumeOf24h", EncodingUtil.bigDecimalToString(value.getVolumeOf24h()));
    }
    if (value.getMarkPrice() != null){
      gen.writeStringField("markPrice", EncodingUtil.bigDecimalToString(value.getMarkPrice()));
    }
    if (value.getIndexPrice() != null){
      gen.writeStringField("indexPrice", EncodingUtil.bigDecimalToString(value.getIndexPrice()));
    }
    if (value.getLastTradePrice() != null){
      gen.writeStringField("lastTradePrice", EncodingUtil.bigDecimalToString(value.getLastTradePrice()));
    }
    if (value.getNextFundingRateTime() != null){
      gen.writeNumberField("nextFundingRateTime", value.getNextFundingRateTime());
    }
    if (value.getMaxLeverage() != null){
      gen.writeStringField("maxLeverage", EncodingUtil.bigDecimalToString(value.getMaxLeverage()));
    }
    if (value.getSourceExchanges() != null){
      gen.writeObjectField("sourceExchanges", value.getSourceExchanges());
    }
    if (value.getPremiumsSymbol1M() != null){
      gen.writeStringField("premiumsSymbol1M", String.valueOf(value.getPremiumsSymbol1M()));
    }
    if (value.getPremiumsSymbol8H() != null){
      gen.writeStringField("premiumsSymbol8H", String.valueOf(value.getPremiumsSymbol8H()));
    }
    if (value.getFundingBaseSymbol1M() != null){
      gen.writeStringField("fundingBaseSymbol1M", String.valueOf(value.getFundingBaseSymbol1M()));
    }
    if (value.getFundingQuoteSymbol1M() != null){
      gen.writeStringField("fundingQuoteSymbol1M", String.valueOf(value.getFundingQuoteSymbol1M()));
    }
    if (value.getLowPrice() != null){
      gen.writeStringField("lowPrice", EncodingUtil.bigDecimalToString(value.getLowPrice()));
    }
    if (value.getHighPrice() != null){
      gen.writeStringField("highPrice", EncodingUtil.bigDecimalToString(value.getHighPrice()));
    }
    if (value.getPriceChgPct() != null){
      gen.writeStringField("priceChgPct", EncodingUtil.bigDecimalToString(value.getPriceChgPct()));
    }
    if (value.getPriceChg() != null){
      gen.writeStringField("priceChg", EncodingUtil.bigDecimalToString(value.getPriceChg()));
    }
    gen.writeEndObject();
  }
}
