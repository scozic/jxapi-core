package com.scz.jxapi.netutils.rest;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Helper methods around REST request where response may come in multiple
 * "pages". It is common that API used to fetch large number of items provides
 * request parameter "index" to indicate page index, and response parameter
 * "index" indicating the next page index, or none if last page is found.
 *
 * @param <R> Type of request data
 * @param <A> Type of response data
 */
public class RestRequestPagination {
	
	private static final Logger log = LoggerFactory.getLogger(RestRequestPagination.class);
	
	/**
	 * Fetches all pages of response to given request.
	 * 
	 * @param request             the request to send with initial page index set. 
	 * 							  If multiple response pages are found,
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
	 */
	public static <R, A> FutureRestResponse<A> fetchAllPages(RestRequest<R> request, 
										 RestEndpoint<R, A> endpoint, 
										 BiConsumer<String, R> setRequestIndex, 
										 Function<A, String> getResponseIndex,
										 BinaryOperator<A> responseAccumulator) {
		return fetchAllPages(request, endpoint, setRequestIndex, getResponseIndex, responseAccumulator, null);
	}
	
	private static <R, A> FutureRestResponse<A> fetchAllPages(RestRequest<R> request, 
			 												  RestEndpoint<R, A> endpoint, 
			 												  BiConsumer<String, R> setRequestIndex, 
			 												  Function<A, String> getResponseIndex,
			 												  BinaryOperator<A> responseAccumulator,
			 												  A cumulativeResult) {
		FutureRestResponse<A> response = new FutureRestResponse<>();
		if (log.isDebugEnabled() ) {
			log.debug("Fetching page for request:" + request);
		}
		endpoint.call(request).thenAccept(responsePage -> {
			if (!responsePage.isOk()) {
				response.complete(responsePage);
			} else {
				 A page = responsePage.getResponse();
				 if (cumulativeResult != null) {
					 responsePage.setResponse(responseAccumulator.apply(cumulativeResult, page));
				 }
				 String nextPageIndex = getResponseIndex.apply(page);
				 if (log.isDebugEnabled())
					 log.debug("Next page index:" + nextPageIndex + " in response to request:" + request);
				 if (nextPageIndex != null) {
					 // Fetch next page
					 setRequestIndex.accept(nextPageIndex, request.getRequest());
					 fetchAllPages(request, endpoint, setRequestIndex, getResponseIndex, responseAccumulator, responsePage.getResponse()).thenAccept(response::complete);
				 } else {
					 // last page found
					 response.complete(responsePage);
				 }
			}
		});
		return response;
	}

}
