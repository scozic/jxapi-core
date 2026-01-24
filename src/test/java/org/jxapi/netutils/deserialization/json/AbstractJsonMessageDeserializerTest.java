package org.jxapi.netutils.deserialization.json;

import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.skipNextValue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Unit test for {@link AbstractJsonMessageDeserializer}
 */
public class AbstractJsonMessageDeserializerTest {
  
  public static class AbstractJsonMessageDeserializerTestItem {
    private String name;
    private int age;
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public int getAge() {
      return age;
    }
    public void setAge(int age) {
      this.age = age;
    }
  }
  
  public static class TestJsonMessageDeserializer extends AbstractJsonMessageDeserializer<AbstractJsonMessageDeserializerTestItem> {

    @Override
    public AbstractJsonMessageDeserializerTestItem deserialize(JsonParser parser) throws IOException {
      AbstractJsonMessageDeserializerTestItem msg = new AbstractJsonMessageDeserializerTestItem();
      while (parser.nextToken() != JsonToken.END_OBJECT) {
        switch (parser.currentName()) {
        case "name":
          msg.setName(parser.nextTextValue());
          break;
        case "age":
          msg.setAge(readNextInteger(parser));
          break;
        default:
          skipNextValue(parser);
        }
      }

      return msg;
    }

  }
  
  @Test
  public void testDeserialize() {
    TestJsonMessageDeserializer deserializer = new TestJsonMessageDeserializer();
    AbstractJsonMessageDeserializerTestItem item = deserializer.deserialize("{\"name\":\"John\",\"age\":30}");
    Assert.assertEquals("John", item.getName());
    Assert.assertEquals(30, item.getAge());
  }

  @Test
  public void testDeserializeNull() {
    TestJsonMessageDeserializer deserializer = new TestJsonMessageDeserializer();
    AbstractJsonMessageDeserializerTestItem item = deserializer.deserialize((String)null);
    Assert.assertNull(item);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDeserializeThrows() {
    TestJsonMessageDeserializer deserializer = new TestJsonMessageDeserializer();
    deserializer.deserialize("blah");
  }
}
