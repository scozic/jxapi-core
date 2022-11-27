package com.scz.jcex.generator;

import com.scz.jcex.util.EncodingUtil;

/**
 * Describes one field of a POJO to be generated using {@link PojoGenerator}
 */
public class PojoField {
	
	public static PojoField create(String type, String name) {
		return create(type, name, null);
	}
	
	public static PojoField create(String type, String name, String description) {
		PojoField res = new PojoField();
		res.setType(type);
		res.setName(name);
		res.setDescription(description);
		return res;
	}

	private String type;
	private String name;
	private String description;
	
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
	
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
