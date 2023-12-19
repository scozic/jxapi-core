package com.x;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.util.EncodingUtil;
import com.x.serializers.MyPojoSerializer;


@JsonSerialize(using = MyPojoSerializer.class)
public class MyPojo {
  private String id;
  
  /**
   * @return Toto ID
   */
  public String getId() {
    return id;
  }
  
  /**
   * @param id Toto ID
   */
  public void setId(String id) {
    this.id = id;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
