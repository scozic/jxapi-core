package com.scz.jcex.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbols;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbolsFilters;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbols instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbols
 */
public class BinanceExchangeInformationAllResponseSymbolsDeserializer extends AbstractJsonMessageDeserializer<BinanceExchangeInformationAllResponseSymbols> {
  private final BinanceExchangeInformationAllResponseSymbolsFiltersDeserializer binanceExchangeInformationAllResponseSymbolsFiltersDeserializer = new BinanceExchangeInformationAllResponseSymbolsFiltersDeserializer();
  private final StructListFieldDeserializer<BinanceExchangeInformationAllResponseSymbolsFilters> binanceExchangeInformationAllResponseSymbolsFiltersListDeserializer = new StructListFieldDeserializer<BinanceExchangeInformationAllResponseSymbolsFilters>(binanceExchangeInformationAllResponseSymbolsFiltersDeserializer);
  
  @Override
  public BinanceExchangeInformationAllResponseSymbols deserialize(JsonParser parser) throws IOException {
    BinanceExchangeInformationAllResponseSymbols msg = new BinanceExchangeInformationAllResponseSymbols();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "status":
        msg.setStatus(parser.nextTextValue());
      break;
      case "baseAsset":
        msg.setBaseAsset(parser.nextTextValue());
      break;
      case "quoteAsset":
        msg.setQuoteAsset(parser.nextTextValue());
      break;
      case "quotePrecision":
        msg.setQuotePrecision(parser.nextIntValue(0));
      break;
      case "quoteAssetPrecision":
        msg.setQuoteAssetPrecision(parser.nextIntValue(0));
      break;
      case "orderTypes":
        parser.nextToken();
        msg.setOrderTypes(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      case "icebergAllowed":
        msg.setIcebergAllowed(parser.nextBooleanValue());
      break;
      case "ocoAllowed":
        msg.setOcoAllowed(parser.nextBooleanValue());
      break;
      case "quoteOrderQtyMarketAllowed":
        msg.setQuoteOrderQtyMarketAllowed(parser.nextBooleanValue());
      break;
      case "allowTrailingStop":
        msg.setAllowTrailingStop(parser.nextBooleanValue());
      break;
      case "cancelReplaceAllowed":
        msg.setCancelReplaceAllowed(parser.nextBooleanValue());
      break;
      case "isSpotTradingAllowed":
        msg.setIsSpotTradingAllowed(parser.nextBooleanValue());
      break;
      case "isMarginTradingAllowed":
        msg.setIsMarginTradingAllowed(parser.nextBooleanValue());
      break;
      case "filters":
        parser.nextToken();
        msg.setFilters(binanceExchangeInformationAllResponseSymbolsFiltersListDeserializer.deserialize(parser));
      break;
      case "permissions":
        parser.nextToken();
        msg.setPermissions(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      case "defaultSelfTradePreventionMode":
        msg.setDefaultSelfTradePreventionMode(parser.nextTextValue());
      break;
      case "allowedSelfTradePreventionModes":
        parser.nextToken();
        msg.setAllowedSelfTradePreventionModes(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
