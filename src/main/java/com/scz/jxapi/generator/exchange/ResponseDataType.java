package com.scz.jxapi.generator.exchange;

/**
 * REST enpoints calls returns text data from HTTP request response body, that
 * is usually converted to a JSON object. Some may return JSON data encapsulated
 * as array (JSON object list) of items. For those endpoints which do not return
 * a default {@link #JSON_OBJECT} response data type, a different type such as
 * {@value #JSON_OBJECT_LIST} can be specified.
 */
public enum ResponseDataType {
	/**
	 * The default data type of a JSON response: a json object like <strong>{"foo":"fooVal"}</strong>
	 */
	JSON_OBJECT,
	
	/**
	 * JSON object list data type like <strong>[{"foo":"fooVal1"},{"foo":"fooVal2}]"</strong>
	 */
	JSON_OBJECT_LIST,
	
	/**
	 * Raw response data type (body of HTML response)
	 */
	STRING
}
