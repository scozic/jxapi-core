package com.scz.jxapi.exchange.descriptor;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of WebsocketEndpointDescriptor describing a field that must match a
 * specific value in the message for the message to be considered as matching
 * the topic of the endpoint.
 */
public class WebsocketMessageTopicMatcherFieldDescriptor {

	private String fieldName;

	private String value;

	/**
	 * @return the name of the field that must match the topic of the endpoint.
	 */
	public String getName() {
		return fieldName;
	}

	/**
	 * @param fieldName the name of the field that must match the topic of the
	 *                  endpoint.
	 */
	public void setName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the value that the field must match.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value that the field must match.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return a string representation of the object. see
	 *         {@link EncodingUtil#pojoToString(Object)}
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
