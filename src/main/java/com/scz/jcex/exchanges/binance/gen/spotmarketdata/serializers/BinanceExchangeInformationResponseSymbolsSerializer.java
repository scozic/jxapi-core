package com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExchangeInformationResponseSymbols
 */
public class BinanceExchangeInformationResponseSymbolsSerializer extends StdSerializer<BinanceExchangeInformationResponseSymbols> {
  public BinanceExchangeInformationResponseSymbolsSerializer() {
    super(BinanceExchangeInformationResponseSymbols.class);
  }
  
  @Override
  public void serialize(BinanceExchangeInformationResponseSymbols value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("status", String.valueOf(value.getStatus()));
    gen.writeStringField("baseAsset", String.valueOf(value.getBaseAsset()));
    gen.writeStringField("quoteAsset", String.valueOf(value.getQuoteAsset()));
    gen.writeNumberField("quotePrecision", value.getQuotePrecision());
    gen.writeNumberField("quoteAssetPrecision", value.getQuoteAssetPrecision());
    gen.writeObjectField("orderTypes", value.getOrderTypes());
    gen.writeBooleanField("icebergAllowed", value.isIcebergAllowed());
    gen.writeBooleanField("ocoAllowed", value.isOcoAllowed());
    gen.writeBooleanField("quoteOrderQtyMarketAllowed", value.isQuoteOrderQtyMarketAllowed());
    gen.writeBooleanField("allowTrailingStop", value.isAllowTrailingStop());
    gen.writeBooleanField("cancelReplaceAllowed", value.isCancelReplaceAllowed());
    gen.writeBooleanField("isSpotTradingAllowed", value.isIsSpotTradingAllowed());
    gen.writeBooleanField("isMarginTradingAllowed", value.isIsMarginTradingAllowed());
    gen.writeObjectField("filters", value.getFilters());
    gen.writeObjectField("permissions", value.getPermissions());
    gen.writeStringField("defaultSelfTradePreventionMode", String.valueOf(value.getDefaultSelfTradePreventionMode()));
    gen.writeObjectField("allowedSelfTradePreventionModes", value.getAllowedSelfTradePreventionModes());
    gen.writeEndObject();
  }
}
