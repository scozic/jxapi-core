package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinListOrdersResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinListOrdersResponseDataSerializer.class)
public class KucoinListOrdersResponseData {
  private int currentPage;
  private List<KucoinListOrdersResponseDataItems> items;
  private int pageSize;
  private int totalNum;
  private int totalPages;
  
  /**
   * @return Current page
   */
  public int getCurrentPage(){
    return currentPage;
  }
  
  /**
   * @param currentPage Current page
   */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }
  
  /**
   * @return Order list
   */
  public List<KucoinListOrdersResponseDataItems> getItems(){
    return items;
  }
  
  /**
   * @param items Order list
   */
  public void setItems(List<KucoinListOrdersResponseDataItems> items) {
    this.items = items;
  }
  
  /**
   * @return Request max number of items
   */
  public int getPageSize(){
    return pageSize;
  }
  
  /**
   * @param pageSize Request max number of items
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  
  /**
   * @return Request max number of items
   */
  public int getTotalNum(){
    return totalNum;
  }
  
  /**
   * @param totalNum Request max number of items
   */
  public void setTotalNum(int totalNum) {
    this.totalNum = totalNum;
  }
  
  /**
   * @return Page count
   */
  public int getTotalPages(){
    return totalPages;
  }
  
  /**
   * @param totalPages Page count
   */
  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
