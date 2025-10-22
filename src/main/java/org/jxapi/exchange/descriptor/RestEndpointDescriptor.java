package org.jxapi.exchange.descriptor;

import java.util.List;

import org.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator;
import org.jxapi.generator.java.exchange.api.rest.RestEndpointClassesGenerator;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.util.EncodingUtil;

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
 * <strong>About request serialization into request parameters:</strong>:<br>
 * <ul>
 * <li>The request will be serialized as URL path (<code>/value1/value2</code>) or query param (<code>?param1=value1&param2=value2...</code>) depends on HTTP method, it will be true for methods
 * where corresponding requests do not expect a body: <code>GET</code>, <code>DELETE</code> <code>HEAD</code>, 
 * <code>OPTIONS</code>, <code>TRACE</code> (see {@link HttpMethod#requestHasBody}).</li>
 * <li>The parameters serialized to URL path or query parameters are derived from <code>request</code> field properties.</li>
 * <li>Serialization rules:
 * <li>Fields are serialized as query parameters by default. In order to specifically serialize request fields as URL path parameters,
 * the {@link Field#getIn()} property must be set to {@link UrlParameterType#PATH}.</li>}
 * <li>If request field is of primitive type, its value will be used as single URL path or query parameter URL encoded value</li>
 * <li>If request or a child field is of object type, and its <code>in</code> property is 
 * unset, its child fields will be serialized as URL path or query parameters</li> 
 * <li>If request or a child field is of object type, and its <code>in</code> property is set, 
 * or of MAP or LIST type, its will be serialized as URL encoded JSON object</li>
 * <ul>
 * <li>Field values will be URL encoded</li>
 * <li>If field is of object type, the object fields will be serialized as query parameters. 
 * If nested objects are contained (object field has fields of object type), their properties 
 * are appended to the list of query parameters</li>
 * <li>If field is of list type, the list items will be serialized as query parameters, 
 * using <code>urlParametersListSeparator</code> as separator</li>
 * </ul>
 * <li><code>requestWeight</code> - the weight of a request call if subject to weighted rate limit rules</li>
 * <li><code>rateLimits</code> - the rate limits this REST API subject to</li>
 * </ul>
 * <p>
 * API endpoints are child elements of api element (see ExchangeApiDescriptor ) in the JSON document.<br>
 * Such descriptor will be used to generate method declaration in API interface and its 
 * implementation, see {@link ExchangeApiInterfaceImplementationGenerator} and {@link ExchangeApiClassesGenerator}.<br>
 * <p>
 * About <code>request</code> and <code>response</code> properties:<br> 
 * <ul>
 * <li>Request and response parameters are described as {@link Field}. The name of the <code>request</code> field 
 * is the name of single argument of the method that will be generated in the API interface.<br></li>
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
  
  private String docUrl;
  
  private HttpMethod httpMethod;
  
  private Field request;
  
  private Field response;
  
  private Integer requestWeight;
  
  private List<String> rateLimits;
  
  private boolean paginated = false;
   
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

}
