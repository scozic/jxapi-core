package org.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.EmployeePaginatedRequest;
import org.jxapi.exchanges.employee.gen.v1.serializers.EmployeeV1GetAllEmployeesRequestSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Request for Employee V1 API getAllEmployees REST endpoint<br>
 * Get all employees
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.PojoGenerator")
@JsonSerialize(using = EmployeeV1GetAllEmployeesRequestSerializer.class)
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
   * @return Number of employees to return per page.<br> Defaults to <a href="V1org/jxapi/exchanges/employee/gen/EmployeeConstants.html#DEFAULT_PAGE_SIZE">defaultPageSize</a>.<br> Maximum is <a href="V1org/jxapi/exchanges/employee/gen/EmployeeConstants.html#MAX_PAGE_SIZE">maxPageSize</a>.
   * 
   */
  public Integer getSize() {
    return size;
  }
  
  /**
   * @param size Number of employees to return per page.<br> Defaults to <a href="V1org/jxapi/exchanges/employee/gen/EmployeeConstants.html#DEFAULT_PAGE_SIZE">defaultPageSize</a>.<br> Maximum is <a href="V1org/jxapi/exchanges/employee/gen/EmployeeConstants.html#MAX_PAGE_SIZE">maxPageSize</a>.
   * 
   */
  public void setSize(Integer size) {
    this.size = size;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    EmployeeV1GetAllEmployeesRequest o = (EmployeeV1GetAllEmployeesRequest) other;
    return Objects.equals(page, o.page)
            && Objects.equals(size, o.size);
  }
  
  @Override
  public int compareTo(EmployeeV1GetAllEmployeesRequest other) {
    if (other == null) {
      return 1;
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
     * @param size Number of employees to return per page.<br> Defaults to <a href="V1org/jxapi/exchanges/employee/gen/EmployeeConstants.html#DEFAULT_PAGE_SIZE">defaultPageSize</a>.<br> Maximum is <a href="V1org/jxapi/exchanges/employee/gen/EmployeeConstants.html#MAX_PAGE_SIZE">maxPageSize</a>.
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
