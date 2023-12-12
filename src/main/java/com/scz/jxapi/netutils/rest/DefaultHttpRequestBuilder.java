package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.util.JsonUtil;

/**
 * Default {@link HttpRequestBuilder} implementation.
 * Can be extended to:
 * <ul>
 * <li>Add specific headers: by default, none are set. Subsclasses may override {@link #build(RestRequest)} to set needed headers to base implementation created {@link HttpRequest}
 * <li>Add specific URL parameters. Subclasses may override {@link #getFullUrl(RestRequest)} for this purpose.
 * <li>Use specific request body encoding. Subclasses may override {@link #getBody(RestRequest)} for this purpose.
 * <ul>
 */
public class DefaultHttpRequestBuilder implements HttpRequestBuilder {

	/**
	 * Base implementatin creates HttpRequest with:
	 * <ul>
	 * <li>Http method from <strong>restRequest</strong> param
	 * <li>URL built using {@link #getFullUrl(RestRequest)}
	 * <li>Body built using {@link #getBody(RestRequest)}
	 * <li>No headers
	 * </ul>
	 * Can be overridden to add specific headers
	 */
	@Override
	public HttpRequest build(RestRequest<?> restRequest) {
		HttpRequest res = new HttpRequest();
		res.setHttpMethod(restRequest.getHttpMethod());
		res.setUrl(getFullUrl(restRequest));
		res.setBody(getBody(restRequest));
		return res;
	}
	
	/**
	 * First hook called in {@link #build(RestRequest)}, can be overridden for
	 * instance when this rest enpoint API specifies that URL must be provided a
	 * signature parameter. Default implementation create URL from
	 * {@link RestRequest#getUrl()} concatenated to
	 * {@link RestEndpointUrlParameters#getUrlParameters()} if request implements
	 * {@link RestEndpointUrlParameters}
	 * 
	 * @param request
	 * @return the full URL, including base url, endpoint suffix and URL parameters
	 *         for given request
	 */
	protected String getFullUrl(RestRequest<?> request) {
		String url = request.getUrl();
		if (request.getRequest() instanceof RestEndpointUrlParameters) {
			String urlParams = ((RestEndpointUrlParameters) request.getRequest()).getUrlParameters();
			if (urlParams != null && !urlParams.isEmpty()) {
				url += urlParams;
			}
		}
		return url;
	}
	
	/**
	 * 2nd hook called in {@link #build(RestRequest)}, can be overridden if for
	 * instance, exchange API requires POST rest calls not to carry a body but URL
	 * parameters. Default implementation returns JSON String resulting of
	 * {@link RestRequest#getRequest()} encoding if request is a POST,
	 * <code>null</code> otherwise.
	 * 
	 * @param request
	 * @return
	 */
	protected String getBody(RestRequest<?> request) {
		if ("POST".equalsIgnoreCase(request.getHttpMethod())) {
			return JsonUtil.pojoToJsonString(request.getRequest());
		}
		return null;
	}

}
