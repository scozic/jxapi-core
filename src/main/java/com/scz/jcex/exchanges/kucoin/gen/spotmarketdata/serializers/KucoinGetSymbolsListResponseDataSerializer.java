package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetSymbolsListResponseData
 */
public class KucoinGetSymbolsListResponseDataSerializer extends StdSerializer<KucoinGetSymbolsListResponseData> {
  public KucoinGetSymbolsListResponseDataSerializer() {
    super(KucoinGetSymbolsListResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetSymbolsListResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getName() != null){
      gen.writeStringField("name", String.valueOf(value.getName()));
    }
    if (value.getBaseCurrency() != null){
      gen.writeStringField("baseCurrency", String.valueOf(value.getBaseCurrency()));
    }
    if (value.getQuoteCurrency() != null){
      gen.writeStringField("quoteCurrency", String.valueOf(value.getQuoteCurrency()));
    }
    if (value.getFeeCurrency() != null){
      gen.writeStringField("feeCurrency", String.valueOf(value.getFeeCurrency()));
    }
    if (value.getMarket() != null){
      gen.writeStringField("market", String.valueOf(value.getMarket()));
    }
    if (value.getBaseMinSize() != null){
      gen.writeStringField("baseMinSize", EncodingUtil.bigDecimalToString(value.getBaseMinSize()));
    }
    if (value.getQuoteMinSize() != null){
      gen.writeStringField("quoteMinSize", EncodingUtil.bigDecimalToString(value.getQuoteMinSize()));
    }
    if (value.getBaseMaxSize() != null){
      gen.writeStringField("baseMaxSize", EncodingUtil.bigDecimalToString(value.getBaseMaxSize()));
    }
    if (value.getQuoteMaxSize() != null){
      gen.writeStringField("quoteMaxSize", EncodingUtil.bigDecimalToString(value.getQuoteMaxSize()));
    }
    if (value.getBaseIncrement() != null){
      gen.writeStringField("baseIncrement", EncodingUtil.bigDecimalToString(value.getBaseIncrement()));
    }
    if (value.getQuoteIncrement() != null){
      gen.writeStringField("quoteIncrement", EncodingUtil.bigDecimalToString(value.getQuoteIncrement()));
    }
    if (value.getPriceIncrement() != null){
      gen.writeStringField("priceIncrement", EncodingUtil.bigDecimalToString(value.getPriceIncrement()));
    }
    if (value.getPriceLLimitRate() != null){
      gen.writeStringField("priceLLimitRate", EncodingUtil.bigDecimalToString(value.getPriceLLimitRate()));
    }
    if (value.getMinFunds() != null){
      gen.writeStringField("minFunds", EncodingUtil.bigDecimalToString(value.getMinFunds()));
    }
    if (value.isEnableTrading() != null){
      gen.writeBooleanField("enableTrading", value.isEnableTrading());
    }
    if (value.isIsMarginEnabled() != null){
      gen.writeBooleanField("isMarginEnabled", value.isIsMarginEnabled());
    }
    gen.writeEndObject();
  }
}
