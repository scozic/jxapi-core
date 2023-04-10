package com.scz.jcex.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextInteger;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols
 */
public class BinanceExchangeInformationResponseSymbolsDeserializer extends AbstractJsonMessageDeserializer<BinanceExchangeInformationResponseSymbols> {
  private final BinanceExchangeInformationResponseSymbolsFiltersDeserializer binanceExchangeInformationResponseSymbolsFiltersDeserializer = new BinanceExchangeInformationResponseSymbolsFiltersDeserializer();
  private final StructListFieldDeserializer<BinanceExchangeInformationResponseSymbolsFilters> binanceExchangeInformationResponseSymbolsFiltersListDeserializer = new StructListFieldDeserializer<BinanceExchangeInformationResponseSymbolsFilters>(binanceExchangeInformationResponseSymbolsFiltersDeserializer);
  
  @Override
  public BinanceExchangeInformationResponseSymbols deserialize(JsonParser parser) throws IOException {
    BinanceExchangeInformationResponseSymbols msg = new BinanceExchangeInformationResponseSymbols();
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
        msg.setQuotePrecision(readNextInteger(parser));
      break;
      case "quoteAssetPrecision":
        msg.setQuoteAssetPrecision(readNextInteger(parser));
      break;
      case "orderTypes":
        parser.nextToken();
        msg.setOrderTypes(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      case "icebergAllowed":
        msg.setIcebergAllowed(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "ocoAllowed":
        msg.setOcoAllowed(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "quoteOrderQtyMarketAllowed":
        msg.setQuoteOrderQtyMarketAllowed(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "allowTrailingStop":
        msg.setAllowTrailingStop(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "cancelReplaceAllowed":
        msg.setCancelReplaceAllowed(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "isSpotTradingAllowed":
        msg.setIsSpotTradingAllowed(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "isMarginTradingAllowed":
        msg.setIsMarginTradingAllowed(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "filters":
        parser.nextToken();
        msg.setFilters(binanceExchangeInformationResponseSymbolsFiltersListDeserializer.deserialize(parser));
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
