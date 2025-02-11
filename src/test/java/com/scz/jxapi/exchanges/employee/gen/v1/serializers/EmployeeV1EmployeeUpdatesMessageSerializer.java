package com.scz.jxapi.exchanges.employee.gen.v1.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see EmployeeV1EmployeeUpdatesMessage
 */
public class EmployeeV1EmployeeUpdatesMessageSerializer extends StdSerializer<EmployeeV1EmployeeUpdatesMessage> {
  public EmployeeV1EmployeeUpdatesMessageSerializer() {
    super(EmployeeV1EmployeeUpdatesMessage.class);
  }
  
  @Override
  public void serialize(EmployeeV1EmployeeUpdatesMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getEventType() != null){
      gen.writeStringField("eventType", String.valueOf(value.getEventType()));
    }
    if (value.getEmployee() != null){
      gen.writeObjectField("employee", value.getEmployee());
    }
    gen.writeEndObject();
  }
}
