package com.scz.jcex.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseBalances;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StringListFieldDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse
 */
public class BinanceAccountResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceAccountResponse> {
  private final BinanceAccountResponseBalancesDeserializer binanceAccountResponseBalancesDeserializer = new BinanceAccountResponseBalancesDeserializer();
  private final BinanceAccountResponseCommissionRatesDeserializer binanceAccountResponseCommissionRatesDeserializer = new BinanceAccountResponseCommissionRatesDeserializer();
  private final StructListFieldDeserializer<BinanceAccountResponseBalances> binanceAccountResponseBalancesListDeserializer = new StructListFieldDeserializer<BinanceAccountResponseBalances>(binanceAccountResponseBalancesDeserializer);
  
  @Override
  public BinanceAccountResponse deserialize(JsonParser parser) throws IOException {
    BinanceAccountResponse msg = new BinanceAccountResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "makerCommission":
        msg.setMakerCommission(parser.nextIntValue(0));
      break;
      case "takerCommission":
        msg.setTakerCommission(parser.nextIntValue(0));
      break;
      case "buyerCommission":
        msg.setBuyerCommission(parser.nextIntValue(0));
      break;
      case "sellerCommission":
        msg.setSellerCommission(parser.nextIntValue(0));
      break;
      case "commissionRates":
        parser.nextToken();
        msg.setCommissionRates(binanceAccountResponseCommissionRatesDeserializer.deserialize(parser));
      break;
      case "canTrade":
        msg.setCanTrade(parser.nextBooleanValue());
      break;
      case "canWithdraw":
        msg.setCanWithdraw(parser.nextBooleanValue());
      break;
      case "canDeposit":
        msg.setCanDeposit(parser.nextBooleanValue());
      break;
      case "brokered":
        msg.setBrokered(parser.nextBooleanValue());
      break;
      case "requireSelfTradePrevention":
        msg.setRequireSelfTradePrevention(parser.nextBooleanValue());
      break;
      case "updateTime":
        msg.setUpdateTime(parser.nextLongValue(0));
      break;
      case "accountType":
        msg.setAccountType(parser.nextTextValue());
      break;
      case "balances":
        parser.nextToken();
        msg.setBalances(binanceAccountResponseBalancesListDeserializer.deserialize(parser));
      break;
      case "permissions":
        parser.nextToken();
        msg.setPermissions(StringListFieldDeserializer.getInstance().deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
