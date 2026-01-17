package org.jxapi.exchanges.employee.gen.v1.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage
 * @see EmployeeV1EmployeeUpdatesMessage
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class EmployeeV1EmployeeUpdatesMessageSerializer extends AbstractJsonValueSerializer<EmployeeV1EmployeeUpdatesMessage> {
  
  private static final long serialVersionUID = 2964408413682863742L;
  
  /**
   * Constructor
   */
  public EmployeeV1EmployeeUpdatesMessageSerializer() {
    super(EmployeeV1EmployeeUpdatesMessage.class);
  }
  
  private EmployeeSerializer employeeSerializer;
  
  @Override
  public void serialize(EmployeeV1EmployeeUpdatesMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "eventType", value.getEventType());
    if(employeeSerializer == null) {
      employeeSerializer = new EmployeeSerializer();
    }
    writeCustomSerializerField(gen, "employee", value.getEmployee(), employeeSerializer, provider);
    gen.writeEndObject();
  }
}
