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
	/** String containing a comma separated list of values */
	STRING_LIST,
	/** Floating point value */
	BIGDECIMAL,
	/** Integer value */
	INT,
	/** Long value */
	LONG,
	/** Timestamp (or datetime) value */
	TIMESTAMP,
	/** Nested structure (JSON block) */
	OBJECT,
	/** Repeated nested structure (JSON array) */
	OBJECT_LIST;
}
