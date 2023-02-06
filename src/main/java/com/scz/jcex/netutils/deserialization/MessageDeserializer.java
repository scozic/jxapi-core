package com.scz.jcex.netutils.deserialization;

public interface MessageDeserializer<T> {

	T deserialize(String msg);
}
