package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse;
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
    gen.writeNumberField("makerCommission", value.getMakerCommission());
    gen.writeNumberField("takerCommission", value.getTakerCommission());
    gen.writeNumberField("buyerCommission", value.getBuyerCommission());
    gen.writeNumberField("sellerCommission", value.getSellerCommission());
    gen.writeObjectField("commissionRates", value.getCommissionRates());
    gen.writeBooleanField("canTrade", value.isCanTrade());
    gen.writeBooleanField("canWithdraw", value.isCanWithdraw());
    gen.writeBooleanField("canDeposit", value.isCanDeposit());
    gen.writeBooleanField("brokered", value.isBrokered());
    gen.writeBooleanField("requireSelfTradePrevention", value.isRequireSelfTradePrevention());
    gen.writeNumberField("updateTime", value.getUpdateTime());
    gen.writeStringField("accountType", String.valueOf(value.getAccountType()));
    gen.writeObjectField("balances", value.getBalances());
    gen.writeObjectField("permissions", value.getPermissions());
    gen.writeEndObject();
  }
}
