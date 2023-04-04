package com.scz.jcex.netutils.rest;

/**
 * Interface for POJOs that represent a REST endpoint request data provider.
 * <br/>
 * Such request may be serialized either as body of HTTP request, or URL
 * parameters in URL suffix. Using for instance <code>http://com.x/api/</code>
 * as base URL, a parameter named <code>id</code> with value <code>myId</code>
 * can be passed as path variable: <code>http://com.x/api/myId</code> or as
 * query parameter:<code>http://com.x/api/?id=myId</code> <br/>
 * This interface must be implemented by POJOs that represent REST request data
 * to provide expected URL suffix.
 * 
 */
public interface RestEndpointUrlParameters {

	/**
	 * @return URL suffix containing serialized parameters
	 */
	String getUrlParameters();
}
