package org.jxapi.exchanges.employee.gen.v1.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.employee.gen.v1.pojo.Employee instances
 * @see org.jxapi.exchanges.employee.gen.v1.pojo.Employee
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class EmployeeDeserializer extends AbstractJsonMessageDeserializer<Employee> {
  
  @Override
  public Employee deserialize(JsonParser parser) throws IOException {
    Employee msg = new Employee();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "id":
        msg.setId(readNextInteger(parser));
      break;
      case "firstName":
        msg.setFirstName(readNextString(parser));
      break;
      case "lastName":
        msg.setLastName(readNextString(parser));
      break;
      case "profile":
        msg.setProfile(readNextString(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
