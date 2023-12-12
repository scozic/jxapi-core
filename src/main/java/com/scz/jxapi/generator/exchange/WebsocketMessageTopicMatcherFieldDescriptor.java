package com.scz.jxapi.generator.exchange;

import com.scz.jxapi.util.JsonUtil;

public class WebsocketMessageTopicMatcherFieldDescriptor {

	private String fieldName;
	
	private String value;

	public String getName() {
		return fieldName;
	}

	public void setName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return JsonUtil.pojoToString(this);
	}
}
