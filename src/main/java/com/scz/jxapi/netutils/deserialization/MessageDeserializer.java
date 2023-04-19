package com.scz.jxapi.netutils.deserialization;

public interface MessageDeserializer<T> {

	T deserialize(String msg);
}
