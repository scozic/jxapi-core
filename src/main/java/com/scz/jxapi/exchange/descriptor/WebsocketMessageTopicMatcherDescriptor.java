package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.util.EncodingUtil;

/**
 * When subscribing to a websocket endpoint, a topic is extracted from the
 * message stream and matched against the topic of the endpoint.
 * The way the message is matched against the topic is described by a list of
 * fields that are extracted from the message and must all match an expected
 * value.
 * This class stands for part of {@link WebsocketEndpointDescriptor} describing
 * the list of field matchers that must all match for the message to be
 * considered as matching the topic.
 * 
 * see {@link WebsocketEndpointDescriptor}
 * see {@link WebsocketMessageTopicMatcherFieldDescriptor}
 */
public class WebsocketMessageTopicMatcherDescriptor {

	private List<WebsocketMessageTopicMatcherFieldDescriptor> fields;

	/**
	 * @return the list of fields that must match the topic of the endpoint.
	 */
	public List<WebsocketMessageTopicMatcherFieldDescriptor> getFields() {
		return fields;
	}

	/**
	 * @param fields the list of fields that must match the topic of the endpoint.
	 */
	public void setFields(List<WebsocketMessageTopicMatcherFieldDescriptor> fields) {
		this.fields = fields;
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
