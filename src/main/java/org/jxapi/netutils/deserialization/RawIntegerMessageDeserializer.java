package org.jxapi.netutils.deserialization;

/**
 * Deserializer for plain integer values.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 */
public class RawIntegerMessageDeserializer implements MessageDeserializer<Integer> {
  
  private static final RawIntegerMessageDeserializer INSTANCE = new RawIntegerMessageDeserializer();
  
  /**
   * @return The singleton instance of this class
   */
  public static RawIntegerMessageDeserializer getInstance() {
    return INSTANCE;
  }

  @Override
  public Integer deserialize(String msg) {
    if (msg == null) {
      return null;
    }
    return Integer.valueOf(msg);
  }

}
