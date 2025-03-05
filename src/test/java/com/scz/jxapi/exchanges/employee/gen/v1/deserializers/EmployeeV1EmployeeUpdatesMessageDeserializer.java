package com.scz.jxapi.exchanges.employee.gen.v1.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import javax.annotation.processing.Generated;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage instances
 * @see com.scz.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerGenerator")
public class EmployeeV1EmployeeUpdatesMessageDeserializer extends AbstractJsonMessageDeserializer<EmployeeV1EmployeeUpdatesMessage> {
  private final EmployeeDeserializer employeeDeserializer = new EmployeeDeserializer();
  
  @Override
  public EmployeeV1EmployeeUpdatesMessage deserialize(JsonParser parser) throws IOException {
    EmployeeV1EmployeeUpdatesMessage msg = new EmployeeV1EmployeeUpdatesMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "eventType":
        msg.setEventType(parser.nextTextValue());
      break;
      case "employee":
        parser.nextToken();
        msg.setEmployee(employeeDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
