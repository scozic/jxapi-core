package org.jxapi.netutils.serialization;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageSerializerTest {

    @Test
    public void testNoOpSerializer() {
        MessageSerializer<String> noOpSerializer = MessageSerializer.NO_OP;
        String input = "test string";
        String result = noOpSerializer.serialize(input);
        assertEquals("The NO_OP serializer should return the input string as is.", input, result);
    }

    @Test
    public void testCustomSerializer() {
        MessageSerializer<Integer> customSerializer = Object::toString;
        Integer input = 123;
        String result = customSerializer.serialize(input);
        assertEquals("The custom serializer should convert the integer to a string.", "123", result);
    }
}