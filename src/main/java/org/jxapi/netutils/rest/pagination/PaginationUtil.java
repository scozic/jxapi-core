package org.jxapi.netutils.rest.pagination;

import java.util.List;
import java.util.function.Function;

import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DeepCloneable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper methods for working with paginated REST responses.
 */
public class PaginationUtil {
  
  private static final Logger log = LoggerFactory.getLogger(PaginationUtil.class);

  private PaginationUtil() {}
  
  /**
   * Checks if the given page has a next page.
   * <ul>
   * <li>Returns false if the page is null.</li>
   * <li>Returns false if the page is not OK (HTTP status code is not 2xx or nested exception).</li>
   * <li>Returns false if the page is not paginated (page payload does not implement {@link PaginatedRestResponse}).</li>
   * <li>Returns false if the page data is null.</li>
   * <li>Returns true if the page data has a next page.</li>
   * </ul>
   * @param page Response to REST call to an endpoint that implements pagination
   * @return true if the page has a next page, false otherwise
   */
  public static boolean hasNextPage(RestResponse<? extends PaginatedRestResponse> page) {
    if (page == null) {
      return false;
    }
    if (!page.isOk()) {
      return false;
    }
    if (!page.isPaginated()) {
      return false;
    }
    
    PaginatedRestResponse pageData = page.getResponse();
    return pageData != null && pageData.hasNextPage();
  }
  
  /**
   * Creates a {@link NextPageResolver} that can be used to fetch the next page of a paginated REST response.
   * @param <R> the type of the paginated REST request
   * @param <A> the type of the paginated REST response
   * @param previousPageRequest the request used to fetch the previous page, which will be cloned to create the next page request
   * @param paginatedRestEndpoint the function that takes a paginated REST request and returns a FutureRestResponse for the next page.
   * @return a NextPageResolver that can be used to fetch the next page of a paginated REST response
   */
  public static <R extends PaginatedRestRequest, A extends PaginatedRestResponse> NextPageResolver<A> getNextPageResolver(
      R previousPageRequest, 
      Function<R, FutureRestResponse<A>> paginatedRestEndpoint ) {
    return a -> {
      if (a == null || !hasNextPage(a)) {
        throw new IllegalArgumentException("Cannot fetch next page: no next page available in the response:" + a);
      }
      @SuppressWarnings("unchecked")
      R nextRequest = ((DeepCloneable<R>) previousPageRequest).deepClone();
      nextRequest.setNextPage(a.getResponse());
      return paginatedRestEndpoint.apply(nextRequest);
    };
  }
  
  /**
   * Fetches the next page of a paginated REST response using the next page resolver
   * 
   * @param pageResponse the paginated REST response from which to fetch the next
   *                     page
   * @param <A> the type of the paginated REST response                    
   * @return a FutureRestResponse containing the next page of the response
   * @throws IllegalArgumentException if the page response is null or does not
   *                                  have a next page
   */
  public static <A extends PaginatedRestResponse> FutureRestResponse<A> fetchNextPage(RestResponse<A> pageResponse) {
    return pageResponse.getNextPageResolver().nextPage(pageResponse);
  }
  
  /**
   * Fetches all pages of a paginated REST response.
   * <p>
   * This method will recursively fetch all pages until there are no more pages
   * available. It uses the provided FutureRestResponse to collect all pages and
   * return them as a list.
   * 
   * @param pageResponse the initial paginated REST response from which to start
   *                     fetching pages
   * @param <A> the type of the paginated REST response                    
   * @return a FutureRestResponse containing a list of all fetched pages starting with the provided first page response.
   * @throws IllegalArgumentException if the page response is null
   */
  public static <A extends PaginatedRestResponse> FutureRestResponse<List<A>> fetchAllPages(FutureRestResponse<A> pageResponse) {
    if (pageResponse == null) {
      throw new IllegalArgumentException("Cannot fetch all pages: page response is null");
    }
    FutureRestResponse<List<A>> finalResponse = new FutureRestResponse<>();
    fetchAllPages(pageResponse, CollectionUtil.createList(), finalResponse);
    return finalResponse;
  }
  
  private static <A extends PaginatedRestResponse> void fetchAllPages(
      FutureRestResponse<A> currentPageResponse, 
      List<A> pages, 
      FutureRestResponse<List<A>> finalResponse) {
    currentPageResponse.thenAccept(pageResponse -> {
      try {
        A page = pageResponse.getResponse();
        if (page != null) {
          pages.add(page);
        }
        if (hasNextPage(pageResponse)) {
          log.debug("fetchAllPages: Got response for page #{}: {}", pages.size(), pageResponse);
          fetchAllPages(fetchNextPage(pageResponse), pages, finalResponse);
        } else {
          log.debug("fetchAllPages: Got final response with page #{}: {}", pages.size(), pageResponse);
          completeFetchAllPages(pageResponse, pages, finalResponse);
        }    
      } catch (Exception ex) {
        // Remark: Normally unreachable code, as processing the pageResponse should not throw an exception here.
        // However, if it does, we log the error and complete the final response with the exception.
        log.error(String.format("Error while fetching all pages, got error at page #%d:%s", pages.size(), pageResponse), ex);
        pageResponse.setException(ex);
        completeFetchAllPages(pageResponse, pages, finalResponse);
      }
    });
  }
  
  private static <A extends PaginatedRestResponse> void completeFetchAllPages(
      RestResponse<A> currentPageResponse, 
      List<A> pages, 
      FutureRestResponse<List<A>> finalResponse) {
    RestResponse<List<A>> pagesResponse = new RestResponse<>(currentPageResponse.getHttpResponse());
    pagesResponse.setResponse(pages);
    pagesResponse.setException(currentPageResponse.getException());
    finalResponse.complete(pagesResponse);
  }

}
