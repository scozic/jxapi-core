package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceAccountResponseSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Binance SpotTrading API account REST endpoint request<br/>Get current account information.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#account-information-user_data">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceAccountResponseSerializer.class)
public class BinanceAccountResponse {
  private String accountType;
  private List<BinanceAccountResponseBalances> balances;
  private boolean brokered;
  private int buyerCommission;
  private boolean canDeposit;
  private boolean canTrade;
  private boolean canWithdraw;
  private BinanceAccountResponseCommissionRates commissionRates;
  private int makerCommission;
  private List<String> permissions;
  private boolean requireSelfTradePrevention;
  private int sellerCommission;
  private int takerCommission;
  private long updateTime;
  
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
  public boolean isBrokered(){
    return brokered;
  }
  
  /**
   * @param brokered 
   */
  public void setBrokered(boolean brokered) {
    this.brokered = brokered;
  }
  
  /**
   * @return 
   */
  public int getBuyerCommission(){
    return buyerCommission;
  }
  
  /**
   * @param buyerCommission 
   */
  public void setBuyerCommission(int buyerCommission) {
    this.buyerCommission = buyerCommission;
  }
  
  /**
   * @return 
   */
  public boolean isCanDeposit(){
    return canDeposit;
  }
  
  /**
   * @param canDeposit 
   */
  public void setCanDeposit(boolean canDeposit) {
    this.canDeposit = canDeposit;
  }
  
  /**
   * @return 
   */
  public boolean isCanTrade(){
    return canTrade;
  }
  
  /**
   * @param canTrade 
   */
  public void setCanTrade(boolean canTrade) {
    this.canTrade = canTrade;
  }
  
  /**
   * @return 
   */
  public boolean isCanWithdraw(){
    return canWithdraw;
  }
  
  /**
   * @param canWithdraw 
   */
  public void setCanWithdraw(boolean canWithdraw) {
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
  public int getMakerCommission(){
    return makerCommission;
  }
  
  /**
   * @param makerCommission 
   */
  public void setMakerCommission(int makerCommission) {
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
  public boolean isRequireSelfTradePrevention(){
    return requireSelfTradePrevention;
  }
  
  /**
   * @param requireSelfTradePrevention 
   */
  public void setRequireSelfTradePrevention(boolean requireSelfTradePrevention) {
    this.requireSelfTradePrevention = requireSelfTradePrevention;
  }
  
  /**
   * @return 
   */
  public int getSellerCommission(){
    return sellerCommission;
  }
  
  /**
   * @param sellerCommission 
   */
  public void setSellerCommission(int sellerCommission) {
    this.sellerCommission = sellerCommission;
  }
  
  /**
   * @return 
   */
  public int getTakerCommission(){
    return takerCommission;
  }
  
  /**
   * @param takerCommission 
   */
  public void setTakerCommission(int takerCommission) {
    this.takerCommission = takerCommission;
  }
  
  /**
   * @return 
   */
  public long getUpdateTime(){
    return updateTime;
  }
  
  /**
   * @param updateTime 
   */
  public void setUpdateTime(long updateTime) {
    this.updateTime = updateTime;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
