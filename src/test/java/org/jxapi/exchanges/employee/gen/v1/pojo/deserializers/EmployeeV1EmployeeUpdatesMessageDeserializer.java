package org.jxapi.exchanges.employee.gen.v1.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage instances
 * @see org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class EmployeeV1EmployeeUpdatesMessageDeserializer extends AbstractJsonMessageDeserializer<EmployeeV1EmployeeUpdatesMessage> {
  private EmployeeDeserializer employeeDeserializer;
  
  @Override
  public EmployeeV1EmployeeUpdatesMessage deserialize(JsonParser parser) throws IOException {
    EmployeeV1EmployeeUpdatesMessage msg = new EmployeeV1EmployeeUpdatesMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "eventType":
        msg.setEventType(readNextString(parser));
      break;
      case "employee":
        parser.nextToken();
        if(employeeDeserializer == null) {
          employeeDeserializer = new EmployeeDeserializer();
        }
        msg.setEmployee(employeeDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
