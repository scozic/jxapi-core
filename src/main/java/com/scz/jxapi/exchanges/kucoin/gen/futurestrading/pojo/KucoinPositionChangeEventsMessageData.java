package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinPositionChangeEventsMessageDataSerializer;
import com.scz.jxapi.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinPositionChangeEventsMessageDataSerializer.class)
public class KucoinPositionChangeEventsMessageData {
  private Boolean autoDeposit;
  private BigDecimal avgEntryPrice;
  private BigDecimal bankruptPrice;
  private String changeReason;
  private Boolean crossMode;
  private BigDecimal currentComm;
  private BigDecimal currentCost;
  private BigDecimal currentQty;
  private Long currentTimestamp;
  private BigDecimal delevPercentage;
  private BigDecimal fundingFee;
  private BigDecimal fundingRate;
  private Long fundingTime;
  private Boolean isOpen;
  private BigDecimal liquidationPrice;
  private BigDecimal maintMargin;
  private BigDecimal maintMarginReq;
  private BigDecimal markPrice;
  private BigDecimal markValue;
  private String msg;
  private Long openingTimestamp;
  private BigDecimal posComm;
  private BigDecimal posCost;
  private BigDecimal posCross;
  private BigDecimal posInit;
  private BigDecimal posLoss;
  private BigDecimal posMaint;
  private BigDecimal posMargin;
  private BigDecimal qty;
  private BigDecimal realLeverage;
  private BigDecimal realisedCost;
  private BigDecimal realisedGrossCost;
  private BigDecimal realisedGrossPnl;
  private BigDecimal realisedPnl;
  private BigDecimal riskLimit;
  private BigDecimal riskLimitLevel;
  private String settleCurrency;
  private Boolean success;
  private String symbol;
  private Long ts;
  private BigDecimal unrealisedCost;
  private BigDecimal unrealisedPnl;
  private BigDecimal unrealisedPnlPcnt;
  private BigDecimal unrealisedRoePcnt;
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Auto deposit margin or not
   */
  public Boolean isAutoDeposit(){
    return autoDeposit;
  }
  
  /**
   * @param autoDeposit Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Auto deposit margin or not
   */
  public void setAutoDeposit(Boolean autoDeposit) {
    this.autoDeposit = autoDeposit;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Average entry price
   */
  public BigDecimal getAvgEntryPrice(){
    return avgEntryPrice;
  }
  
  /**
   * @param avgEntryPrice Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Average entry price
   */
  public void setAvgEntryPrice(BigDecimal avgEntryPrice) {
    this.avgEntryPrice = avgEntryPrice;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Bankruptcy price
   */
  public BigDecimal getBankruptPrice(){
    return bankruptPrice;
  }
  
  /**
   * @param bankruptPrice Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Bankruptcy price
   */
  public void setBankruptPrice(BigDecimal bankruptPrice) {
    this.bankruptPrice = bankruptPrice;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. ChangeReason: <strong>marginChange</strong>, <strong>positionChange</strong>, <strong>liquidation</strong>, <strong>autoAppendMarginStatusChange</strong>, <strong>adl</strong>
   */
  public String getChangeReason(){
    return changeReason;
  }
  
  /**
   * @param changeReason Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. ChangeReason: <strong>marginChange</strong>, <strong>positionChange</strong>, <strong>liquidation</strong>, <strong>autoAppendMarginStatusChange</strong>, <strong>adl</strong>
   */
  public void setChangeReason(String changeReason) {
    this.changeReason = changeReason;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Cross mode or not
   */
  public Boolean isCrossMode(){
    return crossMode;
  }
  
  /**
   * @param crossMode Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Cross mode or not
   */
  public void setCrossMode(Boolean crossMode) {
    this.crossMode = crossMode;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Current commission
   */
  public BigDecimal getCurrentComm(){
    return currentComm;
  }
  
  /**
   * @param currentComm Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Current commission
   */
  public void setCurrentComm(BigDecimal currentComm) {
    this.currentComm = currentComm;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Current position value
   */
  public BigDecimal getCurrentCost(){
    return currentCost;
  }
  
  /**
   * @param currentCost Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Current position value
   */
  public void setCurrentCost(BigDecimal currentCost) {
    this.currentCost = currentCost;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Current position
   */
  public BigDecimal getCurrentQty(){
    return currentQty;
  }
  
  /**
   * @param currentQty Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Current position
   */
  public void setCurrentQty(BigDecimal currentQty) {
    this.currentQty = currentQty;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Current timestamp
   */
  public Long getCurrentTimestamp(){
    return currentTimestamp;
  }
  
  /**
   * @param currentTimestamp Provided when <subject>subject</strong> is <strong>position.change</strong>. Current timestamp
   */
  public void setCurrentTimestamp(Long currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. ADL ranking percentile
   */
  public BigDecimal getDelevPercentage(){
    return delevPercentage;
  }
  
  /**
   * @param delevPercentage Provided when <subject>subject</strong> is <strong>position.change</strong>. ADL ranking percentile
   */
  public void setDelevPercentage(BigDecimal delevPercentage) {
    this.delevPercentage = delevPercentage;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Funding fees
   */
  public BigDecimal getFundingFee(){
    return fundingFee;
  }
  
  /**
   * @param fundingFee Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Funding fees
   */
  public void setFundingFee(BigDecimal fundingFee) {
    this.fundingFee = fundingFee;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Funding rate
   */
  public BigDecimal getFundingRate(){
    return fundingRate;
  }
  
  /**
   * @param fundingRate Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Funding rate
   */
  public void setFundingRate(BigDecimal fundingRate) {
    this.fundingRate = fundingRate;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Funding time
   */
  public Long getFundingTime(){
    return fundingTime;
  }
  
  /**
   * @param fundingTime Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Funding time
   */
  public void setFundingTime(Long fundingTime) {
    this.fundingTime = fundingTime;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Opened position or not
   */
  public Boolean isIsOpen(){
    return isOpen;
  }
  
  /**
   * @param isOpen Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Opened position or not
   */
  public void setIsOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Liquidation price
   */
  public BigDecimal getLiquidationPrice(){
    return liquidationPrice;
  }
  
  /**
   * @param liquidationPrice Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Liquidation price
   */
  public void setLiquidationPrice(BigDecimal liquidationPrice) {
    this.liquidationPrice = liquidationPrice;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Position margin
   */
  public BigDecimal getMaintMargin(){
    return maintMargin;
  }
  
  /**
   * @param maintMargin Provided when <subject>subject</strong> is <strong>position.change</strong>. Position margin
   */
  public void setMaintMargin(BigDecimal maintMargin) {
    this.maintMargin = maintMargin;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Maintenance margin rate
   */
  public BigDecimal getMaintMarginReq(){
    return maintMarginReq;
  }
  
  /**
   * @param maintMarginReq Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Maintenance margin rate
   */
  public void setMaintMarginReq(BigDecimal maintMarginReq) {
    this.maintMarginReq = maintMarginReq;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> or <strong>position.settlement</strong>. Mark price
   */
  public BigDecimal getMarkPrice(){
    return markPrice;
  }
  
  /**
   * @param markPrice Provided when <subject>subject</strong> is <strong>position.change</strong> or <strong>position.settlement</strong>. Mark price
   */
  public void setMarkPrice(BigDecimal markPrice) {
    this.markPrice = markPrice;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Mark value
   */
  public BigDecimal getMarkValue(){
    return markValue;
  }
  
  /**
   * @param markValue Provided when <subject>subject</strong> is <strong>position.change</strong>. Mark value
   */
  public void setMarkValue(BigDecimal markValue) {
    this.markValue = markValue;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.adjustRiskLimit</strong>. Failure reason
   */
  public String getMsg(){
    return msg;
  }
  
  /**
   * @param msg Provided when <subject>subject</strong> is <strong>position.adjustRiskLimit</strong>. Failure reason
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Open time
   */
  public Long getOpeningTimestamp(){
    return openingTimestamp;
  }
  
  /**
   * @param openingTimestamp Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Open time
   */
  public void setOpeningTimestamp(Long openingTimestamp) {
    this.openingTimestamp = openingTimestamp;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Bankruptcy cost
   */
  public BigDecimal getPosComm(){
    return posComm;
  }
  
  /**
   * @param posComm Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Bankruptcy cost
   */
  public void setPosComm(BigDecimal posComm) {
    this.posComm = posComm;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Position value
   */
  public BigDecimal getPosCost(){
    return posCost;
  }
  
  /**
   * @param posCost Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Position value
   */
  public void setPosCost(BigDecimal posCost) {
    this.posCost = posCost;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Manually added margin
   */
  public BigDecimal getPosCross(){
    return posCross;
  }
  
  /**
   * @param posCross Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Manually added margin
   */
  public void setPosCross(BigDecimal posCross) {
    this.posCross = posCross;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Position margin
   */
  public BigDecimal getPosInit(){
    return posInit;
  }
  
  /**
   * @param posInit Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Position margin
   */
  public void setPosInit(BigDecimal posInit) {
    this.posInit = posInit;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Manually added margin amount
   */
  public BigDecimal getPosLoss(){
    return posLoss;
  }
  
  /**
   * @param posLoss Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Manually added margin amount
   */
  public void setPosLoss(BigDecimal posLoss) {
    this.posLoss = posLoss;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Maintenance margin
   */
  public BigDecimal getPosMaint(){
    return posMaint;
  }
  
  /**
   * @param posMaint Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Maintenance margin
   */
  public void setPosMaint(BigDecimal posMaint) {
    this.posMaint = posMaint;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Position margin
   */
  public BigDecimal getPosMargin(){
    return posMargin;
  }
  
  /**
   * @param posMargin Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Position margin
   */
  public void setPosMargin(BigDecimal posMargin) {
    this.posMargin = posMargin;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Position size
   */
  public BigDecimal getQty(){
    return qty;
  }
  
  /**
   * @param qty Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Position size
   */
  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Leverage of the order
   */
  public BigDecimal getRealLeverage(){
    return realLeverage;
  }
  
  /**
   * @param realLeverage Provided when <subject>subject</strong> is <strong>position.change</strong>. Leverage of the order
   */
  public void setRealLeverage(BigDecimal realLeverage) {
    this.realLeverage = realLeverage;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Currently accumulated realised position value
   */
  public BigDecimal getRealisedCost(){
    return realisedCost;
  }
  
  /**
   * @param realisedCost Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Currently accumulated realised position value
   */
  public void setRealisedCost(BigDecimal realisedCost) {
    this.realisedCost = realisedCost;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Accumulated reliased gross profit value
   */
  public BigDecimal getRealisedGrossCost(){
    return realisedGrossCost;
  }
  
  /**
   * @param realisedGrossCost Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Accumulated reliased gross profit value
   */
  public void setRealisedGrossCost(BigDecimal realisedGrossCost) {
    this.realisedGrossCost = realisedGrossCost;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Accumulated realised profit and loss
   */
  public BigDecimal getRealisedGrossPnl(){
    return realisedGrossPnl;
  }
  
  /**
   * @param realisedGrossPnl Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Accumulated realised profit and loss
   */
  public void setRealisedGrossPnl(BigDecimal realisedGrossPnl) {
    this.realisedGrossPnl = realisedGrossPnl;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Realised profit and losts
   */
  public BigDecimal getRealisedPnl(){
    return realisedPnl;
  }
  
  /**
   * @param realisedPnl Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Realised profit and losts
   */
  public void setRealisedPnl(BigDecimal realisedPnl) {
    this.realisedPnl = realisedPnl;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Risk limit
   */
  public BigDecimal getRiskLimit(){
    return riskLimit;
  }
  
  /**
   * @param riskLimit Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Risk limit
   */
  public void setRiskLimit(BigDecimal riskLimit) {
    this.riskLimit = riskLimit;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.adjustRiskLimit</strong>. Current risk limit level
   */
  public BigDecimal getRiskLimitLevel(){
    return riskLimitLevel;
  }
  
  /**
   * @param riskLimitLevel Provided when <subject>subject</strong> is <strong>position.adjustRiskLimit</strong>. Current risk limit level
   */
  public void setRiskLimitLevel(BigDecimal riskLimitLevel) {
    this.riskLimitLevel = riskLimitLevel;
  }
  
  /**
   * @return Currency used to clear and settle the trades
   */
  public String getSettleCurrency(){
    return settleCurrency;
  }
  
  /**
   * @param settleCurrency Currency used to clear and settle the trades
   */
  public void setSettleCurrency(String settleCurrency) {
    this.settleCurrency = settleCurrency;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.adjustRiskLimit</strong>. Successful or not
   */
  public Boolean isSuccess(){
    return success;
  }
  
  /**
   * @param success Provided when <subject>subject</strong> is <strong>position.adjustRiskLimit</strong>. Successful or not
   */
  public void setSuccess(Boolean success) {
    this.success = success;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Symbol
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Symbol
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Current time (nanosecond)
   */
  public Long getTs(){
    return ts;
  }
  
  /**
   * @param ts Provided when <subject>subject</strong> is <strong>position.settlement</strong>. Current time (nanosecond)
   */
  public void setTs(Long ts) {
    this.ts = ts;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Unrealised value
   */
  public BigDecimal getUnrealisedCost(){
    return unrealisedCost;
  }
  
  /**
   * @param unrealisedCost Provided when <subject>subject</strong> is <strong>position.change</strong> and <strong>changeReason</strong> is <strong>position.change</strong>. Unrealised value
   */
  public void setUnrealisedCost(BigDecimal unrealisedCost) {
    this.unrealisedCost = unrealisedCost;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Unrealised profit and loss
   */
  public BigDecimal getUnrealisedPnl(){
    return unrealisedPnl;
  }
  
  /**
   * @param unrealisedPnl Provided when <subject>subject</strong> is <strong>position.change</strong>. Unrealised profit and loss
   */
  public void setUnrealisedPnl(BigDecimal unrealisedPnl) {
    this.unrealisedPnl = unrealisedPnl;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Position profit and loss ratio
   */
  public BigDecimal getUnrealisedPnlPcnt(){
    return unrealisedPnlPcnt;
  }
  
  /**
   * @param unrealisedPnlPcnt Provided when <subject>subject</strong> is <strong>position.change</strong>. Position profit and loss ratio
   */
  public void setUnrealisedPnlPcnt(BigDecimal unrealisedPnlPcnt) {
    this.unrealisedPnlPcnt = unrealisedPnlPcnt;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>position.change</strong>. Rate of return on investment
   */
  public BigDecimal getUnrealisedRoePcnt(){
    return unrealisedRoePcnt;
  }
  
  /**
   * @param unrealisedRoePcnt Provided when <subject>subject</strong> is <strong>position.change</strong>. Rate of return on investment
   */
  public void setUnrealisedRoePcnt(BigDecimal unrealisedRoePcnt) {
    this.unrealisedRoePcnt = unrealisedRoePcnt;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
