package org.jxapi.exchanges.employee.gen.v1.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse instances
 * @see org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerGenerator")
public class EmployeeV1GetAllEmployeesResponseDeserializer extends AbstractJsonMessageDeserializer<EmployeeV1GetAllEmployeesResponse> {
  private final ListJsonFieldDeserializer<Employee> employeesDeserializer = new ListJsonFieldDeserializer<>(new EmployeeDeserializer());
  
  @Override
  public EmployeeV1GetAllEmployeesResponse deserialize(JsonParser parser) throws IOException {
    EmployeeV1GetAllEmployeesResponse msg = new EmployeeV1GetAllEmployeesResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "page":
        msg.setPage(readNextInteger(parser));
      break;
      case "totalPages":
        msg.setTotalPages(readNextInteger(parser));
      break;
      case "employees":
        parser.nextToken();
        msg.setEmployees(employeesDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
