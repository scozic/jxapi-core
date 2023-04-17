package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessageData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessageData
 */
public class KucoinPositionChangeEventsMessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinPositionChangeEventsMessageData> {
  
  @Override
  public KucoinPositionChangeEventsMessageData deserialize(JsonParser parser) throws IOException {
    KucoinPositionChangeEventsMessageData msg = new KucoinPositionChangeEventsMessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "realisedGrossPnl":
        msg.setRealisedGrossPnl(readNextBigDecimal(parser));
      break;
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "crossMode":
        msg.setCrossMode(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "liquidationPrice":
        msg.setLiquidationPrice(readNextBigDecimal(parser));
      break;
      case "posLoss":
        msg.setPosLoss(readNextBigDecimal(parser));
      break;
      case "avgEntryPrice":
        msg.setAvgEntryPrice(readNextBigDecimal(parser));
      break;
      case "unrealisedPnl":
        msg.setUnrealisedPnl(readNextBigDecimal(parser));
      break;
      case "markPrice":
        msg.setMarkPrice(readNextBigDecimal(parser));
      break;
      case "posMargin":
        msg.setPosMargin(readNextBigDecimal(parser));
      break;
      case "autoDeposit":
        msg.setAutoDeposit(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "riskLimit":
        msg.setRiskLimit(readNextBigDecimal(parser));
      break;
      case "unrealisedCost":
        msg.setUnrealisedCost(readNextBigDecimal(parser));
      break;
      case "posComm":
        msg.setPosComm(readNextBigDecimal(parser));
      break;
      case "posMaint":
        msg.setPosMaint(readNextBigDecimal(parser));
      break;
      case "posCost":
        msg.setPosCost(readNextBigDecimal(parser));
      break;
      case "maintMarginReq":
        msg.setMaintMarginReq(readNextBigDecimal(parser));
      break;
      case "bankruptPrice":
        msg.setBankruptPrice(readNextBigDecimal(parser));
      break;
      case "realisedCost":
        msg.setRealisedCost(readNextBigDecimal(parser));
      break;
      case "markValue":
        msg.setMarkValue(readNextBigDecimal(parser));
      break;
      case "posInit":
        msg.setPosInit(readNextBigDecimal(parser));
      break;
      case "realisedPnl":
        msg.setRealisedPnl(readNextBigDecimal(parser));
      break;
      case "maintMargin":
        msg.setMaintMargin(readNextBigDecimal(parser));
      break;
      case "realLeverage":
        msg.setRealLeverage(readNextBigDecimal(parser));
      break;
      case "changeReason":
        msg.setChangeReason(parser.nextTextValue());
      break;
      case "currentCost":
        msg.setCurrentCost(readNextBigDecimal(parser));
      break;
      case "openingTimestamp":
        msg.setOpeningTimestamp(readNextLong(parser));
      break;
      case "currentQty":
        msg.setCurrentQty(readNextBigDecimal(parser));
      break;
      case "delevPercentage":
        msg.setDelevPercentage(readNextBigDecimal(parser));
      break;
      case "currentComm":
        msg.setCurrentComm(readNextBigDecimal(parser));
      break;
      case "realisedGrossCost":
        msg.setRealisedGrossCost(readNextBigDecimal(parser));
      break;
      case "isOpen":
        msg.setIsOpen(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "posCross":
        msg.setPosCross(readNextBigDecimal(parser));
      break;
      case "currentTimestamp":
        msg.setCurrentTimestamp(readNextLong(parser));
      break;
      case "unrealisedRoePcnt":
        msg.setUnrealisedRoePcnt(readNextBigDecimal(parser));
      break;
      case "unrealisedPnlPcnt":
        msg.setUnrealisedPnlPcnt(readNextBigDecimal(parser));
      break;
      case "settleCurrency":
        msg.setSettleCurrency(parser.nextTextValue());
      break;
      case "fundingTime":
        msg.setFundingTime(readNextLong(parser));
      break;
      case "qty":
        msg.setQty(readNextBigDecimal(parser));
      break;
      case "fundingRate":
        msg.setFundingRate(readNextBigDecimal(parser));
      break;
      case "fundingFee":
        msg.setFundingFee(readNextBigDecimal(parser));
      break;
      case "ts":
        msg.setTs(readNextLong(parser));
      break;
      case "success":
        msg.setSuccess(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "riskLimitLevel":
        msg.setRiskLimitLevel(readNextBigDecimal(parser));
      break;
      case "msg":
        msg.setMsg(parser.nextTextValue());
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
