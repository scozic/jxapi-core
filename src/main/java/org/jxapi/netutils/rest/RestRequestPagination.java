package org.jxapi.netutils.rest;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Helper methods around REST request where response may come in multiple
 * "pages". It is common that API used to fetch large number of items provides
 * request property "index" to indicate page index, and response property
 * "index" indicating the next page index, or none if last page is found. 
 */
public class RestRequestPagination {
  
  private static final Logger log = LoggerFactory.getLogger(RestRequestPagination.class);
  
  private RestRequestPagination() {}
  
  /**
   * Fetches all pages of response to given request.
   * Example:
   * <pre>{@code
   * // api: Interface exposing method FutureRestResponse<BybitV5GetPositionInfoResponse> getPositionInfo(BybitV5GetPositionInfoRequest request)
   * // request: Request object prepared
   * FutureRestResponse<BybitV5GetPositionInfoResponse> response = RestRequestPagination.fetchAllPages(         
     *    request, // Initial request to send
     *    api::getPositionInfo, // API method: 
     *    (index, r) -> r.setCursor(index), // Method to set next request page index.
     *    r -> { String index = r.getResult().getNextPageCursor(); return (index == null || index.isEmpty())? null : index;}, // Method to retrieve next page index from a single page response.  
     *    (r1, r2) -> // Accumulator to merge results and create response object containing both page items.
     *        {
     *            List<BybitV5PositionData> l1 = r1.getResult().getList();
     *            List<BybitV5PositionData> l2 = r2.getResult().getList();
     *            List<BybitV5PositionData> l = new ArrayList<>(l1.size() + l2.size());
     *            l.addAll(l1);
     *            l.addAll(l2);
     *            r2.getResult().setList(l);
     *            return r2;
     *         });
   * }</pre>
   * 
   * @param request             the request to send with initial page index set. 
   *                 If multiple response pages are found,
   *                            this object will be reused with next page index to
   *                            process next request.
   * @param endpoint            actual request endpoint to fetch one page of data
   * @param setRequestIndex     function to set requested {@link String} index of
   *                            a single page request
   * @param getResponseIndex    function to retrieve next page index from single
   *                            page response. When last page is found, that
   *                            function must return <code>null</code>.
   * @param responseAccumulator function to merge two results in one with both items.
   * @return response that will complete when all pages are fetched, with
   *         accumulated results. If an error occurs when fetching one page, the
   *         response returned is the error one.
   * @param <R> Type of request
   * @param <A> Type of response data in {@link FutureRestResponse}        
   *         
   */
  public static <R, A> FutureRestResponse<A> fetchAllPages(R request, 
                                Function<R, FutureRestResponse<A>> endpoint, 
                                BiConsumer<String, R> setRequestIndex, 
                                Function<A, String> getResponseIndex,
                                BinaryOperator<A> responseAccumulator) {
    return fetchAllPages(request, endpoint, setRequestIndex, getResponseIndex, responseAccumulator, null);
  }
  
  private static <R, A> FutureRestResponse<A> fetchAllPages(R request, 
                                 Function<R, FutureRestResponse<A>> endpoint, 
                                 BiConsumer<String, R> setRequestIndex, 
                                 Function<A, String> getResponseIndex,
                                 BinaryOperator<A> responseAccumulator,
                                 A cumulativeResult) {
    FutureRestResponse<A> response = new FutureRestResponse<>();
    log.debug("Fetching page for request:{}", request);
    endpoint.apply(request).thenAccept(responsePage -> {
      try {
         log.debug("Got response to request:{}:{}", request, responsePage);
        if (!responsePage.isOk()) {
          response.complete(responsePage);
        } else {
           A page = responsePage.getResponse();
           if (cumulativeResult != null) {
             responsePage.setResponse(responseAccumulator.apply(cumulativeResult, page));
           }
           String nextPageIndex = getResponseIndex.apply(page);
           log.debug("Next page index:{} in response to request:{}", nextPageIndex, request);
           if (nextPageIndex != null) {
             // Fetch next page
             setRequestIndex.accept(nextPageIndex, request);
             fetchAllPages(request, 
                           endpoint, 
                           setRequestIndex, 
                           getResponseIndex, 
                           responseAccumulator, 
                           responsePage.getResponse()).thenAccept(response::complete);
           } else {
             // last page found
             response.complete(responsePage);
           }
        }
      } catch (Exception ex) {
        log.error("Error processing response:" + responsePage, ex);
        responsePage.setException(ex);
        response.complete(responsePage);
      }
    });
    return response;
  }
}
