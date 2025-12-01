package org.jxapi.netutils.deserialization.json.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

public class GenericObjectJsonFieldDeserializerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize_Primitive() throws IOException {
        GenericObjectJsonFieldDeserializer<Integer> deserializer = new GenericObjectJsonFieldDeserializer<>(Integer.class);
        JsonParser parser = objectMapper.getFactory().createParser("123");
        Integer result = deserializer.deserialize(parser);
        assertEquals(Integer.valueOf(123), result);
    }

    @Test
    public void testDeserialize_CustomObject() throws IOException {
        GenericObjectJsonFieldDeserializer<TestObject> deserializer = new GenericObjectJsonFieldDeserializer<>(TestObject.class);
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        TestObject result = deserializer.deserialize(parser);
        assertNotNull(result);
        assertEquals(new TestObject("John", 30), result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_InvalidJson() throws IOException {
        GenericObjectJsonFieldDeserializer<TestObject> deserializer = new GenericObjectJsonFieldDeserializer<>(TestObject.class);
        JsonParser parser = objectMapper.getFactory().createParser("{invalidJson}");
        deserializer.deserialize(parser);
    }
    
    public static class TestObject {
      public String name;
      public int age;

      // Default constructor for Jackson
      public TestObject() {}

      public TestObject(String name, int age) {
          this.name = name;
          this.age = age;
      }

      @Override
      public boolean equals(Object obj) {
          if (this == obj) return true;
          if (obj == null || getClass() != obj.getClass()) return false;
          TestObject that = (TestObject) obj;
          return age == that.age && name.equals(that.name);
      }
  }
}

