package org.jxapi.netutils.serialization.json;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Unit tests for {@link ListJsonValueSerializer}.
 */
public class ListJsonValueSerializerTest {

    private static class StringSerializer extends StdSerializer<String> {
        private static final long serialVersionUID = -6734860022235285020L;

		protected StringSerializer() {
            super(String.class);
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value);
        }
    }

    private ListJsonValueSerializer<String> serializer;
    private StringSerializer stringSerializer;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        stringSerializer = new StringSerializer();
        serializer = new ListJsonValueSerializer<>(stringSerializer);
        jsonFactory = new JsonFactory();
    }

    @Test
    public void testSerializeListOfStrings() throws IOException {
        // Arrange
        List<String> list = Arrays.asList("one", "two", "three");
        StringWriter writer = new StringWriter();
        JsonGenerator gen = jsonFactory.createGenerator(writer);

        // Act
        serializer.serialize(list, gen, null);
        gen.close();

        // Assert
        assertEquals("[\"one\",\"two\",\"three\"]", writer.toString());
    }

    @Test
    public void testSerializeEmptyList() throws IOException {
        // Arrange
        List<String> list = Collections.emptyList();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = jsonFactory.createGenerator(writer);

        // Act
        serializer.serialize(list, gen, null);
        gen.close();

        // Assert
        assertEquals("[]", writer.toString());
    }

    @Test
    public void testSerializeNullList() throws IOException {
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
