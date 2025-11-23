package org.jxapi.exchanges.employee.gen.v1.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest
 * @see EmployeeV1GetAllEmployeesRequest
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class EmployeeV1GetAllEmployeesRequestSerializer extends StdSerializer<EmployeeV1GetAllEmployeesRequest> {
  /**
   * Constructor
   */
  public EmployeeV1GetAllEmployeesRequestSerializer() {
    super(EmployeeV1GetAllEmployeesRequest.class);
  }
  
  @Override
  public void serialize(EmployeeV1GetAllEmployeesRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getPage() != null){
      gen.writeNumberField("page", value.getPage());
    }
    if (value.getSize() != null){
      gen.writeNumberField("size", value.getSize());
    }
    gen.writeEndObject();
  }
}
