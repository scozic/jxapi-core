package com.scz.jxapi.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spottrading.serializers.BinanceAccountResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;
import java.util.List;

/**
 * Response to Binance SpotTrading API account REST endpoint request<br/>Get current account information.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#account-information-user_data">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceAccountResponseSerializer.class)
public class BinanceAccountResponse {
  private String accountType;
  private List<BinanceAccountResponseBalances> balances;
  private Boolean brokered;
  private Integer buyerCommission;
  private Boolean canDeposit;
  private Boolean canTrade;
  private Boolean canWithdraw;
  private BinanceAccountResponseCommissionRates commissionRates;
  private Integer makerCommission;
  private List<String> permissions;
  private Boolean requireSelfTradePrevention;
  private Integer sellerCommission;
  private Integer takerCommission;
  private Long updateTime;
  
  /**
   * @return 
   */
  public String getAccountType(){
    return accountType;
  }
  
  /**
   * @param accountType 
   */
  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }
  
  /**
   * @return Balances array
   */
  public List<BinanceAccountResponseBalances> getBalances(){
    return balances;
  }
  
  /**
   * @param balances Balances array
   */
  public void setBalances(List<BinanceAccountResponseBalances> balances) {
    this.balances = balances;
  }
  
  /**
   * @return 
   */
  public Boolean isBrokered(){
    return brokered;
  }
  
  /**
   * @param brokered 
   */
  public void setBrokered(Boolean brokered) {
    this.brokered = brokered;
  }
  
  /**
   * @return 
   */
  public Integer getBuyerCommission(){
    return buyerCommission;
  }
  
  /**
   * @param buyerCommission 
   */
  public void setBuyerCommission(Integer buyerCommission) {
    this.buyerCommission = buyerCommission;
  }
  
  /**
   * @return 
   */
  public Boolean isCanDeposit(){
    return canDeposit;
  }
  
  /**
   * @param canDeposit 
   */
  public void setCanDeposit(Boolean canDeposit) {
    this.canDeposit = canDeposit;
  }
  
  /**
   * @return 
   */
  public Boolean isCanTrade(){
    return canTrade;
  }
  
  /**
   * @param canTrade 
   */
  public void setCanTrade(Boolean canTrade) {
    this.canTrade = canTrade;
  }
  
  /**
   * @return 
   */
  public Boolean isCanWithdraw(){
    return canWithdraw;
  }
  
  /**
   * @param canWithdraw 
   */
  public void setCanWithdraw(Boolean canWithdraw) {
    this.canWithdraw = canWithdraw;
  }
  
  /**
   * @return 
   */
  public BinanceAccountResponseCommissionRates getCommissionRates(){
    return commissionRates;
  }
  
  /**
   * @param commissionRates 
   */
  public void setCommissionRates(BinanceAccountResponseCommissionRates commissionRates) {
    this.commissionRates = commissionRates;
  }
  
  /**
   * @return 
   */
  public Integer getMakerCommission(){
    return makerCommission;
  }
  
  /**
   * @param makerCommission 
   */
  public void setMakerCommission(Integer makerCommission) {
    this.makerCommission = makerCommission;
  }
  
  /**
   * @return 
   */
  public List<String> getPermissions(){
    return permissions;
  }
  
  /**
   * @param permissions 
   */
  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
  
  /**
   * @return 
   */
  public Boolean isRequireSelfTradePrevention(){
    return requireSelfTradePrevention;
  }
  
  /**
   * @param requireSelfTradePrevention 
   */
  public void setRequireSelfTradePrevention(Boolean requireSelfTradePrevention) {
    this.requireSelfTradePrevention = requireSelfTradePrevention;
  }
  
  /**
   * @return 
   */
  public Integer getSellerCommission(){
    return sellerCommission;
  }
  
  /**
   * @param sellerCommission 
   */
  public void setSellerCommission(Integer sellerCommission) {
    this.sellerCommission = sellerCommission;
  }
  
  /**
   * @return 
   */
  public Integer getTakerCommission(){
    return takerCommission;
  }
  
  /**
   * @param takerCommission 
   */
  public void setTakerCommission(Integer takerCommission) {
    this.takerCommission = takerCommission;
  }
  
  /**
   * @return 
   */
  public Long getUpdateTime(){
    return updateTime;
  }
  
  /**
   * @param updateTime 
   */
  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
