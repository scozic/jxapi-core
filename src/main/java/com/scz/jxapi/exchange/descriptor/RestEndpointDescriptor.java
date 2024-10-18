package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator;
import com.scz.jxapi.generator.java.exchange.api.rest.RestEndpointClassesGenerator;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of JSON document describing a exchange API, describes a specific REST endpoint.
 * Such endpoints are expecting an HTTP request to given URL endpoint, with given request parameters, 
 * and a response should be received.
 * <p>
 * The endpoint is described using properties:
 * <ul>
 * <li><code>name</code> - a unique name of the endpoint</li>
 * <li><code>description</code> - a short description of the endpoint</li>
 * <li><code>url</code> - the URL of the endpoint</li>
 * <li><code>httpMethod</code> - the HTTP method to be used for the request</li>
 * <li><code>request</code> - the request data</li>
 * <li><code>response</code> - the response data</li>
 * <li><code>urlParameters</code> - the URL parameters. A String that will be used as suffix to URL endpoint.
 * Can contain place holders like <code>${myArg}</code> that will be replaced with either the 
 * name of <code>request<field>, or, if request field name does not match and field is of 
 * object type (see {@link Type#isObject()} ), the name of a field of that nested object structure.
 * Can be <code>null</code> if no URL parameters are expected or if request should be serialized 
 * as query parameters (see <code>queryParams</code>).
 * </li>
 * <li><code>urlParametersListSeparator<code> - When request is of LIST type, the separator used between 
 * items of that list in serialzed request. Remark: If request is of type MAP which is not likely 
 * for an API expected request data as URL parameters or query params, the corresponding serialized 
 * object will be URL encoded value of JSON.</li>
 * <li><code>queryParams</code> - whether the request data should be serialized as URL are query parameters.<br/> 
 * <strong>About query parameters</strong>:<br/>
 * <ul>
 * <li>The default value used in generated code depends on HTTP method, it will be true for methods
 * where corresponding requests expect a body: <code>GET</code>, </code>DELETE</code>, <code>HEAD</code>, 
 * <code>OPTIONS</code>, <code>TRACE</code>.</li>
 * <li>Query parameters are serialized in form of <code>?name1=value1&name2=value2</code> 
 * and appended to URL endpoint.</li>
 * <li>Field values will be URL encoded</li>
 * <li>If field is of object type, the object fields will be serialized as query parameters. 
 * If nested objects are contained (object field has fields of object type), their properties 
 * are appended to the list of query parameters</li>
 * <li>If field is of list type, the list items will be serialized as query parameters, 
 * using <code>urlParametersListSeparator</code> as separator</li>
 * </ul>
 * </li>
 * <li><code>requestWeight</code> - the weight of a request call if subject to weighted rate limit rules</li>
 * <li><code>rateLimits</code> - the rate limits this REST API subject to</li>
 * </ul>
 * <p>
 * API endpoints are child elements of api element (see ExchangeApiDescriptor ) in the JSON document.<br/>
 * Such descriptor will be used to generate method declaration in API interface and its 
 * implementation, see {@link ExchangeApiInterfaceImplementationGenerator} and {@link ExchangeApiClassesGenerator}.<br/>
 * <p>
 * About <code>request</code> and <code>response</code> properties:<br/> 
 * <ul>
 * <li>Request and response parameters are described as {@link Field}. The name of the <code>request</code> field 
 * is the name of single argument of the method that will be generated in the API interface.<br/></li>
 * <li>Request and response parameters are described as {@link Field}. 
 * The name of the <code>response</code> field is not relevant. 
 * <li><code>request</code> property is optional. If not present, the method will be generated with no argument.</li>
 * <li><code>response</code> property is optional. If not present, the method will be generated 
 * with STRING return value type containing raw response body value. 
 * This is intended for instance for REST APIs where status code is enough empty body is expected.</li>
 * </ul>
 * 
 * @see Field
 * @see RateLimitRule
 * @see RestEndpointClassesGenerator
 * @see Type
 * @see HttpMethod
 * @see ExchangeApiInterfaceImplementationGenerator
 */
public class RestEndpointDescriptor {
	private String name;
	
	private String description;
	
	private String url;
	
	private HttpMethod httpMethod;
	
	private Field request;
	
	private Field response;

	private String urlParameters;
	
	private String urlParametersListSeparator;
	
	private boolean queryParams;
	
	private Integer requestWeight;
	
	private List<RateLimitRule> rateLimits;
 	
	/**
	 * @return the name of the REST API endpoint
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name of the REST API endpoint
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description of the REST API endpoint
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description of the REST API endpoint
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the URL of the REST API endpoint
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the URL of the REST API endpoint
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the HTTP method to be used for the request
	 */
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod the HTTP method to be used for the request
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	/**
	 * @return The request url parameters template. Can contain place holders like <code>${myArg}</code>
	 */
	public String getUrlParameters() {
		return urlParameters;
	}

	/**
	 * @param urlParameters The request url parameters template. Can contain place holders like <code>${myArg}</code>
	 */
	public void setUrlParameters(String urlParameters) {
		this.urlParameters = urlParameters;
	}

	/**
	 * @return The separator used between items of a list in serialized request url parameters
	 */
	public String getUrlParametersListSeparator() {
		return urlParametersListSeparator;
	}

	/**
	 * @param urlParametersListSeparator The separator used between items of a list in serialized request url parameters
	 */
	public void setUrlParametersListSeparator(String urlParametersListSeparator) {
		this.urlParametersListSeparator = urlParametersListSeparator;
	}
	
	/**
	 * @return whether the request data should be serialized as URL are query parameters
	 */
	public boolean isQueryParams() {
		return queryParams;
	}

	/**
	 * @param queryParams whether the request data should be serialized as URL are query parameters
	 */
	public void setQueryParams(boolean queryParams) {
		this.queryParams = queryParams;
	}
	
	/**
	 * @return the rate limits this REST API subject to
	 */
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	/**
	 * @param rateLimits the rate limits this REST API subject to
	 */
	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}
	
	/**
	 * @return the weight of a request call if subject to weighted rate limit rules
	 */
	public Integer getRequestWeight() {
		return requestWeight;
	}

	/**
	 * @param requestWeight the weight of a request call if subject to weighted rate limit rules
	 */
	public void setRequestWeight(Integer requestWeight) {
		this.requestWeight = requestWeight;
	}
	
	/**
	 * @return the request data
	 */
	public Field getRequest() {
		return request;
	}

	/**
	 * @param request the request data
	 */
	public void setRequest(Field request) {
		this.request = request;
	}

	/**
	 * @return the response data
	 */
	public Field getResponse() {
		return response;
	}

	/**
	 * @param response the response data
	 */
	public void setResponse(Field response) {
		this.response = response;
	}
	
	/**
	 * @return a string representation of the object. See {@link EncodingUtil#pojoToString(Object)}
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
