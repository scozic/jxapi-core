package org.jxapi.netutils.rest.pagination;

import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.RestResponse;

public interface NextPageResolver<A> {

  FutureRestResponse<A> nextPage(RestResponse<A> prevPage);
}
