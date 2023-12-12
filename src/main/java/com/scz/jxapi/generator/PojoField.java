package com.scz.jxapi.generator;

import com.scz.jxapi.util.JsonUtil;

/**
 * Describes one field of a POJO to be generated using {@link PojoGenerator}
 */
public class PojoField {
	
	public static PojoField create(String type, String name) {
		return create(type, name, null, null);
	}
	
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMsgField() {
		return msgField;
	}

	public void setMsgField(String msgField) {
		this.msgField = msgField;
	}

	@Override
	public String toString() {
		return JsonUtil.pojoToString(this);
	}
}
