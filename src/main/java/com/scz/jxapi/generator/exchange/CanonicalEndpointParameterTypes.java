package com.scz.jxapi.generator.exchange;

/**
 * Supported types of parameters in request or response data.
 * Any value for a field in request or response payload can be a 'primitive' ({@link #STRING}, {@link #INT} ... see {@link #isPrimitive}) or nested object that may be repeated:
 * <ul>
 * <li>Once, e.g. data type is a plain {@link #OBJECT} type</li>
 * <li>A list of values of any {@link CanonicalEndpointParameterTypes} (JSON array), see {@link #LIST}</li>
 * <li>A map of values (JSON map with {@link String} keys and values of any {@link CanonicalEndpointParameterTypes}), see {@link #MAP}</li>
 * </ul>
 * 
 * @see EndpointParameter
 */
public enum CanonicalEndpointParameterTypes {
	/** Plain {@link String} value */
	STRING(true),
	
	/** Boolean value */
	BOOLEAN(true),
	
	/** Floating point value */
	BIGDECIMAL(true),
	
	/** Integer value */
	INT(true),
	
	/** Long value */
	LONG(true),
	
	/** Timestamp (or datetime) value */
	TIMESTAMP(true),
	
	/** 
	 * Nested structure (JSON block) like:<br/>
	 * <code>{"a":"val", "b":1}</code>
	 * Such structure will contain a list of fields of a type matching one {@link #EndpointParameterType} values.
	 */
	OBJECT(false),
	
	/**
	 * Nested JSON map with keys of type String and values of same type (which can be any of {@link CanonicalEndpointParameterTypes}).
	 * */
	MAP(false),
	
	LIST(false);
	
	/**
	 * Flag set <code>true</code> when type stands for a primitive value e.g. not a JSON object, array or map.
	 * Matching types:
	 * <ul>
	 * <li>{@link #INT}</li>
	 * <li>{@link #LONG}</li>
	 * <li>{@link #TIMESTAMP}</li>
	 * <li>{@link #BIGDECIMAL}</li>
	 * <li>{@link #STRING}</li>
	 * <li>{@link #BOOLEAN}</li>
	 * </ul>
	 */
	public final boolean isPrimitive;
	
	private CanonicalEndpointParameterTypes(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}
}
