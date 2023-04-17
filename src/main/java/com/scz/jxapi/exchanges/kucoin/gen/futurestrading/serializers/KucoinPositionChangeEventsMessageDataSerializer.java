package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessageData;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPositionChangeEventsMessageData
 */
public class KucoinPositionChangeEventsMessageDataSerializer extends StdSerializer<KucoinPositionChangeEventsMessageData> {
  public KucoinPositionChangeEventsMessageDataSerializer() {
    super(KucoinPositionChangeEventsMessageData.class);
  }
  
  @Override
  public void serialize(KucoinPositionChangeEventsMessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getRealisedGrossPnl() != null){
      gen.writeStringField("realisedGrossPnl", EncodingUtil.bigDecimalToString(value.getRealisedGrossPnl()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.isCrossMode() != null){
      gen.writeBooleanField("crossMode", value.isCrossMode());
    }
    if (value.getLiquidationPrice() != null){
      gen.writeStringField("liquidationPrice", EncodingUtil.bigDecimalToString(value.getLiquidationPrice()));
    }
    if (value.getPosLoss() != null){
      gen.writeStringField("posLoss", EncodingUtil.bigDecimalToString(value.getPosLoss()));
    }
    if (value.getAvgEntryPrice() != null){
      gen.writeStringField("avgEntryPrice", EncodingUtil.bigDecimalToString(value.getAvgEntryPrice()));
    }
    if (value.getUnrealisedPnl() != null){
      gen.writeStringField("unrealisedPnl", EncodingUtil.bigDecimalToString(value.getUnrealisedPnl()));
    }
    if (value.getMarkPrice() != null){
      gen.writeStringField("markPrice", EncodingUtil.bigDecimalToString(value.getMarkPrice()));
    }
    if (value.getPosMargin() != null){
      gen.writeStringField("posMargin", EncodingUtil.bigDecimalToString(value.getPosMargin()));
    }
    if (value.isAutoDeposit() != null){
      gen.writeBooleanField("autoDeposit", value.isAutoDeposit());
    }
    if (value.getRiskLimit() != null){
      gen.writeStringField("riskLimit", EncodingUtil.bigDecimalToString(value.getRiskLimit()));
    }
    if (value.getUnrealisedCost() != null){
      gen.writeStringField("unrealisedCost", EncodingUtil.bigDecimalToString(value.getUnrealisedCost()));
    }
    if (value.getPosComm() != null){
      gen.writeStringField("posComm", EncodingUtil.bigDecimalToString(value.getPosComm()));
    }
    if (value.getPosMaint() != null){
      gen.writeStringField("posMaint", EncodingUtil.bigDecimalToString(value.getPosMaint()));
    }
    if (value.getPosCost() != null){
      gen.writeStringField("posCost", EncodingUtil.bigDecimalToString(value.getPosCost()));
    }
    if (value.getMaintMarginReq() != null){
      gen.writeStringField("maintMarginReq", EncodingUtil.bigDecimalToString(value.getMaintMarginReq()));
    }
    if (value.getBankruptPrice() != null){
      gen.writeStringField("bankruptPrice", EncodingUtil.bigDecimalToString(value.getBankruptPrice()));
    }
    if (value.getRealisedCost() != null){
      gen.writeStringField("realisedCost", EncodingUtil.bigDecimalToString(value.getRealisedCost()));
    }
    if (value.getMarkValue() != null){
      gen.writeStringField("markValue", EncodingUtil.bigDecimalToString(value.getMarkValue()));
    }
    if (value.getPosInit() != null){
      gen.writeStringField("posInit", EncodingUtil.bigDecimalToString(value.getPosInit()));
    }
    if (value.getRealisedPnl() != null){
      gen.writeStringField("realisedPnl", EncodingUtil.bigDecimalToString(value.getRealisedPnl()));
    }
    if (value.getMaintMargin() != null){
      gen.writeStringField("maintMargin", EncodingUtil.bigDecimalToString(value.getMaintMargin()));
    }
    if (value.getRealLeverage() != null){
      gen.writeStringField("realLeverage", EncodingUtil.bigDecimalToString(value.getRealLeverage()));
    }
    if (value.getChangeReason() != null){
      gen.writeStringField("changeReason", String.valueOf(value.getChangeReason()));
    }
    if (value.getCurrentCost() != null){
      gen.writeStringField("currentCost", EncodingUtil.bigDecimalToString(value.getCurrentCost()));
    }
    if (value.getOpeningTimestamp() != null){
      gen.writeNumberField("openingTimestamp", value.getOpeningTimestamp());
    }
    if (value.getCurrentQty() != null){
      gen.writeStringField("currentQty", EncodingUtil.bigDecimalToString(value.getCurrentQty()));
    }
    if (value.getDelevPercentage() != null){
      gen.writeStringField("delevPercentage", EncodingUtil.bigDecimalToString(value.getDelevPercentage()));
    }
    if (value.getCurrentComm() != null){
      gen.writeStringField("currentComm", EncodingUtil.bigDecimalToString(value.getCurrentComm()));
    }
    if (value.getRealisedGrossCost() != null){
      gen.writeStringField("realisedGrossCost", EncodingUtil.bigDecimalToString(value.getRealisedGrossCost()));
    }
    if (value.isIsOpen() != null){
      gen.writeBooleanField("isOpen", value.isIsOpen());
    }
    if (value.getPosCross() != null){
      gen.writeStringField("posCross", EncodingUtil.bigDecimalToString(value.getPosCross()));
    }
    if (value.getCurrentTimestamp() != null){
      gen.writeNumberField("currentTimestamp", value.getCurrentTimestamp());
    }
    if (value.getUnrealisedRoePcnt() != null){
      gen.writeStringField("unrealisedRoePcnt", EncodingUtil.bigDecimalToString(value.getUnrealisedRoePcnt()));
    }
    if (value.getUnrealisedPnlPcnt() != null){
      gen.writeStringField("unrealisedPnlPcnt", EncodingUtil.bigDecimalToString(value.getUnrealisedPnlPcnt()));
    }
    if (value.getSettleCurrency() != null){
      gen.writeStringField("settleCurrency", String.valueOf(value.getSettleCurrency()));
    }
    if (value.getFundingTime() != null){
      gen.writeNumberField("fundingTime", value.getFundingTime());
    }
    if (value.getQty() != null){
      gen.writeStringField("qty", EncodingUtil.bigDecimalToString(value.getQty()));
    }
    if (value.getFundingRate() != null){
      gen.writeStringField("fundingRate", EncodingUtil.bigDecimalToString(value.getFundingRate()));
    }
    if (value.getFundingFee() != null){
      gen.writeStringField("fundingFee", EncodingUtil.bigDecimalToString(value.getFundingFee()));
    }
    if (value.getTs() != null){
      gen.writeNumberField("ts", value.getTs());
    }
    if (value.isSuccess() != null){
      gen.writeBooleanField("success", value.isSuccess());
    }
    if (value.getRiskLimitLevel() != null){
      gen.writeStringField("riskLimitLevel", EncodingUtil.bigDecimalToString(value.getRiskLimitLevel()));
    }
    if (value.getMsg() != null){
      gen.writeStringField("msg", String.valueOf(value.getMsg()));
    }
    gen.writeEndObject();
  }
}
