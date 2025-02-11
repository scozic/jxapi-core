package com.scz.jxapi.exchanges.employee.gen.v1.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static com.scz.jxapi.util.JsonUtil.readNextInteger;
import static com.scz.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.employee.gen.v1.pojo.Employee
 */
public class EmployeeDeserializer extends AbstractJsonMessageDeserializer<Employee> {
  
  @Override
  public Employee deserialize(JsonParser parser) throws IOException {
    Employee msg = new Employee();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "id":
        msg.setId(readNextInteger(parser));
      break;
      case "firstName":
        msg.setFirstName(parser.nextTextValue());
      break;
      case "lastName":
        msg.setLastName(parser.nextTextValue());
      break;
      case "profile":
        msg.setProfile(parser.nextTextValue());
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
