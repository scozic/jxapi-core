package org.jxapi.exchange.descriptor;

import java.util.List;

import org.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator;
import org.jxapi.generator.java.exchange.api.rest.RestEndpointClassesGenerator;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.pojo.descriptor.UrlParameterType;
import org.jxapi.util.EncodingUtil;

/**
 * Describes a REST endpoint as part of a larger exchange API defined in a JSON document.
 * These endpoints handle HTTP requests to a specific URL, process request parameters, and return a response.
 *
 * <h2>Endpoint Properties</h2>
 * The endpoint is defined by the following properties:
 * <ul>
 *   <li>{@code name}: A unique identifier for the endpoint.</li>
 *   <li>{@code description}: A brief summary of the endpoint's purpose.</li>
 *   <li>{@code url}: The URL of the endpoint.</li>
 *   <li>{@code httpMethod}: The HTTP method for the request (e.g., GET, POST).</li>
 *   <li>{@code request}: The data structure for the request.</li>
 *   <li>{@code response}: The data structure for the response.</li>
 *   <li>{@code requestWeight}: The cost of a request, used for rate limiting.</li>
 *   <li>{@code rateLimits}: The rate limits applied to this endpoint.</li>
 * </ul>
 *
 * <h2>Request Serialization</h2>
 * <strong>Serialization to URL Parameters:</strong>
 * <ul>
 *   <li>For HTTP methods that do not have a request body (e.g., {@code GET}, {@code DELETE}), the request is serialized into either URL path parameters (e.g., {@code /value1/value2}) or query parameters (e.g., {@code ?param1=value1}).</li>
 *   <li>By default, request fields are serialized as query parameters. To serialize them as URL path parameters, set the {@link Field#getIn()} property to {@link UrlParameterType#PATH}.</li>
 *   <li>If the request field is a primitive type, its value is URL-encoded and used as a single parameter.</li>
 *   <li>If the request field is an object without the {@code in} property set, its child fields are serialized as parameters.</li>
 *   <li>If the request field is an object with the {@code in} property set, or if it is a {@code MAP} or {@code LIST}, it is serialized as a URL-encoded JSON object.</li>
 *   <li>All field values are URL-encoded.</li>
 * </ul>
 *
 * <h2>Code Generation</h2>
 * API endpoints are defined as child elements of an {@code api} element in the JSON document (see {@link ExchangeApiDescriptor}).
 * This descriptor is used to generate method declarations in the API interface and their implementations.
 *
 * @see ExchangeApiInterfaceImplementationGenerator
 * @see ExchangeApiClassesGenerator
 *
 * <h2>Request and Response Properties</h2>
 * <ul>
 *   <li>Both {@code request} and {@code response} are described as {@link Field} objects.</li>
 *   <li>The name of the {@code request} field becomes the argument name in the generated API method.</li>
 *   <li>The name of the {@code response} field is not used in code generation.</li>
 *   <li>If {@code request} is omitted, the generated method will have no arguments.</li>
 *   <li>If {@code response} is omitted, the method will return a {@code STRING} containing the raw response body. This is useful when only the status code is needed and the body is empty.</li>
 * </ul>
 *
 * @see Field
 * @see RateLimitRule
 * @see RestEndpointClassesGenerator
 * @see Type
 * @see HttpMethod
 * @deprecated
 */
public class RestEndpointDescriptor {
  private String name;
  
  private String description;
  
  private String url;
  
  private String docUrl;
  
  private HttpMethod httpMethod;
  
  private Field request;
  
  private Field response;
  
  private Integer requestWeight;
  
  private List<String> rateLimits;
  
  private Boolean paginated = Boolean.FALSE;
  
  private Boolean requestHasBody = null;
   
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
   * @return The list of IDs of rate limits this REST API subject to. These must
   *         be defined either in enclosing API group descriptor (see
   *         {@link ExchangeApiDescriptor#getRateLimits()}) or in enclosing
   *         exchange descriptor, see {@link ExchangeDescriptor#getRateLimits()}.
   */
  public List<String> getRateLimits() {
    return rateLimits;
  }

  /**
   * @param rateLimits The list of IDs of rate limits this REST API subject to.
   * @see #getRateLimits()
   */
  public void setRateLimits(List<String> rateLimits) {
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
   * @return Exchange website's documentation URL for this API.
   */
  public String getDocUrl() {
    return docUrl;
  }

  /**
   * @param docUrl Exchange website's documentation URL for this API.
   */
  public void setDocUrl(String docUrl) {
    this.docUrl = docUrl;
  }
  
  /**
   * Returns <code>true</code> if this endpoint supports pagination.
   * <p>
   * For an enpoint to support pagination, it must comply with the following:
   * <ul>
   * <li>The request must be of type {@link Type#OBJECT} type and implement
   * {@link PaginatedRestRequest}
   * interface.</li>
   * <li>The response must be of type {@link Type#OBJECT} type and implement
   * {@link PaginatedRestResponse} interface.</li>
   * </ul>
   * Actual wrapper should provide custom sub-interface of
   * {@link PaginatedRestResponse} and
   * {@link PaginatedRestRequest} with default
   * implementation of
   * {@link PaginatedRestRequest#setNextPage(PaginatedRestResponse)}
   * with default implementations for methods of these interfaces relying on API
   * specific fields to find out if a response carries last page and set next
   * request page index from last response otherwise.
   * 
   * @return <code>true</code> If this endpoint supports pagination, <code>false</code> otherwise.
   */
  public boolean isPaginated() {
    return paginated;
  }

  /**
   * Sets whether this endpoint supports pagination. T
   * @param isPaginated <code>true</code> if this endpoint supports pagination, <code>false</code> otherwise.
   * @see #isPaginated()
   */
  public void setPaginated(boolean isPaginated) {
    this.paginated = isPaginated;
  }
  
  /**
   * @return a string representation of the object. See {@link EncodingUtil#pojoToString(Object)}
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

  /**
   * Indicates whether the request should be sent as the body of the HTTP request.
   * Can (and generally should) be left null to use default behavior based on HTTP method.
   * @return <code>null</code> to use default behavior, {@link Boolean#TRUE} to send request as body, ${@link Boolean#FALSE} otherwise.
   */
  public Boolean isRequestHasBody() {
    return requestHasBody;
  }

  /**
   * Sets whether the request should be sent as the body of the HTTP request. Can
   * (and generally should) be left null to use default behavior based on HTTP
   * method.
   * 
   * @param requestHasBody <code>null</code> to use default behavior,
   *                      {@link Boolean#TRUE} to send request as body,
   *                      ${@link Boolean#FALSE} otherwise.
   */
  public void setRequestHasBody(Boolean requestHasBody) {
    this.requestHasBody = requestHasBody;
  }

}
