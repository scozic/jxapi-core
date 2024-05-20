package com.scz.jxapi.netutils.rest;

/**
 * Interface for a {@link RestRequest} body serializer, default implementation
 * as JSON serializer using Jackson can be replaced by a custom one that
 * serializes as any format (XML/Soap...)
 * 
 * @param <T> the class of object to serialize
 */
public interface RestRequestBodySerializer<T> {

	/**
	 * Formats request of 
	 * @param request
	 * @return
	 */
	String serializeBody(T request);
}
