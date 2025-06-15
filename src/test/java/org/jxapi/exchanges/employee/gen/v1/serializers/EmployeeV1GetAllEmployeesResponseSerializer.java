package org.jxapi.exchanges.employee.gen.v1.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse
 * @see EmployeeV1GetAllEmployeesResponse
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class EmployeeV1GetAllEmployeesResponseSerializer extends StdSerializer<EmployeeV1GetAllEmployeesResponse> {
  public EmployeeV1GetAllEmployeesResponseSerializer() {
    super(EmployeeV1GetAllEmployeesResponse.class);
  }
  
  @Override
  public void serialize(EmployeeV1GetAllEmployeesResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getPage() != null){
      gen.writeNumberField("page", value.getPage());
    }
    if (value.getTotalPages() != null){
      gen.writeNumberField("totalPages", value.getTotalPages());
    }
    if (value.getEmployees() != null){
      gen.writeObjectField("employees", value.getEmployees());
    }
    gen.writeEndObject();
  }
}
