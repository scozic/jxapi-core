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
   * @param paginatedRestEndpoint the function that takes a paginated REST request and returns a FutureRestResponse for the next page
   * @return a NextPageResolver that can be used to fetch the next page of a paginated REST response
   */
  public static <R extends PaginatedRestRequest, A extends PaginatedRestResponse> NextPageResolver<A> getNextPageResolver(
      R previousPageRequest, 
      Function<R, FutureRestResponse<A>> paginatedRestEndpoint ) {
    return a -> {
      if (!hasNextPage(a)) {
        FutureRestResponse<A> future = new FutureRestResponse<>();
        future.complete(a);
        return future;
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
   * @return a FutureRestResponse containing the next page of the response
   * @throws IllegalArgumentException if the page response is null or does not
   *                                  have a next page
   */
  public static <A extends PaginatedRestResponse> FutureRestResponse<A> fetchNextPage(RestResponse<A> pageResponse) {
    if (pageResponse == null) {
      throw new IllegalArgumentException("Cannot fetch next page: page response is null");
    }
    if (!hasNextPage(pageResponse)) {
      throw new IllegalArgumentException("Cannot fetch next page: no next page available in the response:" + pageResponse);
    }
    
    return pageResponse.getNextPageResolver().nextPage(pageResponse);
  }
  
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
          completeFetchAllPages(pageResponse, pages, finalResponse, null);
        }    
      } catch (Exception ex) {
        log.error(String.format("Error while fetching all pages, got error at page #%d:%s", pages.size(), pageResponse), ex);
        completeFetchAllPages(pageResponse, pages, finalResponse, ex);
      }
    });
  }
  
  private static <A extends PaginatedRestResponse> void completeFetchAllPages(
      RestResponse<A> currentPageResponse, 
      List<A> pages, 
      FutureRestResponse<List<A>> finalResponse, Exception ex) {
    RestResponse<List<A>> pagesResponse = new RestResponse<>(currentPageResponse.getHttpResponse());
    pagesResponse.setResponse(pages);
    if (ex != null) {
      pagesResponse.setException(ex);
    } else {
      pagesResponse.setException(currentPageResponse.getException());
    }
    finalResponse.complete(pagesResponse);
  }

}
