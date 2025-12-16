package org.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.EmployeePaginatedResponse;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeV1GetAllEmployeesResponseDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.serializers.EmployeeV1GetAllEmployeesResponseSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Response object for Employee v1 API getAllEmployees REST endpoint<br>
 * Get all employees
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = EmployeeV1GetAllEmployeesResponseSerializer.class)
@JsonDeserialize(using = EmployeeV1GetAllEmployeesResponseDeserializer.class)
public class EmployeeV1GetAllEmployeesResponse implements Pojo<EmployeeV1GetAllEmployeesResponse>, EmployeePaginatedResponse {
  
  private static final long serialVersionUID = -4780534191544812805L;
  
  /**
   * @return A new builder to build {@link EmployeeV1GetAllEmployeesResponse} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private Integer page;
  private Integer totalPages;
  private List<Employee> employees;
  
  /**
   * @return Page index, starting from 1
   */
  public Integer getPage() {
    return page;
  }
  
  /**
   * @param page Page index, starting from 1
   */
  public void setPage(Integer page) {
    this.page = page;
  }
  
  /**
   * @return Total number of pages available
   */
  public Integer getTotalPages() {
    return totalPages;
  }
  
  /**
   * @param totalPages Total number of pages available
   */
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }
  
  public List<Employee> getEmployees() {
    return employees;
  }
  
  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
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
    EmployeeV1GetAllEmployeesResponse o = (EmployeeV1GetAllEmployeesResponse) other;
    return Objects.equals(this.page, o.page)
        && Objects.equals(this.totalPages, o.totalPages)
        && Objects.equals(this.employees, o.employees);
  }
  
  @Override
  public int compareTo(EmployeeV1GetAllEmployeesResponse other) {
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
    res = CompareUtil.compare(this.totalPages, other.totalPages);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.employees, other.employees, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(page, totalPages, employees);
  }
  
  @Override
  public EmployeeV1GetAllEmployeesResponse deepClone() {
    EmployeeV1GetAllEmployeesResponse clone = new EmployeeV1GetAllEmployeesResponse();
    clone.page = this.page;
    clone.totalPages = this.totalPages;
    clone.employees = CollectionUtil.deepCloneList(this.employees, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link EmployeeV1GetAllEmployeesResponse}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private Integer page;
    private Integer totalPages;
    private List<Employee> employees;
    
    /**
     * Will set the value of <code>page</code> field in the builder
     * @param page Page index, starting from 1
     * @return Builder instance
     * @see #setPage(Integer)
     */
    public Builder page(Integer page)  {
      this.page = page;
      return this;
    }
    
    /**
     * Will set the value of <code>totalPages</code> field in the builder
     * @param totalPages Total number of pages available
     * @return Builder instance
     * @see #setTotalPages(Integer)
     */
    public Builder totalPages(Integer totalPages)  {
      this.totalPages = totalPages;
      return this;
    }
    
    /**
     * Will set the value of <code>employees</code> field in the builder
     * @return Builder instance
     * @see #setEmployees(List<Employee>)
     */
    public Builder employees(List<Employee> employees)  {
      this.employees = employees;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>employees</code> list.
     * @param item Item to add to current <code>employees</code> list
     * @return Builder instance
     * @see EmployeeV1GetAllEmployeesResponse#setEmployees(List)
     */
    public Builder addToEmployees(Employee item) {
      if (this.employees == null) {
        this.employees = CollectionUtil.createList();
      }
      this.employees.add(item);
      return this;
    }
    
    /**
     * @return a new instance of EmployeeV1GetAllEmployeesResponse using the values set in this builder
     */
    public EmployeeV1GetAllEmployeesResponse build() {
      EmployeeV1GetAllEmployeesResponse res = new EmployeeV1GetAllEmployeesResponse();
      res.page = this.page;
      res.totalPages = this.totalPages;
      res.employees = CollectionUtil.deepCloneList(this.employees, DeepCloneable::deepClone);
      return res;
    }
  }
}
