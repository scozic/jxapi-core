package com.scz.jxapi.netutils.websocket;

import java.util.ArrayList;
import java.util.List;

import com.scz.jxapi.util.JsonUtil;

public class WebsocketMessageTopicMatcherField {
	
	public static List<WebsocketMessageTopicMatcherField> createList(String... namesAndValues) {
		List<WebsocketMessageTopicMatcherField> l = new ArrayList<>(namesAndValues.length / 2);
		for (int i = 0; i < namesAndValues.length;) {
			WebsocketMessageTopicMatcherField f = new WebsocketMessageTopicMatcherField();
			f.setName(namesAndValues[i++]);
			f.setValue(namesAndValues[i++]);
			l.add(f);
		}
		return l;
	}

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
