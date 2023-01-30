package com.scz.jcex.netutils.websocket;

public interface WebsocketMessageDeserializer<T> {

	T deserialize(String msg);
}
