package org.jxapi.exchanges.employee.gen.v1.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeIntField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse
 * @see EmployeeV1GetAllEmployeesResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class EmployeeV1GetAllEmployeesResponseSerializer extends AbstractJsonValueSerializer<EmployeeV1GetAllEmployeesResponse> {
  
  private static final long serialVersionUID = 6108312058755913041L;
  
  /**
   * Constructor
   */
  public EmployeeV1GetAllEmployeesResponseSerializer() {
    super(EmployeeV1GetAllEmployeesResponse.class);
  }
  
  private ListJsonValueSerializer<Employee> employeesSerializer;
  
  @Override
  public void serialize(EmployeeV1GetAllEmployeesResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeIntField(gen, "page", value.getPage());
    writeIntField(gen, "totalPages", value.getTotalPages());
    if(employeesSerializer == null) {
      employeesSerializer = new ListJsonValueSerializer<>(new EmployeeSerializer());
    }
    writeCustomSerializerField(gen, "employees", value.getEmployees(), employeesSerializer, provider);
    gen.writeEndObject();
  }
}
