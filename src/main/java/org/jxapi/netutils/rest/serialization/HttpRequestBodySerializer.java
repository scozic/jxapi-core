package org.jxapi.netutils.rest.serialization;

import org.jxapi.netutils.rest.HttpRequest;

/**
 * Interface for serializing the body of an {@link HttpRequest}.
 */
public interface HttpRequestBodySerializer {

  /**
   * Serializes the body of the given {@link HttpRequest}. Implements should set
   * the body of the request retrived using {@link HttpRequest#getRequest()} using
   * {@link HttpRequest#setBody(String)}.
   * 
   * @param request
   */
  void serializeBody(HttpRequest request);
}
