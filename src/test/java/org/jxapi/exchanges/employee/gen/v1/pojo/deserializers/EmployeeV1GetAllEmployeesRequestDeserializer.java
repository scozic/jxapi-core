package org.jxapi.exchanges.employee.gen.v1.pojo.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest instances
 * @see org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class EmployeeV1GetAllEmployeesRequestDeserializer extends AbstractJsonMessageDeserializer<EmployeeV1GetAllEmployeesRequest> {
  
  @Override
  public EmployeeV1GetAllEmployeesRequest deserialize(JsonParser parser) throws IOException {
    EmployeeV1GetAllEmployeesRequest msg = new EmployeeV1GetAllEmployeesRequest();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "page":
        msg.setPage(readNextInteger(parser));
      break;
      case "size":
        msg.setSize(readNextInteger(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
