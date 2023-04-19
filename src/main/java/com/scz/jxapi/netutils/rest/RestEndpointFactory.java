package com.scz.jxapi.netutils.rest;

import java.util.Properties;

import com.scz.jxapi.generator.exchange.ExchangeApiDescriptor;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;

/**
 * Interface for a specific {@link RestEndpoint} implementation factory. Actual
 * implementations are expected to have a default constructor. Name of such
 * actual implementation class can be provided in JSON CEX descriptor, see
 * {@link ExchangeApiDescriptor}. CEX code generation will create interface for
 * corresponding api rest calls using that factory. </br>
 * Some credential specific data may need to be provided in requests if this
 * factory stands for private API calls. The {@link #setProperties(Properties)}
 * method will be called prior to calls to
 * {@link #createRestEndpoint(MessageDeserializer)}
 */
public interface RestEndpointFactory {
	
	/**
	 * Called with exchange specific connection properties, such as API key/password
	 * for signing requests.
	 * 
	 * @param properties
	 */
	default void setProperties(Properties properties) {}

	/**
	 * @param <R> the request DTO
	 * @param <A> the response DTO
	 * @param messageDeserializer the {@link MessageDeserializer} to use to deserialize response POJO from request response body
	 * @return the rest enpoint
	 */
	<R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer); 
}
