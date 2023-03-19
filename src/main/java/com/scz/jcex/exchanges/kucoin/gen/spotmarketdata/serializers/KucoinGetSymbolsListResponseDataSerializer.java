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
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("name", String.valueOf(value.getName()));
    gen.writeStringField("baseCurrency", String.valueOf(value.getBaseCurrency()));
    gen.writeStringField("quoteCurrency", String.valueOf(value.getQuoteCurrency()));
    gen.writeStringField("feeCurrency", String.valueOf(value.getFeeCurrency()));
    gen.writeStringField("market", String.valueOf(value.getMarket()));
    gen.writeStringField("baseMinSize", EncodingUtil.bigDecimalToString(value.getBaseMinSize()));
    gen.writeStringField("quoteMinSize", EncodingUtil.bigDecimalToString(value.getQuoteMinSize()));
    gen.writeStringField("baseMaxSize", EncodingUtil.bigDecimalToString(value.getBaseMaxSize()));
    gen.writeStringField("quoteMaxSize", EncodingUtil.bigDecimalToString(value.getQuoteMaxSize()));
    gen.writeStringField("baseIncrement", EncodingUtil.bigDecimalToString(value.getBaseIncrement()));
    gen.writeStringField("quoteIncrement", EncodingUtil.bigDecimalToString(value.getQuoteIncrement()));
    gen.writeStringField("priceIncrement", EncodingUtil.bigDecimalToString(value.getPriceIncrement()));
    gen.writeStringField("priceLLimitRate", EncodingUtil.bigDecimalToString(value.getPriceLLimitRate()));
    gen.writeStringField("minFunds", EncodingUtil.bigDecimalToString(value.getMinFunds()));
    gen.writeBooleanField("enableTrading", value.isEnableTrading());
    gen.writeBooleanField("isMarginEnabled", value.isIsMarginEnabled());
    gen.writeEndObject();
  }
}
