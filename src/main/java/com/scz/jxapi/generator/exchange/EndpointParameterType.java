package com.scz.jxapi.generator.exchange;

/**
 * Supported types of parameters in request or response data
 * @see EndpointParameter
 */
public enum EndpointParameterType {
	/** Plain {@link String} value */
	STRING,
	/** Boolean value */
	BOOLEAN,
	/** String array like <code>["a","b","c"]</code> */
	STRING_LIST,
	/** Floating point value */
	BIGDECIMAL,
	/** Integer value */
	INT,
	/**Integer array like <code>[1,2,3]</code> */
	INT_LIST,
	/** Long value */
	LONG,
	/** Timestamp (or datetime) value */
	TIMESTAMP,
	/** Nested structure (JSON block) */
	OBJECT,
	/** Repeated nested structure (JSON array) like <code>[{"x":"xval1"},{"x":"xval2"}]</code>*/
	OBJECT_LIST,
	
	/**Nested JSON map with keys of type String and values of same JSON object structure attributes*/
	OBJECT_MAP;
}
