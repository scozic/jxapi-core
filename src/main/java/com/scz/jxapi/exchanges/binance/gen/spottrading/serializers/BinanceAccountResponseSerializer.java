package com.scz.jxapi.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAccountResponse
 */
public class BinanceAccountResponseSerializer extends StdSerializer<BinanceAccountResponse> {
  public BinanceAccountResponseSerializer() {
    super(BinanceAccountResponse.class);
  }
  
  @Override
  public void serialize(BinanceAccountResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getMakerCommission() != null){
      gen.writeNumberField("makerCommission", value.getMakerCommission());
    }
    if (value.getTakerCommission() != null){
      gen.writeNumberField("takerCommission", value.getTakerCommission());
    }
    if (value.getBuyerCommission() != null){
      gen.writeNumberField("buyerCommission", value.getBuyerCommission());
    }
    if (value.getSellerCommission() != null){
      gen.writeNumberField("sellerCommission", value.getSellerCommission());
    }
    if (value.getCommissionRates() != null){
      gen.writeObjectField("commissionRates", value.getCommissionRates());
    }
    if (value.isCanTrade() != null){
      gen.writeBooleanField("canTrade", value.isCanTrade());
    }
    if (value.isCanWithdraw() != null){
      gen.writeBooleanField("canWithdraw", value.isCanWithdraw());
    }
    if (value.isCanDeposit() != null){
      gen.writeBooleanField("canDeposit", value.isCanDeposit());
    }
    if (value.isBrokered() != null){
      gen.writeBooleanField("brokered", value.isBrokered());
    }
    if (value.isRequireSelfTradePrevention() != null){
      gen.writeBooleanField("requireSelfTradePrevention", value.isRequireSelfTradePrevention());
    }
    if (value.getUpdateTime() != null){
      gen.writeNumberField("updateTime", value.getUpdateTime());
    }
    if (value.getAccountType() != null){
      gen.writeStringField("accountType", String.valueOf(value.getAccountType()));
    }
    if (value.getBalances() != null){
      gen.writeObjectField("balances", value.getBalances());
    }
    if (value.getPermissions() != null){
      gen.writeObjectField("permissions", value.getPermissions());
    }
    gen.writeEndObject();
  }
}
