package com.scz.jxapi.netutils.rest;

/**
 * Implementation of this interface build a generic {@link HttpRequest} request
 * from {@link RestRequest} object. This separates API wrapper specific
 * requirements for requests like setting a signature header or URL parameter,
 * from actual HTTP calling implementation. Actual implementation may extend
 * {@link DefaultHttpRequestBuilder} that performs generic conversion, and
 * override methods according to their needs.
 */
public interface HttpRequestBuilder {

	HttpRequest build(RestRequest<?> restRequest);
}
