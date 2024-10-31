package com.scz.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.util.JsonUtil;
import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.JsonDeserializer;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link Long} fields in JSON messages.
 * It is associated with Canonical JSON field type {@link CanonicalType#TIMESTAMP}.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class TimestampJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Long> {
	
	private static final TimestampJsonFieldDeserializer INSTANCE = new TimestampJsonFieldDeserializer();
	
	/**
	 * @return the singleton instance of this class
	 */
	public static TimestampJsonFieldDeserializer getInstance() {
		return INSTANCE;
	}
	
	private TimestampJsonFieldDeserializer() {}

	@Override
	public Long deserialize(JsonParser parser) throws IOException {
		return JsonUtil.readCurrentLong(parser);
	}

}
