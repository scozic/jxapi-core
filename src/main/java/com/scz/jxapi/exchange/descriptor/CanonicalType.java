package com.scz.jxapi.exchange.descriptor;

import java.math.BigDecimal;

/**
 * Supported types of parameters in request or response data.
 * Any value for a field in request or response payload can be a 'primitive' ({@link #STRING}, {@link #INT} ... see {@link #isPrimitive}) or nested object that may be repeated:
 * <ul>
 * <li>Once, e.g. data type is a plain {@link #OBJECT} type</li>
 * <li>A list of values of any {@link CanonicalType} (JSON array), see {@link #LIST}</li>
 * <li>A map of values (JSON map with {@link String} keys and values of any {@link CanonicalType}), see {@link #MAP}</li>
 * </ul>
 * 
 * @see Field
 */
public enum CanonicalType {
	/** Plain {@link String} value */
	STRING(String.class),
	
	/** Boolean value */
	BOOLEAN(Boolean.class),
	
	/** Floating point value */
	BIGDECIMAL(BigDecimal.class),
	
	/** Integer value */
	INT(Integer.class),
	
	/** Long value */
	LONG(Long.class),
	
	/** Timestamp (or datetime) value */
	TIMESTAMP(Long.class),
	
	/** 
	 * Nested structure (JSON block) like:<br/>
	 * <code>{"a":"val", "b":1}</code>
	 * Such structure will contain a list of fields of a type matching one {@link #EndpointParameterType} values.
	 */
	OBJECT(),
	
	/**
	 * Nested JSON map with keys of type String and values of same type (which can be any of {@link CanonicalEndpointParameterTypes}).
	 * */
	MAP(),
	
	LIST();
	
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
	
	/**
	 * The Class associated to primitive type (see {@link #isPrimitive}, or
	 * <code>null</code> if not a primitive type. To guess the class associated to a
	 * non-primitive type, see
	 * {@link ExchangeJavaWrapperGeneratorUtil#getClassNameForParameterType(Type, java.util.Set, String)}
	 */
	public final Class<?> typeClass;
	
	private CanonicalType(Class<?> typeClass) {
		this.isPrimitive = true;
		this.typeClass = typeClass;
	}
	
	private CanonicalType() {
		this.isPrimitive = false;
		this.typeClass = null;
	}
}
