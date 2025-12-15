package org.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.deserializers.EmployeeDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.serializers.EmployeeSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Employee details
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = EmployeeSerializer.class)
@JsonDeserialize(using = EmployeeDeserializer.class)
public class Employee implements Pojo<Employee> {
  
  private static final long serialVersionUID = 607196601431828222L;
  
  /**
   * @return A new builder to build {@link Employee} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private Integer id;
  private String firstName;
  private String lastName;
  private String profile;
  
  /**
   * @return Employee ID
   */
  public Integer getId() {
    return id;
  }
  
  /**
   * @param id Employee ID
   */
  public void setId(Integer id) {
    this.id = id;
  }
  
  /**
   * @return Employee first name
   */
  public String getFirstName() {
    return firstName;
  }
  
  /**
   * @param firstName Employee first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  /**
   * @return Employee last name
   */
  public String getLastName() {
    return lastName;
  }
  
  /**
   * @param lastName Employee last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  /**
   * @return Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}
   */
  public String getProfile() {
    return profile;
  }
  
  /**
   * @param profile Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}
   */
  public void setProfile(String profile) {
    this.profile = profile;
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
    Employee o = (Employee) other;
    return Objects.equals(this.id, o.id)
        && Objects.equals(this.firstName, o.firstName)
        && Objects.equals(this.lastName, o.lastName)
        && Objects.equals(this.profile, o.profile);
  }
  
  @Override
  public int compareTo(Employee other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.id, other.id);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.firstName, other.firstName);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.lastName, other.lastName);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.profile, other.profile);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, profile);
  }
  
  @Override
  public Employee deepClone() {
    Employee clone = new Employee();
    clone.id = this.id;
    clone.firstName = this.firstName;
    clone.lastName = this.lastName;
    clone.profile = this.profile;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link Employee}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String profile;
    
    /**
     * Will set the value of <code>id</code> field in the builder
     * @param id Employee ID
     * @return Builder instance
     * @see #setId(Integer)
     */
    public Builder id(Integer id)  {
      this.id = id;
      return this;
    }
    
    /**
     * Will set the value of <code>firstName</code> field in the builder
     * @param firstName Employee first name
     * @return Builder instance
     * @see #setFirstName(String)
     */
    public Builder firstName(String firstName)  {
      this.firstName = firstName;
      return this;
    }
    
    /**
     * Will set the value of <code>lastName</code> field in the builder
     * @param lastName Employee last name
     * @return Builder instance
     * @see #setLastName(String)
     */
    public Builder lastName(String lastName)  {
      this.lastName = lastName;
      return this;
    }
    
    /**
     * Will set the value of <code>profile</code> field in the builder
     * @param profile Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}
     * @return Builder instance
     * @see #setProfile(String)
     */
    public Builder profile(String profile)  {
      this.profile = profile;
      return this;
    }
    
    /**
     * @return a new instance of Employee using the values set in this builder
     */
    public Employee build() {
      Employee res = new Employee();
      res.id = this.id;
      res.firstName = this.firstName;
      res.lastName = this.lastName;
      res.profile = this.profile;
      return res;
    }
  }
}
