package org.jxapi.exchanges.employee.gen.v1.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeIntField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest
 * @see EmployeeV1GetAllEmployeesRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class EmployeeV1GetAllEmployeesRequestSerializer extends AbstractJsonValueSerializer<EmployeeV1GetAllEmployeesRequest> {
  
  private static final long serialVersionUID = -7259729291763000435L;
  
  /**
   * Constructor
   */
  public EmployeeV1GetAllEmployeesRequestSerializer() {
    super(EmployeeV1GetAllEmployeesRequest.class);
  }
  
  
  @Override
  public void serialize(EmployeeV1GetAllEmployeesRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeIntField(gen, "page", value.getPage());
    writeIntField(gen, "size", value.getSize());
    gen.writeEndObject();
  }
}
