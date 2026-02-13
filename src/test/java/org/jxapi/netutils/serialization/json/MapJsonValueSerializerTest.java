package org.jxapi.netutils.serialization.json;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Unit tests for {@link MapJsonValueSerializer}.
 */
public class MapJsonValueSerializerTest {

    private static class StringSerializer extends StdSerializer<String> {
        private static final long serialVersionUID = -8486853380882923848L;

		protected StringSerializer() {
            super(String.class);
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value);
        }
    }

    private MapJsonValueSerializer<String> serializer;
    private StringSerializer stringSerializer;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        stringSerializer = new StringSerializer();
        serializer = new MapJsonValueSerializer<>(stringSerializer);
        jsonFactory = new JsonFactory();
    }

    @Test
    public void testSerializeMapWithValues() throws IOException {
        // Arrange
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        StringWriter writer = new StringWriter();
        JsonGenerator gen = jsonFactory.createGenerator(writer);

        // Act
        serializer.serialize(map, gen, null);
        gen.close();

        // Assert
        assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", writer.toString());
    }

    @Test
    public void testSerializeEmptyMap() throws IOException {
        // Arrange
        Map<String, String> map = Collections.emptyMap();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = jsonFactory.createGenerator(writer);

        // Act
        serializer.serialize(map, gen, null);
        gen.close();

        // Assert
        assertEquals("{}", writer.toString());
    }

    @Test
    public void testSerializeNullMap() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator gen = jsonFactory.createGenerator(writer);

        // Act
        serializer.serialize(null, gen, null);
        gen.close();

        // Assert
        assertEquals("null", writer.toString());
    }
}

