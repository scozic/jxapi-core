package org.jxapi.netutils.rest.pagination;

/**
 * Interface representing a paginated REST response.
 * <p>
 * This interface provides a method to check if there is a next page available
 * in the response.
 * <p>
 * The pagination API to provide wrapper simple pagination functionality expects
 * wrappers to provide custom sub-interface of this interface with
 * {@link #hasNextPage()} default implementation, current page index field
 * accessors, and custom {@link PaginatedRestRequest} sub-interface with default
 * implementation of {@link PaginatedRestRequest#setNextPage(RestResponsePage)}
 * that will set desired next page index to request for fetching next page.
 * 
 * @see PaginatedRestRequest
 */
public interface PaginatedRestResponse {

  /**
   * Checks if there is a next page available in the response.
   *
   * @return {@code true} if there is a next page, {@code false} otherwise
   */
  boolean hasNextPage();
}
