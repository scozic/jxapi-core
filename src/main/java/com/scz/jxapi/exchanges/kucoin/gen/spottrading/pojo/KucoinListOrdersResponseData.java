package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinListOrdersResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.util.List;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinListOrdersResponseDataSerializer.class)
public class KucoinListOrdersResponseData {
  private Integer currentPage;
  private List<KucoinListOrdersResponseDataItems> items;
  private Integer pageSize;
  private Integer totalNum;
  private Integer totalPages;
  
  /**
   * @return Current page
   */
  public Integer getCurrentPage(){
    return currentPage;
  }
  
  /**
   * @param currentPage Current page
   */
  public void setCurrentPage(Integer currentPage) {
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
  public Integer getPageSize(){
    return pageSize;
  }
  
  /**
   * @param pageSize Request max number of items
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
  
  /**
   * @return Request max number of items
   */
  public Integer getTotalNum(){
    return totalNum;
  }
  
  /**
   * @param totalNum Request max number of items
   */
  public void setTotalNum(Integer totalNum) {
    this.totalNum = totalNum;
  }
  
  /**
   * @return Page count
   */
  public Integer getTotalPages(){
    return totalPages;
  }
  
  /**
   * @param totalPages Page count
   */
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
