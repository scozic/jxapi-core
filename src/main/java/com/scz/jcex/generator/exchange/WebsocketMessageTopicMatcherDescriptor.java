package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

public class WebsocketMessageTopicMatcherDescriptor {
	
	private List<WebsocketMessageTopicMatcherFieldDescriptor> fields;

	public List<WebsocketMessageTopicMatcherFieldDescriptor> getFields() {
		return fields;
	}

	public void setFields(List<WebsocketMessageTopicMatcherFieldDescriptor> fields) {
		this.fields = fields;
	}

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
