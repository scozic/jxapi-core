package com.scz.jxapi.generator.java;

import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Describes one property of a POJO to be generated using {@link PojoGenerator}
 */
@Deprecated
public class PojoField {
	
	/**
	 * Creates a new PojoField with the given type and name
	 * @param type Field type see, {@link Type}
	 * @param name Field name
	 * @return New PojoField instance with the given type and name
	 */
	public static PojoField create(String type, String name) {
		return create(type, name, null, null);
	}
	
	/**
	 * Creates a new PojoField with the given type, name, message field and description
	 * @param type Field type see, {@link Type}
	 * @param name Field name
	 * @param msgField Message field
	 * @param description Field description, to be used as javadoc
	 * @return New PojoField instance with the given type, name, message field and description
	 */
	public static PojoField create(String type, String name, String msgField, String description) {
		PojoField res = new PojoField();
		res.setType(type);
		res.setName(name);
		res.setDescription(description);
		res.setMsgField(msgField);
		return res;
	}

	private String type;
	private String name;
	private String description;
	private String msgField;
	

	/**
	 * @return Field description to be used as javadoc
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description Field description to be used as javadoc
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return Field name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Field name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Field type see, {@link Type}
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type Field type see, {@link Type}
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return Message field
	 */
	public String getMsgField() {
		return msgField;
	}

	/**
	 * @param msgField Message field
	 */
	public void setMsgField(String msgField) {
		this.msgField = msgField;
	}

	/**
	 * @return String representation of this PojoField see {@link EncodingUtil#pojoToString(Object)}
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
