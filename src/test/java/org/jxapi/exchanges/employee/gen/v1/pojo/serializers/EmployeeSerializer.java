package org.jxapi.exchanges.employee.gen.v1.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeIntField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.employee.gen.v1.pojo.Employee
 * @see Employee
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class EmployeeSerializer extends AbstractJsonValueSerializer<Employee> {
  
  private static final long serialVersionUID = -2778746334669544835L;
  
  /**
   * Constructor
   */
  public EmployeeSerializer() {
    super(Employee.class);
  }
  
  
  @Override
  public void serialize(Employee value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeIntField(gen, "id", value.getId());
    writeStringField(gen, "firstName", value.getFirstName());
    writeStringField(gen, "lastName", value.getLastName());
    writeStringField(gen, "profile", value.getProfile());
    gen.writeEndObject();
  }
}
