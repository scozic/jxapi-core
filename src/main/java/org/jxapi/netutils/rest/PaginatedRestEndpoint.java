package org.jxapi.netutils.rest;

import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.netutils.rest.pagination.PaginationUtil;

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
