package com.scz.jcex.generator.exchange;

/**
 * Supported types of parameters in request or response data
 * @see EndpointParameter
 */
public enum EndpointParameterType {
	/** Plain {@link String} value*/
	STRING,
	/** String containing a comma separated list of values*/
	STRING_LIST,
	/** Floating point value*/
	BIGDECIMAL,
	/** Integer value*/
	INT,
	/** Timestamp (or datetime) value*/
	TIMESTAMP,
	/** Nested structure (JSON block) */
	STRUCT,
	/** Repeated nested structure (JSON array) */
	STRUCT_LIST
}
