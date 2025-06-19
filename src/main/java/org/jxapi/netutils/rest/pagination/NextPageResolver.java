package org.jxapi.netutils.rest.pagination;

import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.RestResponse;

/**
 * Interface for resolving the next page of a paginated REST response.
 * <p>
 * This interface defines a method to fetch the next page based on the previous
 * page's response.
 *
 * @param <A> the type of the paginated REST response
 * @see PaginatedRestResponse
 * @see PaginationUtil#getNextPageResolver(PaginatedRestRequest, java.util.function.Function)
 * 
 */
public interface NextPageResolver<A> {

  /**
   * Fetches the next page based on the previous page's response.
   * <p>
   * Implementations of this method should use the provided previous page's
   * response to build a new request for the next page, and submit it to the same
   * REST endpoint.
   *
   * @param prevPage the previous page's response
   * @return a FutureRestResponse containing the next page's response
   */
  FutureRestResponse<A> nextPage(RestResponse<A> prevPage);
}
