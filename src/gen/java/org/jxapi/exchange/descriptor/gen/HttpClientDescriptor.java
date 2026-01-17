package org.jxapi.exchange.descriptor.gen;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.HttpClientDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.HttpClientDescriptorSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Part of JSON document describing network configuration of a HTTP client that can be used by
 * REST endpoints of the exchange wrapper.  
 * Each client provides implementation of HTTP interceptor and executor factories.
 * The executor factory may not be set, in which case default
 * {@link org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor} is used.
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = HttpClientDescriptorSerializer.class)
@JsonDeserialize(using = HttpClientDescriptorDeserializer.class)
public class HttpClientDescriptor implements Pojo<HttpClientDescriptor> {
  
  private static final long serialVersionUID = -5700991537515947254L;
  
  /**
   * @return A new builder to build {@link HttpClientDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String httpRequestExecutorFactory;
  private String httpRequestInterceptorFactory;
  private Long httpRequestTimeout;
  
  /**
   * @return The unique name of the HTTP client
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The unique name of the HTTP client
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The fully qualified class name of the
   * {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use for
   * REST endpoints using this HTTP client.
   * Can be null, in which case default
   * {@link org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor} instantiated.
   * 
   */
  public String getHttpRequestExecutorFactory() {
    return httpRequestExecutorFactory;
  }
  
  /**
   * @param httpRequestExecutorFactory The fully qualified class name of the
   * {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use for
   * REST endpoints using this HTTP client.
   * Can be null, in which case default
   * {@link org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor} instantiated.
   * 
   */
  public void setHttpRequestExecutorFactory(String httpRequestExecutorFactory) {
    this.httpRequestExecutorFactory = httpRequestExecutorFactory;
  }
  
  /**
   * @return The fully qualified class name of the
   * {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use
   * for REST endpoints using this HTTP client.
   * 
   */
  public String getHttpRequestInterceptorFactory() {
    return httpRequestInterceptorFactory;
  }
  
  /**
   * @param httpRequestInterceptorFactory The fully qualified class name of the
   * {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use
   * for REST endpoints using this HTTP client.
   * 
   */
  public void setHttpRequestInterceptorFactory(String httpRequestInterceptorFactory) {
    this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
  }
  
  /**
   * @return The default HTTP request timeout in milliseconds to use for REST
   * endpoints using this HTTP client.
   * 
   */
  public Long getHttpRequestTimeout() {
    return httpRequestTimeout;
  }
  
  /**
   * @param httpRequestTimeout The default HTTP request timeout in milliseconds to use for REST
   * endpoints using this HTTP client.
   * 
   */
  public void setHttpRequestTimeout(Long httpRequestTimeout) {
    this.httpRequestTimeout = httpRequestTimeout;
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
    HttpClientDescriptor o = (HttpClientDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.httpRequestExecutorFactory, o.httpRequestExecutorFactory)
        && Objects.equals(this.httpRequestInterceptorFactory, o.httpRequestInterceptorFactory)
        && Objects.equals(this.httpRequestTimeout, o.httpRequestTimeout);
  }
  
  @Override
  public int compareTo(HttpClientDescriptor other) {
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
    res = CompareUtil.compare(this.httpRequestExecutorFactory, other.httpRequestExecutorFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpRequestInterceptorFactory, other.httpRequestInterceptorFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpRequestTimeout, other.httpRequestTimeout);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, httpRequestExecutorFactory, httpRequestInterceptorFactory, httpRequestTimeout);
  }
  
  @Override
  public HttpClientDescriptor deepClone() {
    HttpClientDescriptor clone = new HttpClientDescriptor();
    clone.name = this.name;
    clone.httpRequestExecutorFactory = this.httpRequestExecutorFactory;
    clone.httpRequestInterceptorFactory = this.httpRequestInterceptorFactory;
    clone.httpRequestTimeout = this.httpRequestTimeout;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link HttpClientDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String httpRequestExecutorFactory;
    private String httpRequestInterceptorFactory;
    private Long httpRequestTimeout;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The unique name of the HTTP client
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>httpRequestExecutorFactory</code> field in the builder
     * @param httpRequestExecutorFactory The fully qualified class name of the
     * {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use for
     * REST endpoints using this HTTP client.
     * Can be null, in which case default
     * {@link org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor} instantiated.
     * 
     * @return Builder instance
     * @see #setHttpRequestExecutorFactory(String)
     */
    public Builder httpRequestExecutorFactory(String httpRequestExecutorFactory)  {
      this.httpRequestExecutorFactory = httpRequestExecutorFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>httpRequestInterceptorFactory</code> field in the builder
     * @param httpRequestInterceptorFactory The fully qualified class name of the
     * {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use
     * for REST endpoints using this HTTP client.
     * 
     * @return Builder instance
     * @see #setHttpRequestInterceptorFactory(String)
     */
    public Builder httpRequestInterceptorFactory(String httpRequestInterceptorFactory)  {
      this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>httpRequestTimeout</code> field in the builder
     * @param httpRequestTimeout The default HTTP request timeout in milliseconds to use for REST
     * endpoints using this HTTP client.
     * 
     * @return Builder instance
     * @see #setHttpRequestTimeout(Long)
     */
    public Builder httpRequestTimeout(Long httpRequestTimeout)  {
      this.httpRequestTimeout = httpRequestTimeout;
      return this;
    }
    
    /**
     * @return a new instance of HttpClientDescriptor using the values set in this builder
     */
    public HttpClientDescriptor build() {
      HttpClientDescriptor res = new HttpClientDescriptor();
      res.name = this.name;
      res.httpRequestExecutorFactory = this.httpRequestExecutorFactory;
      res.httpRequestInterceptorFactory = this.httpRequestInterceptorFactory;
      res.httpRequestTimeout = this.httpRequestTimeout;
      return res;
    }
  }
}
