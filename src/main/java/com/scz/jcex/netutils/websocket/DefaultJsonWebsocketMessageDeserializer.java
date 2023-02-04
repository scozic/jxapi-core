package com.scz.jcex.netutils.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultJsonWebsocketMessageDeserializer<T> implements WebsocketMessageDeserializer<T> {
	
	protected final ObjectMapper objectMapper;
	
	protected final Class<T> messagePojoClass;
	
	public DefaultJsonWebsocketMessageDeserializer(Class<T> messagePojoClass) {
		this.objectMapper = new ObjectMapper();
		this.messagePojoClass = messagePojoClass;
	}

	@Override
	public T deserialize(String msg) {
		try {
			return this.objectMapper.readValue(msg, messagePojoClass);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(toString() + "Error while trying to deserialize [" + msg + "]", e);
		}
	}
	
	public String toString() {
		return getClass().getSimpleName() + "[" + messagePojoClass.getName() + "]";
	}

}
