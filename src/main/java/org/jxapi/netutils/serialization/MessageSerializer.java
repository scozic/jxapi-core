package org.jxapi.netutils.serialization;

/**
 * Interface for serializing messages. Each implementation should provide a
 * method to serialize an object into a raw String for use in HTTP requests or
 * Websocket messages.
 */
public interface MessageSerializer<T> {

  /**
   * Serializes an object.
   * 
   * @param obj the object to serialize
   * @return the serialized string representation of the object
   */
  String serialize(T obj);
  
  /**
   * Serializer for plain String values.<br>
   * A no-operation serializer that returns the input string as is.
   * 
   * @return the input string
   */
  static MessageSerializer<String> NO_OP = s -> s;
}
