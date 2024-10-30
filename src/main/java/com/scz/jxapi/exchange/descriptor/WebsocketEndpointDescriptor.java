package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of JSON document describing a crypo exchange API, describes a websocket
 * endpoint where clients subscription can be performed using specified topic
 * and eventual additional parameters.
 * The structure of additional subscription parameters and response format are
 * described as {@link Field} lists.
 */
public class WebsocketEndpointDescriptor {

	private String name;

	private String topic;

	private String description;
	
	private String docUrl;

	private Field request;

	private Field message;

	private String topicParametersListSeparator;

	private List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields;

	/**
	 * @return Websocket api endpoint name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the websocket api endpoint.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the topic of the websocket endpoint. Will be used to match messages
	 *         associated with this endpoint in the stream of websocket messages
	 *         that may disseminate messages for several multiplexed streams.
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Set the topic of the websocket endpoint. Will be used to match messages
	 * associated with this endpoint in the stream of websocket messages
	 * that may disseminate messages for several multiplexed streams.
	 * <br>
	 * Topic can contain reference to whole request of request property value for
	 * instance
	 * <code>ticker.${request}</code> topic means actual topic is
	 * <code>ticker.[value_of_subscription_request_as_string]</code>
	 * 
	 * @param topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the description (documentation) of the websocket endpoint.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return Exchange website's documentation URL for this API.
	 */
	public String getDocUrl() {
		return docUrl;
	}

	/**
	 * @param docUrl Exchange website's documentation URL for this API.
	 */
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	/**
	 * Set the description (documentation) of the websocket endpoint.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * When a websocket subscription topic carries references to a property of
	 * 'list' type, the separator used to concatenate parameters when serializing to
	 * string.
	 * 
	 * @return
	 */
	public String getTopicParametersListSeparator() {
		return topicParametersListSeparator;
	}

	/**
	 * When a websocket subscription topic carries references to a property of
	 * 'list' type, the separator used to concatenate parameters when serializing to
	 * string.
	 * 
	 * @param topicParametersListSeparator
	 */
	public void setTopicParametersListSeparator(String topicParametersListSeparator) {
		this.topicParametersListSeparator = topicParametersListSeparator;
	}

	/**
	 * @return the list of field matchers that can be used to match the topic of a
	 *         message
	 *         received on the websocket. To match a topic, the message must match
	 *         all matchers.
	 */
	public List<WebsocketMessageTopicMatcherFieldDescriptor> getMessageTopicMatcherFields() {
		return messageTopicMatcherFields;
	}

	/**
	 * Set the list of field matchers that can be used to match the topic of a
	 * message
	 * received on the websocket. To match a topic, the message must match all
	 * matchers.
	 * 
	 * @param messageTopicMatcherFields
	 */
	public void setMessageTopicMatcherFields(
			List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields) {
		this.messageTopicMatcherFields = messageTopicMatcherFields;
	}

	/**
	 * @return the data structure of the websocket endpoint subscription request.
	 */
	public Field getRequest() {
		return request;
	}

	/**
	 * Set the data structure of the websocket endpoint subscription request.
	 * 
	 * @param request
	 */
	public void setRequest(Field request) {
		this.request = request;
	}

	/**
	 * @return the data structure of the websocket endpoint message.
	 */
	public Field getMessage() {
		return message;
	}

	/**
	 * Set the data structure of the websocket endpoint message.
	 * 
	 * @param message
	 */
	public void setMessage(Field message) {
		this.message = message;
	}

	/**
	 * @return a string representation of the object, see
	 *         {@link EncodingUtil#pojoToString(Object)}.
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
