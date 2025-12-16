package org.jxapi.pojo.descriptor;

import java.util.List;

import org.jxapi.util.EncodingUtil;

/**
 * Descriptor for a list of POJOs.
 * This object is root of document describing POJOs. 
 */
public class PojosDescriptor {
  
  private String basePackage;

  private List<Field> pojos;
  
  /**
   * Gets the base package for the generated POJOs.
   * 
   * @return the base package, for example, <code>com.x.y.gen.pojo</code>
   */
  public String getBasePackage() {
    return basePackage;
  }

  /**
   * Sets the base package for the generated POJOs.
   * @param basePackage the base package to set, for example, <code>com.x.y.gen.pojo</code>
   */
  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  /**
   * Gets the list of POJOs.
   * @return the list of POJOs
   */
  public List<Field> getPojos() {
    return pojos;
  }

  /**
   * Sets the list of POJOs.
   * 
   * @param pojos the list of POJOs to set
   */
  public void setPojos(List<Field> pojos) {
    this.pojos = pojos;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
