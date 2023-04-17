package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols
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
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getStatus() != null){
      gen.writeStringField("status", String.valueOf(value.getStatus()));
    }
    if (value.getBaseAsset() != null){
      gen.writeStringField("baseAsset", String.valueOf(value.getBaseAsset()));
    }
    if (value.getQuoteAsset() != null){
      gen.writeStringField("quoteAsset", String.valueOf(value.getQuoteAsset()));
    }
    if (value.getQuotePrecision() != null){
      gen.writeNumberField("quotePrecision", value.getQuotePrecision());
    }
    if (value.getQuoteAssetPrecision() != null){
      gen.writeNumberField("quoteAssetPrecision", value.getQuoteAssetPrecision());
    }
    if (value.getOrderTypes() != null){
      gen.writeObjectField("orderTypes", value.getOrderTypes());
    }
    if (value.isIcebergAllowed() != null){
      gen.writeBooleanField("icebergAllowed", value.isIcebergAllowed());
    }
    if (value.isOcoAllowed() != null){
      gen.writeBooleanField("ocoAllowed", value.isOcoAllowed());
    }
    if (value.isQuoteOrderQtyMarketAllowed() != null){
      gen.writeBooleanField("quoteOrderQtyMarketAllowed", value.isQuoteOrderQtyMarketAllowed());
    }
    if (value.isAllowTrailingStop() != null){
      gen.writeBooleanField("allowTrailingStop", value.isAllowTrailingStop());
    }
    if (value.isCancelReplaceAllowed() != null){
      gen.writeBooleanField("cancelReplaceAllowed", value.isCancelReplaceAllowed());
    }
    if (value.isIsSpotTradingAllowed() != null){
      gen.writeBooleanField("isSpotTradingAllowed", value.isIsSpotTradingAllowed());
    }
    if (value.isIsMarginTradingAllowed() != null){
      gen.writeBooleanField("isMarginTradingAllowed", value.isIsMarginTradingAllowed());
    }
    if (value.getFilters() != null){
      gen.writeObjectField("filters", value.getFilters());
    }
    if (value.getPermissions() != null){
      gen.writeObjectField("permissions", value.getPermissions());
    }
    if (value.getDefaultSelfTradePreventionMode() != null){
      gen.writeStringField("defaultSelfTradePreventionMode", String.valueOf(value.getDefaultSelfTradePreventionMode()));
    }
    if (value.getAllowedSelfTradePreventionModes() != null){
      gen.writeObjectField("allowedSelfTradePreventionModes", value.getAllowedSelfTradePreventionModes());
    }
    gen.writeEndObject();
  }
}
