package org.jxapi.netutils.rest;

import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.netutils.rest.pagination.PaginationUtil;

/**
 * A REST endpoint that supports pagination.<p>
 * This class extends {@link RestEndpoint} and overrides the submit method to
 * handle paginated requests using the {@link PaginationUtil} to automatically.
 * </p>
 * Request should implement {@link PaginatedRestRequest} and responses should
 * implement {@link PaginatedRestResponse}.
 *
 * @param <R> The type of the paginated REST request
 * @param <A> The type of the paginated REST response
 */
public class PaginatedRestEndpoint<R extends PaginatedRestRequest, A extends PaginatedRestResponse> extends RestEndpoint<R, A> {

  @Override
  public FutureRestResponse<A> submit(R request) {
    return submit(request, PaginationUtil.getNextPageResolver(request, this::submit));
  }
  
  @Override
  public boolean isPaginated() {
    return true;
  }

}
