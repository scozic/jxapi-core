package org.jxapi.exchanges.employee;

import java.util.Optional;

import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;

/**
 * Interface representing a paginated request for employee data.
 * <p>
 * This interface extends {@link PaginatedRestRequest} to provide pagination
 * functionality specifically for employee-related requests.
 */
public interface EmployeePaginatedRequest extends PaginatedRestRequest {
  
  /**
   * Gets the current page number.
   *
   * @return the current page number, or {@code null} if not set
   */
  Integer getPage();
  
  /**
   * Sets the current page number.
   *
   * @param page the page number to set, starting from 1
   */
  void setPage(Integer page);
  
  /**
   * @return the number of items per page, defaults to 10 if not set
   */
  Integer getSize();
  
  /**
   * Sets the number of items per page.
   *
   * @param size the number of items to return per page, defaults to 10
   */
  void setSize(Integer size);
  
  @Override
  default void setNextPage(PaginatedRestResponse lastPage) {
    EmployeePaginatedResponse p = (EmployeePaginatedResponse) lastPage;
    if (p != null) {
      setPage(Optional.ofNullable(p.getPage()).orElse(Integer.valueOf(1)) + 1);
    } else {
      setPage(Integer.valueOf(1));
    }
  }
}
