package com.scz.jxapi.generator.exchange;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Describes the data type of a REST request, response or websocket message
 * field. This type can be a primitive type (see
 * {@link EndpointParameterTypes#isPrimitive}, an object (
 * {@link EndpointParameterTypes#OBJECT}), or a list or ( {@link String} key)
 * map of data with a nested type.
 */
public class EndpointParameterType {
	
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
		EndpointParameterTypes type = null;
		EndpointParameterType subType = null;
		int off = typeName.lastIndexOf('_');
		if (off >= 0) {
			if (off >= typeName.length() - 1) {
				throw new IllegalArgumentException("Invalide type:[" 
													+ typeName 
													+ "], expected "
													+ EndpointParameterTypes.class.getName() 
													+ " (non primitive) value after last '_'");
			}
			String typeStr = typeName.substring(off + 1);
			String subTypeStr = typeName.substring(0, off);
			type = EndpointParameterTypes.valueOf(typeStr);
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
			type = EndpointParameterTypes.valueOf(typeName);
		}
		EndpointParameterType et = new EndpointParameterType();
		et.type = type;
		et.subType = subType;
		return et;
	}

	private EndpointParameterTypes type;
	
	private EndpointParameterType subType;

	public EndpointParameterTypes getType() {
		return type;
	}

	public void setType(EndpointParameterTypes type) {
		this.type = type;
	}

	public EndpointParameterType getSubType() {
		return subType;
	}

	public void setSubType(EndpointParameterType subType) {
		this.subType = subType;
	}
	
	public boolean isObject() {
		return getLeafSubType(this).type == EndpointParameterTypes.OBJECT;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
