package org.jxapi.exchanges.employee;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;

/**
 * Unit test for {@link EmployeePaginatedResponse}.
 */
public class EmployeePaginatedResponseTest {

  @Test
  public void testHasNextPage() {
    EmployeePaginatedResponse response = new EmployeeV1GetAllEmployeesResponse();
    response.setPage(4);
    response.setTotalPages(5);
    Assert.assertTrue(response.hasNextPage());
    response.setPage(5);
    Assert.assertFalse(response.hasNextPage());
    response.setPage(null);
    response.setTotalPages(null);
    Assert.assertFalse(response.hasNextPage());
  }
}
