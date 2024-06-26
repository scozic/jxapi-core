package com.scz.jxapi.exchange.descriptor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Describes the data type of a REST request, response or websocket message
 * field. This type can be a primitive type (see
 * {@link CanonicalType#isPrimitive}, an object (
 * {@link CanonicalType#OBJECT}), or a list or (
 * {@link String} key) map of data with a nested type.<br/>
 * When a field canonical type is primitive or
 * {@link CanonicalType#OBJECT}, it subtype is
 * <code>null</code> as not relevant.<br/>
 * Otherwise, the type stands for a list or {@link String} key map of values of
 * same subtype which can be any type.<br/>
 * This means types are extensible: for instance
 * <code>BIGDECIMAL_LIST_MAP</code> means value of a field is map of list of
 * {@link BigDecimal} values. <br/>
 * When leaf subtype is {@link CanonicalType#OBJECT} (see
 * {@link #isObject()}), the field definition in endpoint descriptor JSON file
 * must specify the list of {@link Field} that defines the object.
 */
public class Type {
	
	private static final Map<String, Type> CANONICAL_TYPES = new HashMap<>(6);
	public static final Type OBJECT = createCanonicalType(CanonicalType.OBJECT);
	public static final Type STRING = createCanonicalType(CanonicalType.STRING);
	public static final Type INT = createCanonicalType(CanonicalType.INT);
	public static final Type LONG = createCanonicalType(CanonicalType.LONG);
	public static final Type TIMESTAMP = createCanonicalType(CanonicalType.TIMESTAMP);
	public static final Type BIGDECIMAL = createCanonicalType(CanonicalType.BIGDECIMAL);
	public static final Type BOOLEAN = createCanonicalType(CanonicalType.BOOLEAN);
	
	private static Type createCanonicalType(CanonicalType canonicalType) {
		Type type = new Type();
		type.setCanonicalType(canonicalType);
		CANONICAL_TYPES.put(canonicalType.name(), type);
		return type;
	}
	
	public static Type getLeafSubType(Type type) {
		Type res = type;
		while (res.subType != null) {
			res = res.subType;
		}
		return res;
	}
	
	public static Type fromTypeName(String typeName) {
		if (typeName == null) {
			return null;
		}
		CanonicalType canonicalType = null;
		Type subType = null;
		int off = typeName.lastIndexOf('_');
		if (off >= 0) {
			if (off >= typeName.length() - 1) {
				throw new IllegalArgumentException("Invalide type:[" 
													+ typeName 
													+ "], expected "
													+ CanonicalType.class.getName() 
													+ " (non primitive) value after last '_'");
			}
			String typeStr = typeName.substring(off + 1);
			String subTypeStr = typeName.substring(0, off);
			canonicalType = CanonicalType.valueOf(typeStr);
			if (canonicalType.isPrimitive) {
				throw new IllegalArgumentException("Invalid type:[" 
													+ canonicalType 
													+ "] in type name[" 
													+ typeName
													+ "], should not be a primitive type when a subtype [" 
													+ subTypeStr 
													+ "] is used");
			}
			subType = fromTypeName(subTypeStr);
		} else {
			return Optional.ofNullable(CANONICAL_TYPES.get(typeName))
						   .orElseThrow(() -> new IllegalArgumentException("Invalid type:" + typeName));
		}
		Type et = new Type();
		et.canonicalType = canonicalType;
		et.subType = subType;
		return et;
	}

	private CanonicalType canonicalType;
	
	private Type subType;

	public CanonicalType getCanonicalType() {
		return canonicalType;
	}

	public void setCanonicalType(CanonicalType type) {
		this.canonicalType = type;
	}

	public Type getSubType() {
		return subType;
	}

	public void setSubType(Type subType) {
		this.subType = subType;
	}
	
	public boolean isObject() {
		return getLeafSubType(this).canonicalType == CanonicalType.OBJECT;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
