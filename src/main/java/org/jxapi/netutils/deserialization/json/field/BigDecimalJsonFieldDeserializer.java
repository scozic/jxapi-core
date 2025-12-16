package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.math.BigDecimal;

import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonParser;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link BigDecimal} fields in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class BigDecimalJsonFieldDeserializer extends AbstractJsonMessageDeserializer<BigDecimal> {

  private static final BigDecimalJsonFieldDeserializer INSTANCE = new BigDecimalJsonFieldDeserializer();

  /**
   * @return the singleton instance of this class
   */
  public static BigDecimalJsonFieldDeserializer getInstance() {
    return INSTANCE;
  }

  private BigDecimalJsonFieldDeserializer() {
  }

  @Override
  public BigDecimal deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentBigDecimal(parser);
  }

}
