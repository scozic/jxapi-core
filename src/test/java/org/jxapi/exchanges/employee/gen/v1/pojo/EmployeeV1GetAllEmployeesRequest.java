package org.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.EmployeePaginatedRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeV1GetAllEmployeesRequestDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.serializers.EmployeeV1GetAllEmployeesRequestSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Page request parameters for 'getAllEmployees' rest endpoint paginated requests.
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = EmployeeV1GetAllEmployeesRequestSerializer.class)
@JsonDeserialize(using = EmployeeV1GetAllEmployeesRequestDeserializer.class)
public class EmployeeV1GetAllEmployeesRequest implements Pojo<EmployeeV1GetAllEmployeesRequest>, EmployeePaginatedRequest {
  
  private static final long serialVersionUID = -7439815753884330991L;
  
  /**
   * @return A new builder to build {@link EmployeeV1GetAllEmployeesRequest} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private Integer page;
  private Integer size;
  
  /**
   * @return Page number to return, defaults to 1.
   */
  public Integer getPage() {
    return page;
  }
  
  /**
   * @param page Page number to return, defaults to 1.
   */
  public void setPage(Integer page) {
    this.page = page;
  }
  
  /**
   * @return Number of employees to return per page.<br> Defaults to {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#DEFAULT_PAGE_SIZE}.<br> Maximum is {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#MAX_PAGE_SIZE}.
   * 
   */
  public Integer getSize() {
    return size;
  }
  
  /**
   * @param size Number of employees to return per page.<br> Defaults to {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#DEFAULT_PAGE_SIZE}.<br> Maximum is {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#MAX_PAGE_SIZE}.
   * 
   */
  public void setSize(Integer size) {
    this.size = size;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    EmployeeV1GetAllEmployeesRequest o = (EmployeeV1GetAllEmployeesRequest) other;
    return Objects.equals(this.page, o.page)
        && Objects.equals(this.size, o.size);
  }
  
  @Override
  public int compareTo(EmployeeV1GetAllEmployeesRequest other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.page, other.page);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.size, other.size);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(page, size);
  }
  
  @Override
  public EmployeeV1GetAllEmployeesRequest deepClone() {
    EmployeeV1GetAllEmployeesRequest clone = new EmployeeV1GetAllEmployeesRequest();
    clone.page = this.page;
    clone.size = this.size;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link EmployeeV1GetAllEmployeesRequest}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private Integer page;
    private Integer size;
    
    /**
     * Will set the value of <code>page</code> field in the builder
     * @param page Page number to return, defaults to 1.
     * @return Builder instance
     * @see #setPage(Integer)
     */
    public Builder page(Integer page)  {
      this.page = page;
      return this;
    }
    
    /**
     * Will set the value of <code>size</code> field in the builder
     * @param size Number of employees to return per page.<br> Defaults to {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#DEFAULT_PAGE_SIZE}.<br> Maximum is {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#MAX_PAGE_SIZE}.
     * 
     * @return Builder instance
     * @see #setSize(Integer)
     */
    public Builder size(Integer size)  {
      this.size = size;
      return this;
    }
    
    /**
     * @return a new instance of EmployeeV1GetAllEmployeesRequest using the values set in this builder
     */
    public EmployeeV1GetAllEmployeesRequest build() {
      EmployeeV1GetAllEmployeesRequest res = new EmployeeV1GetAllEmployeesRequest();
      res.page = this.page;
      res.size = this.size;
      return res;
    }
  }
}
