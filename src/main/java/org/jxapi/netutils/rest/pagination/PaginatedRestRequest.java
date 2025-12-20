package org.jxapi.netutils.rest.pagination;

/**
 * Interface for paginated REST requests.
 * <p>
 * This interface allows setting the next page for a paginated request, using
 * the previous page's response.
 * <p>
 * The pagination API to provide wrapper simple pagination functionality expects
 * wrappers to provide custom sub-interface with default implementation of
 * {@link #setNextPage(PaginatedRestResponse)}, assuming provided
 * {@link PaginatedRestResponse} as argument is of custom sub-interface type with
 * common fields for guessing next page offset.
 */
public interface PaginatedRestRequest {

  /**
   * Sets this request so it fetches next page for the paginated request based on the last page's response.
   * @param lastPage the last page's response, which contains information about the next page.
   */
  void setNextPage(PaginatedRestResponse lastPage);
}
