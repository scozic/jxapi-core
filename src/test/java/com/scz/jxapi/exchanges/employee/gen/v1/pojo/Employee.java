package com.scz.jxapi.exchanges.employee.gen.v1.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.employee.gen.v1.serializers.EmployeeSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to Employee V1 API <br>
 * getEmployee REST endpoint request<br>
 * Get employee details by ID
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = EmployeeSerializer.class)
public class Employee {
  private String firstName;
  private Integer id;
  private String lastName;
  private String profile;
  
  /**
   * @return Employee First Name
   */
  public String getFirstName() {
    return firstName;
  }
  
  /**
   * @param firstName Employee First Name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
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
   * @return Employee last lame
   */
  public String getLastName() {
    return lastName;
  }
  
  /**
   * @param lastName Employee last lame
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  /**
   * @return Employee profile. Can be 'regular' or 'admin'
   */
  public String getProfile() {
    return profile;
  }
  
  /**
   * @param profile Employee profile. Can be 'regular' or 'admin'
   */
  public void setProfile(String profile) {
    this.profile = profile;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    Employee o = (Employee) other;
    return Objects.equals(firstName, o.firstName)
            && Objects.equals(id, o.id)
            && Objects.equals(lastName, o.lastName)
            && Objects.equals(profile, o.profile);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(firstName, id, lastName, profile);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
