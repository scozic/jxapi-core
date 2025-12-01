package org.jxapi.netutils.deserialization.json.field;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RawObjectJsonFieldDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize_Primitives() throws IOException {
        RawObjectJsonFieldDeserializer deserializer = RawObjectJsonFieldDeserializer.getInstance();

        JsonParser parser = objectMapper.getFactory().createParser("123");
        assertEquals(123, deserializer.deserialize(parser));

        parser = objectMapper.getFactory().createParser("123.45");
        assertEquals(123.45, deserializer.deserialize(parser));

        parser = objectMapper.getFactory().createParser("\"test\"");
        assertEquals("test", deserializer.deserialize(parser));

        parser = objectMapper.getFactory().createParser("true");
        assertEquals(true, deserializer.deserialize(parser));

        parser = objectMapper.getFactory().createParser("null");
        assertEquals(null, deserializer.deserialize(parser));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_ComplexTypes() throws IOException {
        RawObjectJsonFieldDeserializer deserializer = RawObjectJsonFieldDeserializer.getInstance();

        JsonParser parser = objectMapper.getFactory().createParser("{\"key\":\"value\",\"number\":42}");
        Map<String, Object> map = (Map<String, Object>) deserializer.deserialize(parser);
        assertEquals(2, map.size());
        assertEquals("value", map.get("key"));
        assertEquals(42, map.get("number"));

        parser = objectMapper.getFactory().createParser("[1,2,3]");
        List<Object> list = (List<Object>) deserializer.deserialize(parser);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize_NestedStructures() throws IOException {
        RawObjectJsonFieldDeserializer deserializer = RawObjectJsonFieldDeserializer.getInstance();

        JsonParser parser = objectMapper.getFactory().createParser("{\"list\":[1,2,{\"nestedKey\":\"nestedValue\"}]}");
        Map<String, Object> map = (Map<String, Object>) deserializer.deserialize(parser);
        assertTrue(map.containsKey("list"));

        List<Object> list = (List<Object>) map.get("list");
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        Map<String, Object> nestedMap = (Map<String, Object>) list.get(2);
        assertEquals("nestedValue", nestedMap.get("nestedKey"));
    }
}
