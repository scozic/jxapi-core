package org.jxapi.exchanges.employee;

import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;

/**
 * Interface representing a paginated response for employee data.
 * <p>
 * This interface extends {@link PaginatedRestResponse} to provide pagination
 * functionality specifically for employee-related responses.
 */
public interface EmployeePaginatedResponse extends PaginatedRestResponse {

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
   * Gets the total number of pages available.
   *
   * @return the total number of pages, or {@code null} if not set
   */
  Integer getTotalPages();
  
  /**
   * Sets the total number of pages available.
   *
   * @param totalPages the total number of pages to set
   */
  void setTotalPages(Integer totalPages);
  
  default boolean hasNextPage() {
    return getPage() != null && getTotalPages() != null && getPage() < getTotalPages();
  }
}
