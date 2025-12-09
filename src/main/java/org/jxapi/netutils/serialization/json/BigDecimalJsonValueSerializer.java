package org.jxapi.netutils.serialization.json;

import java.io.IOException;
import java.math.BigDecimal;

import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link AbstractJsonMessageSerializer} for {@link BigDecimal} values in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageSerializer
 */
public class BigDecimalJsonValueSerializer extends AbstractJsonMessageSerializer<BigDecimal> {
  
  private static final BigDecimalJsonValueSerializer INSTANCE = new BigDecimalJsonValueSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static BigDecimalJsonValueSerializer getInstance() {
    return INSTANCE;
  }

  private BigDecimalJsonValueSerializer() {
    super(BigDecimal.class);
  }

  @Override
  public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeBigDecimalValue(gen, value);
  }

}
