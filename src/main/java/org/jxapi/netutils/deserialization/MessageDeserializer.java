package org.jxapi.netutils.deserialization;

/**
 * Interface for deserializing messages. Each implementation should provide a
 * method to deserialize a raw String from a HTTP response or Websocket message
 * body. Specific implementations are provided for 'primitive' types, such as
 * INTEGER,BOOLEAN... JSON data deserialization is supported by default.
 */
public interface MessageDeserializer<T> {

  /**
   * Deserializes a message.
   * 
   * @param msg the message to deserialize
   * @return the deserialized object, or <code>null</code> if the message is <code>null</code>.
   */
  T deserialize(String msg);
  
  /**
   * Deserializer for plain String values.<br>
   * A no-operation deserializer that returns the input string as is.
   * 
   * @return the input string
   */
  static MessageDeserializer<String> NO_OP = s -> s;
}
