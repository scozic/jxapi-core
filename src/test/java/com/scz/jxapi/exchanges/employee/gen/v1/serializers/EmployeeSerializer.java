package com.scz.jxapi.exchanges.employee.gen.v1.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see Employee
 */
public class EmployeeSerializer extends StdSerializer<Employee> {
  public EmployeeSerializer() {
    super(Employee.class);
  }
  
  @Override
  public void serialize(Employee value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getId() != null){
      gen.writeNumberField("id", value.getId());
    }
    if (value.getFirstName() != null){
      gen.writeStringField("firstName", String.valueOf(value.getFirstName()));
    }
    if (value.getLastName() != null){
      gen.writeStringField("lastName", String.valueOf(value.getLastName()));
    }
    if (value.getProfile() != null){
      gen.writeStringField("profile", String.valueOf(value.getProfile()));
    }
    gen.writeEndObject();
  }
}
