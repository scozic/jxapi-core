package org.jxapi.netutils.serialization.json;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Unit tests for {@link AbstractJsonValueSerializer}.
 */
public class AbstractJsonValueSerializerTest {

    private static class TestJsonMessageSerializer extends AbstractJsonValueSerializer<TestObject> {

        private static final long serialVersionUID = 1479296632702378548L;

		protected TestJsonMessageSerializer() {
            super(TestObject.class);
        }

        @Override
        public void serialize(TestObject value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.getName());
            gen.writeNumberField("a", value.getAge());
            gen.writeEndObject();
        }
    }

    private static class TestObject {
        private String name;
        private int age;

        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    public void testSerialize() {
      TestJsonMessageSerializer serializer = new TestJsonMessageSerializer();
      Assert.assertNull(serializer.serialize(null));
      assertEquals("{\"name\":\"John Doe\",\"a\":30}", serializer.serialize(new TestObject("John Doe", 30)));
    }
}

