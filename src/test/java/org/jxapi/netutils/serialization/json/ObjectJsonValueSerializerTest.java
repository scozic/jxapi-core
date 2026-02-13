package org.jxapi.netutils.serialization.json;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link ObjectJsonValueSerializer}
 */
public class ObjectJsonValueSerializerTest {

  @Test
  public void testSerialize() {
    Assert.assertNull(ObjectJsonValueSerializer.getInstance().serialize(null));
    Assert.assertEquals("{\"age\":25,\"name\":\"Hello \\\"world\\\"!\"}", ObjectJsonValueSerializer.getInstance().serialize(new TestObject("Hello \"world\"!", 25)));
  }
  
  private static class TestObject {
    private String name;
    private int age;

    public TestObject(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public int getAge() {
        return age;
    }
}

}
