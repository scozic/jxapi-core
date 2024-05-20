package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Describes the data type of a REST request, response or websocket message
 * field. This type can be a primitive type (see
 * {@link CanonicalEndpointParameterTypes#isPrimitive}, an object (
 * {@link CanonicalEndpointParameterTypes#OBJECT}), or a list or (
 * {@link String} key) map of data with a nested type.<br/>
 * When a field canonical type is primitive or
 * {@link CanonicalEndpointParameterTypes#OBJECT}, it subtype is
 * <code>null</code> as not relevant.<br/>
 * Otherwise, the type stands for a list or {@link String} key map of values of
 * same subtype which can be any type.<br/>
 * This means types are extensible: for instance
 * <code>BIGDECIMAL_LIST_MAP</code> means value of a field is map of list of
 * {@link BigDecimal} values. <br/>
 * When leaf subtype is {@link CanonicalEndpointParameterTypes#OBJECT} (see
 * {@link #isObject()}), the field definition in endpoint descriptor JSON file
 * must specify the list of {@link EndpointParameter} that defines the object.
 */
public class EndpointParameterType {
	
	public static final EndpointParameterType OBJECT = fromTypeName(CanonicalEndpointParameterTypes.OBJECT.name());
	public static final EndpointParameterType STRING = fromTypeName(CanonicalEndpointParameterTypes.STRING.name());
	public static final EndpointParameterType INT = fromTypeName(CanonicalEndpointParameterTypes.INT.name());
	public static final EndpointParameterType LONG = fromTypeName(CanonicalEndpointParameterTypes.LONG.name());
	public static final EndpointParameterType BIGDECIMAL = fromTypeName(CanonicalEndpointParameterTypes.BIGDECIMAL.name());
	public static final EndpointParameterType BOOLEAN = fromTypeName(CanonicalEndpointParameterTypes.BOOLEAN.name());
	
	public static EndpointParameterType getLeafSubType(EndpointParameterType type) {
		EndpointParameterType res = type;
		while (res.subType != null) {
			res = res.subType;
		}
		return res;
	}
	
	public static EndpointParameterType fromTypeName(String typeName) {
		if (typeName == null) {
			return null;
		}
		CanonicalEndpointParameterTypes type = null;
		EndpointParameterType subType = null;
		int off = typeName.lastIndexOf('_');
		if (off >= 0) {
			if (off >= typeName.length() - 1) {
				throw new IllegalArgumentException("Invalide type:[" 
													+ typeName 
													+ "], expected "
													+ CanonicalEndpointParameterTypes.class.getName() 
													+ " (non primitive) value after last '_'");
			}
			String typeStr = typeName.substring(off + 1);
			String subTypeStr = typeName.substring(0, off);
			type = CanonicalEndpointParameterTypes.valueOf(typeStr);
			if (type.isPrimitive) {
				throw new IllegalArgumentException("Invalid type:[" 
													+ type 
													+ "] in type name[" 
													+ typeName
													+ "], should not be a primitive type when a subtype [" 
													+ subTypeStr 
													+ "] is used");
			}
			subType = fromTypeName(subTypeStr);
		} else {
			type = CanonicalEndpointParameterTypes.valueOf(typeName);
		}
		EndpointParameterType et = new EndpointParameterType();
		et.canonicalType = type;
		et.subType = subType;
		return et;
	}

	private CanonicalEndpointParameterTypes canonicalType;
	
	private EndpointParameterType subType;

	public CanonicalEndpointParameterTypes getCanonicalType() {
		return canonicalType;
	}

	public void setCanonicalType(CanonicalEndpointParameterTypes type) {
		this.canonicalType = type;
	}

	public EndpointParameterType getSubType() {
		return subType;
	}

	public void setSubType(EndpointParameterType subType) {
		this.subType = subType;
	}
	
	public boolean isObject() {
		return getLeafSubType(this).canonicalType == CanonicalEndpointParameterTypes.OBJECT;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
