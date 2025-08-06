package org.jxapi.exchanges.employee;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;

/**
 * Unit test for {@link EmployeePaginatedRequest}.
 */
public class EmployeePaginatedRequestTest {

  @Test
  public void testSetNextPage() {
    
    EmployeePaginatedRequest request = EmployeeV1GetAllEmployeesRequest.builder().build();
    Assert.assertNull(request.getPage());
    request.setNextPage(null);
    Assert.assertEquals(Integer.valueOf(1), request.getPage());
    
    EmployeeV1GetAllEmployeesResponse response = new EmployeeV1GetAllEmployeesResponse();
    request.setNextPage(response);
    Assert.assertNotNull(request.getPage());
    Assert.assertEquals(Integer.valueOf(2), request.getPage());
    
    response.setPage(5);
    request.setNextPage(response);
    Assert.assertNotNull(request.getPage());
    Assert.assertEquals(Integer.valueOf(6), request.getPage());
  }

}
