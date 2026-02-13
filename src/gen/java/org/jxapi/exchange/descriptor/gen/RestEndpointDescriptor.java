package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.RestEndpointDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.RestEndpointDescriptorSerializer;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Describes a REST endpoint as part of a larger exchange API defined in a JSON document.
 * These endpoints handle HTTP requests to a specific URL, process request parameters, and return a response.
 * 
 * <h2>Endpoint Properties</h2>
 * The endpoint is defined by the following properties:
 * <ul>
 *   <li>{@code name}: A unique identifier for the endpoint.</li>
 *   <li>{@code httpClient}: The name of the HTTP client to use for this REST endpoint, as defined in exchange 'network' section. When not set, default client of API group is used.</li>
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
 *   <li>By default, request fields are serialized as query parameters. To serialize them as URL path parameters, set the {@link org.jxapi.pojo.descriptor.Field#getIn()} property to {@link org.jxapi.pojo.descriptor.UrlParameterType#PATH}.</li>
 *   <li>If the request field is a primitive type, its value is URL-encoded and used as a single parameter.</li>
 *   <li>If the request field is an object without the {@code in} property set, its child fields are serialized as parameters.</li>
 *   <li>If the request field is an object with the {@code in} property set, or if it is a {@code MAP} or {@code LIST}, it is serialized as a URL-encoded JSON object.</li>
 *   <li>All field values are URL-encoded.</li>
 * </ul>
 * 
 * <h2>Code Generation</h2>
 * API endpoints are defined as child elements of an {@code api} element in the JSON document (see {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}).
 * This descriptor is used to generate method declarations in the API interface and their implementations.
 * 
 * <h2>Request and Response Properties</h2>
 * <ul>
 *   <li>Both {@code request} and {@code response} are described as {@link org.jxapi.pojo.descriptor.Field} objects.</li>
 *   <li>The name of the {@code request} field becomes the argument name in the generated API method.</li>
 *   <li>The name of the {@code response} field is not used in code generation.</li>
 *   <li>If {@code request} is omitted, the generated method will have no arguments.</li>
 *   <li>If {@code response} is omitted, the method will return a {@code STRING} containing the raw response body. This is useful when only the status code is needed and the body is empty.</li>
 * </ul>
 * 
 * @see org.jxapi.pojo.descriptor.Field
 * @see org.jxapi.netutils.rest.ratelimits.RateLimitRule
 * @see org.jxapi.generator.java.exchange.api.rest.RestEndpointClassesGenerator
 * @see org.jxapi.pojo.descriptor.Type
 * @see org.jxapi.netutils.rest.HttpMethod
 * @see org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator
 * @see org.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = RestEndpointDescriptorSerializer.class)
@JsonDeserialize(using = RestEndpointDescriptorDeserializer.class)
public class RestEndpointDescriptor implements Pojo<RestEndpointDescriptor> {
  
  private static final long serialVersionUID = 3220828939988892700L;
  
  /**
   * @return A new builder to build {@link RestEndpointDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String description;
  private String httpMethod;
  private String url;
  private String docUrl;
  private String httpClient;
  private Integer requestWeight;
  private List<String> rateLimits;
  private Boolean paginated;
  private Boolean requestHasBody;
  private Field request;
  private Field response;
  
  /**
   * @return The unique name of the REST endpoint within the API group
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The unique name of the REST endpoint within the API group
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The description of the REST endpoint
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description The description of the REST endpoint
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return The HTTP method of the REST endpoint, e.g. GET, POST, DELETE, PUT
   */
  public String getHttpMethod() {
    return httpMethod;
  }
  
  /**
   * @param httpMethod The HTTP method of the REST endpoint, e.g. GET, POST, DELETE, PUT
   */
  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }
  
  /**
   * @return The URL of the REST endpoint
   */
  public String getUrl() {
    return url;
  }
  
  /**
   * @param url The URL of the REST endpoint
   */
  public void setUrl(String url) {
    this.url = url;
  }
  
  /**
   * @return The documentation URL of the REST endpoint
   */
  public String getDocUrl() {
    return docUrl;
  }
  
  /**
   * @param docUrl The documentation URL of the REST endpoint
   */
  public void setDocUrl(String docUrl) {
    this.docUrl = docUrl;
  }
  
  /**
   * @return The name of the HTTP client to use for this REST endpoint, as defined
   * in exchange 'network' section. When not set, default client of API
   * group is used.
   * 
   */
  public String getHttpClient() {
    return httpClient;
  }
  
  /**
   * @param httpClient The name of the HTTP client to use for this REST endpoint, as defined
   * in exchange 'network' section. When not set, default client of API
   * group is used.
   * 
   */
  public void setHttpClient(String httpClient) {
    this.httpClient = httpClient;
  }
  
  /**
   * @return The weight of a request to this endpoint when evaluating rate limits.
   * 
   */
  public Integer getRequestWeight() {
    return requestWeight;
  }
  
  /**
   * @param requestWeight The weight of a request to this endpoint when evaluating rate limits.
   * 
   */
  public void setRequestWeight(Integer requestWeight) {
    this.requestWeight = requestWeight;
  }
  
  /**
   * @return The list of IDs of rate limits this REST API subject to. These must
   * be defined either in enclosing exchange descriptor, see {@link org.jxapi.exchange.descriptor.gen.ExchangeDescriptor#getRateLimits()}.
   * 
   */
  public List<String> getRateLimits() {
    return rateLimits;
  }
  
  /**
   * @param rateLimits The list of IDs of rate limits this REST API subject to. These must
   * be defined either in enclosing exchange descriptor, see {@link org.jxapi.exchange.descriptor.gen.ExchangeDescriptor#getRateLimits()}.
   * 
   */
  public void setRateLimits(List<String> rateLimits) {
    this.rateLimits = rateLimits;
  }
  
  /**
   * @return Whether this endpoint supports pagination.
   * 
   */
  public Boolean isPaginated() {
    return paginated;
  }
  
  /**
   * @param paginated Whether this endpoint supports pagination.
   * 
   */
  public void setPaginated(Boolean paginated) {
    this.paginated = paginated;
  }
  
  /**
   * @return Whether this endpoint's request has a body. If true, the request
   * will be sent in the body of the HTTP request. If false, the request
   * will be sent as URL parameters.
   * 
   */
  public Boolean isRequestHasBody() {
    return requestHasBody;
  }
  
  /**
   * @param requestHasBody Whether this endpoint's request has a body. If true, the request
   * will be sent in the body of the HTTP request. If false, the request
   * will be sent as URL parameters.
   * 
   */
  public void setRequestHasBody(Boolean requestHasBody) {
    this.requestHasBody = requestHasBody;
  }
  
  /**
   * @return The request data structure of the REST endpoint
   */
  public Field getRequest() {
    return request;
  }
  
  /**
   * @param request The request data structure of the REST endpoint
   */
  public void setRequest(Field request) {
    this.request = request;
  }
  
  /**
   * @return The response data structure of the REST endpoint
   */
  public Field getResponse() {
    return response;
  }
  
  /**
   * @param response The response data structure of the REST endpoint
   */
  public void setResponse(Field response) {
    this.response = response;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    RestEndpointDescriptor o = (RestEndpointDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.description, o.description)
        && Objects.equals(this.httpMethod, o.httpMethod)
        && Objects.equals(this.url, o.url)
        && Objects.equals(this.docUrl, o.docUrl)
        && Objects.equals(this.httpClient, o.httpClient)
        && Objects.equals(this.requestWeight, o.requestWeight)
        && Objects.equals(this.rateLimits, o.rateLimits)
        && Objects.equals(this.paginated, o.paginated)
        && Objects.equals(this.requestHasBody, o.requestHasBody)
        && Objects.equals(this.request, o.request)
        && Objects.equals(this.response, o.response);
  }
  
  @Override
  public int compareTo(RestEndpointDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.name, other.name);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.description, other.description);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpMethod, other.httpMethod);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.url, other.url);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.docUrl, other.docUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpClient, other.httpClient);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.requestWeight, other.requestWeight);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.rateLimits, other.rateLimits, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.paginated, other.paginated);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.requestHasBody, other.requestHasBody);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.request, other.request);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.response, other.response);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, description, httpMethod, url, docUrl, httpClient, requestWeight, rateLimits, paginated, requestHasBody, request, response);
  }
  
  @Override
  public RestEndpointDescriptor deepClone() {
    RestEndpointDescriptor clone = new RestEndpointDescriptor();
    clone.name = this.name;
    clone.description = this.description;
    clone.httpMethod = this.httpMethod;
    clone.url = this.url;
    clone.docUrl = this.docUrl;
    clone.httpClient = this.httpClient;
    clone.requestWeight = this.requestWeight;
    clone.rateLimits = CollectionUtil.cloneList(this.rateLimits);
    clone.paginated = this.paginated;
    clone.requestHasBody = this.requestHasBody;
    clone.request = this.request != null ? this.request.deepClone() : null;
    clone.response = this.response != null ? this.response.deepClone() : null;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link RestEndpointDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String description;
    private String httpMethod;
    private String url;
    private String docUrl;
    private String httpClient;
    private Integer requestWeight;
    private List<String> rateLimits;
    private Boolean paginated;
    private Boolean requestHasBody;
    private Field request;
    private Field response;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The unique name of the REST endpoint within the API group
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>description</code> field in the builder
     * @param description The description of the REST endpoint
     * @return Builder instance
     * @see #setDescription(String)
     */
    public Builder description(String description)  {
      this.description = description;
      return this;
    }
    
    /**
     * Will set the value of <code>httpMethod</code> field in the builder
     * @param httpMethod The HTTP method of the REST endpoint, e.g. GET, POST, DELETE, PUT
     * @return Builder instance
     * @see #setHttpMethod(String)
     */
    public Builder httpMethod(String httpMethod)  {
      this.httpMethod = httpMethod;
      return this;
    }
    
    /**
     * Will set the value of <code>url</code> field in the builder
     * @param url The URL of the REST endpoint
     * @return Builder instance
     * @see #setUrl(String)
     */
    public Builder url(String url)  {
      this.url = url;
      return this;
    }
    
    /**
     * Will set the value of <code>docUrl</code> field in the builder
     * @param docUrl The documentation URL of the REST endpoint
     * @return Builder instance
     * @see #setDocUrl(String)
     */
    public Builder docUrl(String docUrl)  {
      this.docUrl = docUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>httpClient</code> field in the builder
     * @param httpClient The name of the HTTP client to use for this REST endpoint, as defined
     * in exchange 'network' section. When not set, default client of API
     * group is used.
     * 
     * @return Builder instance
     * @see #setHttpClient(String)
     */
    public Builder httpClient(String httpClient)  {
      this.httpClient = httpClient;
      return this;
    }
    
    /**
     * Will set the value of <code>requestWeight</code> field in the builder
     * @param requestWeight The weight of a request to this endpoint when evaluating rate limits.
     * 
     * @return Builder instance
     * @see #setRequestWeight(Integer)
     */
    public Builder requestWeight(Integer requestWeight)  {
      this.requestWeight = requestWeight;
      return this;
    }
    
    /**
     * Will set the value of <code>rateLimits</code> field in the builder
     * @param rateLimits The list of IDs of rate limits this REST API subject to. These must
     * be defined either in enclosing exchange descriptor, see {@link org.jxapi.exchange.descriptor.gen.ExchangeDescriptor#getRateLimits()}.
     * 
     * @return Builder instance
     * @see #setRateLimits(List<String>)
     */
    public Builder rateLimits(List<String> rateLimits)  {
      this.rateLimits = rateLimits;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>rateLimits</code> list.
     * @param item Item to add to current <code>rateLimits</code> list
     * @return Builder instance
     * @see RestEndpointDescriptor#setRateLimits(List)
     */
    public Builder addToRateLimits(String item) {
      if (this.rateLimits == null) {
        this.rateLimits = CollectionUtil.createList();
      }
      this.rateLimits.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>paginated</code> field in the builder
     * @param paginated Whether this endpoint supports pagination.
     * 
     * @return Builder instance
     * @see #setPaginated(Boolean)
     */
    public Builder paginated(Boolean paginated)  {
      this.paginated = paginated;
      return this;
    }
    
    /**
     * Will set the value of <code>requestHasBody</code> field in the builder
     * @param requestHasBody Whether this endpoint's request has a body. If true, the request
     * will be sent in the body of the HTTP request. If false, the request
     * will be sent as URL parameters.
     * 
     * @return Builder instance
     * @see #setRequestHasBody(Boolean)
     */
    public Builder requestHasBody(Boolean requestHasBody)  {
      this.requestHasBody = requestHasBody;
      return this;
    }
    
    /**
     * Will set the value of <code>request</code> field in the builder
     * @param request The request data structure of the REST endpoint
     * @return Builder instance
     * @see #setRequest(Field)
     */
    public Builder request(Field request)  {
      this.request = request;
      return this;
    }
    
    /**
     * Will set the value of <code>response</code> field in the builder
     * @param response The response data structure of the REST endpoint
     * @return Builder instance
     * @see #setResponse(Field)
     */
    public Builder response(Field response)  {
      this.response = response;
      return this;
    }
    
    /**
     * @return a new instance of RestEndpointDescriptor using the values set in this builder
     */
    public RestEndpointDescriptor build() {
      RestEndpointDescriptor res = new RestEndpointDescriptor();
      res.name = this.name;
      res.description = this.description;
      res.httpMethod = this.httpMethod;
      res.url = this.url;
      res.docUrl = this.docUrl;
      res.httpClient = this.httpClient;
      res.requestWeight = this.requestWeight;
      res.rateLimits = CollectionUtil.cloneList(this.rateLimits);
      res.paginated = this.paginated;
      res.requestHasBody = this.requestHasBody;
      res.request = this.request != null ? this.request.deepClone() : null;
      res.response = this.response != null ? this.response.deepClone() : null;
      return res;
    }
  }
}
