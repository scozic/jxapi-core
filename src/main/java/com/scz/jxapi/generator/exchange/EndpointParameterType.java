package com.scz.jxapi.generator.exchange;

/**
 * Supported types of parameters in request or response data.
 * 
 * @see EndpointParameter
 */
public enum EndpointParameterType {
	/** Plain {@link String} value */
	STRING(false, false, false, false),
	
	/** Boolean value */
	BOOLEAN(false, false, false, false),
	
	/** String array like <code>["a","b","c"]</code> */
	STRING_LIST(true, false, false, false),
	
	/** Floating point value */
	BIGDECIMAL(false, false, false, false),
	
	/** Integer value */
	INT(false, false, false, false),
	
	/**Integer array like <code>[1,2,3]</code> */
	INT_LIST(true, false, false, false),
	
	/** Long value */
	LONG(false, false, false, false),
	
	/** Timestamp (or datetime) value */
	TIMESTAMP(false, false, false, false),
	
	/** 
	 * Nested structure (JSON block) like:<br/>
	 * <code>{"a":"val", "b":1}</code>
	 * Such structure will contain a list of fields of a type matching one {@link #EndpointParameterType} values.
	 * <br/>Flags: <i>object</i>
	 */
	OBJECT(false, true, false, false),
	
	/** Repeated nested structure (JSON array) like:<br/>
	 * <code>[{"x":"xval1"},{"x":"xval2"}]</code>
	 */
	OBJECT_LIST(true, true, false, false),
	
	/**
	 * Nested JSON map with keys of type String and values of same JSON object structure attributes, like:<br/>
	 * <code>{"a": {"key1":"ava11", "key2":"aval2"}, "b":{"key1":"bva11", "key2":"bval2"}}</code>
	 * Flags: object, map.
	 * */
	OBJECT_MAP(false, true, false, true);
	
	/**
	 * Flag set to <code>true</code> when type is a JSON array structure of simple types or objects but not nested arrays or maps.
	 * Matching types:
	 * <ul>
	 * <li>{@link #STRING_LIST}</li>
	 * <li>{@link #INT_LIST}</li>
	 * <li>{@link #OBJECT_LIST}</li>
	 * </ul>
	 */
	public final boolean isList;
	
	/**
	 * Flag set <code>true</code> when type is either a JSON object like
	 * <code>{"key1":"ava11", "key2":"aval2"}</code> or a repeated array, map or matrix of such JSON
	 * object.
	 * Matching types:
	 * <ul>
	 * <li>{@link #OBJECT}</li>
	 * <li>{@link #OBJECT_LIST}</li>
	 * <li>{@link #OBJECT_MAP}</li>
	 * </ul>
	 */
	public final boolean isObject;
	
	/**
	 * Flag set <code>true</code> when type stands for a JSON map.
	 * Matching types:
	 * <ul>
	 * <li>{@link #OBJECT_MAP}</li>
	 * </ul>  
	 */
	public final boolean isMap;
	
	/**
	 * Flag set <code>true</code> when type stands for a JSON array of arrays like
	 * <br/>
	 * <code>[[1,2],[3,4]]</code>. The nested values in array can be of any
	 * primitive {@link EndpointParameterType} or {@link #OBJECT}, but not an array,
	 * map or matrix.
	 * Matching types:
	 * <ul>
	 * </ul>
	 */
	public final boolean isMatrix;
	
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
	
	private EndpointParameterType(boolean isList, boolean isObject, boolean isMap, boolean isMatrix) {
		this.isList = isList;
		this.isObject = isObject;
		this.isMap = isMap;
		this.isMatrix = isMatrix;
		this.isPrimitive = !isList && !isObject && !isMap && !isMatrix;
	}
}
